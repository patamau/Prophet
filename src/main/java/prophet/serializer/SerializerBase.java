package prophet.serializer;

import java.util.HashMap;
import java.util.Map;

public abstract class SerializerBase<T> implements ISerializer<T> {

	private final static Map<Class<?>, ISerializer<?>> serializers = new HashMap<Class<?>, ISerializer<?>>();
	
	public static void addSerializer(ISerializer<?> serializer) {
		serializers.put(serializer.getSerializableClass(), serializer);
	}
	
	public static ISerializer<?> getSerializer(Class<?> clazz) {
		if(serializers.containsKey(clazz)) {
			return serializers.get(clazz);
		} else {
			for (ISerializer<?> serializer : serializers.values()) {
				if(serializer.getSerializableClass().isAssignableFrom(clazz)) {
					return serializer;
				}
			}
		}
		return null;
	}
	
	private final Class<T> serializableClass;

	protected SerializerBase(final Class<T> serializableClass) {
		this.serializableClass = serializableClass;
	}
	
	@Override
	public Class<T> getSerializableClass(){
		return this.serializableClass;
	}
	
}
