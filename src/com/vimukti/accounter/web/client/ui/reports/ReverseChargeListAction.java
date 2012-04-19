package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ReverseChargeListAction extends Action {


	public ReverseChargeListAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return this.report;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				ReverseChargeListReport report = new ReverseChargeListReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, ReverseChargeListAction.this);

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
		// AccounterAsync.createAsync(new CreateViewAsyncCallback() {
		//
		// @Override
		// public void onCreated() {
		//
		// }
		//
		// });
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {
		return null;
	}

	@Override
	public String getHelpToken() {
		return "reverse-charge-list";
	}

	@Override
	public String getText() {
		return messages.reverseChargeList();
	}

}
