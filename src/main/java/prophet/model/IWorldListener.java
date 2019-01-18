package prophet.model;

public interface IWorldListener {

	public void onMapAdded(final IMap map);
	public void onMapRemoved(final IMap map);
	public void onMapsCleared();
	
	public void onTownAdded(final ITown town);
	public void onTownRemoved(final ITown town);
	public void onTownsCleared();
}
