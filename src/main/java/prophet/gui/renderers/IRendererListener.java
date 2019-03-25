package prophet.gui.renderers;

import java.awt.Point;

import prophet.gui.ILayer;

public interface IRendererListener {

	public void onZoomChanged(final double zoom);
	public void onMouseMoved(final Point point);
	
	public void onLayerAdded(final ILayer layer);
	public void onLayerRemoved(final ILayer layer);
	public void onLayersCleared();
	public void onLayersChanged();
}
