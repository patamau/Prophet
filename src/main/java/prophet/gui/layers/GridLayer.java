package prophet.gui.layers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import prophet.gui.IRenderer;
import prophet.model.IWorld;
import prophet.util.Language;
import prophet.util.Logger;

public class GridLayer extends LayerBase {
	
	private static final Logger logger = Logger.getLogger(GridLayer.class);
	
	public static final int CROSS_SIZ = 10; //pixels
	public static final double GRID_STEP_MIN = 0.001d; //degrees
	public static final Color GRID_COLOR = new Color(0, 0, 0, 250);
	public static DecimalFormat COORDS_FORMAT = new DecimalFormat("#.##");
	
	private final IRenderer renderer;
	private final IWorld world;
	
	public GridLayer(final IRenderer renderer, final IWorld world)
	{
		super(Language.string("Grid"));
		this.renderer = renderer;
		this.world = world;
	}
	
	private void drawGrid(final Graphics2D g, final double step)
	{
		//draw the grid depending on zoom level
		String crstr;
		g.setColor(new Color(0,0,0,100));
		for (double x = -180; x <= 180; x+=step) {
			int _x = renderer.getScreenX(world.fromLongitude(x));
			if(_x<0) continue;
			if(_x>renderer.getWidth()) break;
			g.drawLine(_x, 0 - CROSS_SIZ, _x, 0 + CROSS_SIZ);
			g.drawLine(_x + CROSS_SIZ, 0, _x - CROSS_SIZ, 0);
			crstr = COORDS_FORMAT.format(x);
			g.drawString(crstr, _x + 3, 12);

			g.drawLine(_x, renderer.getHeight() - CROSS_SIZ, _x, renderer.getHeight()  + CROSS_SIZ);
			g.drawLine(_x + CROSS_SIZ,  renderer.getHeight() , _x - CROSS_SIZ,  renderer.getHeight() );
			crstr = COORDS_FORMAT.format(x);
			g.drawString(crstr, _x + 3, renderer.getHeight() - 2);
			for (double y = -90; y <= 90; y+=step) {
				int _y = renderer.getScreenY(world.fromLatitude(y));
				if(_y<0) break;
				if(_y>renderer.getHeight()) continue;
				g.drawLine(_x, _y - CROSS_SIZ, _x, _y + CROSS_SIZ);
				g.drawLine(_x + CROSS_SIZ, _y, _x - CROSS_SIZ, _y);
				//crstr = COORDS_FORMAT.format(x) + "x" + COORDS_FORMAT.format(y);
				//g.drawString(crstr, _x + 3, _y - 2);
			}
		}
		
		for (double y = -90; y <= 90; y+=step) {
			int _y = renderer.getScreenY(world.fromLatitude(y));
			if(_y<0) break;
			if(_y>renderer.getHeight()) continue;
			g.drawLine(0, _y - CROSS_SIZ, 0, _y + CROSS_SIZ);
			g.drawLine(0 + CROSS_SIZ, _y, 0 - CROSS_SIZ, _y);
			crstr = COORDS_FORMAT.format(y);
			g.drawString(crstr, 0 + 3, _y - 2);
			g.drawLine(renderer.getWidth(), _y - CROSS_SIZ, renderer.getWidth(), _y + CROSS_SIZ);
			g.drawLine(renderer.getWidth() + CROSS_SIZ, _y, renderer.getWidth() - CROSS_SIZ, _y);
			crstr = COORDS_FORMAT.format(y);
			g.drawString(crstr, renderer.getWidth() - 25, _y - 2);
		}
	}
	
	private double computeOptimalGridStep()
	{ 
		//the grid step to draw in meters
		double gridStep = 10d; //in meters
		int pixelSize; //in pixels

		//keep computing pixel size until in range
		for(;;) {
			pixelSize = renderer.getScreenSize(world.fromLatitude(gridStep));
			if(pixelSize > 300) {
				gridStep *= 0.5d;
			} else if (pixelSize < 150) {
				gridStep *= 2d;
			} else {
				break;
			}
			if(GRID_STEP_MIN >= gridStep)
			{
				gridStep = GRID_STEP_MIN;
				break;
			}
		}
		logger.debug("grid step is ",gridStep);
		return gridStep;
	}

	@Override
	public void draw(final Graphics2D g) {
		g.setColor(Color.BLACK);

		drawGrid(g, computeOptimalGridStep());
	}
}
