package prophet.gui.layers;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import prophet.gui.IRenderer;

public class PictureLayer extends LayerBase {
	
	public static final int CROSS_SIZ = 10; //pixels
	
	private final IRenderer renderer;
	private BufferedImage image;
	private double scale;
	private final Point imageOffset;
	
	
	public PictureLayer(final IRenderer renderer, final BufferedImage image, final double scale, final Point2D offset)
	{
		this.image = image;
		this.renderer = renderer;
		this.scale = scale;
		this.imageOffset = new Point();
		updateOffset(offset);
	}
	
	public void set(final BufferedImage image, final Point2D offset, final double scale) {
		this.image = image;
		this.scale = scale;
		updateOffset(offset);
	}
	
	private void updateOffset(final Point2D offset) {
		final int x = (int)Math.round(offset.getX() - image.getWidth()*scale/2);
		final int y = (int)Math.round(offset.getY() + image.getHeight()*scale/2);
		imageOffset.setLocation(x, y) ;
	}

	@Override
	public void draw(Graphics2D g) {
		final AffineTransform at = new AffineTransform();
		final Point offset = renderer.getOffset();
		final double zoom = renderer.getZoom() * scale;
		at.translate(offset.getX()+(imageOffset.x*renderer.getZoom()), offset.getY()-(imageOffset.y*renderer.getZoom()));
		at.scale(zoom, zoom);
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
        g.setComposite(composite);
        g.drawImage(image, at, null);
        composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f); //restore previous alpha
        g.setComposite(composite);
	}

}
