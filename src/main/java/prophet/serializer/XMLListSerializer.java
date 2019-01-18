package prophet.serializer;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLListSerializer extends XMLSerializer<List> {
	
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
	
	protected void parseChildren(final Element element, final List<Object> list)
	{
		final NodeList children = element.getChildNodes();
		final int nlen = children.getLength();
		for(int i=0; i<nlen; ++i) {
			try {
				Node n = children.item(i);
				String classValue = n.getNodeName();
				Class<?> classType = Class.forName(classValue);
				Object cobject = classType.newInstance();
				ISerializer<?> classSerializer = getSerializer(classType);
				if(null != classSerializer) {
					if(classSerializer instanceof XMLSerializer<?>) {
						((XMLSerializer<?>)classSerializer).parse((Element)n, cobject);
						list.add(cobject);
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
	public void parse(final Element element, final Object object) {
		if(object.getClass().isAssignableFrom(getSerializableClass())) {
			throw new RuntimeException("Incompatible "+getSerializableClass().getName()+" type "+object.getClass());
		}
		final List<Object> list = (List<Object>)object;
		parseChildren(element, list);
	}
	
}
