package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class ItemNameColumn extends
		ComboColumn<ClientTransactionItem, ClientItem> {

	ItemsDropDownTable itemsList = new ItemsDropDownTable(getItemsFilter());

	@Override
	protected ClientItem getValue(ClientTransactionItem row) {
		return (ClientItem) row.getAccountable();
	}

	public abstract ListFilter<ClientItem> getItemsFilter();

	@Override
	@SuppressWarnings( { "unchecked" })
	public AbstractDropDownTable getDisplayTable(ClientTransactionItem row) {
		return itemsList;
	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	protected void setValue(ClientTransactionItem row, ClientItem newValue) {

		if (newValue != null) {
			row.setAccountable(newValue);
			row.setDescription(getDiscription(newValue));
		//	row.setUnitPrice(newValue.getSalesPrice());
			row.setTaxable(newValue.isTaxable());
			double lt = row.getQuantity().getValue() * row.getUnitPrice();
			double disc = row.getDiscount();
			row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
					* disc / 100)) : lt);

			if (getPreferences().isTrackTax()
					&& getPreferences().isTaxPerDetailLine()) {
				row.setTaxCode(newValue.getTaxCode() != 0 ? newValue
						.getTaxCode() : row.getTaxCode());
			}
		}
	}

	protected abstract String getDiscription(ClientItem item);

	@Override
	protected String getColumnName() {
		return Accounter.constants().name();
	}

	public void setItemForCustomer(boolean isForCustomer) {
		itemsList.setForCustomer(isForCustomer);
	}
}
