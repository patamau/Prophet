package prophet.serializer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import prophet.util.ClassUtils;
import prophet.util.Logger;

public class XMLListSerializer extends XMLSerializer<List> {
	
	private static final Logger logger = Logger.getLogger(XMLListSerializer.class);
	
	public XMLListSerializer() {
		super(List.class);
	}

	/**
	 * Custom serialization of a list.
	 * <name>
	 *   <objectclass>{serialized object}</objectclass>
	 * </name>
	 * There is no way to get the item name since it was not a variable
	 * However it is not relevant at all since it will be added back to the list
	 */
	@Override
	public String serialize(final String name, final Object object) {
		if(object.getClass().isAssignableFrom(getSerializableClass())) {
			throw new RuntimeException("Incompatible "+getSerializableClass().getName()+" type "+object.getClass());
		}
		final StringBuilder builder = new StringBuilder();
		builder.append('<');
		builder.append(name);
		builder.append(" class=\"");
		builder.append(ArrayList.class.getName());
		builder.append("\">");
		for(Object o : (List<?>)object) {
			final ISerializer<?> oserializer = getSerializer(o.getClass());
			final String oname = o.getClass().getName();
			if(null != oserializer) {
				builder.append(oserializer.serialize(oname, o));
			} else {
				builder.append('<');
				builder.append(oname);
				builder.append('>');
				builder.append(o.toString());
				builder.append("</");
				builder.append(oname);
				builder.append('>');
			}
		}
		builder.append("</");
		builder.append(name);
		builder.append('>');
		return builder.toString();
	}
	
	protected void parseChildren(final Element element, final List<Object> list, final Object parent)
	{
		final NodeList children = element.getChildNodes();
		final int nlen = children.getLength();
		final Method amet = ClassUtils.getAdderMethod(parent, element.getNodeName());
		for(int i=0; i<nlen; ++i) {
			try {
				Node n = children.item(i);
				String classValue = decodeXml(n.getNodeName());
				Class<?> classType = Class.forName(classValue);
				Object cobject = classType.newInstance();
				ISerializer<?> classSerializer = getSerializer(classType);
				if(null != classSerializer) {
					if(classSerializer instanceof XMLSerializer<?>) {
						((XMLSerializer<?>)classSerializer).parse((Element)n, cobject, parent);
						try {
							amet.invoke(parent, cobject);
						} catch (Exception e) {
							logger.error("Cannot add ",cobject," to ",parent," list: ", e);
						}
						//list.add(cobject);
					} else {
						throw new UnsupportedOperationException("Invalid serializer type "+classSerializer.getClass().getName()+" instead of "+this.getClass().getName());
					}
				} else {
					list.add(n.getTextContent());
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
	
	@Override
	public void parse(final Element element, final Object object, final Object parent) {
		if(object.getClass().isAssignableFrom(getSerializableClass())) {
			throw new RuntimeException("Incompatible "+getSerializableClass().getName()+" type "+object.getClass());
		}
		final List<Object> list = (List<Object>)object;
		parseChildren(element, list, parent);
	}
	
}
