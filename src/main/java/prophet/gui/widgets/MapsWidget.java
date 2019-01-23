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

/**
 * Handle all the maps
 * @author patam
 *
 */
@SuppressWarnings("serial")
public class MapsWidget extends WidgetBase implements IWorldListener, ListSelectionListener {

	private final static Logger logger = Logger.getLogger(MapsWidget.class);
	
	private final static String WTITLE = "Maps";
	
	private final ISetting setting;
	private final DefaultListModel<IMap> mapsListModel;
	private final KeyValueTableModel mapTableModel;
	private final JList<IMap> mapsList;
	private final JTable mapTable;
	
	public MapsWidget(final ISetting setting) {
		super(WTITLE);
		this.setting = setting;
		mapsListModel = new DefaultListModel<IMap>();
		mapTableModel = new KeyValueTableModel();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = gc.gridy = 0;
		gc.weightx = 1.0;
		gc.weighty = 0.5;
		gc.fill = 1;
		mapsList = new JList<IMap>(mapsListModel);
		mapsList.addListSelectionListener(this);
		mapsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final JPanel mapsPanel = new JPanel();
		mapsPanel.setBorder(BorderFactory.createTitledBorder("Maps"));
		mapsPanel.add(new JScrollPane(mapsList));
		this.add(mapsPanel, gc);
		++gc.gridy;
		final JPanel mapPanel = new JPanel(new BorderLayout());
		mapPanel.setBorder(BorderFactory.createTitledBorder("Map"));
		mapTable = new JTable(mapTableModel);
		mapTable.setFillsViewportHeight(true);
		mapTableModel.setColumnIdentifiers(new Object[] { "key", "value" });
		mapPanel.add(mapTable, BorderLayout.CENTER);
		this.add(mapPanel, gc);
		
		//populate the lists
		final List<IMap> maps = new ArrayList<IMap>();
		setting.getWorld().getMaps(maps);
		for(final IMap m: maps) {
			onMapAdded(m);
		}
		//list to world changes
		setting.getWorld().addWorldListener(this);
	}

	@Override
	public void onMapAdded(IMap map) {
		logger.debug("map ",map," added");
		mapsListModel.addElement(map);
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
		// TODO Auto-generated method stub
		
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
		if(src == mapsList) {
			final IMap map = mapsList.getSelectedValue();
			if(null != map) {
				logger.debug("selected map ", map);
				mapTableModel.setObject(map);
				mapTable.invalidate();
				mapTable.repaint();
			}
		}
	}

	
}
