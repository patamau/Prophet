package prophet.gui.widgets;

import java.awt.BorderLayout;
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
import prophet.model.IMap;
import prophet.model.ISetting;
import prophet.model.ITown;
import prophet.model.IWorldListener;
import prophet.util.Language;
import prophet.util.Logger;

@SuppressWarnings("serial")
public class TownsWidget extends WidgetBase implements IWorldListener, ListSelectionListener, MouseListener, ActionListener {

	private final static Logger logger = Logger.getLogger(TownsWidget.class);
	private final static String WTITLE = "Towns";
	
	private final ISetting setting;
	private final DefaultListModel<ITown> townsListModel;
	private final KeyValueTableModel townTableModel;
	private final JList<ITown> townsList;
	private final JTable townTable;
	private final JPopupMenu contextMenu;
	private final JMenuItem removeTownItem;
	
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
		townsList.addMouseListener(this);
		townsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final JPanel townsPanel = new JPanel();
		townsPanel.setBorder(BorderFactory.createTitledBorder("Towns"));
		townsPanel.add(new JScrollPane(townsList));
		this.add(townsPanel, gc);
		++gc.gridy;
		final JPanel townPanel = new JPanel(new BorderLayout());
		townPanel.setBorder(BorderFactory.createTitledBorder("Town"));
		townTable = new JTable(townTableModel);
		townTable.setFillsViewportHeight(true);
		townTableModel.setColumnIdentifiers(new Object[] { "key", "value" });
		townPanel.add(townTable, BorderLayout.CENTER);
		this.add(townPanel, gc);
		
		//populate the lists
		final List<ITown> towns = new ArrayList<ITown>();
		setting.getWorld().getTowns(towns);
		for(final ITown t: towns) {
			onTownAdded(t);
		}
		//list to world changes
		setting.getWorld().addWorldListener(this);

		removeTownItem = new JMenuItem(Language.string("Remove"));
		contextMenu = createContextMenu();
	}	
	
	private JPopupMenu createContextMenu() {
		final JPopupMenu contextMenu  = new JPopupMenu();
		contextMenu.add(removeTownItem);
		removeTownItem.addActionListener(this);
		return contextMenu;
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
	public void onTownAdded(final ITown town) {
		logger.debug("town ",town," added");
		townsListModel.addElement(town);
	}

	@Override
	public void onTownChanged(ITown map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTownRemoved(final ITown town) {
		townsListModel.removeElement(town);
	}

	@Override
	public void onTownsCleared() {
		townsListModel.removeAllElements();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		final Object src = e.getSource();
		if(src == townsList) {
			final ITown town = townsList.getSelectedValue();
			if(null != town) {
				logger.debug("selected town ", town);
				townTableModel.setObject(town);
				townsList.invalidate();
				townsList.repaint();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
			final int idx = townsList.locationToIndex(e.getPoint());
			if(idx < 0) return;
			townsList.setSelectedIndex(idx);
			final ITown town = townsList.getSelectedValue();
			if(null == town) return;
			logger.debug("selected item is ",town);
			contextMenu.show(townsList, e.getX(), e.getY());
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
		if(src == removeTownItem) {
			townTableModel.setObject(null);
			setting.getWorld().removeTown(townsList.getSelectedValue());
		}
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
