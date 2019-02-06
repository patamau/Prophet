package prophet.model;

import java.awt.geom.Point2D;
import java.util.List;

public interface IPolygon {

	public String getName();
	public void setName(final String name);

	public void getPoints(final List<Point2D> outPoints);
}
