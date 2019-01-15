package prophet.serializer;

import java.util.List;

import org.w3c.dom.Element;

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
		builder.append('>');
		for(Object o : (List<?>)object) {
			final ISerializer<?> oserializer = getSerializer(o.getClass());
			final String oname = o.getClass().getSimpleName();
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
	
	@Override
	public void parse(final Element element, final Object object) {
		if(object.getClass().isAssignableFrom(getSerializableClass())) {
			throw new RuntimeException("Incompatible "+getSerializableClass().getName()+" type "+object.getClass());
		}
		final List<?> list = (List<?>)object;
		//TODO
	}
	
}
