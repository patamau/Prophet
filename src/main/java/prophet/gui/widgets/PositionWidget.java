package prophet.gui.widgets;

import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import prophet.gui.ILayer;
import prophet.gui.IRenderer;
import prophet.gui.renderers.IRendererListener;
import prophet.model.IWorld;
import prophet.util.Language;

@SuppressWarnings("serial")
public class PositionWidget extends ToolBarWidgetBase implements IRendererListener {
	
	public static final String WTITLE = "Position";
	
	private final IRenderer renderer;
	private final IWorld world;
	private final JLabel zoomLabel, positionLabel, polarLabel;
	
	public PositionWidget(final IRenderer renderer, final IWorld world) {
		super(Language.string(WTITLE));
		this.renderer = renderer;
		this.world = world;
		renderer.addListener(this);
		zoomLabel = new JLabel("1x");
		zoomLabel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		positionLabel = new JLabel("0,0");
		positionLabel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		polarLabel = new JLabel("0,0");
		polarLabel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		add(zoomLabel);
		add(new JSeparator(JSeparator.VERTICAL));
		add(positionLabel);
		add(new JSeparator(JSeparator.VERTICAL));
		add(polarLabel);
	}

	@Override
	public void onZoomChanged(final double zoom) {
		zoomLabel.setText(String.format("%.3f", zoom)+"x");
		zoomLabel.invalidate();
	}

	@Override
	public void onMouseMoved(final Point point) {
		final double wx = renderer.getWorldX(point.x);
		final double wy = renderer.getWorldY(point.y);
		positionLabel.setText(String.format("%.0f, %.0f", wx, wy));
		positionLabel.invalidate();
		final double px = world.toLongitude(wx);
		final double py = world.toLatitude(wy);
		polarLabel.setText(String.format("%.2f� LON, %.2f� LAT", px, py));
	}

	@Override
	public void onLayerAdded(ILayer layer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLayerRemoved(ILayer layer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLayersCleared() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLayersChanged() {
		// TODO Auto-generated method stub
		
	}
}
