package prophet.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import prophet.Prophet;
import prophet.gui.widgets.FloatingWidgetBase;
import prophet.gui.widgets.ToolBarWidgetBase;
import prophet.model.ISetting;
import prophet.util.Configuration;
import prophet.util.ConfigurationDialog;
import prophet.util.Language;
import prophet.util.Logger;

public class ProphetGUI implements ActionListener {
	
	private static final Logger logger = Logger.getLogger(ProphetGUI.class);
	
	public static final int TOPPANEL_SIZE_PREF = 20, BOTTOMPANEL_SIZE_PREF = 20;
	public static final int COMPONENT_TOP = 0, COMPONENT_BOTTOM = 1;
	
	private final Prophet prophet;
	private JFrame frame;
	
	private JComponent topComponent, bottomComponent;
	private JMenuItem openItem, saveItem, configItem, exitItem;
	
	private final JMenu widgetsMenu;
	private final Map<JCheckBoxMenuItem, FloatingWidgetBase> floatingWidgets;
	
	public ProphetGUI(final Prophet prophet) {
		this.prophet = prophet;
		floatingWidgets = new HashMap<JCheckBoxMenuItem, FloatingWidgetBase>();
		widgetsMenu = new JMenu("Widgets");
		
		createGUI();
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	/**
	 * Adds the given widget to the specified constraint location, if any
	 * @param widget
	 * @param location COMPONENT_ flag where the widget will be (initially) placed
	 */
	public void addWidget(final ToolBarWidgetBase widget, final int component) {
		switch(component) {
		case COMPONENT_BOTTOM:
			bottomComponent.add(widget);
			break;
		case COMPONENT_TOP:
			topComponent.add(widget);
			break;
		default:
			logger.error("Adding widget ",widget.getName()," to unsupported component ",component);
			break;
		}
	}
	
	public void addWidget(final FloatingWidgetBase widget) {
		final JCheckBoxMenuItem wcmi = new JCheckBoxMenuItem(widget.getTitle());
		wcmi.addActionListener(this);
		widget.addWindowListener(new WidgetMenuListener(wcmi, widgetsMenu));
		widget.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		widgetsMenu.add(wcmi);
		floatingWidgets.put(wcmi, widget);
	}
	
	private void createMenubar() {
		//create menu bar
		final JMenuBar menubar = new JMenuBar();
		final JMenu fileMenu = new JMenu("File");
		
		openItem = new JMenuItem(Language.string("Open Setting..."));
		openItem.addActionListener(this);
		saveItem = new JMenuItem(Language.string("Save Setting..."));
		saveItem.addActionListener(this);
		configItem = new JMenuItem(Language.string("Options..."));
		configItem.addActionListener(this);
		exitItem = new JMenuItem(Language.string("Quit"));
		exitItem.addActionListener(this);
		
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(new JSeparator());
		fileMenu.add(configItem);
		fileMenu.add(new JSeparator());
		fileMenu.add(exitItem);
		
		menubar.add(fileMenu);
		menubar.add(widgetsMenu);
		frame.setJMenuBar(menubar);
	}
	
	private JComponent createBottomPanel() {
		bottomComponent = new JPanel(new BorderLayout());
		bottomComponent.setPreferredSize(new Dimension(0, BOTTOMPANEL_SIZE_PREF));
		return bottomComponent;
	}
	
	private JComponent createTopPanel() {
		topComponent = new JPanel();
		topComponent.setPreferredSize(new Dimension(0, TOPPANEL_SIZE_PREF));
		return topComponent;
	}
	
	private void createGUI() {
		frame = new JFrame(Prophet.APP+" "+Prophet.VERSION+" "+Prophet.BUILD);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		createMenubar();
		frame.add(createTopPanel(), BorderLayout.NORTH);
		frame.add(prophet.getRenderer(), BorderLayout.CENTER);
		frame.add(createBottomPanel(), BorderLayout.SOUTH);
		
		frame.setSize(800, 500); //TODO: load from configuration or adapt to content
	}
	
	public void show() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
	
	private void loadSetting() {
		final JFileChooser fc = new JFileChooser();
		final File lastFile = new File(System.getProperty("user.dir"));
		fc.setSelectedFile(lastFile);
		fc.setCurrentDirectory(lastFile.getParentFile());
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int ch = fc.showOpenDialog(frame);
		if(ch!=JFileChooser.APPROVE_OPTION){
			return;
		}
		final File f = fc.getSelectedFile();
		if(!f.exists()||!f.canRead()){
			JOptionPane.showMessageDialog(frame, "Unable to access file", "Load error", JOptionPane.ERROR_MESSAGE);
		} else {
			try {
				final String source = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())), StandardCharsets.UTF_8);
				final ISetting setting = prophet.getSetting();
				setting.reset();
				prophet.getSettingSerializer().parse(source, prophet.getSetting(), null);
				setting.init();
			} catch (Throwable e) {
				JOptionPane.showMessageDialog(frame, "Error while loading file: "+e.getMessage(), "Load error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void saveSetting() {
		final JFileChooser fc = new JFileChooser();
		final File lastFile = new File(System.getProperty("user.dir"));
		fc.setSelectedFile(lastFile);
		fc.setCurrentDirectory(lastFile.getParentFile());
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int ch = fc.showSaveDialog(frame);
		if(ch!=JFileChooser.APPROVE_OPTION){
			return;
		}
		final File f = fc.getSelectedFile();
		FileWriter writer = null;
		try {
			final String serialized = prophet.getSettingSerializer().serialize("setting", prophet.getSetting());
			writer = new FileWriter(f);
			writer.write(serialized);
		} catch (Throwable e) {
			JOptionPane.showMessageDialog(frame, "Error while saving file: "+e.getMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
		} finally {
			if(null != writer)
			{
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void editConfiguration() {
		ConfigurationDialog cdialog = new ConfigurationDialog(frame);
		Configuration c = cdialog.showDialog(Configuration.getGlobalConfiguration());
		if(null != c) {
			Configuration.setGlobalConfiguration(c);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object src = e.getSource();
		if(src == openItem) {
			loadSetting();
		} else if(src == saveItem) {
			saveSetting();
		} else if(src == configItem) {
			editConfiguration();
		} else if(src == exitItem) {
			frame.dispose();
		} else if(src instanceof JCheckBoxMenuItem) {
			final FloatingWidgetBase w = floatingWidgets.get(src);
			if(null != w) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						w.setVisible(((JCheckBoxMenuItem)src).isSelected());
					}
				});
			}
		} else {
			//??
		}
	}
}
