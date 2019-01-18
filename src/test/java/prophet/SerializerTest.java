package prophet;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import prophet.model.IMap;
import prophet.model.ISetting;
import prophet.model.ITown;
import prophet.model.SimpleMap;
import prophet.model.SimpleSetting;
import prophet.model.SimpleTown;
import prophet.serializer.ISerializer;
import prophet.serializer.XMLSettingSerializer;

class SerializerTest {

	@Test
	void test() {
		final ISetting setting = new SimpleSetting();
		setting.setName("My Awesome Setting");
		setting.getWorld().setRadius(1d);
		final ITown town = new SimpleTown();
		town.setName("my awesome town");
		setting.getWorld().addTown(town);
		final ITown town2 = new SimpleTown();
		town2.setName("another awesome town");
		setting.getWorld().addTown(town2);
		final IMap map = new SimpleMap();
		map.setPicturePath("/earth.jpg");
		setting.getWorld().addMap(map);
		final ISerializer<ISetting> serializer = new XMLSettingSerializer();
		String xml = serializer.serialize("setting", setting);
		System.out.println(xml);
		final ISetting setting2 = new SimpleSetting();
		try {
			serializer.parse(xml, setting2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		xml = serializer.serialize("setting", setting2);
		System.out.println(xml);
	}

}
