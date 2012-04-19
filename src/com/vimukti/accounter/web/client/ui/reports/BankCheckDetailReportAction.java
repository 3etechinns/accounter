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

public class BankCheckDetailReportAction extends Action {

	public BankCheckDetailReportAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				BankCheckDetailReport report = new BankCheckDetailReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, BankCheckDetailReportAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			public void onCreated() {
//				
//
//			}
//
//			public void onCreateFailed(Throwable t) {
//				System.err.println("Failed to Load Report.." + t);
//			}
//		});

	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();

	}

	@Override
	public String getHistoryToken() {
		return "CheckDetail";
	}

	@Override
	public String getHelpToken() {
		return "Check-Detail";
	}

	@Override
	public String getText() {
		return messages.checkDetail();
	}

}
