package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.CustomerListGrid;

public class CustomerListView extends BaseListView<PayeeList> {

	public CustomerListView() {

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		super.deleteFailed(caught);
		AccounterException accounterException = caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void init() {
		super.init();

	}

	@Override
	protected Action getAddNewAction() {

		if (Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getNewCustomerAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {

		if (Accounter.getUser().canDoInvoiceTransactions())
			return messages.addaNew(Global.get().Customer());
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {

		return messages.payeeList(Global.get().Customer());
	}

	@Override
	protected HorizontalPanel getTotalLayout(BaseListGrid grid) {
		// grid.addFooterValue(FinanceApplication.constants().total(),
		// 8);
		// grid.addFooterValue(DataUtils.getAmountAsString(grid.getTotal()) +
		// "",
		// 9);
		return null;
	}

	@Override
	protected void initGrid() {
		grid = new CustomerListGrid();
		// grid.addStyleName("listgrid-tl");
		grid.init();

		// listOfCustomers = FinanceApplication.getCompany().getCustomers();
		// filterList(true);
		// getTotalLayout(grid);
	}

	@Override
	protected void filterList(boolean isActive) {
		this.isActive = isActive;
		onPageChange(0, getPageSize());

		// grid.removeAllRecords();
		// grid.setTotal();
		// for (PayeeList customer : listOfCustomers) {
		// if (isActive) {
		// if (customer.isActive() == true) {
		// grid.addData(customer);
		// }
		// } else if (customer.isActive() == false) {
		// grid.addData(customer);
		// }
		// }
		// if (grid.getRecords().isEmpty())
		// grid.addEmptyMessage(messages.noRecordsToShow());
		//
		// getTotalLayout(grid);
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void onPageChange(int start, int length) {
		Accounter.createHomeService().getPayeeList(ClientPayee.TYPE_CUSTOMER,
				isActive, start, length, this);
	}

	@Override
	public void onSuccess(PaginationList<PayeeList> result) {
		if (result.isEmpty()) {
			grid.removeAllRecords();
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.sort(10, false);
		grid.setRecords(result);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());

	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectedValue = viewSelect.getSelectedValue();
		if (selectedValue.equalsIgnoreCase(messages.active())) {
			isActive = true;
		} else {
			isActive = false;
		}
		map.put("isActive", isActive);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(Map<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		isActive = (Boolean) viewDate.get("isActive");
		start = (Integer) viewDate.get("start");
		if (isActive) {
			viewSelect.setComboItem(messages.active());
		} else {
			viewSelect.setComboItem(messages.inActive());
		}
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void updateInGrid(PayeeList objectTobeModified) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
		// its not using any where

	}

	@Override
	protected String getViewTitle() {
		return messages.payees(Global.get().Customers());
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> selectTypes = new ArrayList<String>();
		selectTypes.add(messages.active());
		selectTypes.add(messages.inActive());
		viewSelect.setComboItem(messages.active());
		return selectTypes;
	}
}
