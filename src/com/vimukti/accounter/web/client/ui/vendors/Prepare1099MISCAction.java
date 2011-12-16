package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class Prepare1099MISCAction extends Action {

	Prepare1099MISCView view;

	public Prepare1099MISCAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new Prepare1099MISCView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, Prepare1099MISCAction.this);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelpToken() {
		return "prepare-1099-MISC";
	}

	@Override
	public String getText() {
		return messages.prepare1099MiscForms();
	}

}
