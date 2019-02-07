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
import prophet.model.IBorderSelectionManager;
import prophet.model.IMap;
import prophet.model.ITown;
import prophet.model.ISetting;
import prophet.model.IWorldListener;
import prophet.util.Language;
import prophet.util.Logger;

@SuppressWarnings("serial")
public class BordersWidget extends WidgetBase implements IWorldListener, ListSelectionListener, MouseListener, ActionListener {

	private final static Logger logger = Logger.getLogger(BordersWidget.class);
	private final static String WTITLE = "Towns";
	
	private final ISetting setting;
	private final DefaultListModel<IBorder> bordersListModel;
	private final KeyValueTableModel borderTableModel;
	private final JList<IBorder> bordersList;
	private final JTable borderTable;
	private final JPopupMenu contextMenu;
	private final JMenuItem removeBorderItem;
	private final IBorderSelectionManager selectionManager;
	
	public BordersWidget(final ISetting setting, final IBorderSelectionManager selectionManager) {
		super(WTITLE);
		this.setting = setting;
		this.selectionManager = selectionManager;
		bordersListModel = new DefaultListModel<IBorder>();
		borderTableModel = new KeyValueTableModel();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = gc.gridy = 0;
		gc.weightx = 1.0;
		gc.weighty = 0.5;
		gc.fill = 1;
		bordersList = new JList<IBorder>(bordersListModel);
		bordersList.addListSelectionListener(this);
		bordersList.addMouseListener(this);
		bordersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final JPanel bordersPanel = new JPanel();
		bordersPanel.setBorder(BorderFactory.createTitledBorder("Borders"));
		bordersPanel.add(new JScrollPane(bordersList));
		this.add(bordersPanel, gc);
		++gc.gridy;
		final JPanel borderPanel = new JPanel(new BorderLayout());
		borderPanel.setBorder(BorderFactory.createTitledBorder("Border"));
		borderTable = new JTable(borderTableModel);
		borderTable.setFillsViewportHeight(true);
		borderTableModel.setColumnIdentifiers(new Object[] { "key", "value" });
		borderPanel.add(borderTable, BorderLayout.CENTER);
		this.add(borderPanel, gc);
		
		//populate the lists
		final List<IBorder> borders = new ArrayList<IBorder>();
		setting.getWorld().getBorders(borders);
		for(final IBorder b: borders) {
			onBorderAdded(b);
		}
		//list to world changes
		setting.getWorld().addWorldListener(this);

		removeBorderItem = new JMenuItem(Language.string("Remove"));
		contextMenu = createContextMenu();
	}	
	
	private JPopupMenu createContextMenu() {
		final JPopupMenu contextMenu  = new JPopupMenu();
		contextMenu.add(removeBorderItem);
		removeBorderItem.addActionListener(this);
		return contextMenu;
	}

	@Override
	public void onMapAdded(IMap map) {
	}

	@Override
	public void onMapChanged(IMap map) {
	}

	@Override
	public void onMapRemoved(IMap map) {
	}

	@Override
	public void onMapsCleared() {
	}

	@Override
	public void onTownAdded(final ITown town) {
	}

	@Override
	public void onTownChanged(ITown map) {
	}

	@Override
	public void onTownRemoved(final ITown town) {
	}

	@Override
	public void onTownsCleared() {
	}

	@Override
	public void onBorderAdded(final IBorder Border) {
		bordersListModel.addElement(Border);
	}

	@Override
	public void onBorderChanged(final IBorder map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBorderRemoved(final IBorder Border) {
		bordersListModel.removeElement(Border);
	}

	@Override
	public void onBordersCleared() {
		bordersListModel.removeAllElements();
	}
	
	@Override
	public void valueChanged(final ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		final Object src = e.getSource();
		if(src == bordersList) {
			final IBorder border = bordersList.getSelectedValue();
			if(null != border) {
				logger.debug("selected border ", border);
				borderTableModel.setObject(border);
				bordersList.invalidate();
				bordersList.repaint();
				selectionManager.onBorderSelected(border);
			}
		}
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
			final int idx = bordersList.locationToIndex(e.getPoint());
			if(idx < 0) return;
			bordersList.setSelectedIndex(idx);
			final IBorder border = bordersList.getSelectedValue();
			if(null == border) return;
			logger.debug("selected item is ",border);
			contextMenu.show(bordersList, e.getX(), e.getY());
			selectionManager.onBorderSelected(border);
		}
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(final MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final Object src = e.getSource();
		if(src == removeBorderItem) {
			borderTableModel.setObject(null);
			setting.getWorld().removeBorder(bordersList.getSelectedValue());
		}
	}

}
