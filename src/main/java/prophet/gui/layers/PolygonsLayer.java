package prophet.gui.layers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import prophet.gui.IRenderer;
import prophet.model.IPolygon;

public class PolygonsLayer extends LayerBase {

	private final IRenderer renderer;
	
	//graph structure
	private final List<IPolygon> polygons;
	
	public PolygonsLayer(final IRenderer renderer) {
		this.renderer = renderer;
		this.polygons = new ArrayList<IPolygon>();
	}
	
	private void drawPolygons(final Graphics2D g) {
		final List<Point2D> points = new LinkedList<Point2D>();
		Point2D prev;
		synchronized(polygons) {
			for(final IPolygon poly : polygons)
			{
				poly.getPoints(points);
				if(points.size()==0) continue;
				prev = points.get(points.size()-1);
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
