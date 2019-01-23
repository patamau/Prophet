package prophet;

import java.awt.geom.Point2D;
import java.lang.Thread.UncaughtExceptionHandler;
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

public class Prophet implements IWorldListener, UncaughtExceptionHandler {
	
	private static final Logger logger = Logger.getLogger(Prophet.class);
	
	public static final String 
		APP = Prophet.class.getPackage().getImplementationTitle()==null?Prophet.class.getSimpleName():Prophet.class.getPackage().getImplementationTitle(),
		VERSION = "0.1a",
		BUILD = Prophet.class.getPackage().getImplementationVersion()==null?"[dev]":Prophet.class.getPackage().getImplementationVersion(),
		AUTHOR = Prophet.class.getPackage().getImplementationVendor();

	private final ISetting setting;
	private final WorldRendererComponent renderer;
	private final ISerializer<ISetting> serializer;
	
	private final Map<IMap, PictureLayer> mapLayers;
	
	public Prophet() {
		mapLayers = new HashMap<IMap, PictureLayer>();
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
	public void onMapAdded(final IMap map) {
		if(mapLayers.containsKey(map)) return;
		logger.info("Map added: ",map.getPicturePath());
		final Point2D offset = setting.getWorld().toCartesian(new Point2D.Double(map.getLongitude(), map.getLatitude()));
		final PictureLayer layer = new PictureLayer(renderer, map.getPicture(), map.getScale(), offset);
		mapLayers.put(map, layer);
		renderer.addLayer(layer);
		logger.info("Maps are now "+mapLayers.size());
	}
	
	@Override
	public void onMapChanged(final IMap map) {
		logger.debug("Map changed");
		final PictureLayer layer = mapLayers.get(map);
		if(null != layer) {
			final Point2D offset = setting.getWorld().toCartesian(new Point2D.Double(map.getLongitude(), map.getLatitude()));
			layer.set(map.getPicture(), offset, map.getScale());
			renderer.repaint();
		}
	}

	@Override
	public void onMapRemoved(final IMap map) {
		logger.info("Map removed: ",map.getPicturePath());
		renderer.removeLayer(mapLayers.remove(map));
		logger.info("Maps are now "+mapLayers.size());
	}
	
	@Override
	public void onMapsCleared() {
		logger.info("Maps cleared");
		for(ILayer l : mapLayers.values()) {
			renderer.removeLayer(l);
		}
		mapLayers.clear();
	}

	@Override
	public void onTownAdded(final ITown town) {
		logger.debug("Town added");
		//TODO: implement
	}
	
	@Override
	public void onTownChanged(final ITown map) {
		logger.debug("Town changed");
		//TODO: implement
	}

	@Override
	public void onTownRemoved(final ITown town) {
		logger.debug("Town removed");
		//TODO: implement
	}
	
	@Override
	public void onTownsCleared() {
		logger.debug("Towns cleared");
		//TODO: implement
	}

	@Override
	public void uncaughtException(final Thread t, final Throwable e) {
		logger.error(e.getMessage());
		e.printStackTrace();
	}
	
	public void finalize() {
		//TODO: save everything's needed to be saved and close streams properly
		Logger.clearStreams();
	}
}
