package prophet.serializer;

import prophet.geom.Point2D;
import prophet.model.IBorder;
import prophet.model.IMap;
import prophet.model.ISetting;
import prophet.model.ITown;
import prophet.model.IWorld;

public class XMLSettingSerializer extends XMLSerializer<ISetting> {

	public XMLSettingSerializer() {
		super(ISetting.class);
		addSerializer(new XMLSerializer<IWorld>(IWorld.class));
		addSerializer(new XMLSerializer<ITown>(ITown.class));
		addSerializer(new XMLSerializer<IMap>(IMap.class));
		addSerializer(new XMLSerializer<IBorder>(IBorder.class));
		addSerializer(new XMLSerializer<Point2D>(Point2D.class));
		addSerializer(new XMLListSerializer());
	}
	
	@Override
	public String serialize(String name, Object object) {
		return super.serialize(name, object);
	}

}
