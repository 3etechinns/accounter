package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.TextBoxBase;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;

public class TransactionDiscountColumn extends
		AmountColumn<ClientTransactionItem> {

	public TransactionDiscountColumn(ICurrencyProvider currencyProvider) {
		super(currencyProvider,false);
	}

	@Override
	protected double getAmount(ClientTransactionItem row) {
		return row.getDiscount();
	}

	@Override
	protected String getValue(ClientTransactionItem row) {
		double amount = getAmount(row);
		return DataUtils.getAmountAsString(amount);
	}

	@Override
	public void setValue(ClientTransactionItem row, String value) {
		try {
			double amount = DataUtils.getAmountStringAsDouble(value);
			setAmount(row, amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setAmount(ClientTransactionItem row, double value) {
		row.setDiscount(value);
		// TODO doubt, currencyConversion.
		double lt = row.getQuantity().getValue() * row.getUnitPrice();
		double disc = row.getDiscount();
		row
				.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
						* disc / 100)) : lt);
		getTable().update(row);
	}

	@Override
	public int getWidth() {
		return 45;
	}

	@Override
	protected void configure(TextBoxBase textBox) {
		super.configure(textBox);
		textBox.addStyleName("discount");
	}

	@Override
	protected String getColumnName() {
		return Accounter.constants().discPerc();
	}
}
