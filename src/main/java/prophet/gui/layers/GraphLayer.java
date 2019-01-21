package prophet.gui.layers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prophet.gui.IRenderer;

public class GraphLayer extends LayerBase {

	private final IRenderer renderer;
	
	//graph structure
	private final List<Point> points;
	private final Map<Point, List<Point>> pointMap;
	
	public GraphLayer(final IRenderer renderer) {
		this.renderer = renderer;
		this.points = new ArrayList<Point>();
		this.pointMap = new HashMap<Point, List<Point>>();
	}
	
	private void updateGraph() {
		this.points.clear();
		this.pointMap.clear();
		
		Point a = new Point(0,100);
		Point b = new Point(6000, 120);
		Point c = new Point(6500, 2520);
		Point d = new Point(5800, -2000);
		points.add(a);
		points.add(b);
		points.add(c);
		points.add(d);
		
		List<Point> apoints = new ArrayList<Point>();
		apoints.add(b);
		pointMap.put(a, apoints);
		List<Point> bpoints = new ArrayList<Point>();
		bpoints.add(c);
		bpoints.add(d);
		pointMap.put(b, bpoints);
	}
	
	private void drawGraph(final Graphics2D g) {
		for(Point a : points)
		{
			List<Point> alist = pointMap.get(a);
			if(null == alist) continue;
			for(Point b : alist)
			{
				g.drawLine(renderer.getScreenX(a.x), renderer.getScreenY(a.y),
						renderer.getScreenX(b.x), renderer.getScreenY(b.y));
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		Stroke oldStroke = g.getStroke();
		updateGraph();
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(12f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
		drawGraph(g);
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(10f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
		drawGraph(g);
		g.setStroke(oldStroke);
	}

}
