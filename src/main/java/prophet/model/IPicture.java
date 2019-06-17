package prophet.model;

import java.awt.image.BufferedImage;

public interface IPicture extends IObservable {
	
	public String getName();

	public BufferedImage getImage();
	public void setPicturePath(final String picturePath);
	public String getPicturePath();

	public double getLatitude();
	public double getLongitude();
	public void setLatitude(final double latitude);
	public void setLongitude(final double longitude);
	
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
