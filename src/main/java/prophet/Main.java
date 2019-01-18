package prophet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import prophet.gui.ProphetGUI;
import prophet.gui.layers.GraphLayer;
import prophet.gui.layers.GridLayer;
import prophet.model.IMap;
import prophet.model.IWorld;
import prophet.model.SimpleMap;
import prophet.util.Logger;

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
		PrintStream logStream;
		try {
			logStream = new PrintStream(Prophet.class.getSimpleName().toLowerCase()+".log");
			Logger.addStream(logStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		final Prophet prophet = new Prophet();

		//temporary shit
		final IWorld world = prophet.getSetting().getWorld();
		final IMap map = new SimpleMap();
		map.setPicturePath("/earth.jpg");
		map.setScale(world.getCirconference()/map.getPicture().getWidth());
		prophet.getSetting().getWorld().addMap(map);

		//final IMap map2 = new SimpleMap();
		//map2.setPicturePath("/map-full.jpg");
		//map2.setScale(world.getCirconference()/map2.getPicture().getWidth());
		//prophet.getSetting().getWorld().addMap(map2);
		prophet.getRenderer().addLayer(new GraphLayer(prophet.getRenderer()));
		prophet.getRenderer().addLayer(new GridLayer(prophet.getRenderer()));
		//----
		
		final ProphetGUI gui = new ProphetGUI(prophet);
		gui.show();
	}
}
