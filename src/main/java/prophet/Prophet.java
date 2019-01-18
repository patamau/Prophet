package prophet;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import prophet.gui.layers.PictureLayer;
import prophet.gui.renderers.WorldRendererComponent;
import prophet.model.IMap;
import prophet.model.ISetting;
import prophet.model.ITown;
import prophet.model.IWorldListener;
import prophet.model.SimpleSetting;
import prophet.gui.ILayer;
import prophet.serializer.ISerializer;
import prophet.serializer.XMLSettingSerializer;
import prophet.util.Logger;

public class Prophet implements IWorldListener {
	
	private static final Logger logger = Logger.getLogger(Prophet.class);

	private final ISetting setting;
	private final WorldRendererComponent renderer;
	private final ISerializer<ISetting> serializer;
	
	private final Map<IMap, ILayer> mapLayers;
	
	public Prophet() {
		mapLayers = new HashMap<IMap, ILayer>();
		setting = new SimpleSetting();
		renderer = new WorldRendererComponent(setting.getWorld());
		setting.getWorld().addWorldListener(this);
		serializer = new XMLSettingSerializer();
	}
	
	public ISerializer<ISetting> getSettingSerializer() {
		return serializer;
	}
	
	public ISetting getSetting() {
		return setting;
	}
	
	public WorldRendererComponent getRenderer() {
		return renderer;
	}

	@Override
	public void onMapAdded(IMap map) {
		if(mapLayers.containsKey(map)) return;
		logger.info("Map added: ",map.getPicturePath());
		final Point2D offset = setting.getWorld().toCartesian(new Point2D.Double(map.getLongitude(), map.getLatitude()));
		ILayer layer = new PictureLayer(renderer, map.getPicture(), map.getScale(), offset);
		mapLayers.put(map, layer);
		renderer.addLayer(layer);
		logger.info("Maps are now "+mapLayers.size());
	}

	@Override
	public void onMapRemoved(IMap map) {
		logger.info("Map removed: ",map.getPicturePath());
		mapLayers.remove(map);
		logger.info("Maps are now "+mapLayers.size());
	}
	
	@Override
	public void onMapsCleared() {
		logger.info("Maps cleared");
		mapLayers.clear();
	}

	@Override
	public void onTownAdded(ITown town) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTownRemoved(ITown town) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onTownsCleared() {
		logger.info("Towns cleared");
		// TODO
	}
}
