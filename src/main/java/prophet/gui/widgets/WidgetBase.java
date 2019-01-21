package prophet.gui.widgets;

import javax.swing.JToolBar;

import prophet.gui.IWidget;

@SuppressWarnings("serial")
public abstract class WidgetBase extends JToolBar implements IWidget {

	private boolean enabled;
	
	protected WidgetBase(final String title) {
		super(title);
		setFloatable(true);
		enabled = true;
	}
	
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
}
