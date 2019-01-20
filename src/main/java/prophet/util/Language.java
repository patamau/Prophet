package prophet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

/**
 * Loads a set of different languages file and relative strings.<br>
 * When calling getString, the localized string is returned
 * @version 0.3
 * @author Matteo
 *
 */
public class Language {
	
	/**
	 * An short example showing how to use this Language class
	 * @param args
	 */
	public static void main(String args[]){
		simpleExample();
		complexExample();
	}
	
	/**
	 * A typical language usage example
	 */
	private static void simpleExample(){
		//retrieve the automatically configured language
		System.out.println(Language.getCurrentLanguage());
		//load strings
		String world = Language.string("world");
		String hello = Language.string("Hello");
		System.out.println(hello+" "+world+"!");
		
		//change to another language
		Language.setCurrentLanguage("en");
		System.out.println(Language.getCurrentLanguage());
		//load strings
		world = Language.string("world");
		hello = Language.string("Hello");
		System.out.println(hello+" "+world+"!");
	}
	
	/**
	 * An advanced language usage example
	 */
	private static void complexExample(){
		//explicitly load files from custom folder
		Language.loadLanguages("lang");
		
		//customize a new language
		Language l = new Language("test");
		l.setFile(new File("lang/korean.test"));
		l.setLocale(Locale.KOREAN);
		l.open();
		
		//update languages with the customized language
		Language.updateLanguages(l);
		
		//print updated languages
		System.out.println("Loaded languages:");
		for(LanguageDescriptor ld: Language.getDescriptors()){
			System.out.println(ld);
		}	
		
		//set current language
		Language.setCurrentLanguage(l);
		
		//print language short description (toString)
		System.out.println("Current language: "+Language.getCurrentLanguage());
		
		//a few getString calls
		System.out.println("Testing Custom Language:");
		String s1 = Language.string("Hello world!");
		System.out.println("> "+s1);
		String s2 = Language.string("This is number {0}! Bye {1}...", new Object[]{1.0d,"bye"});
		System.out.println("> "+s2);
		String s3 = Language.string("A few numbers: {0} {1} {2}", new Object[]{1100d,1100.123d,0.02000f});
		System.out.println("> "+s3);
	}
	
	public static final String VERSION = "0.3";
	
	//the following strings can be customized (they are public and static)
	public static String LANG_HEAD_FRMT = "{0} {1} - v.{2}"; //header format
	public static String LANG_FILE_SUFFIX = ".lang"; //language filename suffix
	public static String LANG_ROOT_FIL = "lang"; //language root path
	
	private static final MessageFormat headerFormat = new MessageFormat(LANG_HEAD_FRMT);
	private static final char CODE_START_CHR = '.';
	private static final String UNKNOWN_STR = "<unknown>";
	
	private static LanguageManager lman;
	
	//a language manager logical container
	private static class LanguageManager implements Runnable{
		//root folder to look for language files
		private File lroot = new File(LANG_ROOT_FIL);
		//language code and descriptors
		private HashMap<String, LanguageDescriptor> languages = new HashMap<String,LanguageDescriptor>();
		//an internal class to filter valid language files
		private LanguageFilenameFilter lfilter = new LanguageFilenameFilter();
		//the internal default language 
		private Language lcurrent;
		
		public LanguageManager(){
			Runtime.getRuntime().addShutdownHook(new Thread(this));
		}
		
		public void run(){
			if(lcurrent!=null){
				lcurrent.close();
			}
		}
	}
	
	public static class LanguageDescriptor implements Comparable<Object>{
		private File file;
		private Locale locale;
		public LanguageDescriptor(File f, Locale l){
			this.file=f;
			this.locale=l;
		}
		public String toString(){
			return locale.getDisplayLanguage();
		}
		public int compareTo(Object arg) {
			return this.toString().compareTo(arg.toString());
		}
	}
	
	/**
	 * Retrieve the internal static instance of the LanguageManager
	 * @return
	 */
	private static synchronized LanguageManager getManager(){
		if(lman==null){
			//load languages also initialize the lman object
			loadLanguages();
		}
		return lman;
	}
	
	/**
	 * Utility method to obtain a language object<br>
	 * The Language is opened automatically before returning.<br>
	 * There is no need to open it again
	 * @param code
	 * @return
	 */
	public static Language getLanguage(String code){
		Language l = new Language(code);
		l.open();
		updateLanguages(l);
		return l;
	}
	
	/**
	 * Get the current default language.<br>
	 * This language is used for the standard static calls to retrieve global strings
	 * @return
	 */
	public static Language getCurrentLanguage(){
		if(getManager().lcurrent==null){
			lman.lcurrent = getLanguage(Locale.getDefault().getLanguage());
		}
		return lman.lcurrent;
	}
	
