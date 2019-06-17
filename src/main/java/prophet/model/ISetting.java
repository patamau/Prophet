package prophet.model;

/**
 * The campaign setting container
 * @author patam
 *
 */
public interface ISetting {

	public String getName();
	public void setName(final String name);
	
	/**
	 * Set all the internal assets to their default value
	 */
	public void reset();
	
	/**
	 * Initialise this setting to a coherent state
	 */
	public void init();
	
	public IWorld getWorld();
	//calendar
	//religion
	//story
}
