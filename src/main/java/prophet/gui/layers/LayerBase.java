package prophet.gui.layers;

import java.util.Observer;

import prophet.gui.ILayer;
import prophet.util.Logger;

public abstract class LayerBase implements ILayer {
	
	private static final Logger logger = Logger.getLogger(LayerBase.class);

	protected boolean enabled;
	private String name;
	
	protected LayerBase(final String name) {
		this.name = name;
		this.enabled = true;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean hasChanged() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void notifyObservers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addObserver(Observer observer) {
		logger.debug("adding observer",observer);
	}

	@Override
	public void deleteObserver(Observer observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteObservers() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return null!=name&&name.length()>0?name:getClass().getSimpleName();
	}
}
