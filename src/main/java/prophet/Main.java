package prophet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;

import javax.imageio.ImageIO;

import prophet.gui.ProphetGUI;
import prophet.gui.widgets.BordersFloatingWidget;
import prophet.gui.widgets.BordersWidget;
import prophet.gui.widgets.LayersFloatingWidget;
import prophet.gui.widgets.LayersWidget;
import prophet.gui.widgets.MapsFloatingWidget;
import prophet.gui.widgets.PositionWidget;
import prophet.gui.widgets.TownsFloatingWidget;
import prophet.gui.widgets.TownsWidget;
import prophet.util.Configuration;
import prophet.util.Logger;
import prophet.util.Options;

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
		
		prophet.initialize();
		
		final ProphetGUI gui = new ProphetGUI(prophet);
		gui.addWidget(new PositionWidget(prophet.getRenderer(), prophet.getSetting().getWorld()), ProphetGUI.COMPONENT_BOTTOM);
		//gui.addWidget(/*toolbox*/, ProphetGUI.COMPONENT_TOP);
		
		gui.addWidget(new MapsFloatingWidget(prophet.getSetting(), prophet.getRenderer(), gui.getFrame()));
		gui.addWidget(new TownsFloatingWidget(prophet.getSetting(), prophet.getRenderer(), gui.getFrame()));
		gui.addWidget(new BordersFloatingWidget(prophet.getSetting(), prophet.getRenderer(), gui.getFrame()));
		gui.addWidget(new LayersFloatingWidget(prophet.getRenderer(), gui.getFrame()));
		gui.show();
	}
}
