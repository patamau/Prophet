package prophet.gui.layers;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import prophet.gui.IRenderer;
import prophet.model.IPicture;
import prophet.model.IWorld;

public class PictureLayer extends LayerBase {
	
	public static final int CROSS_SIZ = 10; //pixels
	
	private final IRenderer renderer;
	private final IPicture picture;
	private transient final Point imageOffset;
	private transient final IWorld world;
	
	public PictureLayer(final IPicture picture, final IWorld world, final IRenderer renderer)
	{
		super(picture.getName());
		this.world = world;
		this.picture = picture;
		this.renderer = renderer;
		this.imageOffset = new Point();
	}
	
	@Override
	public String getName() {
		return picture.getName();
	}

	@Override
	public void draw(final Graphics2D g) {
		if(null == picture.getImage()) return;
		final BufferedImage image = picture.getImage();
		final AffineTransform at = new AffineTransform();
		final Point offset = renderer.getOffset();
		final int x = (int)Math.round(world.fromLongitude(picture.getLongitude())-image.getWidth()*picture.getScale()/2);
		final int y = (int)Math.round(world.fromLatitude(picture.getLatitude())+image.getHeight()*picture.getScale()/2);
		imageOffset.setLocation(x, y) ;
		final double zoom = renderer.getZoom() * picture.getScale();
		at.translate(offset.getX()+(imageOffset.x*renderer.getZoom()), offset.getY()-(imageOffset.y*renderer.getZoom()));
		at.scale(zoom, zoom);
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
        g.setComposite(composite);
        g.drawImage(image, at, null);
        composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f); //restore previous alpha
        g.setComposite(composite);
	}

}
