package prophet.serializer;

import java.util.HashMap;
import java.util.Map;

public abstract class SerializerBase<T> implements ISerializer<T> {

	private final Map<Class<?>, ISerializer<?>> serializers;
	
	protected SerializerBase() {
		serializers = new HashMap<Class<?>, ISerializer<?>>();
	}
	
	public void addSerializer(ISerializer<?> serializer) {
		serializers.put(serializer.getSerializableClass(), serializer);
	}
	
	public ISerializer<?> getSerializer(Class<?> clazz) {
		return serializers.get(clazz);
	}
	
}
