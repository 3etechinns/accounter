package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;

public class CustomersListGrid extends BaseListGrid<PayeeList> {
	Map<Integer, Integer> colsMap = new HashMap<Integer, Integer>();
	private CustomerSelectionListener customerSelectionListener;
	ClientCustomer selectedCustomer;
	ArrayList<PayeeList> listOfCustomers;

	public CustomersListGrid() {
		super(false, true);
		addDataToGrid();
	}

	@Override
	protected Object getColumnValue(PayeeList payee, int col) {

		switch (col) {

		case 0:
			return payee.getPayeeName();
		case 1:
			return DataUtils.amountAsStringWithCurrency(payee.getBalance(),
					payee.getCurrecny());

		default:
			break;
		}

		return null;
	}

	@Override
	protected String[] getColumns() {

		return new String[] { "Name", "Balance" };

	}

	@Override
	protected void onClick(PayeeList obj, int row, int col) {

		AccounterAsyncCallback<ClientCustomer> callback = new AccounterAsyncCallback<ClientCustomer>() {

			@Override
			public void onException(AccounterException caught) {
			}

			@Override
			public void onResultSuccess(ClientCustomer result) {
				if (result != null) {
					setSelectedCustomer(result);
					customerSelectionListener.cusotmerSelected();
				}
			}

		};
		Accounter.createGETService().getObjectById(AccounterCoreType.CUSTOMER,
				obj.id, callback);

	}

	@Override
	public void addEmptyMessage(String msg) {
		super.addEmptyMessage(msg);
	}

	private void addDataToGrid() {
		Accounter.createHomeService().getPayeeList(ClientPayee.TYPE_CUSTOMER,
				true, 0, 0, false,
				new AsyncCallback<PaginationList<PayeeList>>() {

					@Override
					public void onSuccess(PaginationList<PayeeList> result) {
						if (result.size() == 0) {
							addEmptyMessage(messages.youDontHaveAny(Global
									.get().Customers()));
						} else {
							for (PayeeList payeeList : result) {
								listOfCustomers = result;
								if (payeeList.isActive())
									addData(payeeList);
							}
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});

	}

	public void filterList(boolean isActive) {
		removeAllRecords();
		if (listOfCustomers != null) {
			for (PayeeList customer : listOfCustomers) {
				if (isActive) {
					if (customer.isActive() == true)
						addData(customer);
				} else if (customer.isActive() == false) {
					addData(customer);
				}
			}
		}
		if (getRecords().size() == 0)
			addEmptyMessage(messages.youDontHaveAny(Global.get().Customers()));
	}

	@Override
	protected void onValueChange(PayeeList obj, int col, Object value) {
		Accounter.showInformation("on value change called");
	}

	@Override
	public void onDoubleClick(PayeeList obj) {

	}

	@Override
	protected void executeDelete(final PayeeList recordToBeDeleted) {

	}

	public void setCustomerSelectionListener(
			CustomerSelectionListener customerSelectionListener) {
		this.customerSelectionListener = customerSelectionListener;

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	protected void updateTotal(PayeeList customer, boolean add) {

		if (add) {
			if (customer.isActive())
				total += customer.getBalance();
			else
				total += customer.getBalance();
		} else
			total -= customer.getBalance();
	}

	@Override
	public Double getTotal() {
		return total;
	}

	@Override
	public void setTotal() {
		this.total = 0.0D;
	}

	@Override
	protected int getCellWidth(int index) {

		return 100;
	}

	@Override
	public void addRecords(List<PayeeList> list) {
		super.addRecords(list);

	}

	@Override
	protected int sort(PayeeList obj1, PayeeList obj2, int index) {
		switch (index) {
		case 1:
			return obj1.getPayeeName().toLowerCase()
					.compareTo(obj2.getPayeeName().toLowerCase());

		default:
			break;
		}

		return 0;
	}

	@Override
	public AccounterCoreType getType() {
		return AccounterCoreType.CUSTOMER;
	}

	@Override
	public void addData(PayeeList obj) {
		super.addData(obj);

	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		if (errorCode == AccounterException.ERROR_OBJECT_IN_USE) {
			Accounter.showError(AccounterExceptions.accounterMessages
					.payeeInUse(Global.get().Customer()));
			return;
		}
		super.deleteFailed(caught);
	}

	public void setSelectedCustomer(ClientCustomer selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public ClientCustomer getSelectedCustomer() {
		return selectedCustomer;
	}

}
