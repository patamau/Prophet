package prophet.gui.layers;

import java.util.Observer;

import prophet.gui.ILayer;

public abstract class LayerBase implements ILayer {

	protected boolean enabled;
	
	protected LayerBase() {
		this.enabled = true;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteObserver(Observer observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteObservers() {
		// TODO Auto-generated method stub
		
	}
}
