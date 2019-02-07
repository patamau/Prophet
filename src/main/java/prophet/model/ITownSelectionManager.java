package prophet.model;

public interface ITownSelectionManager {
	
	public void onTownSelected(final ITown town);
	
	public ITown getSelectedTown();

}
