package prophet.gui.layers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import prophet.geom.Point2D;
import prophet.gui.IRenderer;
import prophet.model.IPoi;
import prophet.util.Logger;

public class IconsLayer extends LayerBase {

	private static final Logger logger = Logger.getLogger(IconsLayer.class);
	
	private final IRenderer renderer;
	private final List<IPoi> points;
	private Image icon;
	private double scale;
	
	public IconsLayer (final String name, final IRenderer renderer) {
		super(name);
		points = new ArrayList<IPoi>();
		this.renderer = renderer;
		this.scale = 1d;
	}

	public void setIcon(final Image icon) {
		this.icon = icon;
	}

	public void addPoint(final IPoi p) {
		synchronized(points) {
			points.add(p);
		}
	}
	
	public void removePoint(final IPoi p) {
		synchronized (points) {
			points.remove(p);
		}
	}
	
	public void clearPoints() {
		synchronized (points) {
			points.clear();
		}
	}

	@Override
	public void draw(final Graphics2D g) {
		final Point offset = renderer.getOffset();
		double zoom = renderer.getZoom() * scale;
		if(zoom>1d) zoom = 1d;
		if(zoom<0.1d) return;
		synchronized(points) {
			for(final IPoi poi: points) {
				final AffineTransform at = new AffineTransform();
				final Point2D p = poi.getWorldPosition();
				final double x = offset.getX()+(p.getX()*renderer.getZoom())-((double)icon.getWidth(null)*zoom)/2d;
				final double y = offset.getY()+(p.getY()*renderer.getZoom())-((double)icon.getHeight(null)*zoom)/2d;
				at.translate(x, y);
				at.scale(zoom, zoom);
				if(renderer.isSelected(poi)) {
					g.setColor(Color.MAGENTA);
				} else {
					g.setColor(Color.BLACK);
				}
				g.drawImage(icon, at, null);
				g.drawString(poi.getName(), (float)x, (float)y);
			}
		}
	}
}
