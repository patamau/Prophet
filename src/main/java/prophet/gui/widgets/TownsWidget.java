package prophet.gui.widgets;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import prophet.gui.KeyValueTableModel;
import prophet.model.IMap;
import prophet.model.ISetting;
import prophet.model.ITown;
import prophet.model.IWorldListener;
import prophet.util.Logger;

@SuppressWarnings("serial")
public class TownsWidget extends WidgetBase implements IWorldListener, ListSelectionListener {

	private final static Logger logger = Logger.getLogger(TownsWidget.class);
	private final static String WTITLE = "Towns";
	
	private final ISetting setting;
	private final DefaultListModel<ITown> townsListModel;
	private final KeyValueTableModel townTableModel;
	private final JList<ITown> townsList;
	private final JTable townTable;
	
	public TownsWidget(final ISetting setting) {
		super(WTITLE);
		this.setting = setting;
		townsListModel = new DefaultListModel<ITown>();
		townTableModel = new KeyValueTableModel();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = gc.gridy = 0;
		gc.weightx = 1.0;
		gc.weighty = 0.5;
		gc.fill = 1;
		townsList = new JList<ITown>(townsListModel);
		townsList.addListSelectionListener(this);
		townsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final JPanel mapsPanel = new JPanel();
		mapsPanel.setBorder(BorderFactory.createTitledBorder("Towns"));
		mapsPanel.add(new JScrollPane(townsList));
		this.add(mapsPanel, gc);
		++gc.gridy;
		final JPanel mapPanel = new JPanel(new BorderLayout());
		mapPanel.setBorder(BorderFactory.createTitledBorder("Map"));
		townTable = new JTable(townTableModel);
		townTable.setFillsViewportHeight(true);
		townTableModel.setColumnIdentifiers(new Object[] { "key", "value" });
		mapPanel.add(townTable, BorderLayout.CENTER);
		this.add(mapPanel, gc);
		
		//populate the lists
		final List<ITown> towns = new ArrayList<ITown>();
		setting.getWorld().getTowns(towns);
		for(final ITown t: towns) {
			onTownAdded(t);
		}
		//list to world changes
		setting.getWorld().addWorldListener(this);
	}

	@Override
	public void onMapAdded(IMap map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapChanged(IMap map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapRemoved(IMap map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapsCleared() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTownAdded(ITown town) {
		logger.debug("town ",town," added");
		townsListModel.addElement(town);
	}

	@Override
	public void onTownChanged(ITown map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTownRemoved(ITown town) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTownsCleared() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		final Object src = e.getSource();
		if(src == townsList) {
			final ITown map = townsList.getSelectedValue();
			if(null != map) {
				logger.debug("selected town ", map);
				townTableModel.setObject(map);
				townsList.invalidate();
				townsList.repaint();
			}
		}
	}

}
