package prophet.gui.renderers;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import prophet.gui.ILayer;
import prophet.gui.IRenderer;
import prophet.model.IWorld;
import prophet.model.SimpleTown;

/**
 * Handles rendering of custom layers in a JPanel.
 * 
 * It supports panning and zooming.
 * 
 * @author patamau
 */
@SuppressWarnings("serial")
public class WorldRendererComponent extends JComponent implements IRenderer,
	MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener, ActionListener {
	
	public static final double ZOOM_MAX = 1000.0d, ZOOM_MIN = 0.001d;
	
	private final Cursor normalCursor, dragCursor;
	private final Point mousePressedPos;
	private final Point offset;
	private double zoom;
	private Dimension currentSize;
	private final List<ILayer> layers;
	private final Set<IRendererListener> listeners;
	private final IWorld world;
	private JMenuItem newTownItem;

	private final JPopupMenu contextMenu;
	
	public WorldRendererComponent(final IWorld world)
	{
		this.world = world;
		layers = new ArrayList<ILayer>();
		listeners = new HashSet<IRendererListener>();
		normalCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
		dragCursor = new Cursor(Cursor.HAND_CURSOR);
		mousePressedPos = new Point(-1,-1);
		currentSize = new Dimension(getWidth(), getHeight());
		offset = new Point();
		zoom = 1d;
		contextMenu = createContextMenu();
		
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		addComponentListener(this);
	}
	
	private JPopupMenu createContextMenu() {
		final JPopupMenu contextMenu  = new JPopupMenu();
		final JMenu editMenu = new JMenu("Editor");
		newTownItem = new JMenuItem("New Town");
		newTownItem.addActionListener(this);
		editMenu.add(newTownItem);
		contextMenu.add(editMenu);
		return contextMenu;
	}
	
	@Override
	public void setZoom(final double z)
	{
		double _z = z < ZOOM_MIN ? ZOOM_MIN : z > ZOOM_MAX ? ZOOM_MAX : z;
		//compute the distance to the zoom focus
		//in this case the focus is the panel center (width and height divided by 2)
		double dx = getWidth()*0.5 - offset.getX();
		double dy = getHeight()*0.5 - offset.getY();
		double zdx = dx*zoom - dx*_z;
		double zdy = dy*zoom - dy*_z;
		//adapt distance to the old zoom
		offset.x += zdx/zoom;
		offset.y += zdy/zoom;
		zoom = _z;
	    repaint();
	    fireZoomChanged(zoom);
	}
	
	@Override
	public double getZoom() {
		return zoom;
	}
	
	@Override
	public int getScreenX(final double x) {
		return (int)Math.round(x * zoom + offset.getX());
	}

	@Override
	public int getScreenY(final double y) {
		return (int)Math.round(-y * zoom + offset.getY());
	}
	
	@Override
	public double getWorldX(int x) {
		return (x - offset.getX()) / zoom;
	}
	
	@Override
	public double getWorldY(int y) {
		return (y - offset.getY()) / zoom;
	}
	
	@Override
	public int getScreenSize(double size) {
		return (int)Math.round(size * zoom);
	}
	
	@Override
	public Point getOffset() {
		return offset;
	}

	@Override
	protected void paintComponent(final Graphics g) {
		final Graphics2D g2 = (Graphics2D)g;
		g2.clearRect(0, 0, this.getWidth(), this.getHeight());
		drawLayers(g2);
		g2.dispose();
	}
	
	public void drawLayers(final Graphics2D g) {
		synchronized(layers) {
			//draw all layers
			for(final ILayer layer : layers) {
				if(layer.isEnabled()) {
					layer.draw(g);
				}
			}
		}
	}
	
	public void addLayer(final ILayer layer) {
		synchronized (layers) {
			layers.add(layer);
		}
	}
	
	public void removeLayer(final ILayer layer) {
		synchronized (layers) {
			layers.remove(layer);
		}
	}
	
	public void clearLayers() {
		synchronized (layers) {
			layers.clear();
		}
	}
	
	public void addListener(final IRendererListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}
	
	private void fireCursorMoved(final Point point) {
		synchronized (listeners) {
			for(IRendererListener l: listeners) {
				l.onMouseMoved(point);
			}
		}
	}
	
	private void fireZoomChanged(final double zoom) {
		synchronized (listeners) {
			for(IRendererListener l: listeners) {
				l.onZoomChanged(zoom);
			}
		}
	}
	
	/*
	 * Mouse stuff
	 */

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		final double z = zoom + zoom * -0.1d * e.getWheelRotation();
		setZoom(z);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (getCursor() != dragCursor) {
				setCursor(dragCursor);
			}
			if (mousePressedPos.x > -1) {
				offset.x += (int) (e.getX() - mousePressedPos.getX());
				offset.y += (int) (e.getY() - mousePressedPos.getY());
			}
			mousePressedPos.setLocation(e.getPoint());
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePressedPos.setLocation(e.getPoint());

		double wx = getWorldX(e.getPoint().x);
		double wy = getWorldY(e.getPoint().y);
		double lon = world.toLongitude(wx);
		double lat = world.toLatitude(wy);
		fireCursorMoved(e.getPoint());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			contextMenu.show(this, e.getX(), e.getY());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (this.getCursor() != normalCursor) {
			this.setCursor(normalCursor);
		}
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
	public void componentResized(ComponentEvent e) {
		double dw = getWidth()  - currentSize.width;
		double dh = getHeight() - currentSize.height;
		if(dw!=0.0 || dh!=0.0) {
			//adapt to offset so that image is not fucked up
			currentSize.setSize(getWidth(), getHeight());
			offset.x += dw*0.5;
			offset.y += dh*0.5;
		}
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final Object src = e.getSource();
		if(newTownItem == src) {
			final SimpleTown town = new SimpleTown();
			town.setLongitude(world.toLongitude(getWorldX(mousePressedPos.x)));
			town.setLatitude(world.toLatitude(getWorldY(mousePressedPos.y)));
			world.addTown(town);
		}
	}
	
}
