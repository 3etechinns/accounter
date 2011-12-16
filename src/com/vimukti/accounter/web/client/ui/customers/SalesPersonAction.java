package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.company.SalesPersonListView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class SalesPersonAction extends Action {

	protected SalesPersonListView view;

	public SalesPersonAction() {
		super();
		this.catagory = Accounter.messages().company();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new SalesPersonListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, SalesPersonAction.this);

			}

		});
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().customers();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/customers.png";
	// }

	@Override
	public String getHistoryToken() {
		return "salesPersons";
	}

	@Override
	public String getHelpToken() {
		return "sales-person-list";
	}

	@Override
	public String getText() {
		return messages.salesPersons();
	}
}
