package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.ShippingTermListDialog;

public class ShippingTermsCombo extends CustomCombo<ClientShippingTerms> {

	public ShippingTermsCombo(String title) {
		super(title);
		initCombo(FinanceApplication.getCompany().getShippingTerms());
	}

	public ShippingTermsCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1);
		initCombo(FinanceApplication.getCompany().getShippingTerms());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newShippingTerms();
	}

	@Override
	protected String getDisplayName(ClientShippingTerms object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onAddNew() {
		ShippingTermListDialog shippingTermDialog = new ShippingTermListDialog(
				"", "");

		shippingTermDialog.addCallBack(createAddNewCallBack());
		shippingTermDialog.hide();
		shippingTermDialog.showAddEditTermDialog(null);
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.SHIPPING_TERMS;
	}

	@Override
	protected String getColumnData(ClientShippingTerms object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
