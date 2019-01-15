package prophet.serializer;

import prophet.model.ISetting;
import prophet.model.IWorld;

public class XMLSettingSerializer extends XMLSerializer<ISetting> {

	public XMLSettingSerializer() {
		super(ISetting.class);
		addSerializer(new XMLSerializer<IWorld>(IWorld.class));
		addSerializer(new XMLListSerializer());
	}
	
	@Override
	public String serialize(String name, Object object) {
		// TODO Auto-generated method stub
		return super.serialize(name, object);
	}

}
