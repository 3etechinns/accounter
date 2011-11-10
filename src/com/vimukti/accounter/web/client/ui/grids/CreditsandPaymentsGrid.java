package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.customers.CustomerCreditsAndPaymentsDialiog;
import com.vimukti.accounter.web.client.ui.customers.NewApplyCreditsDialog;

public class CreditsandPaymentsGrid extends
		AbstractTransactionGrid<ClientCreditsAndPayments> {

	private CustomerCreditsAndPaymentsDialiog dialog;
	private boolean isCanEdit;
	private AccounterConstants customerConstants = Accounter.constants();
	List<Integer> selectedValues = new ArrayList<Integer>();
	private List<ClientCreditsAndPayments> actualRecords, copyRecords;
	private int columns[] = { ListGrid.COLUMN_TYPE_TEXT,
			ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
			ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX,
			ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX };

	ClientCurrency currency = getCompany().getPreferences()
			.getPrimaryCurrency();

	private ClientTransactionReceivePayment traReceivePayment;
	public LinkedHashMap<ClientTransactionReceivePayment, List<ClientCreditsAndPayments>> creditsMap = new LinkedHashMap<ClientTransactionReceivePayment, List<ClientCreditsAndPayments>>();
	private NewApplyCreditsDialog newdialog;

	public CreditsandPaymentsGrid(boolean isMultiSelectionEnable,
			CustomerCreditsAndPaymentsDialiog creditdialog,
			ClientTransactionReceivePayment trareceivePayment) {
		super(isMultiSelectionEnable);
		this.dialog = creditdialog;
		this.traReceivePayment = trareceivePayment;
	}

	public CreditsandPaymentsGrid(boolean isMultiSelectionEnable,
			NewApplyCreditsDialog creditdialog,
			ClientTransactionReceivePayment trareceivePayment) {
		super(isMultiSelectionEnable);
		this.newdialog = creditdialog;
		this.traReceivePayment = trareceivePayment;
	}

	@Override
	public void setCanEdit(boolean enabled) {
		this.isCanEdit = enabled;
		super.setCanEdit(enabled);
	}

	private void resetValidValue(ClientCreditsAndPayments creditsAndPayments) {
		double balance = creditsAndPayments.getBalance();
		double amountToUse = creditsAndPayments.getAmtTouse();
		if (DecimalUtil.isGreaterThan(balance, 0)
				&& DecimalUtil.isGreaterThan(amountToUse, 0)) {
			ClientCreditsAndPayments editRecord = creditsAndPayments;
			// ClientCreditsAndPayments originalRecord = actualRecords
			// .get(indexOf(creditsAndPayments));
			// editRecord.setAmtTouse(originalRecord.getRemaoningBalance());
			resetValue(editRecord);
		} else
			resetValue(creditsAndPayments);
	}

	@Override
	protected void onSelectionChanged(ClientCreditsAndPayments core, int row,
			boolean isChecked) {
		if (isChecked)
			updateValue(core);
		else if (!isChecked)
			resetValidValue(core);
		super.onSelectionChanged(core, row, isChecked);
	}

	public void updateValue(ClientCreditsAndPayments creditsAndpayments) {
		double bal = creditsAndpayments.getBalance();
		creditsAndpayments.setBalance(0.0d);
		creditsAndpayments.setAmtTouse(bal);
		creditsAndpayments.setRemaoningBalance(0.0);
		updateData(creditsAndpayments);
		updateAmountValues();
	}

	/*
	 * This method invoked when the record is unselected.It resets the
	 * non-editable fields as-well-as records values
	 */
	public void resetValue(ClientCreditsAndPayments creditPayments) {
		// creditPayments
		// .setBalance(creditPayments.isRecordChanged() ? creditPayments
		// .getRemaoningBalance() + creditPayments.getAmtTouse()
		// : creditPayments.getAmtTouse());
		creditPayments.setBalance(creditPayments.getBalance()
				+ creditPayments.getAmtTouse());
		creditPayments.setRemaoningBalance(creditPayments.getBalance());
		creditPayments.setAmtTouse(0.0D);
		updateData(creditPayments);
		updateAmountValues();
	}

	public void updateAmountValues() {
		if (dialog != null) {
			dialog.totalBalances = 0.0D;
			dialog.totalAmountToUse = 0.0D;
			for (ClientCreditsAndPayments creditsAndPayments : getRecords()) {
				dialog.totalBalances += creditsAndPayments.getBalance();
				dialog.totalAmountToUse += creditsAndPayments.getAmtTouse();
			}
			dialog.updateFields();
		} else {
			newdialog.totalBalances = 0.0D;
			newdialog.totalAmountToUse = 0.0D;
			for (ClientCreditsAndPayments creditsAndPayments : getRecords()) {
				newdialog.totalBalances += creditsAndPayments.getBalance();
				newdialog.totalAmountToUse += creditsAndPayments.getAmtTouse();
			}
			newdialog.updateFields();
		}

	}

	@Override
	protected void onClick(ClientCreditsAndPayments obj, int row, int index) {
		// if (index != 4)
		// selectRow(row);
		super.onClick(obj, row, index);
	}

	@Override
	protected int getColumnType(int index) {
		return columns[index];
	}

	@Override
	public void selectRow(int row) {
		if (isCanEdit) {
			if (currentCol == 4) {
				startEditing(row);
			}
		}
	}

	@Override
	protected boolean isEditable(ClientCreditsAndPayments obj, int row,
			int index) {
		if (index == 4) {
			return true;
		}
		return false;
	}

	@Override
	protected String[] getColumns() {

		return new String[] { customerConstants.date(),
				customerConstants.memo(), customerConstants.creditAmount(),
				customerConstants.balance(), customerConstants.amountToUse() };
	}

	@Override
	public void editComplete(ClientCreditsAndPayments item, Object value,
			int col) {

		switch (col) {
		case 4:
			ClientCreditsAndPayments editingRecord = item;

			try {
				double prevAmtToUse = item.getAmtTouse();
				double prevBalance = item.getBalance();
				Double amtTouse = Double.parseDouble(DataUtils
						.getReformatedAmount(value.toString()) + "");
				// ClientCreditsAndPayments actualRecord = actualRecords
				// .get(indexOf(item));

				// Double originalBalance = actualRecord.getActualAmt();
				Double balance = item.getRemaoningBalance()
						+ (item.getAmtTouse() - amtTouse);

				if (DecimalUtil.isLessThan(amtTouse, 0)
						|| DecimalUtil.isLessThan(balance, 0)) {
					// || balance != 0 ? ((amtTouse
					// .compareTo(item.getActualAmt()) > 0) ? true : false)
					// : false) {
					Accounter.showError(Accounter.constants()
							.receivedPaymentAppliedCreditsAmount());
					setText(indexOf(item), 4,
							DataUtils.amountAsStringWithCurrency(item.getAmtTouse(), currency));
				} else {
					if (DecimalUtil.isLessThan(balance, 0)) {
						Accounter.showError(Accounter.constants()
								.receivedPaymentAppliedCreditsAmount());
						setText(indexOf(item), 4,
								DataUtils.amountAsStringWithCurrency(item.getAmtTouse(), currency));
					} else {
						double newValue = getAmountInBaseCurrency((Double) amtTouse);
						editingRecord.setAmtTouse(newValue);
						editingRecord.setBalance(balance);
						editingRecord.setRemaoningBalance(balance);
						editingRecord.setRecordChanged(true);

						updateAmountValues();
						if (!newdialog.validTotalAmountUse()) {
							Accounter.showError(Accounter.constants()
									.amountToUseMustLessthanTotal());
							editingRecord.setAmtTouse(prevAmtToUse);
							editingRecord.setBalance(prevBalance);
							editingRecord.setRemaoningBalance(prevBalance);
						}
						updateData(editingRecord);
					}

				}
			} catch (Exception e) {
				Accounter.showError(Accounter.constants().invalidAmount());
			}

			break;

		default:
			break;
		}

		super.editComplete(item, value, col);
	}

	/*
	 * This method invoked when record values need to set to its original values
	 */
	public void initialCreditsAndPayments(
			List<ClientCreditsAndPayments> actualRecs) {
		actualRecords = new ArrayList<ClientCreditsAndPayments>();
		for (ClientCreditsAndPayments rec : actualRecs) {
			ClientCreditsAndPayments r = new ClientCreditsAndPayments();
			r.setAmtTouse(rec.getAmtTouse());
			r.setBalance(rec.getBalance());
			r.setRemaoningBalance(rec.getBalance());
			r.setCreditAmount(rec.getCreditAmount());
			r.setMemo(rec.getMemo() != null ? rec.getMemo() : "");
			r.setPayee(rec.getPayee());
			r.setTransactionCreditsAndPayments(rec
					.getTransactionCreditsAndPayments());
			r.setID(rec.getID());
			r.setTransaction(rec.getTransaction());
			r.setActualAmt(rec.getActualAmt());
			this.actualRecords.add(r);
		}
	}

	public void createCopyCreditandPayments(
			List<ClientCreditsAndPayments> actualRecs) {
		copyRecords = new ArrayList<ClientCreditsAndPayments>();
		for (ClientCreditsAndPayments rec : actualRecs) {
			ClientCreditsAndPayments r = new ClientCreditsAndPayments();
			r.setAmtTouse(rec.getAmtTouse());
			r.setBalance(rec.getBalance());
			r.setCreditAmount(rec.getCreditAmount());
			r.setMemo(rec.getMemo() != null ? rec.getMemo() : "");
			r.setPayee(rec.getPayee());
			r.setTransactionCreditsAndPayments(rec
					.getTransactionCreditsAndPayments());
			r.setID(rec.getID());
			r.setTransaction(rec.getTransaction());
			r.setActualAmt(rec.getActualAmt());
			r.setRecordChanged(rec.isRecordChanged());
			this.copyRecords.add(r);
		}

	}

	public List<ClientCreditsAndPayments> getCopyCreditAndPayments() {
		return this.copyRecords;
	}

	public List<ClientCreditsAndPayments> getUpdatedRecords() {
		List<ClientCreditsAndPayments> updatedPayments = new ArrayList<ClientCreditsAndPayments>();
		for (ClientCreditsAndPayments record : this.getRecords()) {
			// if (isSelected(record))
			// record.setRecordChanged(true);
			// else
			// record.setRecordChanged(false);

			updatedPayments.add(record);
		}
		return updatedPayments;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 75;
		case 2:
		case 3:
		case 4:
			return 100;
		default:
			return -1;
		}
	}

	@Override
	protected Object getColumnValue(
			ClientCreditsAndPayments creditsAndPayments, int index) {

		switch (index) {
		case 0:
			return UIUtils.dateFormat(creditsAndPayments.getTransaction()
					.getDate());
		case 1:
			return creditsAndPayments.getMemo();
		case 2:
			return DataUtils.amountAsStringWithCurrency(
					getAmountInForeignCurrency(creditsAndPayments
							.getCreditAmount()),
					currency);
		case 3:
			return DataUtils.amountAsStringWithCurrency(
					getAmountInForeignCurrency(creditsAndPayments.getBalance()),
					currency);
		case 4:
			return DataUtils.amountAsStringWithCurrency(
					getAmountInForeignCurrency(creditsAndPayments.getAmtTouse()),
					currency);
		default:
			break;
		}
		return null;
	}

	@Override
	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		if (this.getRecords().size() > 0
				&& this.getSelectedRecords().size() == 0) {
			result.addError(this, Accounter.constants().selectTransaction());
		}
		return result;
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientCreditsAndPayments obj,
			int colIndex) {
		// its not using any where
		return null;
	}

	@Override
	public List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object) {
		return null;
	}

	public List<ClientCreditsAndPayments> getActualRecords() {
		return this.actualRecords;
	}

	public void setCheckboxValue(ClientCreditsAndPayments record) {
		((CheckBox) getWidget(indexOf(record), 0)).setValue(record
				.isRecordChanged());
	}

	public void setCreditsAndPaymentsMap(
			ClientTransactionReceivePayment record,
			List<ClientCreditsAndPayments> actualRecords2) {
		creditsMap.put(record, actualRecords2);
	}

	@Override
	public void setTaxCode(long taxCode) {
		// TODO Auto-generated method stub
	}

}
