package prophet.gui.widgets;

import java.awt.Frame;

import javax.swing.JDialog;

import prophet.gui.IWidget;

@SuppressWarnings("serial")
public abstract class FloatingWidgetBase extends JDialog implements IWidget {

	private boolean enabled;
	
	protected FloatingWidgetBase(final String title, final Frame parent) {
		super(parent, title, false);
		enabled = true;
	}
	
	@Override
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
