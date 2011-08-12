package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Mandeep Singh
 */

public class CustomerTransactionHistoryAction extends Action {

	protected CustomerTransactionHistoryReport report;

	public CustomerTransactionHistoryAction(String text) {
		super(text);
		this.catagory = Accounter.constants().report();
	}

	public void runAsync(final Object data, final Boolean dependent) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				report = new CustomerTransactionHistoryReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						dependent, CustomerTransactionHistoryAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.report;
	// }

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "customerTransactionHistory";
	}

}
