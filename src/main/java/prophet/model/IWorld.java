package prophet.model;

import java.awt.geom.Point2D;

public interface IWorld {

	public void addTown(final ITown town);
	
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
