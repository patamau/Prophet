package prophet.model;

import java.awt.image.BufferedImage;

public interface IMap {

	public double getLatitude();
	public double getLongitude();
	public void setLatitude(final double latitude);
	public void setLongitude(final double longitude);
	
	public BufferedImage getPicture();
	public void setPicturePath(final String picturePath);
	public String getPicturePath();
	
	/**
	 * Get width in kilometres
	 * @return
	 */
	public double getWidth();
	
	/**
	 * Get height in kilometres
	 */
	public double getHeight();
	
	/**
	 * Map scale in metres per pixel
	 * @return
	 */
	public double getScale();
	
	/**
	 * Set the map scale in metres per pixel
	 * @param scale
	 */
	public void setScale(final double scale);
}
