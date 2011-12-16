package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationDetails;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class SalesByLocationDetailsAction extends Action {

	private boolean isLocation;

	public SalesByLocationDetailsAction(boolean isLocation) {
		super();
		this.isLocation = isLocation;
	}

	@Override
	public void run() {
		final boolean isLoction = this.isLocation;
		runAsync(data, isDependent, isLoction);

	}

	private void runAsync(final Object data, final boolean isDependent,
			final boolean isLoction) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				AbstractReportView<SalesByLocationDetails> report = new SalesByLocationDetailsReport(
						isLoction);
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, SalesByLocationDetailsAction.this);

			}

			public void onCreateFailed(Throwable t) {
				// UIUtils.logError("Failed to Load Report...", t);
			}
		});

	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		if (!isLocation) {
			return "salesByLocationDetails";
		}
		return "salesByClassDetails";
	}

	@Override
	public String getHelpToken() {
		if (!isLocation) {
			return "sales-by-class";
		}
		return "sales-by-location";
	}

	@Override
	public String getText() {
		String actionsting = messages.getSalesByLocationDetails(Global.get()
				.Location());
		if (!isLocation) {
			actionsting = messages.salesByClassDetails();
		}
		return actionsting;
	}

}
