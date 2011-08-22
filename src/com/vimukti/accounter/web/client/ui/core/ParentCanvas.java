package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;

public abstract class ParentCanvas<T> extends VerticalPanel {

	abstract public void init();

	private Action action;

	/**
	 * Flag, to Determine, whether in Edit Mode or Create mode.
	 */

	protected T data;
	
	private ClientCompanyPreferences preferences;

	private ViewManager manager;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	

	public void cancel() {
		setData(null);
		getManager().closeCurrentView();
	}


	public void setAction(Action action) {
		this.action = action;
	}

	public Action getAction() {
		return action;
	}

	@Override
	public String toString() {
		if (action == null)
			return super.toString();
		else
			return action.getText();
	}

	public boolean shouldSaveInHistory() {
		return false;
	}

	public boolean isAListView() {
		return false;
	}

	public void disableUserEntry() {

	}

	/**
	 * return String values for Select box in Grid,
	 * 
	 * @param colIndex
	 *            is selectBox index in row
	 */
	public String[] getGridSelectBoxValues(int colIndex) {
		return null;
	}

	/**
	 * called when values has been changed of Selectbox in grid
	 * 
	 * @param colIndex
	 *            is selectBox index in row
	 */
	public void onSelectBoxValueChange(int colIndex, Object value) {
	}


	/**
	 * call this method to set focus in View
	 */
	public abstract void setFocus();

	public abstract void fitToSize(int height, int width);


	public abstract void onEdit();

	public abstract void printPreview();

	public abstract void print();

	public void exportToCsv() {
	}

	public void initData() {
		// TODO Auto-generated method stub
		
	}

	public ViewManager getManager() {
		return manager;
	}

	public void setManager(ViewManager manager) {
		this.manager = manager;
	}

	public ClientCompanyPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
	}
	
	

}
