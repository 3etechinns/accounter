package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterConstants;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.CompanyMessages;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.core.Accounter.AccounterType;

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
			return DataUtils.getAmountAsString(obj.getTotal());
		case 4:
			if (!obj.isVoid())
				return FinanceApplication.getFinanceImages().notvoid();
			else
				return FinanceApplication.getFinanceImages().voided();
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
		// TODO Auto-generated method stub
		switch (index) {
		case 0:
			return 150;
		case 1:
			return 200;
			// return 100;
			// case 1:
			// return 130;
			// case 3:
			// return 170;
		case 4:
			return 75;
			// return 70;
			// case 5:
			// return 60;
			// case 6:
			// return 30;
		default:
			return super.getCellWidth(index);
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
		companyConstants = GWT.create(CompanyMessages.class);
		return new String[] { companyConstants.voucherNo(),
				companyConstants.dateCreated(), companyConstants.memo(),
				companyConstants.amount(), companyConstants.isVoided()

		};
	}

	@Override
	public void onDoubleClick(ClientJournalEntry obj) {
		HistoryTokenUtils.setPresentToken(CompanyActionFactory
				.getNewJournalEntryAction(), obj);
		CompanyActionFactory.getNewJournalEntryAction().run(obj, true);
	}

	@Override
	protected void onClick(ClientJournalEntry obj, int row, int col) {

		if (col == 4 && !obj.isVoid()) {
			if (obj.getReference() == null) {
				showWarningDialog(obj, col);
			} else if (obj.getReference().equals(
					AccounterConstants.JOURNAL_ENTRY_FOR_DEPRECIATION)) {
				Accounter
						.showWarning(
								"You cannot void this journal entry. This entry is created by running depreciation. "
										+ "Roll back depreciation to void this entry",
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
			msg = FinanceApplication.getCustomersMessages()
					.doyouwanttoVoidtheTransaction();
		} else if (col == 5) {
			msg = FinanceApplication.getCustomersMessages()
					.doyouwanttoDeletetheTransaction();

		}
		Accounter.showWarning(msg, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() throws InvalidEntryException {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean onNoClick() throws InvalidEntryException {
						return true;
					}

					@Override
					public boolean onYesClick() throws InvalidEntryException {
						if (col == 4)
							voidTransaction(obj);
						else if (col == 5)
							deleteTransaction(obj);
						return true;
					}

				});
	}

	// protected void voidTransaction(final ClientJournalEntry obj) {
	// AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
	//
	// @Override
	// public void onFailure(Throwable caught) {
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
	// rpcDoSerivce.voidTransaction(type, obj.getStringID(), callback);
	// }
	protected void voidTransaction(final ClientJournalEntry obj) {
		ViewManager.getInstance().voidTransaction(
				UIUtils.getAccounterCoreType(obj.getType()), obj.getStringID(),
				this);
	}

	protected void deleteTransaction(final ClientJournalEntry obj) {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Boolean result) {

				if (viewType != null && !viewType.equalsIgnoreCase("All"))
					deleteRecord(obj);
				obj.setStatus(ClientTransaction.STATUS_DELETED);
				isDeleted = true;
				obj.setVoid(true);
				updateData(obj);

			}
		};
		AccounterCoreType type = UIUtils.getAccounterCoreType(obj.getType());
		rpcDoSerivce.deleteTransaction(type, obj.getStringID(), callback);
	}

	@Override
	public boolean validateGrid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void executeDelete(ClientJournalEntry object) {
		// TODO Auto-generated method stub

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
				return obj1.getMemo().toLowerCase().compareTo(
						obj2.getMemo().toLowerCase());
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

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	public AccounterCoreType getType() {
		return AccounterCoreType.JOURNALENTRY;
	}

}
