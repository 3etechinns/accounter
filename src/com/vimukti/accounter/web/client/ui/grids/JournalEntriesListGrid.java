package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;

public class JournalEntriesListGrid extends BaseListGrid<ClientJournalEntry> {

	public JournalEntriesListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	boolean isDeleted;

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected Object getColumnValue(ClientJournalEntry obj, int col) {
		switch (col) {
		case 0:
			return obj.getNumber();
		case 1:
			return obj.getDate() != null ? UIUtils.getDateByCompanyType(obj
					.getDate()) : "";
			// FIXME--need to implement
			// case 2:
			// return FinanceApplication.getUser().getFullName();
		case 2:
			return obj.getMemo() != null ? obj.getMemo() : "";
		case 3:
			return amountAsString(obj.getTotal(),
					getCompany().getCurrency(obj.getCurrency()));
		case 4:
			if (!obj.isVoid())
				return Accounter.getFinanceImages().notvoid();
			else
				return Accounter.getFinanceImages().voided();
			// return "/images/voided.png";
			// case 5:
			// if (obj.getStatus() == ClientTransaction.STATUS_DELETED)
			// return FinanceApplication.getFinanceImages().delSuccess()
			// .getURL();
			// else
			// return FinanceApplication.getFinanceImages().delete().getURL();
		}

		return null;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 150;
		case 1:
			return 150;
		case 3:
			return 150;
		case 4:
			return 100;
		default:
			return -1;
		}

	}

	/*
	 * @Override protected int getCellWidth(int index) { switch (index) { case
	 * 1: return 80; case 0: return 100; case 3: return 200; case 4: return 100;
	 * case 5: return 100; case 6: return 15; default: return
	 * super.getCellWidth(index); } };
	 */

	@Override
	protected String[] getColumns() {
		companyConstants = Accounter.constants();
		return new String[] { companyConstants.no(), companyConstants.date(),
				companyConstants.memo(), companyConstants.amount(),
				companyConstants.Voided()

		};
	}

	@Override
	public void onDoubleClick(ClientJournalEntry obj) {
		ActionFactory.getNewJournalEntryAction().run(obj, false);
	}

	@Override
	protected void onClick(ClientJournalEntry obj, int row, int col) {

		if (col == 4 && !obj.isVoid()) {
			if (obj.getReference() == null) {
				showWarningDialog(obj, col);
			} else if (obj.getReference().equals(
					AccounterClientConstants.JOURNAL_ENTRY_FOR_DEPRECIATION)) {
				Accounter.showWarning(Accounter.constants()
						.youcantvoidJournalEntrycreatedbyrunningDeprecation(),
						AccounterType.ERROR);
			}

		}
		if (col == 5) {
			if (!isDeleted)
				showWarningDialog(obj, col);
			else
				return;
		}

	}

	private void showWarningDialog(final ClientJournalEntry obj, final int col) {
		String msg = null;
		if (col == 4 && !obj.isVoid()) {
			msg = Accounter.constants().doyouwanttoVoidtheTransaction();
		} else if (col == 5) {
			msg = Accounter.constants().doyouwanttoDeletetheTransaction();

		}
		Accounter.showWarning(msg, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {
						// NOTHING TO DO.
						return false;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onYesClick() {
						if (col == 4)
							voidTransaction(obj);
						else if (col == 5)
							deleteTransaction(obj);
						return true;
					}

				});
	}

	// protected void voidTransaction(final ClientJournalEntry obj) {
	// AccounterAsyncCallback<Boolean> callback = new
	// AccounterAsyncCallback<Boolean>() {
	//
	// @Override
	// public void onException(AccounterException caught) {
	//
	// }
	//
	// @Override
	// public void onSuccess(Boolean result) {
	//
	// if (viewType != null && !viewType.equalsIgnoreCase("All")) {
	// deleteRecord(obj);
	//
	// }
	// obj.setVoid(true);
	// updateData(obj);
	//
	// }
	// };
	// AccounterCoreType type = UIUtils.getAccounterCoreType(obj.getType());
	// rpcDoSerivce.voidTransaction(type, obj.getID(), callback);
	// }
	protected void voidTransaction(final ClientJournalEntry obj) {
		voidTransaction(UIUtils.getAccounterCoreType(obj.getType()),
				obj.getID());
	}

	protected void deleteTransaction(final ClientJournalEntry obj) {
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {

			}

			@Override
			public void onResultSuccess(Boolean result) {

				if (viewType != null && !viewType.equalsIgnoreCase("All"))
					deleteRecord(obj);
				obj.setStatus(ClientTransaction.STATUS_DELETED);
				isDeleted = true;
				obj.setVoid(true);
				updateData(obj);

			}
		};
		AccounterCoreType type = UIUtils.getAccounterCoreType(obj.getType());
		rpcDoSerivce.deleteTransaction(type, obj.getID(), callback);
	}

	@Override
	protected void executeDelete(ClientJournalEntry object) {
		// NOTHING TO DO.
	}

	@Override
	protected int sort(ClientJournalEntry obj1, ClientJournalEntry obj2,
			int index) {
		switch (index) {
		case 0:
			int num1 = UIUtils.isInteger(obj1.getNumber()) ? Integer
					.parseInt(obj1.getNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getNumber()) ? Integer
					.parseInt(obj2.getNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return obj1.getNumber().compareTo(obj2.getNumber());
		case 1:
			ClientFinanceDate date1 = obj1.getDate();
			ClientFinanceDate date2 = obj2.getDate();
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
			break;
		case 2:
			if (obj1.getMemo() != null && obj2.getMemo() != null)
				return obj1.getMemo().toLowerCase()
						.compareTo(obj2.getMemo().toLowerCase());
			break;
		case 3:
			Double amt1 = obj1.getTotal();
			Double amt2 = obj2.getTotal();
			return amt1.compareTo(amt2);

		default:
			break;
		}
		return 0;
	}

	public AccounterCoreType getType() {
		return AccounterCoreType.JOURNALENTRY;
	}

}
