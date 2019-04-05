package prophet.model;

import java.util.List;

import prophet.geom.Point2D;

public interface IPolygon {

	public String getName();
	public void setName(final String name);

	public void getPoints(final List<Point2D> outPoints);
	
	public Point2D getCenter();
}
