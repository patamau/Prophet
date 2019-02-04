package prophet.model;

public interface ITown extends IPoi {

	public double getSize();
	public int getPopulation();
	
	public void setSize(final double size);
	public void setPopulation(final int population);
}
