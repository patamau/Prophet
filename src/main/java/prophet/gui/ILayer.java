package prophet.gui;

import java.awt.Graphics2D;

public interface ILayer {

	public void draw(final Graphics2D g);	
	public void setEnabled(final boolean enabled);
	public boolean isEnabled();
}
