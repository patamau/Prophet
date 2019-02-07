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
	public Point2D getNearestPoint(final double x, final double y, final double maxDist) {
		double mindist = maxDist;
		double d;
		Point2D nearest = null;
		synchronized (points) {
			for(Point2D p : points) {
				d = p.distanceSq(x, y);
				if (d < mindist) {
					nearest = p;
					mindist = d;
				}
			}
		}
		return nearest;
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
	public void removePoint(Point2D p) {
		synchronized (points) {
			points.remove(p);
		}
		setChanged();
	}

	@Override
	public Point2D getLastPoint() {
		synchronized (points) {
			if(points.size()>0) {
				return points.get(points.size()-1);
			}
		}
		return null;
	}

	@Override
	public void clearPoints() {
		synchronized (points) {
			points.clear();
		}
		setChanged();
	}
}
