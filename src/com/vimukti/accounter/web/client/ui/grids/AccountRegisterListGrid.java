package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class AccountRegisterListGrid extends BaseListGrid<AccountRegister> {

	private int accountType;
	public double balance = 0.0, payment = 0.0, deposit = 0.0;
	public double totalBalance = 0.0;

	public AccountRegisterListGrid(boolean isMultiSelectionEnable, int type) {
		super(isMultiSelectionEnable, true);
		accountType = type;
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] getColumns() {
		if (accountType == ClientAccount.TYPE_BANK)
			return new String[] { Accounter.getCustomersMessages().date(),
					Accounter.getCustomersMessages().type(),
					(Accounter.getCustomersMessages().checkNo() + "."),
					Accounter.getCustomersMessages().payTo(),
					Accounter.getCustomersMessages().memo(),
					Accounter.getCustomersMessages().account(),
					Accounter.getCustomersMessages().payment(),
					Accounter.getCustomersMessages().deposit(),
					Accounter.getCustomersMessages().currentBalance(),
					Accounter.getCustomersMessages().voided() };
		else if (accountType == ClientAccount.TYPE_CREDIT_CARD)
			return new String[] { Accounter.getCustomersMessages().date(),
					Accounter.getCustomersMessages().type(),
					Accounter.getCustomersMessages().documentNo(),
					Accounter.getCustomersMessages().payTo(),
					Accounter.getCustomersMessages().memo(),
					Accounter.getCustomersMessages().account(),
					Accounter.getCustomersMessages().charge(),
					Accounter.getCustomersMessages().payment(),
					Accounter.getCustomersMessages().currentBalance(),
					Accounter.getCustomersMessages().voided() };
		return new String[0];
	}

	@Override
	protected Object getColumnValue(AccountRegister accRegister, int col) {
		switch (col) {
		case 0:
			return accRegister.getDate();
		case 1:
			return Utility.getTransactionName((accRegister.getType()));
		case 2:
			if (accountType == ClientAccount.TYPE_BANK)
				return (accRegister.getCheckNumber() == null ? ""
						: (accRegister.getCheckNumber()).toString());
			else if (accountType == ClientAccount.TYPE_CREDIT_CARD)
				return accRegister.getNumber();
		case 3:
			return accRegister.getPayTo();
		case 4:
			return accRegister.getMemo();
		case 5:
			return accRegister.getAccount();
		case 6:
			if (accountType == ClientAccount.TYPE_BANK)
				return getBankAccValue(accRegister.getAmount(), 6);
			else if (accountType == ClientAccount.TYPE_CREDIT_CARD)
				return getCreditAccValue(accRegister.getAmount(), 6);
		case 7:
			if (accountType == ClientAccount.TYPE_BANK)
				return getBankAccValue(accRegister.getAmount(), 7);
			else if (accountType == ClientAccount.TYPE_CREDIT_CARD)
				return getCreditAccValue(accRegister.getAmount(), 7);
		case 8:
			return DataUtils.getAmountAsString(getBalanceValue(accRegister));

		case 9:
			if (!accRegister.isVoided())
				return Accounter.getFinanceImages().notvoid();
			// return "/images/not-void.png";
			else
				return Accounter.getFinanceImages().voided();
			// return "/images/voided.png";
		}
		return null;
	}

	/*
	 * For accountType "CreditCard" the "Payment" column contains the amount
	 * value(and the "Deposit" column value is "0") if the amount is -ve. And
	 * "Deposit" column contains amount value( and "Payment" column value is 0)
	 * if the amount is +ve
	 */
	private String getBankAccValue(double value, int col) {
		double selectedvalue = value;
		switch (col) {
		case 6:
			return (DecimalUtil.isLessThan(selectedvalue, 0.0)) ? DataUtils
					.getAmountAsString(selectedvalue) : DataUtils
					.getAmountAsString(0.00);
		case 7:
			return (DecimalUtil.isGreaterThan(selectedvalue, 0.0)) ? DataUtils
					.getAmountAsString(selectedvalue) : DataUtils
					.getAmountAsString(0.00);
		}
		return "";
	}

	/*
	 * For accountType "CreditCard" the "Payment" column contains the amount
	 * value(and the "Deposit" column value is "0") if the amount is +ve. And
	 * "Deposit" column contains amount value( and "Payment" column value is 0)
	 * if the amount is -ve
	 */
	private String getCreditAccValue(double value, int col) {
		double selectedvalue = value;
		switch (col) {
		case 6:
			return (DecimalUtil.isGreaterThan(selectedvalue, 0.0)) ? DataUtils
					.getAmountAsString(selectedvalue) : DataUtils
					.getAmountAsString(0.00);
		case 7:
			return (DecimalUtil.isLessThan(selectedvalue, 0.0)) ? DataUtils
					.getAmountAsString(selectedvalue) : DataUtils
					.getAmountAsString(0.00);
		}
		return "";
	}

	/*
	 * The balance values for each row is obtained by using the formula...
	 * balance = balance - payment + deposit where balance: previous record's
	 * balance
	 */
	double getBalanceValue(AccountRegister accountRegister) {
		/* Here 'd' value might be "payment" or "Deposit" */
		double d = accountRegister.getAmount();

		if (accountType == ClientAccount.TYPE_BANK) {
			if (DecimalUtil.isLessThan(d, 0.0)) {
				d = -1 * d;
				balance = balance - d;
			} else {
				balance = balance + d;
			}
		} else {
			if (DecimalUtil.isLessThan(d, 0.0)) {
				d = -1 * d;
				balance = balance + d;
			} else {
				balance = balance - d;
			}

		}
		totalBalance += balance;
		return balance;
	}

	@Override
	protected int getCellWidth(int index) {

		if (index == 0)
			return 75;
		return super.getCellWidth(index);
		// return -1;
	}

	protected void onClick(AccountRegister obj, int row, int col) {
		// if (col == 9 && !obj.isVoided()) {
		// showWarningDialog(obj);
		// }

	}

	@SuppressWarnings("unused")
	private void showWarningDialog(final AccountRegister obj) {
		Accounter.showWarning("Do you want to Void the Transaction",
				AccounterType.WARNING, new ErrorDialogHandler() {

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
						voidTransaction(obj);
						return true;
					}

				});

	}

	protected void voidTransaction(final AccountRegister obj) {
		// AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
		//
		// @Override
		// public void onFailure(Throwable caught) {
		//
		// }
		//
		// @Override
		// public void onSuccess(Boolean result) {
		// if (result) {
		// obj.setVoided(true);
		// updateData(obj);
		//
		// }
		//
		// }
		// };
		//
		// AccounterCoreType coretype = UIUtils
		// .getAccounterCoreType(obj.getType());
		// if (coretype != null) {
		// rpcDoSerivce.voidTransaction(coretype, obj.getTransactionId(),
		// callback);
		// }

		ViewManager.getInstance().voidTransaction(
				UIUtils.getAccounterCoreType(obj.getType()),
				obj.getTransactionId(), this);
	}

	@Override
	public void onDoubleClick(AccountRegister obj) {
		// NOTHING TO DO.
	}

	@Override
	public boolean validateGrid() {
		// NOTHING TO DO.
		return true;
	}

	@Override
	protected void executeDelete(AccountRegister object) {
		// NOTHING TO DO.
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	public AccounterCoreType getType() {
		// TODO
		return null;
	}

	public boolean isVoided(AccountRegister obj) {
		return obj.isVoided();
	}

	public AccounterCoreType getAccounterCoreType(AccountRegister obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}
}
