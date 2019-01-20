package prophet.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
import prophet.model.ISetting;
import prophet.util.Configuration;
import prophet.util.ConfigurationDialog;
import prophet.util.Language;

public class ProphetGUI implements ActionListener {
	
	public static final int LEFTPANEL_SIZE_PREF = 300, RIGHTPANEL_SIZE_PREF = 300, BOTTOMPANEL_SIZE_PREF = 26;
	
	private final Prophet prophet;
	private final JFrame frame;
	
	private JMenuItem openItem, saveItem, configItem, exitItem;
	
	public ProphetGUI(final Prophet prophet) {
		this.prophet = prophet;
		
		frame = new JFrame(Prophet.APP+" "+Prophet.VERSION+" "+Prophet.BUILD);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		frame.setJMenuBar(menubar);
	}
	
	private JComponent createLeftPanel() {
		final JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(LEFTPANEL_SIZE_PREF, 0));
		return leftPanel;
	}
	
	private JComponent createCentralPanel() {
		final JSplitPane centralPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		centralPane.setOneTouchExpandable(true);
		centralPane.setResizeWeight(1.0);
		centralPane.setDividerSize(12);
		centralPane.setLeftComponent(prophet.getRenderer());
		centralPane.setRightComponent(createRightPanel());
		final JSplitPane middlePane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		middlePane.setOneTouchExpandable(true);
		middlePane.setResizeWeight(0.0);
		middlePane.setDividerSize(12);
		middlePane.setRightComponent(centralPane);
		middlePane.setLeftComponent(createLeftPanel());
		return middlePane;
	}
	
	private JComponent createRightPanel() {
		final JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(RIGHTPANEL_SIZE_PREF, 0));
		return rightPanel;
	}
	
	private void createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(0, BOTTOMPANEL_SIZE_PREF));
		frame.add(bottomPanel, BorderLayout.SOUTH);
	}
	
	private void createAndShowGUI() {
		frame.setLayout(new BorderLayout());
		
		createMenubar();
		frame.add(createCentralPanel(), BorderLayout.CENTER);
		createBottomPanel();
		
		frame.setSize(800, 500); //TODO: load configuration
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
				prophet.getSettingSerializer().parse(source, prophet.getSetting());
				setting.update();
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
		}else if(src == exitItem) {
			frame.dispose();
		} else {
			//??
		}
	}
}
