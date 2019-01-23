package prophet.model;

public interface ITown extends IObservable {

	public String getName();
	public void setName(final String name);
	
	public double getLongitude();
	public double getLatitude();
	
	public void setLongitude(final double longitude);
	public void setLatitude(final double latitude);
}
