package prophet.serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLSerializer<T> extends SerializerBase<T> {

	protected XMLSerializer(Class<T> serializableClass) {
		super(serializableClass);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Retrieve all the non static, non volatile, non transient fields of the given class
	 * @param c
	 * @return
	 */
	private List<Field> getFields(final Class<?> c) {
		final List<Field> rf = new ArrayList<Field>();
		final Field[] fields = c.getDeclaredFields();
		int fieldsn = fields.length;
		for (int i = 0; i < fieldsn; ++i) {
			final Field f = fields[i];
			if (!Modifier.isStatic(f.getModifiers())
					&& !Modifier.isVolatile(f.getModifiers())
					&& !Modifier.isTransient(f.getModifiers())) {
				f.setAccessible(true);
				rf.add(f);
			}
		}
		return rf;
	}
	
	private Field getField(final Class<?> c, final String name) {
		final Field[] fields = c.getDeclaredFields();
		int fieldsn = fields.length;
		for (int i = 0; i < fieldsn; ++i) {
			final Field f = fields[i];
			if(f.getName().equals(name)
					&& !Modifier.isStatic(f.getModifiers())
					&& !Modifier.isVolatile(f.getModifiers())
					&& !Modifier.isTransient(f.getModifiers())) {
				f.setAccessible(true);
				return f;
			}
		}
		return null;
	}
	
	private void serializeAttributes(final Object object, final StringBuilder builder) {
		builder.append(" class=\"");
		builder.append(object.getClass().getName());
		builder.append('"');
		
		for (Field f : getFields(object.getClass())) {
			final String fname = f.getName();
			final Class<?> ftype = f.getType();
			try {
				final Object fvalue = f.get(object);
				if(null == fvalue) {
					throw new IllegalArgumentException("Object "+object+" "+fname+" value is null");
				}
				//look for supported serializers
				final ISerializer<?> fserializer = getSerializer(ftype);
				if(null == fserializer) {
					builder.append(' ');
					builder.append(fname);
					builder.append("=\"");
					builder.append(fvalue.toString());
					builder.append('\"');
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void serializeContent(final Object object, final StringBuilder builder) {
		for (Field f : getFields(object.getClass())) {
			final Class<?> ftype = f.getType();
			try {
				final Object fvalue = f.get(object);
				//look for not supported serializers
				final ISerializer<?> fserializer = getSerializer(ftype);
				if(null != fserializer) {
					final String fname = f.getName();
					builder.append(fserializer.serialize(fname, fvalue));
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String serialize(final String name, final Object object) {
		final StringBuilder builder = new StringBuilder();
		builder.append('<');
		builder.append(name);
		serializeAttributes(object, builder);
		builder.append('>');
		serializeContent(object, builder);
		builder.append("</");
		builder.append(name);
		builder.append('>');
		return builder.toString();
	}
	
	private String getSetterName(final String attribute) {
		return "set"+Character.toUpperCase(attribute.charAt(0))+attribute.substring(1);
	}
	
	private Method getSetterMethod(final Object object, final String methodName) {
		Method[] omethods = object.getClass().getMethods();
		for(Method m: omethods) {
			final String mname = m.getName();
			if(mname.equals(methodName)) {
				return m;
			}
		}
		return null;
	}
	
	protected void parseAttributes(final Element element, final Object object) {
		final NamedNodeMap nodes = element.getAttributes();
		final int nlen = nodes.getLength();
		for(int i=0; i<nlen; ++i) {
			Node n = nodes.item(i);
			final String aname = n.getNodeName();
			final String avalue = n.getNodeValue();
			final String setterName = getSetterName(aname);
			final Method amethod = getSetterMethod(object, setterName);
			if(null != amethod) {
				final Class<?>[] atypes = amethod.getParameterTypes();
				final Class<?> atype = atypes[0];
				try {
					if(atype.isAssignableFrom(String.class)) {
						amethod.invoke(object, avalue);
					} else if(atype.isAssignableFrom(double.class)) {
						amethod.invoke(object, Double.valueOf(avalue));
					} else { 
						throw new IllegalArgumentException("No such supported type "+atype);
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void parseChildren(final Element element, final Object object) {
		final NodeList children = element.getChildNodes();
		final int nlen = children.getLength();
		for(int i=0; i<nlen; ++i) {
			try {
				Node n = children.item(i);
				Node classAttribute = n.getAttributes().getNamedItem("class");
				if(null == classAttribute) {
					throw new UnsupportedOperationException("Cannot find class attribute for element "+n.getNodeName());
				}
				final String classValue = classAttribute.getNodeValue();
				final String cname = n.getNodeName();
				final Field cfield = getField(object.getClass(), cname);
				Class<?> classType = Class.forName(classValue);
				Object cobject = classType.newInstance();
				ISerializer<?> classSerializer = getSerializer(classType);
				if(null != classSerializer) {
					if(classSerializer instanceof XMLSerializer<?>) {
						((XMLSerializer<?>)classSerializer).parse((Element)n, cobject);
						cfield.set(object, cobject);
					} else {
						throw new UnsupportedOperationException("Invalid serializer type "+classSerializer.getClass().getName()+" instead of "+this.getClass().getName());
					}
				} else {
					throw new UnsupportedOperationException("No such serializer for "+classType);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} 
		}
	}

	protected void parse(final Element element, final Object object) {
		parseAttributes(element, object);
		parseChildren(element, object);
	}
	
	@Override
	public void parse(final String source, final Object object) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			ByteArrayInputStream input = new ByteArrayInputStream(source.getBytes("UTF-8"));
			Document document = builder.parse(input);
			Element root = document.getDocumentElement();
			parse(root, object);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
