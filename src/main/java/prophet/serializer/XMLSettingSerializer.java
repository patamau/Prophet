package prophet.serializer;

import prophet.model.ISetting;

public class XMLSettingSerializer extends XMLSerializerBase<ISetting> {

	public XMLSettingSerializer() {
		super();
		//addSerializer(new XMLWorldSerializer());
	}
	
	@Override
	public Class<ISetting> getSerializableClass() {
		return ISetting.class;
	}

}
