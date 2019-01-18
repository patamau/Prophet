package prophet.util;

import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Logger {
	
	public static final int 
		L_DEBUG = 0, //useful information for developers
		L_INFO = 1, //useful information for users
		L_WARN = 2, //an application problem (could be solved)
		L_ERROR = 3; //an application error
	
	private static final String[] _levels = new String[]{
		"DEBUG",
		"INFO ",
		"WARN ",
		"ERROR"
	};

	private final static Map<String, Logger> loggers = new HashMap<String,Logger>();
	private final static StringBuilder sbuild = new StringBuilder();
	private static int LEVEL = L_DEBUG;
	private final static List<PrintStream> streams = new LinkedList<PrintStream>();
	private final static RuntimeMXBean runtimeMan = ManagementFactory.getRuntimeMXBean();
	
	static{
		streams.add(System.out);
	}
	
	private final String name;
	
	private Logger(final String name){
		this.name = name;
	}
	
	public static void setLevel(final int level){
		LEVEL = level;
	}
	
	public static void addStream(final PrintStream output){
		synchronized(streams) {
			streams.add(output);
		}
	}
	
	public static void removeStream(final PrintStream output){
		synchronized(streams) {
			streams.remove(output);
		}
	}
	
	public static void clearStreams(){
		synchronized(streams) {
			streams.clear();
		}
	}
	
	public static Logger getLogger(final Class<?> clazz){
		return getLogger(clazz.getName());
	}
	
	public static Logger getLogger(final String name){
		Logger logger = loggers.get(name);
		if(null==logger){
			logger = new Logger(name);
			loggers.put(name, logger);
		}
		return logger;
	}
	
	private static void print(final String msg){
		synchronized(streams) {
			for(PrintStream p: streams){
				p.println(msg);
			}
		}
	}
	
	private void prepareLogEntry(final int level) {
		sbuild.setLength(0);
		sbuild.append('[');
		sbuild.append(_levels[level]);
		sbuild.append("]@");
		sbuild.append(runtimeMan.getUptime());
		sbuild.append(' ');
		sbuild.append(name);
		sbuild.append(" - ");
	}
	
	private void log(final int level, final Object message) {
		if(LEVEL>level) return;
		synchronized(sbuild){
			prepareLogEntry(level);
			sbuild.append(message);
			print(sbuild.toString());
		}
	}
	
	private void log(final int level, final Object ... messages) {
		if(LEVEL>level) return;
		synchronized(sbuild){
			prepareLogEntry(level);
			for(Object m: messages){
				sbuild.append(m);
			}
			print(sbuild.toString());
		}
	}
	
	public void debug(final String msg){
		if(LEVEL>L_DEBUG) return;
		log(L_DEBUG, msg);
	}
	
	public void debug(final Object ... messages){
		if(LEVEL>L_DEBUG) return;
		log(L_DEBUG, messages);
	}
	
	public void info(final String msg){
		if(LEVEL>L_INFO) return;
		log(L_INFO, msg);
	}
	
	public void info(final Object ... messages){
		if(LEVEL>L_INFO) return;
		log(L_INFO, messages);
	}
	
	public void warning(final String msg){
		if(LEVEL>L_WARN) return;
		log(L_WARN, msg);
	}
	
	public void warning(final Object ... messages){
		if(LEVEL>L_WARN) return;
		log(L_WARN, messages);
	}
	
	public void error(final String msg){
		if(LEVEL>L_ERROR) return;
		log(L_ERROR, msg);
	}
	
	public void error(final Object ... messages){
		if(LEVEL>L_ERROR) return;
		log(L_ERROR, messages);
	}
}
