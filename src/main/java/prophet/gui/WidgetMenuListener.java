package prophet.gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

public class WidgetMenuListener implements WindowListener {
	
	private final JCheckBoxMenuItem cmi;
	private final JMenu widgetsMenu;
	
	public WidgetMenuListener(final JCheckBoxMenuItem cmi, final JMenu widgetsMenu) {
		this.cmi = cmi;
		this.widgetsMenu = widgetsMenu;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		cmi.setSelected(true);
		widgetsMenu.invalidate();
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		cmi.setSelected(false);
		widgetsMenu.invalidate();
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
	
}
