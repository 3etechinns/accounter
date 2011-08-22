package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerAction;

public class CustomerCombo extends CustomCombo<ClientCustomer> {

	public CustomerCombo(String title) {
		super(title);
		initCombo(getCompany().getActiveCustomers());

	}

	public CustomerCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1);
		initCombo(getCompany().getActiveCustomers());

	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newCustomer();
	}

	@Override
	protected String getDisplayName(ClientCustomer object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		NewCustomerAction action = ActionFactory.getNewCustomerAction();
		action.setCallback(new ActionCallback<ClientCustomer>() {

			@Override
			public void actionResult(ClientCustomer result) {
				if (result.getDisplayName() != null)
					addItemThenfireEvent(result);
			}
		});

		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientCustomer object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
}
