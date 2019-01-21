package prophet.gui.layers;

import java.awt.Color;
import java.awt.Graphics2D;

import prophet.gui.IRenderer;

public class GridLayer extends LayerBase {
	
	public static final int CROSS_SIZ = 10; //pixels
	
	private final IRenderer renderer;
	
	
	public GridLayer(final IRenderer renderer)
	{
		this.renderer = renderer;
	}
	
	private void drawGrid(final Graphics2D g, final double step)
	{
		//draw the grid depending on zoom level
		double wsiz = 40000*1000; //meters
		double hsiz = wsiz * 0.5d;
		double hhsiz = hsiz * 0.5d;
		for (double x = -hsiz; x < hsiz; x+=step) {				
			int _x = renderer.getScreenX(x);
			if(_x<0) continue;
			if(_x>renderer.getWidth()) break;
			for (double y = -hhsiz; y < hhsiz; y+=step) {
				int _y = renderer.getScreenY(y);
				if(_y<0) break;
				if(_y>renderer.getHeight()) continue;
				//only draw if inside renderer
				g.drawLine(_x, _y - CROSS_SIZ, _x, _y + CROSS_SIZ);
				g.drawLine(_x + CROSS_SIZ, _y, _x - CROSS_SIZ, _y);
			}
		}
	}
	
	private double computeOptimalGridStep()
	{ 
		//the grid step to draw in meters
		double gridStep = 10d; //in meters
		int pixelSize; //in pixels

		//keep computing pixel size until in range
		for(;;) {
			pixelSize = renderer.getScreenSize(gridStep);
			if(pixelSize > 300) {
				gridStep *= 0.5d;
			} else if (pixelSize < 150) {
				gridStep *= 2d;
			} else {
				break;
			}
			if(1d >= gridStep)
			{
				gridStep = 1d;
				break;
			}
		}
		System.err.println("using grid step "+gridStep+" at pixel size "+pixelSize);
		return gridStep;
	}

	@Override
	public void draw(final Graphics2D g) {
		g.setColor(Color.BLACK);

		drawGrid(g, computeOptimalGridStep());
	}

}
