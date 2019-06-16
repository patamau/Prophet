package prophet.model;

import java.awt.image.BufferedImage;

public interface IPicture {
	
	public String getName();

	public BufferedImage getImage();
	public void setPicturePath(final String picturePath);
	public String getPicturePath();
	
	public double getX();
	public double getY();
	
	public void setX(final double x);
	public void setY(final double y);
	
	/**
	 * Scale in metres per pixel
	 * @return
	 */
	public double getScale();
	
	/**
	 * Scale in metres per pixel
	 * @param scale
	 */
	public void setScale(final double scale);
	
	public double getWidth();
	public double getHeight();
}
