package prophet.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Handle string arguments
 * @author Matteo
 *
 */
public class Options {

	private String string;
	private ArrayList<String> keys;
	private ArrayList<String> opts;
	private ArrayList<String> args;
	
	public static String ARGUMENT_STRING_SEPARATOR = " ",
		OPTION_PREFIX = "-";
	
	public Options(String _args[]){
		args = new ArrayList<String>();
		opts = new ArrayList<String>();
		keys = new ArrayList<String>();
		String lastKey = null;
		for(String arg: _args){
			if(arg.startsWith(OPTION_PREFIX)){
				//remove the prefix from the option key
				lastKey=arg.substring(1);
				keys.add(lastKey);				
			}else{
				if(lastKey!=null){
					//try to insert the key's option
					if(keys.indexOf(lastKey)>=opts.size()){
						opts.add(arg);
					}else{
						//the key was already set, 
						//this is an argument
						args.add(arg);
					}
				}else{
					//add the argument
					args.add(arg);
				}
			}
		}
		//prepare a trimmed string with all the arguments
		string = computeString(_args);
	}
	
	protected String computeString(String args[]){
		StringBuffer sb = new StringBuffer();
		for(String arg: args){
			if(sb.length()>0) sb.append(ARGUMENT_STRING_SEPARATOR);
			sb.append(arg);
		}
		return sb.toString();
	}
	
	/**
	 * Get the whole argument list as a single string
	 * @return
	 */
	public String toString(){
		return string;
	}
	
	/**
	 * Internal debug test
	 */
	public void printStrings(){
		for(String arg: args){
			System.out.println(arg);
		}
	}
	
	/**
	 * Tells if the given option has been set
	 * @param optionKey
	 * @return
	 */
	public boolean isSet(String optionKey){
		return keys.contains(optionKey);
	}
	
	/**
	 * Return the arguments without options.
	 * @param index
	 * @return
	 */
	public Collection<String> getArguments(){
		return args;
	}
	
	/**
	 * Return all the arguments associated to an option.
	 * @return
	 */
	public Collection<String> getOptions(){
		return opts;
	}
	
	/**
	 * Return the value associated to the given key
	 * @param key option key to look for
	 * @param def default return value
	 * @return def if no such option is specified
	 */
	public String getOption(String key, String def){
		if(isSet(key)){
			String val = opts.get(keys.indexOf(key));
			if(val==null) return def;
			else return val;
		}else{
			return def;
		}
	}
	
	/**
	 * Return the value associated to the given key
	 * @param key
	 * @return null if no such option is set
	 */
	public String getOption(String key){
		if(isSet(key)){
			return opts.get(keys.indexOf(key));
		}else{
			return null;
		}
	}
}
