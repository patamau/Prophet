package prophet.model;

import java.awt.image.BufferedImage;
import java.util.Observable;

import prophet.util.Resources;

public class SimpleMap extends Observable implements IMap {
	
	private double latitude, longitude;
	private double scale; //this is meters per pixels
	private String picturePath;
	private String name;
	
	private transient BufferedImage picture;
	private transient double width, height;
	private transient IWorld world;
	
	public SimpleMap() {
		this.name = "Unknown Map";
		this.world = null;
		latitude = longitude = 0d;
		scale = 1d;
		picturePath = null;
		picture = null;
	}
	
	public void setWorld(final IWorld world) {
		if(this.world == world) return;
		this.world = world;
		setChanged();
	}
	
	public void setName(final String name) {
		if(this.name == name) return;
		this.name = name;
		setChanged();
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
		if(picturePath == path) return;
		this.picturePath = path;
		picture = Resources.getImage(path);
		updateSize();
		setChanged();
	}
	
	@Override
	public String getPicturePath () {
		return picturePath;
	}
	
	@Override
	public BufferedImage getImage () {
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
		if(this.latitude==latitude) return;
		this.latitude = latitude;
		setChanged();
	}

	@Override
	public void setLongitude(final double longitude) {
		if(this.longitude==longitude) return;
		this.longitude = longitude;
		setChanged();
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
		if(this.scale==scale) return;
		this.scale = scale;
		updateSize();
		setChanged();
	}
	
	@Override
	public String toString() {
		return  this.name;
	}

	@Override
	public String getName() {
		return this.name;
	}

}
