package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientStockAdjustment;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class StockAdjustmentsListGrid extends BaseListGrid<StockAdjustmentList> {

	public StockAdjustmentsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected void executeDelete(StockAdjustmentList object) {

		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly())) {
			AccounterAsyncCallback<ClientStockAdjustment> callback = new AccounterAsyncCallback<ClientStockAdjustment>() {

				@Override
				public void onException(AccounterException caught) {
				}

				@Override
				public void onResultSuccess(ClientStockAdjustment result) {
					if (result != null) {
						deleteObject(result);
					}
				}

			};
			Accounter.createGETService().getObjectById(
					AccounterCoreType.STOCK_ADJUSTMENT,
					object.getStockAdjustment(), callback);
		}
	}

	@Override
	protected void onClick(StockAdjustmentList obj, int row, int col) {
		switch (col) {
		case 3:
			showWarnDialog(obj);
			break;
		default:
			break;
		}
	}

	@Override
	protected void showWarnDialog(final StockAdjustmentList object) {

		Accounter.showWarning(
				messages.doyouwanttoDeleteObj(object.getWareHouse()),
				AccounterType.WARNING, new ErrorDialogHandler() {

					@Override
					public boolean onYesClick() {
						executeDelete(object);
						return true;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onCancelClick() {
						return false;
					}
				});

	}

	@Override
	protected Object getColumnValue(StockAdjustmentList obj, int index) {
		switch (index) {
		case 0:
			return obj.getWareHouse();
		case 1:
			return obj.getItem();
		case 2:
			StringBuffer result = new StringBuffer();
			ClientUnit unit = getCompany().getUnitById(
					obj.getQuantity().getUnit());
			result.append(obj.getQuantity().getValue());
			result.append(" ");
			result.append(unit.getName());
			return result.toString();
		case 3:
			return Accounter.getFinanceMenuImages().delete();
		}
		return null;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 3:
			return 40;
		default:
			break;
		}
		return 0;
	}

	@Override
	public void onDoubleClick(StockAdjustmentList obj) {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly())) {
			AccounterAsyncCallback<ClientStockAdjustment> callback = new AccounterAsyncCallback<ClientStockAdjustment>() {

				@Override
				public void onException(AccounterException caught) {
				}

				@Override
				public void onResultSuccess(ClientStockAdjustment result) {
					if (result != null) {
						ActionFactory.getStockAdjustmentAction().run(result,
								false);
					}
				}

			};
			Accounter.createGETService().getObjectById(
					AccounterCoreType.STOCK_ADJUSTMENT,
					obj.getStockAdjustment(), callback);
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.wareHouse(), messages.itemName(),
				messages.adjustmentQty(), messages.delete() };
	}

}
