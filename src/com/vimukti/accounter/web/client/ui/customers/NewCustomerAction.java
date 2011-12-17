package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class NewCustomerAction extends Action<ClientCustomer> {

	private CustomerView view;
	private String quickAddText;
	private boolean isEditable;

	public NewCustomerAction() {
		super();
		this.catagory = Global.get().Customer();
		super.setToolTip(Global.get().Customer());
	}

	public NewCustomerAction(String quickAddText) {
		super();
		this.catagory = Global.get().Customer();
		super.setToolTip(Global.get().Customer());
		this.quickAddText = quickAddText;

	}

	public NewCustomerAction(ClientCustomer customer,
			AccounterAsyncCallback<Object> callback) {
		super();
		this.catagory = Global.get().customer();
	}

	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new CustomerView();
				if (quickAddText != null) {
					view.prepareForQuickAdd(quickAddText);
				}

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewCustomerAction.this);
				if (isCustomerViewEditable()) {
					view.onEdit();
				}

			}

		});
	}

	public boolean isCustomerViewEditable() {
		return isEditable;
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCustomer();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_customer.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newCustomer";
	}

	@Override
	public String getHelpToken() {
		return "add-customer";
	}

	public void setCustomerName(String text) {
		this.quickAddText = text;
	}

	public void setisCustomerViewEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	@Override
	public String getText() {
		return messages.newPayee(Global.get().Customer());
	}

}
