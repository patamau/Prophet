package prophet.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * A generic GUI dialog for configuration data
 * @author Matteo
 *
 */
public class ConfigurationDialog implements 
		ActionListener{
	
	public static String 
		COL_KEY = "Key",
		VAL_KEY = "Value",
		DIALOG_TITLE = "Configuration",
		ACCEPT_LBL = "Accept",
		CANCEL_LBL = "Cancel",
		RESET_LBL = "Reset",
		REMOVE_LBL = "Remove",
		SAVE_LBL = "Save";
	
	//GUI components
	private JDialog dialog;
	private JButton acceptButton, 
		cancelButton, 
		resetButton,
		removeButton,
		saveButton;
	private JTable table;
	private DefaultTableModel tableModel;
	private TableSorter tableSorter;
	
	//resources
	private boolean accept;
	private Configuration oldconf;
	private Configuration conf;
	
	/**
	 * Creates a new configuration interface for the given parent Frame<br>
	 * Use <code>showDialog</code> to actually visualize the dialog.
	 * @param parent
	 */
	public ConfigurationDialog(Frame parent){
		createDialog(parent);
		accept=false;
	}
	
	/**
	 * Show a small gui that allows the user to modify the properties manually.<br>
	 * Modified configuration is returned
	 */
	public Configuration showDialog(Configuration config){
		this.oldconf=config;
		this.conf=(Configuration)config.clone();
		//avoid unwanted leechers to listen to my events
		this.conf.clearListeners();
		tableModel = update(tableModel);
		tableSorter.setSortingStatus(0, TableSorter.ASCENDING);
		dialog.setVisible(true);
		if(accept){
			oldconf.setProperties(this.conf.getProperties());
			return oldconf;
		}else{
			return oldconf;
		}
	}
	
	private Component createSouthPanel(){
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=0;
		gc.insets = new Insets(5,5,5,5);
		saveButton = new JButton(Language.string(SAVE_LBL));
		saveButton.addActionListener(this);
		panel.add(saveButton,gc);
		gc.gridx++;
		acceptButton = new JButton(Language.string(ACCEPT_LBL));
		acceptButton.addActionListener(this);
		panel.add(acceptButton,gc);
		gc.gridx++;
		resetButton = new JButton(Language.string(RESET_LBL));
		resetButton.addActionListener(this);
		panel.add(resetButton,gc);
		gc.gridx++;
		removeButton = new JButton(Language.string(REMOVE_LBL));
		removeButton.addActionListener(this);
		panel.add(removeButton,gc);
		gc.gridx++;
		cancelButton = new JButton(Language.string(CANCEL_LBL));
		cancelButton.addActionListener(this);
		panel.add(cancelButton,gc);
		return panel;
	}
	
	private void createDialog(Frame parent){
		dialog = new JDialog(parent);
		dialog.setModal(true);
		dialog.setTitle(Language.string(DIALOG_TITLE));
		
		tableModel = new ConfigurationTableModel();
		update(tableModel);
		tableSorter = new TableSorter(tableModel);
		table = new JTable(tableSorter);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoCreateColumnsFromModel(false);
		tableSorter.setTableHeader(table.getTableHeader());
		tableSorter.setSortingStatus(0, TableSorter.ASCENDING);
	    JScrollPane tspane = new JScrollPane();
	    tspane.setViewportView(table);
	    
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(tspane,BorderLayout.CENTER);
		dialog.getContentPane().add(createSouthPanel(),BorderLayout.SOUTH);
		
		//dialog.setSize(400,300);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
	}
	
	private void reset(){
		this.conf=(Configuration)oldconf.clone();
		this.conf.clearListeners();
		update(tableModel);
	}
	
	/**
	 * Loads the configuration properties to the table model
	 */
	private DefaultTableModel update(DefaultTableModel tmodel){
		Vector<String> columnIdentifiers = new Vector<String>();
		columnIdentifiers.add(COL_KEY);
		columnIdentifiers.add(VAL_KEY);
		Vector<Vector<String>> dataVector = new Vector<Vector<String>>();
		if(conf!=null){
			for(Map.Entry<Object,Object> entry: conf.getProperties().entrySet()){
				Vector<String> ev = new Vector<String>();
				ev.add((String)entry.getKey());
				ev.add((String)entry.getValue());
				dataVector.add(ev);
			}
		}
		tmodel.setDataVector(dataVector, columnIdentifiers);
		return tmodel;
	}
	
	
	public String getKey(int index){
		int i=0;
		for(Object key: conf.getProperties().keySet()){
			if(i==index) return (String)key;
			i++;
		}
		return null; //not found
	}
	
	public String getValue(int index){
		int i=0;
		for(Object val: conf.getProperties().values()){
			if(i==index) return (String)val;
			i++;
		}
		return null; //not found
	}

	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if(s==acceptButton){
			accept=true;
			dialog.dispose();
		}else if(s==cancelButton){
			accept=false;
			dialog.dispose();
		}else if(s==resetButton){
			reset();
		}else if(s==saveButton){
			conf.save(conf.getFile());
		}else if(s==removeButton){
			tableModel.removeRow(table.getSelectedRow());
		}
	}
	
	/**
	 * Internally used to get customized representation
	 * for the current configuration
	 * @author Matteo
	 *
	 */
	@SuppressWarnings("serial")
	private class ConfigurationTableModel 
		extends DefaultTableModel {
		
		public boolean isCellEditable(int row, int col){
			if(col>0){
				return true;
			}else{
				return false;
			}
		}
		
		public void removeRow(int r){
			conf.remove(getKey(r));
			super.removeRow(r);
		}
		
		public void setValueAt(Object v, int r, int c){
			String key = getKey(r);
			if(c==1){
				conf.setProperty(key, (String)v);
			}else if(c==0){
				String val = conf.getProperty(key);
				conf.remove(key);
				conf.setProperty((String)v, val);
			}
			super.setValueAt(v, r, c);
		}
	}
}
