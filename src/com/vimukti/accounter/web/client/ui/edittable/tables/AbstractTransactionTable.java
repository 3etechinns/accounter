package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;

public abstract class AbstractTransactionTable extends
		EditTable<ClientTransactionItem> {

	AccounterConstants constants = Accounter.constants();

	double lineTotal;
	double taxableLineTotal;
	double totalTax;
	double grandTotal;
	double totaldiscount;

	protected boolean enableTax;
	protected boolean showTaxCode;
	protected boolean isCustomerAllowedToAdd;

	protected boolean needDiscount = true;

	private final boolean isSales;

	protected ICurrencyProvider currencyProvider;

	public AbstractTransactionTable(boolean needDiscount, boolean isSales,
			boolean isCustomerAllowedToAdd, ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
		this.needDiscount = needDiscount;
		this.isCustomerAllowedToAdd = isCustomerAllowedToAdd;
		this.isSales = isSales;
	}

	public AbstractTransactionTable(boolean needDiscount, boolean isSales,
			ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
		this.needDiscount = needDiscount;
		this.isSales = isSales;
	}

	protected abstract void addEmptyRecords();

	public void updateTotals() {

		List<ClientTransactionItem> allrecords = getAllRows();
		totaldiscount = 0;
		lineTotal = 0.0;
		taxableLineTotal = 0.0;
		totalTax = 0.0;

		for (ClientTransactionItem record : allrecords) {

			int type = record.getType();

			if (type == 0)
				continue;
			totaldiscount += record.getDiscount();

			Double lineTotalAmt = record.getLineTotal();
			lineTotal += lineTotalAmt;

			if (record != null && record.isTaxable()) {
				// ClientTAXItem taxItem = getCompany().getTAXItem(
				// citem.getTaxCode());
				// if (taxItem != null) {
				// totalVat += taxItem.getTaxRate() / 100 * lineTotalAmt;
				// }
				taxableLineTotal += lineTotalAmt;

				double taxAmount = getVATAmount(record.getTaxCode(), record);
				if (isShowPriceWithVat()) {
					lineTotal -= taxAmount;
				}
				record.setVATfraction(taxAmount);
				totalTax += record.getVATfraction();

			}

			super.update(record);
			// totalVat += citem.getVATfraction();
		}

		// if (getCompany().getPreferences().isChargeSalesTax()) {
		grandTotal = totalTax + lineTotal;
		// } else {
		// grandTotal = totallinetotal;
		// totalValue = grandTotal;
		// }
		// if (getCompany().getPreferences().isRegisteredForVAT()) {

		// grandTotal = totallinetotal;
		// totalValue = grandTotal + totalTax;
		// }
		// } else {
		// grandTotal = totallinetotal;
		// totalValue = grandTotal;
		// }

		updateNonEditableItems();
	}

	@Override
	public void delete(ClientTransactionItem row) {
		super.delete(row);
		updateTotals();
	}

	@Override
	public void setAllRows(List<ClientTransactionItem> rows) {
		createColumns();
		for (ClientTransactionItem item : rows) {
			item.setID(0);
			item.taxRateCalculationEntriesList.clear();
		}
		super.setAllRows(rows);

	}

	public void addRows(List<ClientTransactionItem> rows) {
		for (ClientTransactionItem item : getRecords()) {
			if (item.isEmpty()) {
				delete(item);
			}
		}
		createColumns();
		super.addRows(rows);
		List<ClientTransactionItem> itemList = new ArrayList<ClientTransactionItem>();
		if (getAllRows().size() < 4) {
			for (int ii = 0; ii < (4 - getAllRows().size()); ii++) {
				ClientTransactionItem item = new ClientTransactionItem();
				itemList.add(item);
			}
			createColumns();
			super.addRows(itemList);
		}
	}

	protected ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	@Override
	public void update(ClientTransactionItem row) {
		super.update(row);
		updateTotals();
	}

	protected abstract void updateNonEditableItems();

	public double getTaxableLineTotal() {
		return taxableLineTotal;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public double getLineTotal() {
		return lineTotal;
	}

	public double getTotalTax() {
		return totalTax;
	}

	// public double getTotalValue() {
	// return totalValue;
	// }
	//
	// public Double getTotal() {
	// return lineTotal != null ? lineTotal.doubleValue() : 0.0d;
	// }

	public void removeAllRecords() {
		clear();
	}

	public void setRecords(List<ClientTransactionItem> transactionItems) {
		setAllRows(transactionItems);
		updateTotals();
	}

	public abstract boolean isShowPriceWithVat();

	public List<ClientTransactionItem> getRecords() {
		return getAllRows();
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		// Validations
		// 1. checking for the name of the transaction item
		// 2. checking for the vat code if the company is of type UK
		// TODO::: check whether this validation is working or not
		for (ClientTransactionItem item : this.getRecords()) {
			if (item.isEmpty()) {
				continue;
			}
			if (item.getAccountable() == null) {
				result.addError("GridItem-" + item.getType(), Accounter
						.messages().pleaseSelect(
								Utility.getItemType(item.getType())));
			}
			if (enableTax && showTaxCode) {
				if (item.getTaxCode() == 0) {
					result.addError("GridItemUK-" + item.getAccount(),
							Accounter.messages().pleaseSelect(
									Accounter.constants().taxCode()));
				}

			}
		}
		if (DecimalUtil.isLessThan(lineTotal, 0.0)) {
			result.addError(this, Accounter.constants()
					.invalidTransactionAmount());
		}
		return result;
	}

	public double getVATAmount(long TAXCodeID, ClientTransactionItem record) {

		double vatRate = 0.0;
		try {
			if (TAXCodeID != 0) {
				// Checking the selected object is VATItem or VATGroup.
				// If it is VATItem,the we should get 'VATRate',otherwise
				// 'GroupRate
				vatRate = UIUtils.getVATItemRate(Accounter.getCompany()
						.getTAXCode(TAXCodeID), isSales);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Double vat = 0.0;
		if (isShowPriceWithVat()) {
			vat = ((ClientTransactionItem) record).getLineTotal()
					- (100 * (((ClientTransactionItem) record).getLineTotal() / (100 + vatRate)));
		} else {
			vat = ((ClientTransactionItem) record).getLineTotal() * vatRate
					/ 100;
		}
		vat = UIUtils.getRoundValue(vat);
		return vat.doubleValue();
	}

	public void setTaxCode(long taxCode, boolean force) {
		for (ClientTransactionItem item : getRecords()) {
			if ((item.getTaxCode() == 0) || force) {
				// Only set this for account and if we have not specified
				// already
				// This works only once
				item.setTaxCode(taxCode);
				// update(item);
			}
			update(item);
		}
	}

	public void resetRecords() {
		clear();
		addEmptyRecords();
	}

	public void updateAmountsFromGUI() {
		for (ClientTransactionItem item : this.getAllRows()) {
			updateFromGUI(item);
		}
		updateColumnHeaders();
	}

	public void setDisableTax(boolean isDisable) {
		if (getCompany().getPreferences().isTrackTax()) {
			enableTax = !isDisable;
		}
	}

	public void setShowTax(boolean isShow) {
		if (getCompany().getPreferences().isTrackTax()) {
			showTaxCode = isShow;
		}
	}

	protected abstract boolean isInViewMode();
}
