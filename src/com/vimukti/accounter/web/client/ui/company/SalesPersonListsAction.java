package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class SalesPersonListsAction extends Action {

	public SalesPersonListsAction(String text) {
		super(text);

	}

	public SalesPersonListsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	@Override
	public ParentCanvas<?> getView() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					// UIUtils.setCanvas(new SalesPersonListView(),
					// getViewConfiguration());
					// MainFinanceWindow.getViewManager().showView(new
					// SalesPersonListView(), null,
					// false, SalesPersonListsAction.this);

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load the Sales Persons List",
				// t);

			}
		});
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public String getHistoryToken() {

		return "salesPersonList";
	}

}
