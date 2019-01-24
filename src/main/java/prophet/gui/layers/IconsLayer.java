package prophet.gui.layers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import prophet.gui.IRenderer;
import prophet.util.Logger;

public class IconsLayer extends LayerBase {

	private static final Logger logger = Logger.getLogger(IconsLayer.class);
	
	private final IRenderer renderer;
	private final List<Point> points;
	private Image icon;
	private double scale;
	
	public IconsLayer (final IRenderer renderer) {
		points = new ArrayList<Point>();
		this.renderer = renderer;
		this.scale = 1d;
	}

	public void setIcon(final Image icon) {
		this.icon = icon;
	}

	public void addPosition(final Point p) {
		synchronized(points) {
			points.add(p);
		}
	}

	@Override
	public void draw(final Graphics2D g) {
		final Point offset = renderer.getOffset();
		double zoom = renderer.getZoom() * scale;
		if(zoom>1d) zoom = 1d;
		if(zoom<0.1d) return;
		synchronized(points) {
			for(final Point p: points) {
				final AffineTransform at = new AffineTransform();
				at.translate(offset.getX()+(p.x*renderer.getZoom()), offset.getY()-(p.y*renderer.getZoom()));
				at.scale(zoom, zoom);
				g.drawImage(icon, at, null);
			}
		}
	}
}
