package prophet;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;

import javax.imageio.ImageIO;

import prophet.gui.ProphetGUI;
import prophet.gui.layers.GraphLayer;
import prophet.gui.layers.GridLayer;
import prophet.gui.layers.IconsLayer;
import prophet.gui.layers.PolygonsLayer;
import prophet.gui.widgets.MapsWidget;
import prophet.gui.widgets.PositionWidget;
import prophet.gui.widgets.TownsWidget;
import prophet.model.IMap;
import prophet.model.IWorld;
import prophet.model.SimpleBorder;
import prophet.model.SimpleMap;
import prophet.util.Configuration;
import prophet.util.Logger;
import prophet.util.Options;
import prophet.util.Resources;

public class Main {
	
	private static final Logger logger = Logger.getLogger(Main.class);
	
	public static final String 
			OPTKEY_LOGFILENAME = "logfile", 
			OPTKEY_LOGLEVEL = "loglevel",
			OPT_DEBUG = "debug";
	
	public static final String
			CFG_FILENAME = "prophet.cfg";
	
	private static void printSystemProperty(String key){
		logger.debug(key+" "+System.getProperty(key));
	}
	
	private static void printSystemProperties(){
		printSystemProperty("os.name");
		printSystemProperty("os.version");
		printSystemProperty("os.arch");
		printSystemProperty("java.version");
		printSystemProperty("java.vendor");
		printSystemProperty("java.class.path");
		printSystemProperty("java.library.path");
	}
	
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
		//parse arguments
		final Options opts = new Options(args);
		//configure logging
		if(opts.isSet(OPT_DEBUG)) {
			//verbose console for debugging
			Logger.addStream(System.out);
		} else {
			final String logfilename = opts.getOption(Main.OPTKEY_LOGFILENAME, Prophet.class.getSimpleName().toLowerCase()+".log");
			final PrintStream logStream;
			try {
				logStream = new PrintStream(logfilename);
				Logger.addStream(logStream);
			} catch (FileNotFoundException e) {
				System.err.println("Unfortunately it was not possible to configure logging to "+logfilename);
				e.printStackTrace();
				//we can keep on going with console only (-_-)
				Logger.addStream(System.out);
			}
		}
		//print all the stuff we might want to check when looking for errors on user systems
		printSystemProperties();
		//force log level
		final int loglevel = Logger.toLevel(opts.getOption(Main.OPTKEY_LOGLEVEL, Logger.Levels[opts.isSet(OPT_DEBUG) ? Logger.L_DEBUG : Logger.L_INFO]));
		Logger.setLevel(loglevel);
		//load configuration
		Configuration.getGlobalConfiguration().load(CFG_FILENAME);
		
		final Prophet prophet = new Prophet();
		Thread.setDefaultUncaughtExceptionHandler(prophet);
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				logger.debug("Shutdown hook called...");
				prophet.finalize();
			}
		});

		//temporary shit ----
		final IWorld world = prophet.getSetting().getWorld();
		final IMap map = new SimpleMap();
		map.setPicturePath("maps/earth.jpg");
		map.setScale(world.getCirconference()/map.getPicture().getWidth());
		prophet.getSetting().getWorld().addMap(map);

		//final IMap map2 = new SimpleMap();
		//map2.setPicturePath("/map-full.jpg");
		//map2.setScale(world.getCirconference()/map2.getPicture().getWidth());
		//prophet.getSetting().getWorld().addMap(map2);
		prophet.getRenderer().addLayer(new GraphLayer(prophet.getRenderer()));
		prophet.getRenderer().addLayer(new GridLayer(prophet.getRenderer()));
		final PolygonsLayer pl = new PolygonsLayer(prophet.getRenderer());
		final SimpleBorder border = new SimpleBorder();
		border.addPoint(0, 0);
		border.addPoint(20, 10);
		border.addPoint(10, 15);
		border.addPoint(0, 15);
		pl.addPolygon(border);
		prophet.getRenderer().addLayer(pl);
		final IconsLayer il = new IconsLayer(prophet.getRenderer());
		il.setIcon(Resources.getImage("icons/town.png"));
		il.addPosition(new Point(10,20));
		il.addPosition(new Point(100,220));
		il.addPosition(new Point(-90,-80));
		prophet.getRenderer().addLayer(il);
		//---  ----
		
		final ProphetGUI gui = new ProphetGUI(prophet);
		gui.addWidget(new PositionWidget(prophet.getRenderer(), prophet.getSetting().getWorld()), ProphetGUI.COMPONENT_BOTTOM);
		gui.addWidget(new MapsWidget(prophet.getSetting()), ProphetGUI.COMPONENT_LEFT);
		gui.addWidget(new TownsWidget(prophet.getSetting()), ProphetGUI.COMPONENT_LEFT);
		gui.show();
	}
}
