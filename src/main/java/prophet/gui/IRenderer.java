package prophet.gui;

import java.awt.Graphics2D;
import java.awt.Point;

import prophet.gui.renderers.IRendererListener;

public interface IRenderer {
	
	public void addListener(final IRendererListener listener);

	public void addLayer(final ILayer layer);
	public void removeLayer(final ILayer layer);
	public void clearLayers();
	
	public void drawLayers(final Graphics2D g);
	
	public void setZoom(final double z);
	public double getZoom();
	public int getScreenX(final double x);
	public int getScreenY(final double y);
	public double getWorldX(final int x);
	public double getWorldY(final int y);
	public int getScreenSize(final double size);
	public Point getOffset();
	
	public int getWidth();
	public int getHeight();
}
