package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ETdsFillingAction extends Action {

	protected ETdsFillingView view;

	public ETdsFillingAction() {
		super();
		this.catagory = "TDS";
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new ETdsFillingView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ETdsFillingAction.this);

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
		return "eTDSFilling";
	}

	@Override
	public String getHelpToken() {
		return null;
	}

}
