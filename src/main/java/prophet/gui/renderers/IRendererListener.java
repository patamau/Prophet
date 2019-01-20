package prophet.gui.renderers;

import java.awt.Point;

public interface IRendererListener {

	public void onZoomChanged(final double zoom);
	public void onMouseMoved(final Point point);
}
