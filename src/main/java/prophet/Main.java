package prophet;


import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import prophet.gui.layers.GraphLayer;
import prophet.gui.layers.GridLayer;
import prophet.gui.layers.PictureLayer;
import prophet.gui.renderers.WorldRendererComponent;
import prophet.model.World;

public class Main {
	
	private static BufferedImage loadImage(String resource) throws IOException
	{
		final File f = new File(resource);
		final URL url;
		if (!f.exists()) {
			url = Main.class.getResource(resource);
		} else {
			url = f.toURI().toURL();
		}
		return ImageIO.read(url);
	}

	public static void main(String args[])
	{
		final String mapFilename = "/map-full.jpg";
		final String mapFilename2 = "/map-acquimontana.png";
		final BufferedImage mapImage;
		final BufferedImage mapImage2;
		
		try {
			mapImage = loadImage(mapFilename);
			mapImage2 = loadImage(mapFilename2);
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final World world = new World(6378000d);
		final WorldRendererComponent globe = new WorldRendererComponent(world);
		globe.addLayer(new PictureLayer(globe, mapImage, 1000d, world.toCartesian(new Point2D.Double(0d, 45d))));
		globe.addLayer(new PictureLayer(globe, mapImage2, 400d, world.toCartesian(new Point2D.Double(25d, 45d))));
		globe.addLayer(new GraphLayer(globe));
		globe.addLayer(new GridLayer(globe));
		frame.add(globe);
		frame.setSize(400, 300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
