package prophet.model;

import java.awt.geom.Point2D;

/**
 * This simplistic world approach is used to intuitively support flat coordinates system commonly used for visualization.
 * It implies the surface curvature is not relevant and provides hints rather than actual world coordinates.
 * @author patam
 *
 */
public class SimpleWorld implements IWorld {

	public static final double WORLD_RADIUS_DEF = 6300;
	
	private double radius;
	private double circonference, circonference2, circonference4;
	
	public SimpleWorld() {
		this(WORLD_RADIUS_DEF);
	}
	
	public SimpleWorld(final double radius) {
		setRadius(radius);
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
