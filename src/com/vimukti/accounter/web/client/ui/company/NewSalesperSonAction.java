package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * @modified by Ravi Kiran.G
 * 
 */
public class NewSalesperSonAction extends Action<ClientSalesPerson> {

	private ClientSalesPerson salesPerson;

	private boolean isEdit;

	public NewSalesperSonAction() {
		super();
		this.catagory = Accounter.messages().company();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				NewSalesPersonView view = new NewSalesPersonView();

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewSalesperSonAction.this);

			}

		});
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// // NOTHING TO DO.
	// return null;
	// }

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
		return "newSalesPerson";
	}

	@Override
	public String getHelpToken() {
		return "new_sales-person";
	}

	@Override
	public String getText() {
		return messages.newSalesPerson();
	}

}
