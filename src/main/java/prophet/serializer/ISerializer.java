package prophet.serializer;

public interface ISerializer<T> {

	public void addSerializer(ISerializer<?> serializer);
	public ISerializer<?> getSerializer(Class<?> clazz);
	
	/**
	 * The class this serializer is meant to serialize
	 * @return
	 */
	public Class<T> getSerializableClass();
	
	public String serialize(final Object object);
	
	/**
	 * Fill the given object with data from the serialized source 
	 * @param source
	 * @param setting
	 */
	public void parse(final String source, final T object);
}
