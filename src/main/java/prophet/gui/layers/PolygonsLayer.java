package prophet.gui.layers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import prophet.geom.Point2D;
import prophet.gui.IRenderer;
import prophet.model.IPolygon;

public class PolygonsLayer extends LayerBase {

	private final IRenderer renderer;
	
	private final List<IPolygon> polygons;
	private transient final List<Point2D> points; //temporary list
	
	public PolygonsLayer(final String name, final IRenderer renderer) {
		super(name);
		this.renderer = renderer;
		this.polygons = new ArrayList<IPolygon>();
		//temporary list of points
		this.points = new LinkedList<Point2D>();
	}
	
	private void drawPolygons(final Graphics2D g) {
		Point2D prev;
		synchronized(polygons) {
			for(final IPolygon poly : polygons)
			{
				poly.getPoints(points);
				if(points.size()==0) continue;
				prev = points.get(points.size()-1);
				final Point2D center = poly.getCenter();
				g.drawString(poly.getName(), renderer.getScreenX(center.getX()), renderer.getScreenY(center.getY()));
				for(final Point2D p : points)
				{
					g.drawLine(renderer.getScreenX(prev.getX()), renderer.getScreenY(prev.getY()),
							renderer.getScreenX(p.getX()), renderer.getScreenY(p.getY()));
					prev = p;
				}
				points.clear();
			}
		}
	}
	
	public void addPolygon(final IPolygon p) {
		synchronized(polygons) {
			polygons.add(p);
		}
	}
	
	public void removePolygon(final IPolygon p) {
		synchronized (polygons) {
			polygons.remove(p);
		}
	}
	
	public void clearPolygons() {
		synchronized (polygons) {
			polygons.clear();
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		Stroke oldStroke = g.getStroke();
		g.setColor(Color.MAGENTA);
		g.setStroke(new BasicStroke(4f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
		drawPolygons(g);
		g.setStroke(oldStroke);
	}

}
