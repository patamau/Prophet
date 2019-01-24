package prophet.model;

public interface IBorder extends IPolygon, IObservable {

	public String getName();
	public void setName(final String name);
	
	public void addPoint(final double x, final double y);
	public void removeLastPoint();
	public void clearPoints();
}
