package prophet.model;

import prophet.geom.Point2D;

public interface IBorder extends IPolygon, IObservable {
	
	public Point2D getNearestPoint(final double x, final double y, final double maxDist);
	public int getNearestPointIndex(final double x, final double y, final double maxDist);
	public int getNearestSegment(final double x, final double y);
	
	public void addPoint(final Point2D p);
	public void insertPoint(final int idx, final Point2D p);
	public void removePoint(final Point2D p);
	
	public int indexOf(final Point2D p);
	public Point2D getPoint(final int idx);
	public Point2D getLastPoint();
	public void clearPoints();
	
	/**
	 * How many points
	 * @return
	 */
	public int size();
}
