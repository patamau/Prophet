package prophet;

import java.awt.geom.Point2D;

import prophet.gui.layers.PictureLayer;
import prophet.gui.renderers.WorldRendererComponent;
import prophet.model.IMap;
import prophet.model.ISetting;
import prophet.model.ITown;
import prophet.model.IWorldListener;
import prophet.model.SimpleSetting;

public class Prophet implements IWorldListener {

	private final ISetting setting;
	private final WorldRendererComponent renderer;
	
	public Prophet() {
		setting = new SimpleSetting("Default");
		renderer = new WorldRendererComponent(setting.getWorld());
		setting.getWorld().addWorldListener(this);
	}
	
	public ISetting getSetting() {
		return setting;
	}
	
	public WorldRendererComponent getRenderer() {
		return renderer;
	}

	@Override
	public void onMapAdded(IMap map) {
		final Point2D offset = setting.getWorld().toCartesian(new Point2D.Double(map.getLongitude(), map.getLatitude()));
		renderer.addLayer(new PictureLayer(renderer, map.getPicture(), map.getScale(), offset));
	}

	@Override
	public void onMapRemoved(IMap map) {
		// TODO
	}

	@Override
	public void onTownAdded(ITown town) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTownRemoved(ITown town) {
		// TODO Auto-generated method stub
		
	}
}
