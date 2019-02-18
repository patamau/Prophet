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
	
	public static double getDistSqrdToSegmentXY(final double x, final double y, final Point2D a, final Point2D b) {
		final double abx = b.getX() - a.getX(); 
		final double aby = b.getY() - a.getY();
		final double r = ((x - a.getX()) * abx + (y - a.getY()) * aby) / ((abx * abx) + (aby * aby));
		if (0d >= r) {
			return a.distanceSq(x, y);
		} else if (1d <= r) {
			return b.distanceSq(x, y);
		} else {
			// find point within the segment
			final double rx = a.getX() + r * abx, ry = a.getY() + r * aby;
			final Point2D rv = new Point2D.Double(rx, ry);
			return rv.distanceSq(x, y);
		}
	}
	
	public int getNearestSegment(final double x, final double y) {
		if(points.size()<3) return 0;
		double mindist = Double.MAX_VALUE;
		double d;
		int nearest = -1;
		int i = 0;
		Point2D prev = points.get(points.size()-1);
		synchronized (points) {
			for(Point2D p : points) {
				d = getDistSqrdToSegmentXY(x, y, prev, p);
				if (d < mindist) {
					nearest = i;
					mindist = d;
				}
				++i;
				prev = p;
			}
		}
		return nearest;
	}
	
	@Override
	public Point2D getNearestPoint(final double x, final double y, final double maxDist) {
		double mindist = maxDist*maxDist;
		if(mindist < 0d) mindist = Double.MAX_VALUE;
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
	public int getNearestPointIndex(double x, double y, double maxDist) {
		double mindist = maxDist*maxDist;
		if(mindist < 0d) mindist = Double.MAX_VALUE;
		double d;
		int nearest = -1;
		int i = 0;
		synchronized (points) {
			for(Point2D p : points) {
				d = p.distanceSq(x, y);
				if (d < mindist) {
					nearest = i;
					mindist = d;
				}
				++i;
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

	@Override
	public void addPoint(int idx, double x, double y) {
		synchronized (points) {
			points.add(idx, new Point2D.Double(x, y));
		}
		setChanged();
	}

	@Override
	public int indexOf(Point2D p) {
		synchronized(points) {
			return points.indexOf(p);
		}
	}

	@Override
	public Point2D getPoint(final int idx) {
		final int i = idx % points.size();
		synchronized(points) {
			return points.get(i);
		}
	}
	
	@Override
	public Point2D getCenter() {
		double minx = Double.MAX_VALUE, miny = Double.MAX_VALUE;
		double maxx = -Double.MAX_VALUE, maxy = -Double.MAX_VALUE;
		synchronized (points) {
			for(Point2D p : points) {
				final double x = p.getX();
				final double y = p.getY();
				if(x<minx) minx = x;
				if(x>maxx) maxx = x;
				if(y<miny) miny = y;
				if(y>maxy) maxy = y;
			}
		}
		return new Point2D.Double(minx+(maxx-minx)*0.5, miny+(maxy-miny)*0.5);
	}

	@Override
	public int size() {
		return points.size();
	}
}
