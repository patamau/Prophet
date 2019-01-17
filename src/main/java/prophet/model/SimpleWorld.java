package prophet.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This simplistic world approach is used to intuitively support flat coordinates system commonly used for visualization.
 * It implies the surface curvature is not relevant and provides hints rather than actual world coordinates.
 * @author patam
 *
 */
public class SimpleWorld implements IWorld {

	public static final double WORLD_RADIUS_DEF = 6378000d;
	
	private double radius;
	private double circonference, circonference2, circonference4;
	private List<ITown> towns;
	private List<IMap> maps;
	private Set<IWorldListener> listeners;
	
	public SimpleWorld() {
		setRadius(WORLD_RADIUS_DEF);
		this.towns = new ArrayList<ITown>();
		this.maps = new ArrayList<IMap>();
		this.listeners = new HashSet<IWorldListener>();
	}
	
	@Override
	public void addWorldListener(final IWorldListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}
	
	@Override
	public void removeWorldListener(final IWorldListener listener) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}
	
	@Override
	public void clearWorldListeners() {
		synchronized(listeners) {
			listeners.clear();
		}
	}
	
	private void fireTownAdded(final ITown town) {
		synchronized (listeners) {
			for(IWorldListener l : listeners) {
				l.onTownAdded(town);
			}
		}
	}
	
	private void fireTownRemoved(final ITown town) {
		synchronized (listeners) {
			for(IWorldListener l : listeners) {
				l.onTownRemoved(town);
			}
		}
	}
	
	@Override
	public void addTown(final ITown town) {
		synchronized(towns) {
			towns.add(town);
		}
		fireTownAdded(town);
	}

	@Override
	public void removeTown(final ITown town) {
		synchronized(towns) {
			towns.remove(town);
		}
		fireTownRemoved(town);
	}
	
	@Override
	public void getTowns(final List<ITown> towns) {
		synchronized(towns) {
			towns.addAll(towns);
		}
	}
	
	@Override
	public void clearTowns() {
		synchronized (towns) {
			towns.clear();
		}
	}

	@Override
	public void addMap(final IMap map) {
		synchronized(maps) {
			maps.add(map);
		}
		fireMapAdded(map);
	}

	@Override
	public void removeMap(final IMap map) {
		synchronized(maps) {
			maps.remove(map);
		}
		fireMapRemoved(map);
	}
	
	@Override
	public void getMaps(final List<IMap> maps) {
		synchronized(maps) {
			maps.addAll(maps);
		}
	}
	
	@Override
	public void clearMaps() {
		synchronized (maps) {
			maps.clear();
		}
	}
	
	private void fireMapAdded(final IMap map) {
		synchronized (listeners) {
			for(IWorldListener l : listeners) {
				l.onMapAdded(map);
			}
		}
	}
	
	private void fireMapRemoved(final IMap map) {
		synchronized (listeners) {
			for(IWorldListener l : listeners) {
				l.onMapRemoved(map);
			}
		}
	}
	
	@Override
	public void setRadius(final double radius) {
		this.radius = radius;
		this.circonference2 = radius * Math.PI;
		this.circonference = 2 * circonference2;
		this.circonference4 = 0.5 * circonference2;
	}
	
	@Override
	public double getRadius() {
		return radius;
	}
	
	@Override
	public double getCirconference() {
		return circonference;
	}
	
	@Override
	public double toLatitude(final double y) {
		return y/circonference4 * 90d;
	}
	
	@Override
	public double toLongitude(final double x) {
		return x/circonference2 * 180d;
	}
	
	@Override
	public double fromLatitude(final double latitude) {
		return latitude/90d * circonference4;
	}
	
	@Override
	public double fromLongitude(final double longitude) {
		return longitude/180d * circonference2;
	}
	
	@Override
	public Point2D toPolar(final Point2D p) {
		return new Point2D.Double(toLongitude(p.getX()), toLatitude(p.getY()));
	}
	
	@Override
	public Point2D toCartesian(final Point2D p) {
		return new Point2D.Double(fromLongitude(p.getX()), fromLatitude(p.getY()));
	}
	
}
