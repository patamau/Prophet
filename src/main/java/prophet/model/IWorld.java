package prophet.model;

import java.awt.geom.Point2D;
import java.util.List;

public interface IWorld {

	public void reset();
	public void update();
	
	public void addWorldListener(final IWorldListener listener);
	public void removeWorldListener(final IWorldListener listener);
	public void clearWorldListeners();
	
	public void addTown(final ITown town);
	public void removeTown(final ITown town);
	public void getTowns(final List<ITown> towns);
	public void clearTowns();
	
	public void addMap(final IMap map);
	public void removeMap(final IMap map);
	public void getMaps(final List<IMap> maps);
	public void clearMaps();
	
	public void setRadius(final double radius);
	
	public double getRadius();
	public double getCirconference();
	
	/**
	 * Retrieve latitude angle for the given y coordinate
	 * @param y
	 * @return the latitude angle in degrees
	 */
	public double toLatitude(final double y);
	
	/**
	 * Retrieve longitude angle for the given x coordinate
	 * @param x
	 * @return the longitude angle in degrees
	 */
	public double toLongitude(final double x);
	
	/**
	 * Retrieve vertical cartesian coordinate for the given latitude
	 * @param latitude angle in degrees
	 * @return
	 */
	public double fromLatitude(final double latitude);
	
	/**
	 * Retrieve horizontal cartesian coordinate from the given longitude
	 * @param longitude angle in degrees
	 * @return
	 */
	public double fromLongitude(final double longitude);
	
	/**
	 * Retrieve a new point with polar coordinates
	 * @param p
	 * @return
	 */
	public Point2D toPolar(final Point2D p);
	
	/**
	 * Retrieve a new point with cartesian coordinates
	 * @param p
	 * @return
	 */
	public Point2D toCartesian(final Point2D p);
}
