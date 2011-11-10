package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;

public class TransactionVatColumn extends TransactionAmountColumn {

	public TransactionVatColumn(ICurrencyProvider currencyProvider) {
		super(currencyProvider, false);
	}

	@Override
	protected double getAmount(ClientTransactionItem row) {
		return row.getVATfraction();
	}

	@Override
	protected void setAmount(ClientTransactionItem row, double value) {
		row.setVATfraction(value);
	}

	@Override
	public int getWidth() {
		return 80;
	}

	@Override
	protected String getColumnName() {
		return Accounter.constants().tax();
	}

	@Override
	protected boolean isEnable() {
		return false;
	}
}
