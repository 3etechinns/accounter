package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author kumar kasimala
 */

public class ReceivePaymentAction extends Action {

	protected ReceivePaymentView view;

	public ReceivePaymentAction(String text) {
		super(Accounter.constants().newReceivePayment());
		this.catagory = Accounter.constants().customer();
	}

	public ReceivePaymentAction(String text,
			ClientReceivePayment receivePayment,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new ReceivePaymentView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ReceivePaymentAction.this);

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
	// return this.view;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newReceivePayment();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/recive_payments.png";
	// }

	@Override
	public String getHistoryToken() {
		return "receivePayment";
	}
}
