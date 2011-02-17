package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.user.client.ui.FocusWidget;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ProductCombo;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;

public class ItemDialogGrid extends ListGrid<ClientItem> {

	private ProductCombo productItemCombo;
	private ClientItemGroup itemGroup;

	public ItemDialogGrid(boolean isMultiSelectionEnable,
			ClientItemGroup itemGroup) {
		super(isMultiSelectionEnable);
		this.itemGroup = itemGroup;
	}

	@Override
	public void init() {
		super.init();
		createControls();

	}

	public void createControls() {
		boolean isAddNewRequired = false;
		productItemCombo = new ProductCombo(FinanceApplication
				.getCustomersMessages().PRoduct(), 1, isAddNewRequired);
		productItemCombo.setGrid(this);
		productItemCombo.setRequired(true);
		IAccounterComboSelectionChangeHandler<ClientItem> changeHandler = new IAccounterComboSelectionChangeHandler<ClientItem>() {

			@Override
			public void selectedComboBoxItem(ClientItem selectItem) {
				if (selectItem != null) {
					deleteRecord(selectedObject);
					addData(selectItem);
					editComplete(selectItem, selectItem.getName(), 0);
				}
			}

		};
		productItemCombo.addSelectionChangeHandler(changeHandler);

	}

	@Override
	protected void addOrEditSelectBox(ClientItem obj, Object value) {
		CustomCombo box = getCustomCombo(obj, currentCol);
		if (box != null) {
			FocusWidget widget = (FocusWidget) box.getMainWidget();
			this.setWidget(currentRow, currentCol, widget);
		} else
			super.addOrEditSelectBox(obj, value);

	}

	public <E> CustomCombo<E> getCustomCombo(ClientItem obj, int colIndex) {
		switch (colIndex) {
		case 0:
			return (CustomCombo<E>) productItemCombo;
		default:
			break;
		}
		return null;
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return (ListGrid.COLUMN_TYPE_SELECT);
		case 1:
			return (ListGrid.COLUMN_TYPE_TEXT);
		}
		return 0;
	}

	@Override
	protected Object getColumnValue(ClientItem obj, int index) {
		switch (index) {
		case 0:
			return obj.getName();
		case 1:
			return obj.getSalesPrice();
		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getSelectValues(ClientItem obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isEditable(ClientItem obj, int row, int index) {
		return true;
	}

	@Override
	protected void onClick(ClientItem obj, int row, int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case 0:
			// productItemCombo.setComboItem(obj);
			break;

		default:
			break;
		}
	}

	@Override
	public void editComplete(ClientItem item, Object value, int col) {
		if (col == 0) {
			if (itemGroup != null)
				item.setItemGroup(itemGroup.getStringID());
		}
		updateData(item);
	}

	@Override
	public void onDoubleClick(ClientItem obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onValueChange(ClientItem obj, int index, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(ClientItem obj1, ClientItem obj2, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		return false;
	}

	@Override
	protected int getCellWidth(int index) {
		return -1;
	}

	@Override
	protected String[] getColumns() {
		return new String[] {
				FinanceApplication.getFinanceUIConstants().itemName(),
				FinanceApplication.getFinanceUIConstants().price() };
	}

}
