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
	 * Perform updates on the internal components.
	 * Call this method when changes in the data need to be applied
	 */
	public void update();
	
	public IWorld getWorld();
	//calendar
	//religion
	//story
}
