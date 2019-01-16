package prophet.model;

public interface ITown {

	public String getName();
	public void setName(final String name);
	
	public double getLongitude();
	public double getLatitude();
	
	public void setLongitude(final double longitude);
	public void setLatitude(final double latitude);
}
