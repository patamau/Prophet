package prophet.gui;

import java.awt.Graphics2D;

import prophet.model.IObservable;

public interface ILayer extends IObservable {

	public void draw(final Graphics2D g);	
	public void setEnabled(final boolean enabled);
	public boolean isEnabled();
}
