package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class DashBoardBankAccountGrid extends ListGrid<ClientAccount> {

	public DashBoardBankAccountGrid() {
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
		return ListGrid.COLUMN_TYPE_LABEL;
	}

	@Override
	protected Object getColumnValue(ClientAccount obj, int index) {
		switch (index) {
		case 0:
			return obj.getName();
		case 1:
			return DataUtils.amountAsStringWithCurrency(
					obj.getTotalBalanceInAccountCurrency(), obj.getCurrency());
		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getSelectValues(ClientAccount obj, int index) {
		return null;
	}

	@Override
	protected void onValueChange(ClientAccount obj, int index, Object value) {

	}

	@Override
	protected boolean isEditable(ClientAccount obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(ClientAccount obj, int row, int index) {
		ActionFactory.getAccountRegisterAction().run(obj, false);
	}

	@Override
	public void onDoubleClick(ClientAccount obj) {
		ActionFactory.getNewBankAccountAction().run((ClientBankAccount) obj,
				false);
	}

	@Override
	protected int sort(ClientAccount obj1, ClientAccount obj2, int index) {
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		return 0;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "", "" };
	}

	@Override
	protected String getHeaderStyle(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getRowElementsStyle(int index) {
		// TODO Auto-generated method stub
		return null;
	}

}
