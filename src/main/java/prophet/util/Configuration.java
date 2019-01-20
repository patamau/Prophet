package prophet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Implement a simple global configuration container
 * @author Matteo
 *
 */
public class Configuration {
	
	public static final String FILE_SUFFIX = ".cfg";
	
	/**
	 * Implement and subcribe to receive property modification events
	 * @author Matteo
	 *
	 */
	public interface ConfigurationListener{
		public void onPropertyChange(String key, String newValue, String oldValue);
	}
	
	//used globally to access the loaded configuration
	private static Configuration globalConfiguration = new Configuration();
	
	/**
	 * Set the global configuration to the given configuration object<br>
	 * Given object can be null.<br>
	 * Previous global configuration listeners are automatically moved to the new configuration.
	 * @param configuration
	 */
	public static void setGlobalConfiguration(Configuration configuration){
		if(configuration==null) throw new IllegalArgumentException("Global configuration cannot be null");
		//unsubscribe previous destroyer
		if(configuration!=null){
			for(ConfigurationListener listener: globalConfiguration.listeners){
				configuration.addListener(listener);
			}
		}
		globalConfiguration = configuration;
	}
	
	/**
	 * Retrieve the global configuration.<br>
	 * Can be null
	 * @return
	 */
	public static Configuration getGlobalConfiguration(){
		return globalConfiguration;
	}
	
	public static String getGlobal(String key){
		return globalConfiguration.getProperty(key);
	}
	
	public static String getGlobal(String key, String def){
		return globalConfiguration.getProperty(key, def);
	}
	
	public static int getGlobal(String key, int def){
		return globalConfiguration.getProperty(key, def);
	}
	
	public static float getGlobal(String key, float def){
		return globalConfiguration.getProperty(key, def);
	}
	
	public static double getGlobal(String key, double def){
		return globalConfiguration.getProperty(key, def);
	}
	
	public static boolean getGlobal(String key, boolean def){
		return globalConfiguration.getProperty(key, def);
	}
	
	//-
	private boolean modified; //used internally to track changes
	private File file; //configuration data location
	private Properties properties; //key-value list
	private String comments; //custom comments for the properties file
	private ArrayList<ConfigurationListener> listeners; //subscribed listeners will receive updates when properties change
	
	/**
	 * Initializes a new configuration
	 */
	public Configuration(){
		comments = "";
		properties = new Properties();
		modified=false;
		listeners = new ArrayList<ConfigurationListener>();
	}
	
	/**
	 * Initializes a new configuration with the given comments
	 * @param comments
	 */
	public Configuration(String comments){
		this.comments=comments;
		properties = new Properties();
		modified=false;
		this.listeners = new ArrayList<ConfigurationListener>();
	}
	
	public Configuration(Configuration configuration){
		this.modified=configuration.modified;
		this.file=configuration.file;
		this.properties=(Properties)configuration.properties.clone();
		this.comments=configuration.comments;
		this.listeners = new ArrayList<ConfigurationListener>();
		this.listeners.addAll(configuration.listeners);
	}
	
	public Object clone(){
		return new Configuration(this);
	}
	
	public File getFile(){
		return this.file;
	}
	
	public Properties getProperties(){
		return properties;
	}
	
	public void setProperties(Properties properties){
		this.properties.clear();
		for(Object key: properties.keySet()){
			setProperty(String.valueOf(key),String.valueOf(properties.get(key)));
		}
	}

	public void remove(String key){
		properties.remove(key);
		this.modified=true;
	}
	
	public void setProperty(String key, String value){
		String old = String.valueOf(properties.setProperty(key, value));
		firePropertyChange(key,value,old);
		this.modified=true;
	}
	
	public void setProperty(String key, boolean value){
		String svalue = Boolean.toString(value);
		String old = String.valueOf(properties.setProperty(key, svalue));
		firePropertyChange(key,svalue,old);
		this.modified=true;
	}
	
	/**
	 * Subscribe to property change events
	 * @param listener
	 */
	public synchronized void addListener(ConfigurationListener listener){
		if(!this.listeners.contains(listener))
			this.listeners.add(listener);
	}
	
	/**
	 * Remove to property change events
	 * @param listener
	 */
	public synchronized void removeListener(ConfigurationListener listener){
		this.listeners.remove(listener);
	}
	
	/**
	 * Remove all the property change listeners
	 */
	public synchronized void clearListeners(){
		this.listeners.clear();
	}
	
	/**
	 * Propagates property modification to all the listeners
	 * @param key
	 * @param newValue
	 * @param oldValue
	 */
	public synchronized void firePropertyChange(String key, String newValue, String oldValue){
		for(ConfigurationListener listener: listeners){
			listener.onPropertyChange(key,newValue,oldValue);
		}
	}
	
	/**
	 * Returns the value associated to the given key or null if no such value exists
	 * @param key
	 * @return
	 */
	public String getProperty(String key){
		if(!properties.keySet().contains(key)){
			setProperty(key, null);
		}
		return properties.getProperty(key);
	}
	
	/**
	 * Returns the value associated to the given key.<br>
	 * If no such value exists, the def value is stored and returned.<br>
	 * @param key
	 * @param def
	 * @return
	 */
	public String getProperty(String key, String def){
		String v = properties.getProperty(key);
		if(v==null){
			setProperty(key, def);
			return def;
		}else{
			return v;
		}
	}
	
	public int getProperty(String key, int def){
		String v = properties.getProperty(key);
		if(v==null){
			setProperty(key, String.valueOf(def));
			return def;
		}else{
			return Integer.valueOf(v);
		}
	}
	
	public float getProperty(String key, float def){
		String v = properties.getProperty(key);
		if(v==null){
			setProperty(key, String.valueOf(def));
			return def;
		}else{
			return Float.valueOf(v);
		}
	}
	
	public double getProperty(String key, double def){
		String v = properties.getProperty(key);
		if(v==null){
			setProperty(key, String.valueOf(def));
			return def;
		}else{
			return Double.valueOf(v);
		}
	}
	
	public boolean getProperty(String key, boolean def){
		String v = properties.getProperty(key);
		if(v==null){
			setProperty(key, String.valueOf(def));
			return def;
		}else{
			return Boolean.valueOf(v);
		}
	}
	
	/**
	 * Tells if the Configuration has been modified since the last
	 * time it was saved or loaded
	 * @return
	 */
	public boolean isModified(){
		return this.modified;
	}
	
	/**
	 * Loads a properties file<br>
	 * Previous properties are removed and modified status is reset
	 * @param file
	 */
	public void load(String file){
		load(new File(file));
	}
	
	/**
	 * Loads a properties file<br>
	 * Previous properties are removed and modified status is reset
	 * @param file
	 */
	public void load(File file){
		this.file=file;
		if(!file.exists()){
			save(file);
			return;
		}
		FileInputStream inStream = null;
		properties.clear();
		try{
			inStream = new FileInputStream(file);
			properties.load(inStream);
			modified=false;
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(inStream!=null){
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Stores the properties to the given file<br>
	 * The given file becomes the new de-facto file for this configuration<br>
	 * Modified flag is set to false
	 * @param file
	 */
	public void save(File file){
		this.file=file;
		FileOutputStream out = null;
		try{
			out= new FileOutputStream(file);
			properties.store(out, comments);
			modified=false;
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * When destroyed, try to save the current configuration automatically
	 */
	public void destroy() {
		if(modified&&(file!=null)){
			save(file);
		}
	}
}
