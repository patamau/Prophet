package prophet.serializer;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import prophet.util.ClassUtils;
import prophet.util.Logger;

public class XMLSerializer<T> extends SerializerBase<T> {

	private static final Logger logger = Logger.getLogger(XMLSerializer.class);
	
	protected XMLSerializer(Class<T> serializableClass) {
		super(serializableClass);
	}
	
	private void serializeAttributes(final Object object, final StringBuilder builder) {
		builder.append(" class=\"");
		builder.append(object.getClass().getName());
		builder.append('"');
		
		for (Field f : ClassUtils.getFields(object.getClass())) {
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
		for (Field f : ClassUtils.getFields(object.getClass())) {
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
	
	protected void parseAttributes(final Element element, final Object object) {
		final NamedNodeMap nodes = element.getAttributes();
		final int nlen = nodes.getLength();
		for(int i=0; i<nlen; ++i) {
			Node n = nodes.item(i);
			final String aname = n.getNodeName();
			final String avalue = n.getNodeValue();
			try {
				ClassUtils.setFieldValue(object, aname, avalue);
			} catch (Exception e) {
				logger.error(e);
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
				final Field cfield = ClassUtils.getField(object.getClass(), cname);
				final Class<?> classType = Class.forName(classValue);
				Object cobject = cfield.get(object);
				if(null == cobject) {
					logger.warning("creating new object ",cname," (",classType.getName(),")");
					cobject = classType.newInstance();
				} else {
					logger.debug("Reusing preallocated object ",cobject, " (", cname, " ", cobject.getClass().getName(),")");
				}
				ISerializer<?> classSerializer = getSerializer(classType);
				if(null != classSerializer) {
					if(classSerializer instanceof XMLSerializer<?>) {
						((XMLSerializer<?>)classSerializer).parse((Element)n, cobject);
						//cfield.set(object, cobject);
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
	public void parse(final String source, final Object object) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		ByteArrayInputStream input = new ByteArrayInputStream(source.getBytes("UTF-8"));
		Document document = builder.parse(input);
		Element root = document.getDocumentElement();
		parse(root, object);
	}
}