	/**
	 * Set a default language for static calls to retrieve global strings.<br>
	 * If a previous language was loaded, it is safely closed
	 * @param language
	 */
	public static void setCurrentLanguage(Language language){
		if(language==null) throw new IllegalArgumentException("Language cannot be null");
		if(getManager().lcurrent!=null){
			getManager().lcurrent.close();
		}
		getManager().lcurrent=language;
		updateLanguages(language);
		Locale.setDefault(language.locale);
	}
	
	/**
	 * Internally used to update the languages list dynamically.<br>
	 * If the same code is already in list, the new language, if different is set
	 * @param language the language to add to the current languages repository
	 */
	public static void updateLanguages(Language language){
		//update available languages
		LanguageDescriptor ldesc = new Language.LanguageDescriptor(language.file,language.locale);
		lman.languages.put(language.code,ldesc);
	}
	
	/**
	 * Set a default language for static calls to retrieve global strings<br>
	 * @param code
	 */
	public static void setCurrentLanguage(String code){
		setCurrentLanguage(getLanguage(code));
	}
	
	/**
	 * Retrive a string value for the given key using the current language
	 * @param key
	 * @return
	 */
	public static String string(String key){
		return getCurrentLanguage().getString(key);
	}
	
	/**
	 * Retrieve a string value for the given key using the current language.<br>
	 * The returned value is properly formed using the provided data objects
	 * @param key
	 * @param data
	 * @return
	 */
	public static String string(String key, Object ...data){
		return getCurrentLanguage().getString(key,data);
	}
	
	/**
	 * Loads the language files found in the LANG_ROOT_FIL folder.<br>
	 * Previous languages are removed.<br>
	 * Language files must start with LANG_FILE_STR
	 */
	public static void loadLanguages(){
		loadLanguages(LANG_ROOT_FIL);
	}
	
	/**
	 * Loads the language files found in the given folder.<br>
	 * Previous loaded languages are removed.<br>
	 * Valid language files must start with LANG_FILE_STR
	 */
	public static void loadLanguages(String root){
		if(lman==null){ //ensure lman is initialized properly (without loading languages)
			lman = new LanguageManager();
		}
		if(!root.equals(LANG_ROOT_FIL)){
			lman.lroot = new File(root);
			if(lman.lroot.isFile()){
				lman.lroot = lman.lroot.getParentFile();
			}
		}
		lman.languages.clear();
		if(!lman.lroot.exists()){
			if(!lman.lroot.mkdir()){
				return; //ignore if no such root exists or cannot be created
			}
		}
		File[] lfiles = lman.lroot.listFiles(lman.lfilter);
		if(lfiles!=null)
		for(File lf:lfiles){
			String c = getCode(lf.getName());
			LanguageDescriptor ldesc = new LanguageDescriptor(lf,getLocale(c));
			lman.languages.put(c, ldesc);
		}
	}
	
	/**
	 * Retrieve available languages codes (ie: it, en, de, etc...)
	 * @return
	 */
	public static String[] getCodes(){
		return getManager().languages.keySet().toArray(new String[0]);
	}
	
	/**
	 * Retrieve a list of available languages
	 * @return
	 */
	public static LanguageDescriptor[] getDescriptors(){
		return getManager().languages.values().toArray(new LanguageDescriptor[0]);
	}
	
	/**
	 * Retrieve a list of loaded languages
	 * @return
	 */
	public static String[] getDisplayLanguages(){
		int siz = getManager().languages.size();
		String[] dls = new String[siz];
		int i=0;
		for(LanguageDescriptor ld: lman.languages.values()){
			if(ld!=null){
				if(ld.locale==null){
					System.err.println(ld.file+": no locale available for this file");
				}else{
					dls[i++]=ld.locale.getDisplayLanguage();
				}
			}
		}
		return dls;
	}
	
	//- utility methods (internal)
	
	/**
	 * Get the last part of a file name.<br>
	 * For a language file, this last part is the considered the language code
	 */
	private static String getCode(String lname){
		String code = null;
		int cs = lname.indexOf(CODE_START_CHR);
		if(cs>0){
			code = lname.substring(0, cs);
		}
		return code;
	}
	
	/**
	 * Used internally to load a suitable locale for the given code.<br>
	 * The Locale is just the first valid found Locale, 
	 * thus it might not match the Country.
	 * @param code
	 * @return
	 */
	private static Locale getLocale(String code){
		for(Locale l:Locale.getAvailableLocales()){
			if(l.getLanguage().equalsIgnoreCase(code)){
				return l;
			}
		}
		return Locale.getDefault();
	}
	
