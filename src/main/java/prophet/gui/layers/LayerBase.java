package prophet.gui.layers;

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
}
