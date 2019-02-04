package prophet.model;

import java.awt.geom.Point2D;
import java.util.Observable;

public class SimpleTown extends Observable implements ITown {
	
	private String name;
	private double size;
	private int population;
	private double longitude, latitude;
	
	//this is the world position and it's computed by the world
	private transient Point2D position;
	
	public SimpleTown() {
		name = "Town";
		position = new Point2D.Double();
	}
	
	@Override
	public void setPopulation(final int population) {
		if(this.population == population) return;
		this.population = population;
		setChanged();
	}
	
	@Override
	public int getPopulation() {
		return population;
	}
	
	@Override
	public void setSize(final double size) {
		if(this.size == size) return;
		this.size = size;
		setChanged();
	}
	
	@Override
	public double getSize() {
		return size;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public void setName(final String name) {
		if(this.name == name) return;
		this.name = name;
		setChanged();
	}
	
	@Override
	public void setLongitude(final double longitude) {
		if(this.longitude == longitude) return;
		this.longitude = longitude;
		setChanged();
	}
	
	@Override
	public void setLatitude(final double latitude) {
		if(this.latitude == latitude) return;
		this.latitude = latitude;
		setChanged();
	}
	
	 @Override
	public double getLongitude() {
		return longitude;
	}
	
	@Override
	public double getLatitude() {
		return latitude;
	}

	@Override
	public Point2D getWorldPosition() {
		return position;
	}
	
	@Override
	public void updateWorldPosition(final IWorld world) {
		final double x = world.fromLongitude(longitude);
		final double y = world.fromLatitude(latitude);
		position.setLocation(x, y);
	}
}
