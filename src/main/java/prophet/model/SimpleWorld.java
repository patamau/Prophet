package prophet.model;

import prophet.geom.Point2D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import prophet.util.Logger;

/**
 * This simplistic world approach is used to intuitively support flat coordinates system commonly used for visualization.
 * It implies the surface curvature is not relevant and provides hints rather than actual world coordinates.
 * @author patam
 *
 */
public class SimpleWorld implements IWorld, Observer {

	private static final Logger logger = Logger.getLogger(SimpleWorld.class);
	
	public static final double WORLD_RADIUS_DEF = 6378d; //6378 km is the Earth radius
	
	private double radius;
	private List<ITown> towns;
	private List<IMap> maps;
	private List<IBorder> borders;
	
	private transient double circonference, circonference2, circonference4;
	private transient Set<IWorldListener> listeners;
	
	public SimpleWorld() {
		setRadius(WORLD_RADIUS_DEF);
		this.towns = new ArrayList<ITown>();
		this.maps = new ArrayList<IMap>();
		this.borders = new ArrayList<IBorder>();
		this.listeners = new HashSet<IWorldListener>();
	}
	
	@Override
	public void reset() {
		this.towns.clear();
		this.maps.clear();
		this.borders.clear();
		//do not remove the listeners, we want to keep them, just reset the data
		fireTownsCleared();
		fireMapsCleared();
		fireBordersCleared();
	}
	
	@Override
	public void update() {
		//notify the listeners of towns
		for (final ITown t: towns) {
			fireTownAdded(t);
		}
		//notify the listeners of maps
		for (final IMap m: maps) {
			fireMapAdded(m);
		}
		//notify the listeners of maps
		for (final IBorder b: borders) {
			fireBorderAdded(b);
		}
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
			for(final IWorldListener l : listeners) {
				l.onTownAdded(town);
			}
		}
	}
	
	private void fireTownChanged(final ITown town) {
		synchronized (listeners) {
			for(final IWorldListener l : listeners) {
				l.onTownChanged(town);
			}
		}
	}
	
	private void fireTownRemoved(final ITown town) {
		synchronized (listeners) {
			for(final IWorldListener l : listeners) {
				l.onTownRemoved(town);
			}
		}
	}
	
	private void fireTownsCleared() {
		synchronized (listeners) {
			for(final IWorldListener l : listeners) {
				l.onTownsCleared();;
			}
		}
	}
	
	@Override
	public void addTown(final ITown town) {
		town.updateWorldPosition(this);
		synchronized(towns) {
			towns.add(town);
		}
		town.addObserver(this);
		fireTownAdded(town);
	}
	
	@Override
	public void removeTown(final ITown town) {
		synchronized(towns) {
			towns.remove(town);
		}
		town.deleteObserver(this);
		fireTownRemoved(town);
	}
	
	@Override
	public void getTowns(final List<ITown> outTowns) {
		synchronized(towns) {
			outTowns.addAll(towns);
		}
	}
	
	@Override
	public void clearTowns() {
		synchronized (towns) {
			for(final ITown t : towns) {
				t.deleteObserver(this);
			}
			towns.clear();
		}
		fireTownsCleared();
	}

	@Override
	public void addMap(final IMap map) {
		synchronized(maps) {
			maps.add(map);
		}
		map.addObserver(this);
		fireMapAdded(map);
	}

	@Override
	public void removeMap(final IMap map) {
		synchronized(maps) {
			maps.remove(map);
		}
		map.deleteObserver(this);
		fireMapRemoved(map);
	}
	
	@Override
	public void getMaps(final List<IMap> outMaps) {
		synchronized(maps) {
			outMaps.addAll(maps);
		}
	}
	
	@Override
	public void clearMaps() {
		synchronized (maps) {
			for(final IMap m : maps) {
				m.deleteObserver(this);
			}
			maps.clear();
		}
		fireMapsCleared();
	}
	
	private void fireMapAdded(final IMap map) {
		synchronized (listeners) {
			for(final IWorldListener l : listeners) {
				l.onMapAdded(map);
			}
		}
	}
	
	private void fireMapChanged(final IMap map) {
		synchronized (listeners) {
			for(final IWorldListener l : listeners) {
				l.onMapChanged(map);
			}
		}
	}
	
	private void fireMapRemoved(final IMap map) {
		synchronized (listeners) {
			for(final IWorldListener l : listeners) {
				l.onMapRemoved(map);
			}
		}
	}
	
	private void fireMapsCleared() {
		synchronized (listeners) {
			for(final IWorldListener l : listeners) {
				l.onMapsCleared();;
			}
		}
	}
	
	@Override
	public void addBorder(final IBorder border) {
		logger.debug("border added ",border);
		synchronized (borders) {
			borders.add(border);
		}
		border.addObserver(this);
		fireBorderAdded(border);
	}
	
	@Override
	public void removeBorder(final IBorder border) {
		synchronized (borders) {
			borders.remove(border);
		}
		border.deleteObserver(this);
		fireBorderRemoved(border);
	}
	
	@Override
	public void getBorders(final List<IBorder> maps) {
		synchronized (borders) {
			maps.addAll(borders);
		}
	}
	
	@Override
	public void clearBorders() {
		synchronized (borders) {
			for(final IBorder b : borders) {
				b.deleteObserver(this);
			}
			borders.clear();
		}
		fireBordersCleared();
	}
	
	private void fireBorderAdded(final IBorder map) {
		synchronized (listeners) {
			for(final IWorldListener l : listeners) {
				l.onBorderAdded(map);
			}
		}
	}
	
	private void fireBorderChanged(final IBorder map) {
		synchronized (listeners) {
			for(final IWorldListener l : listeners) {
				l.onBorderChanged(map);
			}
		}
	}
	
	private void fireBorderRemoved(final IBorder map) {
		synchronized (listeners) {
			for(final IWorldListener l : listeners) {
				l.onBorderRemoved(map);
			}
		}
	}
	
	private void fireBordersCleared() {
		synchronized (listeners) {
			for(final IWorldListener l : listeners) {
				l.onBordersCleared();;
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
		return new Point2D(toLongitude(p.getX()), toLatitude(p.getY()));
	}
	
	@Override
	public Point2D toCartesian(final Point2D p) {
		return new Point2D(fromLongitude(p.getX()), fromLatitude(p.getY()));
	}

	@Override
	public void update(final Observable o, final Object arg) {
		if(o instanceof IMap) {
			final IMap map = (IMap)o;
			fireMapChanged(map);
		} else if (o instanceof ITown) {
			final ITown town = (ITown)o;
			town.updateWorldPosition(this);
			fireTownChanged((ITown)o);
		} else if (o instanceof IBorder) {
			final IBorder border = (IBorder)o;
			fireBorderChanged(border);
		}
	}
	
}
