package prophet.model;

import java.awt.geom.Point2D;

public interface IBorder extends IPolygon, IObservable {
	
	public Point2D getNearestPoint(final double x, final double y, final double maxDist);
	
	public void addPoint(final double x, final double y);
	public void removePoint(final Point2D p);
	
	public Point2D getLastPoint();
	public void clearPoints();
}
