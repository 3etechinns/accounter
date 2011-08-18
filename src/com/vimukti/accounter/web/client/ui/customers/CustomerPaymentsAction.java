package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class CustomerPaymentsAction extends Action {

	public CustomerPaymentsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				NewCustomerPaymentView view = NewCustomerPaymentView
						.getInstance();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, CustomerPaymentsAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO
	// return null;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newVendorPayment();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/vendor_payments.png";
	// }

	@Override
	public String getHistoryToken() {
		return "customerPrepayment";
	}
}
