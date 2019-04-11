package prophet;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;

import prophet.geom.Point2D;
import prophet.gui.ILayer;
import prophet.gui.layers.GridLayer;
import prophet.gui.layers.IconsLayer;
import prophet.gui.layers.PictureLayer;
import prophet.gui.layers.PolygonsLayer;
import prophet.gui.renderers.WorldRendererComponent;
import prophet.model.IBorder;
import prophet.model.IMap;
import prophet.model.ISetting;
import prophet.model.ITown;
import prophet.model.IWorldListener;
import prophet.model.SimpleSetting;
import prophet.serializer.ISerializer;
import prophet.serializer.XMLSettingSerializer;
import prophet.util.Configuration;
import prophet.util.Logger;
import prophet.util.Resources;

public class Prophet implements IWorldListener, UncaughtExceptionHandler {
	
	private static final Logger logger = Logger.getLogger(Prophet.class);
	
	public static final String
		ICON_TOWN_KEY = "icon.town.url";
	
	public static final String
		ICON_TOWN_DEF = "icons/town.png";
	
	public static final String 
		APP = Prophet.class.getPackage().getImplementationTitle()==null?Prophet.class.getSimpleName():Prophet.class.getPackage().getImplementationTitle(),
		VERSION = "0.1a",
		BUILD = Prophet.class.getPackage().getImplementationVersion()==null?"[dev]":Prophet.class.getPackage().getImplementationVersion(),
		AUTHOR = Prophet.class.getPackage().getImplementationVendor();

	private final ISetting setting;
	private final WorldRendererComponent renderer;
	private final ISerializer<ISetting> serializer;
	
	private final Map<IMap, PictureLayer> mapLayers;
	private final IconsLayer townsLayer;
	private final PolygonsLayer bordersLayer;
	private final GridLayer gridLayer;
	
	public Prophet() {
		mapLayers = new HashMap<IMap, PictureLayer>();
		setting = new SimpleSetting();
		renderer = new WorldRendererComponent(setting.getWorld());
		townsLayer = new IconsLayer("Towns", renderer);
		bordersLayer = new PolygonsLayer("Borders", renderer);
		gridLayer = new GridLayer(renderer);
		setting.getWorld().addWorldListener(this);
		serializer = new XMLSettingSerializer();
	}
	
	public void initialize() {
		final String townIconUrl = Configuration.getGlobal(ICON_TOWN_KEY, ICON_TOWN_DEF);
		townsLayer.setIcon(Resources.getImage(townIconUrl));
		renderer.addLayer(bordersLayer);
		renderer.addLayer(townsLayer);
		renderer.addLayer(gridLayer);
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
		final Point2D offset = setting.getWorld().toCartesian(new Point2D(map.getLongitude(), map.getLatitude()));
		final PictureLayer layer = new PictureLayer(map.getPicturePath(), renderer, map.getPicture(), map.getScale(), offset);
		mapLayers.put(map, layer);
		renderer.addLayer(layer);
	}
	
	@Override
	public void onMapChanged(final IMap map) {
		logger.debug("Map changed");
		final PictureLayer layer = mapLayers.get(map);
		if(null != layer) {
			final Point2D offset = setting.getWorld().toCartesian(new Point2D(map.getLongitude(), map.getLatitude()));
			layer.set(map.getPicture(), offset, map.getScale());
			layer.setName(map.getPicturePath());
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
		final Point2D p = setting.getWorld().toCartesian(new Point2D(town.getLongitude(), town.getLatitude()));
		logger.debug("Town added at ", p, " (",town.getLongitude(),",",town.getLatitude(),")");
		townsLayer.addPoint(town);
	}
	
	@Override
	public void onTownChanged(final ITown map) {
		logger.debug("Town changed");
		renderer.repaint();
	}

	@Override
	public void onTownRemoved(final ITown town) {
		logger.debug("Town removed");
		townsLayer.removePoint(town);
		renderer.repaint();
	}
	
	@Override
	public void onTownsCleared() {
		logger.debug("Towns cleared");
		townsLayer.clearPoints();
		renderer.repaint();
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

	@Override
	public void onBorderAdded(final IBorder border) {
		logger.debug("Border added ", border);
		bordersLayer.addPolygon(border);
		renderer.repaint();
	}

	@Override
	public void onBorderChanged(final IBorder border) {
		logger.debug("Border changed ", border);
		renderer.repaint();
	}

	@Override
	public void onBorderRemoved(final IBorder border) {
		logger.debug("Border removed ", border);
		bordersLayer.removePolygon(border);
		renderer.repaint();
	}

	@Override
	public void onBordersCleared() {
		logger.debug("Borders cleared");
		bordersLayer.clearPolygons();
		renderer.repaint();
	}
}
