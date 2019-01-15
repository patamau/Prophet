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
	private List<Field> getFields(Class<?> c) {
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
	
	private void serializeAttributes(final Object object, final StringBuilder builder) {
		for (Field f : getFields(object.getClass())) {
			final String fname = f.getName();
			final Class<?> ftype = f.getType();
			try {
				final Object fvalue = f.get(object);
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
	
	
	public void parse(final Element element, final Object object) {
		NamedNodeMap nodes = element.getAttributes();
		int nlen = nodes.getLength();
		for(int i=0; i<nlen; ++i) {
			Node n = nodes.item(i);
			final String aname = n.getNodeName();
			final String avalue = n.getNodeValue();
			final String setterName = getSetterName(aname);
			final Method amethod = getSetterMethod(object, setterName);
			if(null != amethod) {
				try {
					amethod.invoke(object, avalue);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} else {
				throw new UnsupportedOperationException(setterName);
			}
		}
		NodeList children = element.getChildNodes();
		nlen = children.getLength();
		for(int i=0; i<nlen; ++i) {
			//TODO
		}
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
