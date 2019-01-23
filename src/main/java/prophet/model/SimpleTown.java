package prophet.model;

import java.util.Observable;

public class SimpleTown extends Observable implements ITown {
	
	private String name;
	private double longitude, latitude;
	
	public SimpleTown() {
		name = "Town";
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public void setName(final String name) {
		this.name = name;
	}
	
	@Override
	public void setLongitude(final double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public void setLatitude(final double latitude) {
		this.latitude = latitude;
	}
	
	 @Override
	public double getLongitude() {
		return longitude;
	}
	
	@Override
	public double getLatitude() {
		return latitude;
	}

}
