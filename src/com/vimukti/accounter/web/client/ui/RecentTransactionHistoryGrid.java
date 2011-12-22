package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.RecentTransactionsList;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class RecentTransactionHistoryGrid extends
		ListGrid<RecentTransactionsList> {

	public RecentTransactionHistoryGrid() {
		super(false);
	}

	@Override
	public void init() {
		super.init();
		this.header.getElement().getParentElement().getParentElement()
				.addClassName("dashboard_grid_header");
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 1:
			return ListGrid.COLUMN_TYPE_LINK;
		case 2:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		default:
			return ListGrid.COLUMN_TYPE_LABEL;
		}
	}

	@Override
	protected Object getColumnValue(RecentTransactionsList obj, int index) {
		switch (index) {
		case 1:
			return Utility.getTransactionName(obj.getType());
		case 2:
			return DataUtils.amountAsStringWithCurrency(
					Math.abs(obj.getAmount()),
					getCompany().getCurrency(obj.getCurrecyId()));
		case 0:
			return obj.getTransactionDate();
		case 3:
			return obj.getName();
		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getSelectValues(RecentTransactionsList obj, int index) {
		return null;
	}

	@Override
	protected void onValueChange(RecentTransactionsList obj, int index,
			Object value) {

	}

	@Override
	protected boolean isEditable(RecentTransactionsList obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(RecentTransactionsList obj, int row, int index) {

	}

	@Override
	public void onDoubleClick(RecentTransactionsList obj) {
		ReportsRPC.openTransactionView(obj.getType(), obj.getID());
	}

	@Override
	protected int sort(RecentTransactionsList obj1,
			RecentTransactionsList obj2, int index) {
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 60;
		case 3:
			return 100;
		default:
			break;
		}
		return 0;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "", "", "", "" };
	}

}
