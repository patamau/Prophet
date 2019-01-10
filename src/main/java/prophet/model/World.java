package prophet.model;

import java.awt.geom.Point2D;

public class World {

	private double radius;
	private double circonference, circonference2, circonference4;
	
	public World(final double radius) {
		setRadius(radius);
	}
	
	public void setRadius(final double radius) {
		this.radius = radius;
		this.circonference2 = radius * Math.PI;
		this.circonference = 2 * circonference2;
		this.circonference4 = 0.5 * circonference2;
	}
	
	/**
	 * Retrieve latitude angle for the given y coordinate
	 * @param y
	 * @return the latitude angle in degrees
	 */
	public double toLatitude(final double y) {
		return y/circonference4 * 90d;
	}
	
	/**
	 * Retrieve longitude angle for the given x coordinate
	 * @param x
	 * @return the longitude angle in degrees
	 */
	public double toLongitude(final double x) {
		return x/circonference2 * 180d;
	}
	
	/**
	 * Retrieve vertical cartesian coordinate for the given latitude
	 * @param latitude angle in degrees
	 * @return
	 */
	public double fromLatitude(final double latitude) {
		return latitude/90d * circonference4;
	}
	
	/**
	 * Retrieve horizontal cartesian coordinate from the given longitude
	 * @param longitude angle in degrees
	 * @return
	 */
	public double fromLongitude(final double longitude) {
		return longitude/180d * circonference2;
	}
	
	public Point2D toPolar(final Point2D p)
	{
		return new Point2D.Double(toLongitude(p.getX()), toLatitude(p.getY()));
	}
	
	public Point2D toCartesian(final Point2D p)
	{
		return new Point2D.Double(fromLongitude(p.getX()), fromLatitude(p.getY()));
	}
	
}
