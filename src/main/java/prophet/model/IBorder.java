package prophet.model;

public interface IBorder extends IPolygon, IObservable {
	
	public void addPoint(final double x, final double y);
	public void removeLastPoint();
	public void clearPoints();
}
