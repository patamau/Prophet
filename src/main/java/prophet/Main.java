package prophet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import prophet.gui.ProphetGUI;
import prophet.gui.layers.GraphLayer;
import prophet.gui.layers.GridLayer;
import prophet.model.IMap;
import prophet.model.IWorld;
import prophet.model.SimpleMap;
import prophet.model.SimpleWorld;

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
		final Prophet prophet = new Prophet();

		//temporary shit
		final IWorld world = prophet.getSetting().getWorld();
		final IMap map = new SimpleMap();
		map.setPicturePath("/earth.jpg");
		map.setScale(world.getCirconference()/map.getPicture().getWidth());
		prophet.getSetting().getWorld().addMap(map);
		prophet.getRenderer().addLayer(new GraphLayer(prophet.getRenderer()));
		prophet.getRenderer().addLayer(new GridLayer(prophet.getRenderer()));
		//----
		
		final ProphetGUI gui = new ProphetGUI(prophet);
		gui.show();
	}
}
