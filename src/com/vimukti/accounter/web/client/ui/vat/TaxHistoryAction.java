package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class TaxHistoryAction extends Action {

	public TaxHistoryAction() {
		super();
		catagory = messages.tax();
	}

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				MainFinanceWindow.getViewManager().showView(
						new TaxHistoryView(), null, false,
						TaxHistoryAction.this);

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Vendor list", t);
			}
		});

	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "taxHistory";
	}

	@Override
	public String getHelpToken() {
		return "taxHistory";
	}

	@Override
	public String getText() {
		return messages.taxHistory();
	}

}
