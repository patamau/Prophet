package prophet.gui.widgets;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
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

import prophet.gui.ILayer;
import prophet.gui.KeyValueTableModel;
import prophet.gui.renderers.IRendererListener;
import prophet.gui.renderers.WorldRendererComponent;
import prophet.util.Language;
import prophet.util.Logger;

/**
 * Handle all the maps
 * @author patam
 *
 */
@SuppressWarnings("serial")
public class LayersWidget extends ToolBarWidgetBase implements IRendererListener, ListSelectionListener, MouseListener, ActionListener {

	private final static Logger logger = Logger.getLogger(LayersWidget.class);
	
	private final static String WTITLE = "Layers";
	
	private final JList<ILayer> layersList;
	private final DefaultListModel<ILayer> layersListModel;
	private final KeyValueTableModel layerTableModel;
	private final JTable layerTable;
	private final JMenuItem moveUpLayerItem, moveDownLayerItem;
	private final WorldRendererComponent worldRenderer;
	
	private final JPopupMenu contextMenu;
	
	public LayersWidget(final WorldRendererComponent worldRenderer) {
		super(WTITLE);
		this.worldRenderer = worldRenderer;
		layersListModel = new DefaultListModel<ILayer>();
		layerTableModel = new KeyValueTableModel();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = gc.gridy = 0;
		gc.weightx = 1.0;
		gc.weighty = 0.5;
		gc.fill = 1;
		layersList = new JList<ILayer>(layersListModel);
		layersList.addListSelectionListener(this);
		layersList.addMouseListener(this);
		layersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final JPanel layersPanel = new JPanel();
		layersPanel.setBorder(BorderFactory.createTitledBorder("Layers"));
		layersPanel.add(new JScrollPane(layersList));
		this.add(layersPanel, gc);
		++gc.gridy;
		final JPanel layerPanel = new JPanel(new BorderLayout());
		layerPanel.setBorder(BorderFactory.createTitledBorder("Layer"));
		layerTable = new JTable(layerTableModel);
		layerTable.setFillsViewportHeight(true);
		layerTableModel.setColumnIdentifiers(new Object[] { "key", "value" });
		layerPanel.add(layerTable, BorderLayout.CENTER);
		this.add(layerPanel, gc);
		
		//populate the lists

		updateLayers();
		//list to world changes
		worldRenderer.addListener(this);

		moveUpLayerItem = new JMenuItem(Language.string("Up"));
		moveDownLayerItem = new JMenuItem(Language.string("Down"));
		contextMenu = createContextMenu();
	}
	
	private void updateLayers() {
		layersListModel.clear();
		layerTableModel.setObject(null);
		final List<ILayer> layers = new ArrayList<ILayer>();
		worldRenderer.getLayers(layers);
		for(final ILayer m: layers) {
			onLayerAdded(m);
		}
	}

	private JPopupMenu createContextMenu() {
		final JPopupMenu contextMenu  = new JPopupMenu();
		contextMenu.add(moveUpLayerItem);
		contextMenu.add(moveDownLayerItem);
		moveUpLayerItem.addActionListener(this);
		moveDownLayerItem.addActionListener(this);
		return contextMenu;
	}

	@Override
	public void onLayerAdded(final ILayer layer) {
		layersListModel.addElement(layer);
		layersList.invalidate();
		layersList.repaint();
	}

	@Override
	public void onLayerRemoved(final ILayer layer) {
		layersListModel.removeElement(layer);
		layersList.invalidate();
	}

	@Override
	public void onLayersCleared() {
		layersListModel.removeAllElements();
		layersList.invalidate();
	}
	
	@Override
	public void onLayersChanged() {
		updateLayers();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		final Object src = e.getSource();
		if(src == layersList) {
			final ILayer layer = layersList.getSelectedValue();
			if(null != layer) {
				logger.debug("selected layer ", layer);
				layerTableModel.setObject(layer);
				layerTable.invalidate();
				layerTable.repaint();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
			final int idx = layersList.locationToIndex(e.getPoint());
			if(idx < 0) return;
			layersList.setSelectedIndex(idx);
			final ILayer layer = layersList.getSelectedValue();
			if(null == layer) return;
			logger.debug("selected item is ",layer);
			contextMenu.show(layersList, e.getX(), e.getY());
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
		if(src == moveUpLayerItem) {
			final ILayer selectedLayer = layersList.getSelectedValue();
			if (selectedLayer != null) {
				final int depth = worldRenderer.getLayerDepth(selectedLayer);
				worldRenderer.setLayerDepth(selectedLayer, depth>0?depth-1:0);
			}
		} else if(src == moveDownLayerItem) {
			final ILayer selectedLayer = layersList.getSelectedValue();
			if (selectedLayer != null) {
				final int depth = worldRenderer.getLayerDepth(selectedLayer);
				worldRenderer.setLayerDepth(selectedLayer, depth+1);
			}
		}
	}

	@Override
	public void onZoomChanged(double zoom) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseMoved(Point point) {
		// TODO Auto-generated method stub
		
	}
}
