package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class WarehouseListGrid extends BaseListGrid<ClientWarehouse> {

	public WarehouseListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 1:
			return ListGrid.COLUMN_TYPE_LINK;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 3:
			return ListGrid.COLUMN_TYPE_IMAGE;
		case 4:
			return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return 0;
		}
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 3:
			return 40;
		case 4:
			return 40;
		default:
			break;
		}
		return 0;
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.messages().warehouseCode(),
				Accounter.messages().warehouseName(),
				Accounter.messages().ddiNumber(),
				Accounter.messages().items(), Accounter.messages().delete() };
	}

	@Override
	protected void executeDelete(ClientWarehouse object) {

		AccounterAsyncCallback<ClientWarehouse> callback = new AccounterAsyncCallback<ClientWarehouse>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientWarehouse result) {
				if (result != null) {
					deleteObject(result);
				}
			}
		};
		Accounter.createGETService().getObjectById(AccounterCoreType.WAREHOUSE,
				object.getID(), callback);

	}

	@Override
	protected Object getColumnValue(ClientWarehouse obj, int index) {
		switch (index) {
		case 0:
			return obj.getWarehouseCode();
		case 1:
			return obj.getName();
		case 2:
			return obj.getDDINumber();
		case 3:
			return Accounter.getFinanceMenuImages().accounterRegisterIcon();
		case 4:
			return Accounter.getFinanceMenuImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	protected void onClick(ClientWarehouse obj, int row, int col) {
		switch (col) {
		case 3:
			ActionFactory.getWareHouseItemsListAction(obj.getID()).run(null, false);
			break;
		case 4:
			showWarnDialog(obj);
			break;

		default:
			break;
		}
	}

	@Override
	public void onDoubleClick(ClientWarehouse obj) {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly())) {
			WareHouseViewAction action = new WareHouseViewAction();
			action.run(obj, false);
		}
	}
}
