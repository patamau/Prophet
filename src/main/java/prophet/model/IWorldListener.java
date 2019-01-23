package prophet.model;

public interface IWorldListener {

	public void onMapAdded(final IMap map);
	public void onMapChanged(final IMap map);
	public void onMapRemoved(final IMap map);
	public void onMapsCleared();
	
	public void onTownAdded(final ITown town);
	public void onTownChanged(final ITown map);
	public void onTownRemoved(final ITown town);
	public void onTownsCleared();
}
