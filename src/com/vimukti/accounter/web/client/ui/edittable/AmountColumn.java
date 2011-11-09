package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;

public abstract class AmountColumn<T> extends TextEditColumn<T> {

	ICurrencyProvider currencyProvider;

	public AmountColumn(ICurrencyProvider currencyProvider) {
		super();
		this.currencyProvider = currencyProvider;
	}

	@Override
	protected String getValue(T row) {
		double amount = currencyProvider
				.getAmountInTransactionCurrency(getAmount(row));
		return DataUtils.getAmountAsString(amount);
	}

	protected abstract double getAmount(T row);

	@Override
	public void setValue(T row, String value) {
		try {
			double amount = DataUtils.getAmountStringAsDouble(value);
			double baseCurrencyAmount = currencyProvider
					.getAmountInBaseCurrency(amount);
			setAmount(row, baseCurrencyAmount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void setAmount(T row, double value);

	@Override
	protected void configure(TextBoxBase textBox) {
		super.configure(textBox);
		textBox.addStyleName("amount");
	}

	@Override
	public void setTable(EditTable<T> table) {
		super.setTable(table);
		FlexTable flexTable = (FlexTable) table.getWidget();
		flexTable.getCellFormatter().addStyleName(0, flexTable.getCellCount(0),
				"amount");
	}

	protected String getColumnNameWithCurrency(String name) {
		String currencyName = Accounter.getCompany().getPreferences()
				.getPrimaryCurrency().getFormalName();
		if (currencyProvider.getTransactionCurrency() != null) {
			currencyName = currencyProvider.getTransactionCurrency()
					.getFormalName();
		}
		return Accounter.messages().nameWithCurrency(name, currencyName);
	}

}
