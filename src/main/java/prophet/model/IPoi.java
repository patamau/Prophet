package prophet.model;

import java.awt.geom.Point2D;

public interface IPoi  extends IObservable {

	public String getName();
	public void setName(final String name);
	
	public double getLongitude();
	public double getLatitude();
	
	public void setLongitude(final double longitude);
	public void setLatitude(final double latitude);
	
	public void updateWorldPosition(final IWorld world);
	public Point2D getWorldPosition();
}
