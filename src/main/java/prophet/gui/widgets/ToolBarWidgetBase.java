package prophet.gui.widgets;

import javax.swing.JToolBar;

import prophet.gui.IWidget;

@SuppressWarnings("serial")
public abstract class ToolBarWidgetBase extends JToolBar implements IWidget {

	private boolean enabled;
	
	protected ToolBarWidgetBase(final String title) {
		super(title);
		setFloatable(true);
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
