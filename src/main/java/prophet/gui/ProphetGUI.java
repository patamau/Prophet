package prophet.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import prophet.Prophet;

public class ProphetGUI {

	public static final String PROPHET_TITLE = "Prophet";
	
	private final Prophet prophet;
	private final JFrame frame;
	
	public ProphetGUI(final Prophet prophet) {
		this.prophet = prophet;
		
		frame = new JFrame(PROPHET_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void createAndShowGUI() {
		frame.add(prophet.getRenderer());
		frame.setSize(400, 300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void show() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
