package prophet.gui.layers;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import prophet.gui.ILayer;
import prophet.gui.IRenderer;

public class PictureLayer implements ILayer {
	
	public static final int CROSS_SIZ = 10; //pixels
	
	private final IRenderer renderer;
	private final BufferedImage image;
	private double scale;
	private final Point imageOffset;
	
	
	public PictureLayer(final IRenderer renderer, final BufferedImage image, final double scale, final Point offset)
	{
		this.image = image;
		this.renderer = renderer;
		this.scale = scale;
		this.imageOffset = offset;
	}

	@Override
	public void draw(Graphics2D g) {
		final AffineTransform at = new AffineTransform();
		final Point offset = renderer.getOffset();
		final double zoom = renderer.getZoom() * scale;
		at.translate(offset.getX()-(-imageOffset.x + image.getWidth()/2)*zoom, offset.getY()-(imageOffset.y + image.getHeight()/2)*zoom);
		at.scale(zoom, zoom);
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
        g.setComposite(composite);
        g.drawImage(image, at, null);
        composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f); //restore previous alpha
        g.setComposite(composite);
	}

}
