package prophet.model;

public interface IMap extends IObservable, IPicture {

	public double getLatitude();
	public double getLongitude();
	public void setLatitude(final double latitude);
	public void setLongitude(final double longitude);

}
