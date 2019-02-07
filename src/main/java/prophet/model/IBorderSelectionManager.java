package prophet.model;

public interface IBorderSelectionManager {
	
	public void onBorderSelected(final IBorder border);
	
	public IBorder getSelectedBorder();
	
}
