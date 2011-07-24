package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransactionPayVAT;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.vat.PayVATView;

public class TransactionPayVATGrid extends
		AbstractTransactionGrid<ClientTransactionPayVAT> {

	private int[] columns = { ListGrid.COLUMN_TYPE_TEXT,
			ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXTBOX };
	private PayVATView payVATView;

	public TransactionPayVATGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	public TransactionPayVATGrid(boolean isMultiSelectionEnable,
			boolean showFooter) {
		super(isMultiSelectionEnable, showFooter);
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionPayVAT obj,
			int colIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClientTransactionItem> getallTransactions(
			ClientTransaction object) throws InvalidEntryException {
		return null;
	}

	@Override
	protected int getColumnType(int index) {
		return columns[index];
	}

	@Override
	protected boolean isEditable(ClientTransactionPayVAT obj, int row, int index) {
		if (index == 2)
			return true;
		return false;
	}

	@Override
	public void editComplete(ClientTransactionPayVAT editingRecord,
			Object value, int col) {

		try {

			double payment = Double.parseDouble(DataUtils
					.getReformatedAmount(value.toString())
					+ "");
			editingRecord.setAmountToPay(payment);
			updateData(editingRecord);

			double toBeSetAmount = 0.0;
			for (ClientTransactionPayVAT rec : this.getSelectedRecords()) {
				toBeSetAmount += rec.getAmountToPay();
			}

			payVATView.adjustAmountAndEndingBalance(toBeSetAmount);
			// updateFooterValues(DataUtils.getAmountAsString(toBeSetAmount),
			// 2);

		} catch (Exception e) {
			Accounter.showError(AccounterErrorType.INVALIDAMOUNT);
		}
		super.editComplete(editingRecord, value, col);
	}

	@Override
	protected int getCellWidth(int index) {
		return -1;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { companyConstants.vatAgency(),
				companyConstants.taxDue(), companyConstants.amountToPay() };
	}

	@Override
	protected Object getColumnValue(ClientTransactionPayVAT payVAT, int index) {
		switch (index) {
		case 0:
			ClientTAXAgency taxAgency = getCompany()
					.getTaxAgency(payVAT.getTaxAgency());
			return taxAgency != null ? taxAgency.getName() : "";
		case 1:
			return DataUtils.getAmountAsString(payVAT.getTaxDue());

		case 2:
			return DataUtils.getAmountAsString(payVAT.getAmountToPay());
		default:
			break;
		}
		return null;
	}

	public void setPayVATView(PayVATView payVATView) {
		this.payVATView = payVATView;
	}

	@Override
	public void onHeaderCheckBoxClick(boolean isChecked) {
		if (isChecked) {
			selectAllRows();
		} else {
			resetValues();
		}
		super.onHeaderCheckBoxClick(isChecked);
	}

	/*
	 * This method inkvoked when headercheckbox value is true.And calls the
	 * methods to update the non editable fileds and record's payment value.
	 */
	public void selectAllRows() {

		double totalAmount = 0.0;
		for (ClientTransactionPayVAT obj : this.getRecords()) {
			if (!isSelected(obj)) {
				((CheckBox) this.body.getWidget(indexOf(obj), 0))
						.setValue(true);

				// selectedValues.add(indexOf(obj));
				// updateValue(obj);
			}
			totalAmount += obj.getAmountToPay();
		}
		payVATView.adjustAmountAndEndingBalance(totalAmount);
		// updateFooterValues(DataUtils.getAmountAsString(totalAmount), 2);
	}

	public boolean isSelected(ClientTransactionPayVAT obj) {
		return ((CheckBox) getWidget(indexOf(obj), 0)).getValue();

	}

	/*
	 * This method invoked when header checkbox value is false(unchecked)
	 */
	public void resetValues() {
		// for (ClientTransactionPayVAT obj : this.getRecords()) {
		// obj.setPayment(0.0);
		// obj.setCashDiscount(0);
		// obj.setAppliedCredits(0);
		// selectedValues.remove((Integer) indexOf(obj));
		// updateData(obj);
		// }
		// updateFooterValues("0.0", 2);
		payVATView.adjustAmountAndEndingBalance(0.0);
	}

	@Override
	protected void onSelectionChanged(ClientTransactionPayVAT obj, int row,
			boolean isChecked) {
		super.onSelectionChanged(obj, row, isChecked);

		double toBeSetAmount = 0.0;
		for (ClientTransactionPayVAT rec : this.getSelectedRecords()) {
			toBeSetAmount += rec.getAmountToPay();
		}

		payVATView.adjustAmountAndEndingBalance(toBeSetAmount);
		// updateFooterValues(DataUtils.getAmountAsString(toBeSetAmount), 2);
	}

	@Override
	public Double getTotal() {
		// TODO Auto-generated method stub
		return super.getTotal();
	}

	@Override
	public void setTaxCode(String taxCode) {
		// TODO Auto-generated method stub

	}
}
