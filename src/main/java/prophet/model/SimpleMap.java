package prophet.model;

import java.awt.image.BufferedImage;

import prophet.util.Resources;

public class SimpleMap implements IMap{
	
	private double latitude, longitude;
	private double width, height;
	private double scale; //this is meters per pixels
	private String picturePath;
	private transient BufferedImage picture;
	
	public SimpleMap() {
		latitude = longitude = 0d;
		scale = 1d;
		picturePath = null;
		picture = null;
	}
	
	private void updateSize() {
		if(null == picture) {
			width = height = 0;
		} else {
			width = picture.getWidth() * scale;
			height = picture.getHeight() * scale;
		}
	}
	
	@Override
	public void setPicturePath(final String path) {
		this.picturePath = path;
		picture = Resources.getImage(path);
		/*
		final File f = new File(path);
		final URL url;
		if (!f.exists()) {
			System.out.println("no such file "+path);
			url = Main.class.getResource(path);
		} else {
			try {
				url = f.toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
				picture = null;
				updateSize();
				return;
			}
		}
		try {
			picture = ImageIO.read(url);
		} catch (Exception e) {
			System.out.println("bad url "+url+": file "+f+" path "+path);
			picture = null;
			e.printStackTrace();
		}
		*/
		updateSize();
	}
	
	@Override
	public String getPicturePath () {
		return picturePath;
	}
	
	@Override
	public BufferedImage getPicture () {
		return picture;
	}

	@Override
	public double getLatitude() {
		return latitude;
	}

	@Override
	public double getLongitude() {
		return longitude;
	}

	@Override
	public void setLatitude(final double latitude) {
		this.latitude = latitude;
	}

	@Override
	public void setLongitude(final double longitude) {
		this.longitude = longitude;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public double getScale() {
		return scale;
	}

	@Override
	public void setScale(final double scale) {
		this.scale = scale;
		updateSize();
	}

}
