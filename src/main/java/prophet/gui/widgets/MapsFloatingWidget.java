package prophet.gui.widgets;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import prophet.gui.KeyValueTableModel;
import prophet.model.IBorder;
import prophet.model.IBorderSelectionManager;
import prophet.model.IMap;
import prophet.model.IMapSelectionManager;
import prophet.model.ISetting;
import prophet.model.ITown;
import prophet.model.IWorldListener;
import prophet.model.SimpleMap;
import prophet.util.Language;
import prophet.util.Logger;

/**
 * Handle all the maps
 * @author patam
 *
 */
@SuppressWarnings("serial")
public class MapsFloatingWidget extends FloatingWidgetBase implements IWorldListener, ListSelectionListener, MouseListener, ActionListener {

	private final static Logger logger = Logger.getLogger(MapsFloatingWidget.class);
	
	private final static String WTITLE = "Maps";
	
	private final ISetting setting;
	private final DefaultListModel<IMap> mapsListModel;
	private final KeyValueTableModel mapTableModel;
	private final JList<IMap> mapsList;
	private final JTable mapTable;
	private final JPopupMenu contextMenu;
	private final JMenuItem removeMapItem;
	private final JButton addMapButton;
	private final IMapSelectionManager selectionManager;
	
	public MapsFloatingWidget(final ISetting setting, final IMapSelectionManager selectionManager, final Frame parent) {
		super(WTITLE, parent);
		this.setting = setting;
		this.selectionManager = selectionManager;
		mapsListModel = new DefaultListModel<IMap>();
		mapTableModel = new KeyValueTableModel();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = gc.gridy = 0;
		gc.weightx = 1.0;
		gc.fill = 1;
		gc.weighty = 0.0;
		JPanel buttonsPanel = new JPanel();
		addMapButton = new JButton("+");
		addMapButton.addActionListener(this);
		addMapButton.setToolTipText("Adds a new map");
		buttonsPanel.add(addMapButton);
		buttonsPanel.add(new JButton("-"));
		this.add(buttonsPanel, gc);
		++gc.gridy;
		gc.weighty = 0.5;
		mapsList = new JList<IMap>(mapsListModel);
		mapsList.addListSelectionListener(this);
		mapsList.addMouseListener(this);
		mapsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final JPanel mapsPanel = new JPanel(new BorderLayout());
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
		
		removeMapItem = new JMenuItem(Language.string("Remove"));
		contextMenu = createContextMenu();
	}

	private JPopupMenu createContextMenu() {
		final JPopupMenu contextMenu  = new JPopupMenu();
		contextMenu.add(removeMapItem);
		removeMapItem.addActionListener(this);
		return contextMenu;
	}

	@Override
	public void onMapAdded(final IMap map) {
		logger.debug("map ",map," added");
		mapsListModel.addElement(map);
	}
	
	@Override
	public void onMapChanged(IMap map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapRemoved(final IMap map) {
		mapsListModel.removeElement(map);
	}

	@Override
	public void onMapsCleared() {
		mapsListModel.removeAllElements();
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
				selectionManager.onMapSelected(map);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
			final int idx = mapsList.locationToIndex(e.getPoint());
			if(idx < 0) return;
			mapsList.setSelectedIndex(idx);
			final IMap map = mapsList.getSelectedValue();
			if(null == map) return;
			logger.debug("selected item is ",map);
			contextMenu.show(mapsList, e.getX(), e.getY());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final Object src = e.getSource();
		if(src == removeMapItem) {
			mapTableModel.setObject(null);
			setting.getWorld().removeMap(mapsList.getSelectedValue());
		} else if (src == addMapButton) {
			IMap map = new SimpleMap("Unknown map", setting.getWorld());
			setting.getWorld().addMap(map);
			mapsList.setSelectedValue(map, true);
			mapTableModel.setObject(map);
			selectionManager.onMapSelected(map);
		}
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
	public void onBorderAdded(IBorder Border) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBorderChanged(IBorder map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBorderRemoved(IBorder Border) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBordersCleared() {
		// TODO Auto-generated method stub
		
	}

	
}
