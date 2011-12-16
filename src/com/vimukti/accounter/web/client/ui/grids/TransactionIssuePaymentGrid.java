package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionIssuePayment;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.IssuePaymentView;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;

public class TransactionIssuePaymentGrid extends
		AbstractTransactionGrid<ClientTransactionIssuePayment> {

	private double total = 0.0;
	private IssuePaymentView issuePaymentView;

	public TransactionIssuePaymentGrid() {
		super(true, true);
	}

	@Override
	protected String[] getColumns() {
		// addFooterValue("Total", 3);
		return new String[] { Accounter.messages().date(),
				Accounter.messages().number(), Accounter.messages().name(),
				Accounter.messages().memo(), Accounter.messages().amount() };
	}

	public boolean isSelected(ClientTransactionIssuePayment transactionList) {
		return ((CheckBox) getWidget(indexOf(transactionList), 0)).getValue();
	}

	@Override
	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		if (this.getRecords().isEmpty() || isEmptyGrid()) {
			result.addError(this, messages.blankTransaction());
		}
		return result;
	}

	public boolean isEmptyGrid() {
		for (ClientTransactionIssuePayment bill : getRecords()) {
			if (isSelected(bill)) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected Object getColumnValue(ClientTransactionIssuePayment issuepayment,
			int index) {
		switch (index) {
		case 0:
			return issuepayment.getDate() != 0 ? UIUtils
					.getDateByCompanyType(new ClientFinanceDate(issuepayment
							.getDate())) : null;
		case 1:
			return issuepayment.getNumber();
		case 2:
			return issuepayment.getName();
		case 3:
			return issuepayment.getMemo();
		case 4:
			return DataUtils
					.amountAsStringWithCurrency(issuepayment.getAmount(),
							getCompany().getPrimaryCurrency());
		default:
			return null;
		}
	}

	public void setIssuePaymentView(IssuePaymentView issuePaymentView) {
		this.issuePaymentView = issuePaymentView;
	}

	@Override
	public void onDoubleClick(ClientTransactionIssuePayment obj) {
		// not required for this class

	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionIssuePayment obj,
			int colIndex) {
		// not required for this class
		return null;
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_DATE;
		default:
			return ListGrid.COLUMN_TYPE_TEXT;
		}

	}

	@Override
	protected boolean isEditable(ClientTransactionIssuePayment obj, int row,
			int index) {
		return false;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 85;
		case 1:
			return 100;
		case 2:
			return 120;
		case 3:
			return 220;
		case 4:
			return 120;
		default:
			return -1;
		}

	}

	@Override
	protected void onSelectionChanged(ClientTransactionIssuePayment obj,
			int row, boolean isChecked) {
		if (isChecked) {
			total += obj.getAmount();
			issuePaymentView.totalAmount = total;
			// updateFooterValues(amountAsString(total), 4);
		} else {
			total -= obj.getAmount();
			issuePaymentView.totalAmount = total;
			// updateFooterValues(amountAsString(total), 4);
		}
		super.onSelectionChanged(obj, row, isChecked);
	}

	@Override
	public void onHeaderCheckBoxClick(boolean isChecked) {
		total = 0;
		if (isChecked) {
			for (ClientTransactionIssuePayment rec : getRecords())
				total += rec.getAmount();
			issuePaymentView.totalAmount = total;
		} else {
			total = 0;
			issuePaymentView.totalAmount = total;
		}
		// updateFooterValues(amountAsString(total), 4);
		super.onHeaderCheckBoxClick(isChecked);
	}

	@Override
	public List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object) {
		// not required
		return null;
	}

	@Override
	public void setTaxCode(long taxCode) {
		// not required for this class

	}

}
