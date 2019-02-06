package prophet.model;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class SimpleBorder extends Observable implements IBorder {

	private final List<Point2D> points;
	private String name;
	
	public SimpleBorder() {
		points = new LinkedList<Point2D>();
		this.name = "Unknown";
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		if(this.name == name) return;
		this.name = name;
		setChanged();
	}

	@Override
	public void getPoints(final List<Point2D> outPoints) {
		synchronized(points) {
			outPoints.addAll(points);
		}
	}

	@Override
	public void addPoint(double x, double y) {
		synchronized (points) {
			points.add(new Point2D.Double(x, y));
		}
		setChanged();
	}

	@Override
	public void removeLastPoint() {
		synchronized (points) {
			if(points.size()>0) {
				points.remove(points.size()-1);
			}
		}
		setChanged();
	}

	@Override
	public void clearPoints() {
		synchronized (points) {
			points.clear();
		}
		setChanged();
	}
}