	/**
	 * Class used by loadLanguages() to select language files 
	 */
	private static class LanguageFilenameFilter implements FilenameFilter{
		public boolean accept(File dir, String name) {
			if(name.toLowerCase().endsWith(LANG_FILE_SUFFIX))
				return true;
			else
				return false;
		}
	}
	
	//- class implementation -
	
	private String code; //language id
	private Properties strings; //currently loaded language
	private File file; //language file descriptor
	private Locale locale; //auto loaded locale
	private boolean modified; //modified flag
	
	/**
	 * Creates a new Language object.<br>
	 * Langauge strings can be loaded using the <code>open()</code> method.
	 */
	public Language(String code){
		this.code = code;
		this.strings = new Properties();
		LanguageDescriptor ld = getManager().languages.get(code);
		if(ld==null){
			this.file=null;
			//try loading the locale dynamically
			this.locale=Language.getLocale(code);
		}else{
			this.file=ld.file;
			this.locale=ld.locale;
		}
		//ensure the locale is properly loaded
		if(this.locale==null){
			locale=Locale.getDefault();
		}
		modified=false;
	}
	
	/**
	 * Force the file for this language
	 * @param f
	 */
	public void setFile(File f){
		if(f!=this.file){
			this.file = f;
			modified=true;
		}
	}
	
	/**
	 * Force the locale for this language
	 * @param locale
	 */
	public void setLocale(Locale locale){
		this.locale=locale;
	}
	
	/**
	 * Retrieves the language code
	 * @return
	 */
	public String getCode(){
		return this.code;
	}
	
	/**
	 * Retrieve a string value from the loaded language strings.<br>
	 * If no such value or key exists, a new value is added into the language.<br>
	 * When the language will be closed (via the <code>close()</code> method) the new string will be saved
	 * @param key
	 * @return
	 */
	public String getString(String key){
		if(key==null||key.length()==0) return "";
		String value = this.strings.getProperty(key);
		if(value==null){
			value = key;
			this.strings.setProperty(key, value);
			modified=true;	
		}
		return value;
	}
	
	/**
	 * Retrieves a valid value from the language strings.<br>
	 * The returned value will be used as a format for the given data.<br>
	 * For example if the string is: "Hello {0}! This is {1}!"<br>
	 * and the given data objects are "world" and "me"<br>
	 * the resulting string will be "Hello world! This is me!"
	 * @param key
	 * @param data
	 * @return
	 */
	public String getString(String key, Object... data){
		MessageFormat mformat = new MessageFormat(getString(key));
		mformat.setLocale(locale);
		return mformat.format(data, new StringBuffer(), null).toString();
	}
	
	/**
	 * Loads the strings from the right file.<br>
	 * Previous strings are cleared.<br>
	 * If no such file exists, a new one will be created when closing<br>
	 */
	public void open(){
		if(file==null||!file.exists()){
			//do nothing, but set the save flag when the language is closed
			modified=true;
		}else{
			FileInputStream fis = null;
			try{
				strings.clear();
				fis = new FileInputStream(file);
				strings.load(fis);
			}catch(IOException ioe){
				ioe.printStackTrace();
			}finally{
				if(fis!=null)
					try{
						fis.close();
					}catch(IOException e){
						e.printStackTrace();
					}
			}
			modified=false;
		}
	}
	private File getDefaultFile(String code){
		StringBuffer sb = new StringBuffer();
		sb.append(getManager().lroot.getAbsolutePath());
		sb.append(File.separatorChar);
		sb.append(code);
		sb.append(LANG_FILE_SUFFIX);
		return new File(sb.toString());
	}
	
	/**
	 * If modified, save properties to file<br>
	 * If no such file exists, a new file is created
	 */
	public void close(){
		if(modified){
			if(file==null){
				file = getDefaultFile(code);
			}
			FileOutputStream fos=null;
			try {
				fos = new FileOutputStream(file);
				strings.store(fos,getLanguageHeader());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				if(fos!=null){
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Get a language description with display language, language code and version.<br>
	 * The string is formatted using the LANG_HEAD_FRMT static string
	 * @return
	 */
	public String getLanguageHeader(){
		Object[] data = new Object[]{
				locale.getDisplayLanguage(),
				locale.getLanguage(),
				VERSION
		};
		return headerFormat.format(data, new StringBuffer(), null).toString();
	}
	
	/**
	 * Return the display name for this language
	 * @return
	 */
	public String getDisplay(){
		return locale.getDisplayLanguage();
	}
	
	/**
	 * Return a string representation of this language class
	 */
	public String toString(){
		String fnam;
		if(file==null){
			fnam = UNKNOWN_STR;
		}else{
			fnam = file.getName();
		}
		return "["+this.getClass().getSimpleName()+"] File:"+fnam+" Display:"+locale.getDisplayLanguage()+" Strings:"+strings.size()+".";
	}
}

