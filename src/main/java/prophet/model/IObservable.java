package prophet.model;

import java.util.Observer;

public interface IObservable {

	public boolean hasChanged();
	
	public void notifyObservers();
	public void addObserver(final Observer observer);
	public void deleteObserver(final Observer observer);
	public void deleteObservers();
}
