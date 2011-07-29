package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.ItemListView;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class ItemsListGrid extends BaseListGrid<ClientItem> {

	public ItemsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0)
			return 40;
		else if (index == 4)
			return 200;
		else if (index == 5)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;

		return super.getCellWidth(index);
	};

	@Override
	protected Object getColumnValue(ClientItem obj, int col) {
		switch (col) {
		case 0:
			return obj.isActive();
		case 1:
			return obj.getName() != null ? obj.getName() : "";
		case 2:
			if (!ItemListView.isPurchaseType) {
				return obj.getSalesDescription() != null ? obj
						.getSalesDescription() : "";
			} else
				return obj.getPurchaseDescription() != null ? obj
						.getPurchaseDescription() : "";

		case 3:
			return Utility.getItemTypeText(obj) != null ? Utility
					.getItemTypeText(obj) : "";
		case 4:
			if (!ItemListView.isPurchaseType) {
				return DataUtils.getAmountAsString(obj.getSalesPrice()) != null ? DataUtils
						.getAmountAsString(obj.getSalesPrice()) : "";
			} else
				return DataUtils.getAmountAsString(obj.getPurchasePrice()) != null ? DataUtils
						.getAmountAsString(obj.getPurchasePrice()) : "";

		case 5:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		}
		return null;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().active(),
				Accounter.constants().itemName(),
				Accounter.constants().description(),
				Accounter.constants().type(),
				Accounter.constants().price(), "" };
	}

	@Override
	public void onDoubleClick(ClientItem obj) {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			ActionFactory.getNewItemAction().run(obj, true);
		}
	}

	@Override
	public boolean validateGrid() {
		return true;
	}

	protected void onClick(ClientItem item, int row, int col) {
		if (!Accounter.getUser().canDoInvoiceTransactions())
			return;
		if (col == 5) {
			if (item != null)
				showWarnDialog(item);
		}

	}

	protected void executeDelete(final ClientItem recordToBeDeleted) {
		ViewManager.getInstance().deleteObject(recordToBeDeleted,
				AccounterCoreType.ITEM, this);
	}

	@Override
	protected int sort(ClientItem item1, ClientItem item2, int index) {

		switch (index) {
		case 1:
			return item1.getName().toLowerCase()
					.compareTo(item2.getName().toLowerCase());
		case 2:
			if (!ItemListView.isPurchaseType) {
				String obj1 = item1.getSalesDescription() != null ? item1
						.getSalesDescription() : "";
				String obj2 = item2.getSalesDescription() != null ? item2
						.getSalesDescription() : "";
				return obj1.toLowerCase().compareTo(obj2.toLowerCase());
			} else {
				String obj1 = item1.getPurchaseDescription() != null ? item1
						.getPurchaseDescription() : "";
				String obj2 = item2.getPurchaseDescription() != null ? item2
						.getPurchaseDescription() : "";
				return obj1.toLowerCase().compareTo(obj2.toLowerCase());
			}

		case 3:
			String type1 = Utility.getItemTypeText(item1) != null ? Utility
					.getItemTypeText(item1) : "";
			String type2 = Utility.getItemTypeText(item2) != null ? Utility
					.getItemTypeText(item2) : "";
			return type1.compareTo(type2);

		case 4:
			if (!ItemListView.isPurchaseType) {
				Double price1 = item1.getSalesPrice();
				Double price2 = item2.getSalesPrice();
				return price1.compareTo(price2);
			} else {
				Double price1 = item1.getPurchasePrice();
				Double price2 = item2.getPurchasePrice();
				return price1.compareTo(price2);
			}

		default:
			break;
		}

		return 0;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	public AccounterCoreType getType() {
		return AccounterCoreType.ITEM;
	}

	@Override
	public void addData(ClientItem obj) {
		super.addData(obj);
		((CheckBox) this.getWidget(currentRow, 0)).setEnabled(false);
	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
		for (int i = 0; i < this.getRowCount(); i++) {
			((CheckBox) this.getWidget(i, 0)).setEnabled(false);
		}
	}

}
