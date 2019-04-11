package prophet.gui.widgets;

import java.awt.Frame;

@SuppressWarnings("serial")
public class DefaultFloatingWidget extends FloatingWidgetBase {

	public DefaultFloatingWidget(final String title, final Frame parent) {
		super(title, parent);
		super.setSize(400, 400);
	}
	
	public DefaultFloatingWidget(final String title) {
		super(title, null);
		super.setSize(400, 400);
	}

}
