package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.CustomersMessages;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class CustomerRefundListGrid extends BaseListGrid<CustomerRefundsList> {

	public CustomerRefundListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	boolean isDeleted;

	@Override
	protected Object getColumnValue(CustomerRefundsList customerRefund, int col) {
		switch (col) {
		case 0:
			return UIUtils
					.getDateByCompanyType(customerRefund.getPaymentDate());
		case 1:
			return customerRefund.getPaymentNumber();
		case 2:
			return Utility.getStatus(ClientTransaction.TYPE_CUSTOMER_REFUNDS,
					customerRefund.getStatus());
		case 3:
			return UIUtils.getDateByCompanyType(customerRefund.getIssueDate());
		case 4:
			return customerRefund.getName();
		case 5:
			return Utility.getTransactionName((customerRefund.getType()));
		case 6:
			return customerRefund.getPaymentMethod();
		case 7:
			return DataUtils.getAmountAsString(customerRefund.getAmountPaid());
		case 8:
			if (!customerRefund.isVoided())
				return Accounter.getFinanceImages().notvoid();
			// return "/images/not-void.png";
			else
				return Accounter.getFinanceImages().voided();
			// return "/images/voided.png";
			// case 9:
			// if (customerRefund.isDeleted())
			// return FinanceApplication.getFinanceImages().delSuccess()
			// .getURL();
			// else
			// return FinanceApplication.getFinanceImages().delete().getURL();

		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getColumns() {
		customerConstants = GWT.create(CustomersMessages.class);
		return new String[] { customerConstants.paymentDate(),
				customerConstants.paymentNo(), customerConstants.status(),
				customerConstants.issueDate(), customerConstants.name(),
				customerConstants.type(), customerConstants.paymentMethod(),
				customerConstants.amountPaid(), customerConstants.voided()
		// , ""
		};
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE
		// ,ListGrid.COLUMN_TYPE_IMAGE
		};
	}

	@Override
	public void onDoubleClick(CustomerRefundsList customerRefunds) {
		ReportsRPC.openTransactionView(customerRefunds.getType(),
				customerRefunds.getTransactionId());
	}

	protected void onClick(CustomerRefundsList obj, int row, int col) {
		if (col == 8 && !obj.isVoided()) {
			showWarningDialog(obj, col);
		}
		// else if (col == 9) {
		// if (!isDeleted)
		// showWarningDialog(obj, col);
		// else
		// return;
		// }

	}

	private void showWarningDialog(final CustomerRefundsList obj, final int col) {
		String msg = null;
		if (col == 8) {
			msg = Accounter.constants()
					.doyouwanttoVoidtheTransaction();
		}
		// else if (col == 9) {
		// msg = "Do you want to Delete the Transaction";
		//
		// }
		Accounter.showWarning(msg, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() throws InvalidEntryException {
						return false;
					}

					@Override
					public boolean onNoClick() throws InvalidEntryException {
						return true;
					}

					@Override
					public boolean onYesClick() throws InvalidEntryException {
						if (col == 8)
							voidTransaction(obj);
						// else if (col == 9)
						// deleteTransaction(obj);
						return true;
					}

				});

	}

	protected void voidTransaction(final CustomerRefundsList obj) {
		ViewManager.getInstance().voidTransaction(
				UIUtils.getAccounterCoreType(obj.getType()),
				obj.getTransactionId(), this);
	}

	protected void deleteTransaction(final CustomerRefundsList obj) {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					if (!viewType.equalsIgnoreCase("All"))
						deleteRecord(obj);
					obj.setStatus(ClientTransaction.STATUS_DELETED);
					isDeleted = true;
					obj.setVoided(true);
					updateData(obj);
				}

			}
		};
		AccounterCoreType type = UIUtils.getAccounterCoreType(obj.getType());
		rpcDoSerivce.deleteTransaction(type, obj.getTransactionId(), callback);
	}

	@Override
	public boolean validateGrid() {
		// its not using any where
		return false;
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 8)
			return 50;
		// else if (index == 9)
		// return 30;
		return -1;
	}

	@Override
	protected void executeDelete(CustomerRefundsList object) {
		// its not using any where

	}

	@Override
	protected int sort(CustomerRefundsList obj1, CustomerRefundsList obj2,
			int index) {
		switch (index) {
		case 0:
			ClientFinanceDate date1 = obj1.getPaymentDate();
			ClientFinanceDate date2 = obj2.getPaymentDate();
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
			break;

		case 1:
			int num1 = UIUtils.isInteger(obj1.getPaymentNumber()) ? Integer
					.parseInt(obj1.getPaymentNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getPaymentNumber()) ? Integer
					.parseInt(obj2.getPaymentNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return obj1.getPaymentNumber().compareTo(
						obj2.getPaymentNumber());

		case 2:
			String status1 = Utility.getStatus(
					ClientTransaction.TYPE_CUSTOMER_REFUNDS, obj1.getStatus());
			String status2 = Utility.getStatus(
					ClientTransaction.TYPE_CUSTOMER_REFUNDS, obj2.getStatus());
			return status1.compareTo(status2);

		case 3:
			ClientFinanceDate issuedate1 = obj1.getIssueDate();
			ClientFinanceDate issuedate2 = obj2.getIssueDate();
			if (issuedate1 != null && issuedate2 != null)
				return issuedate1.compareTo(issuedate2);
			break;

		case 4:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());

		case 5:
			String type1 = Utility.getTransactionName((obj1.getType()))
					.toLowerCase();
			String type2 = Utility.getTransactionName((obj2.getType()))
					.toLowerCase();
			return type1.compareTo(type2);

		case 6:
			return obj1.getPaymentMethod().toLowerCase()
					.compareTo(obj2.getPaymentMethod().toLowerCase());

		case 7:
			return obj1.getAmountPaid().compareTo(obj2.getAmountPaid());

		default:
			break;
		}
		return 0;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// its not using any where

	}

	public AccounterCoreType getType() {
		return AccounterCoreType.CUSTOMERREFUND;
	}

	private long getTransactionID(CustomerRefundsList obj) {
		return obj.getTransactionId();
	}

	public boolean isVoided(CustomerRefundsList obj) {
		return obj.isVoided();
	}

	public AccounterCoreType getAccounterCoreType(CustomerRefundsList obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}
}
