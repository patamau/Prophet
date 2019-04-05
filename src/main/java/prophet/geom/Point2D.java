package prophet.geom;

public class Point2D {

	private double x, y;
	
	public Point2D() {
		x = y = 0d;
	}
	
	public Point2D(final double x, final double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(final double x) {
		this.x = x;
	}
	
	public void setY(final double y) {
		this.y =y;
	}
	
	public void set(final Point2D p) {
		set(p.x, p.y);
	}
	
	public void set(final double x, final double y) {
		this.x = x;
		this.y = y;
	}
	
	public double distanceSq(final Point2D p) {
		return distanceSq(p.x, p.y);
	}
	
	public double distanceSq(final double x, final double y) {
		final double dx = this.x - x;
		final double dy = this.y - y;
		return dx*dx + dy * dy;
	}
}
