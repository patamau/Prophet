package prophet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import prophet.geom.Point2D;
import prophet.model.IMap;
import prophet.model.ISetting;
import prophet.model.ITown;
import prophet.model.SimpleMap;
import prophet.model.SimpleSetting;
import prophet.model.SimpleTown;
import prophet.serializer.ISerializer;
import prophet.serializer.XMLSettingSerializer;
import prophet.util.Logger;

class SerializerTest {
	
	@BeforeAll
    public static void setUp() {
        Logger.setLevel(Logger.L_DEBUG);
        Logger.addStream(System.out);
    }

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
		final IMap map = new SimpleMap("map", setting.getWorld());
		map.setPicturePath("maps/earth.jpg");
		setting.getWorld().addMap(map);
		final ISerializer<ISetting> serializer = new XMLSettingSerializer();
		String xml = serializer.serialize("setting", setting);
		System.out.println(xml);
		final ISetting setting2 = new SimpleSetting();
		try {
			serializer.parse(xml, setting2, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		xml = serializer.serialize("setting", setting2);
		System.out.println(xml);
	}

	@Test
	void point2Dtest() {
		try {
			Class<?> cl = Class.forName("prophet.geom.Point2D");
			Point2D p = new Point2D();
			Point2D p2 = (Point2D)cl.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
