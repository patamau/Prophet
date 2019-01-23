package prophet.gui;

import java.lang.reflect.Field;
import java.util.Observable;

import javax.swing.table.DefaultTableModel;

import prophet.model.IObservable;
import prophet.util.ClassUtils;
import prophet.util.Logger;

@SuppressWarnings("serial")
public class KeyValueTableModel extends DefaultTableModel {

	private static final Logger logger = Logger.getLogger(KeyValueTableModel.class);
	
	private IObservable object;

	public KeyValueTableModel() {
		object = null;
		this.addColumn("Key");
		this.addColumn("Value");
	}

	public void setObject(final IObservable object) {
		// remove all the data from the table
		while (this.getRowCount() > 0) {
			super.removeRow(0);
		}
		this.object = object;
		// fill in the new data only if the node was not null
		if (object != null) {
			// assign values at the table cell
			for (final Field f : ClassUtils.getFields(object.getClass())) {
				try {
					this.addRow(new Object[] { f.getName(), f.get(object) });
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isCellEditable(int row, int col) {
		return col>0;
	}

	public void setValueAt(Object v, int r, int c) {
		final String key = (String) this.getValueAt(r, 0);
		final String current = ClassUtils.getFieldValue(object, key);
		if(current.equals(v)) return;
		logger.debug("setting ", key, " to ", v," for ", object);
		try {
			ClassUtils.setFieldValue(object, key, (String)v);
			super.setValueAt(v, r, c);
			object.notifyObservers();
		} catch (Exception e) {
			logger.error(e.getMessage());
			super.setValueAt(getValueAt(r, 1), r, 1);
		}
	}
}
