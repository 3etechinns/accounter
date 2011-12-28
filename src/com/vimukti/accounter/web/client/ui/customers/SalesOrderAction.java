package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class SalesOrderAction extends Action {

	private SalesOrderView view;

	public SalesOrderAction() {
		super();
		this.catagory = messages.sales();
	}

	public SalesOrderAction(ClientSalesOrder salesOrder,
			AccounterAsyncCallback<Object> callback) {
		super();
		this.catagory = Global.get().Customer();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new SalesOrderView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, SalesOrderAction.this);

			}

		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().salesOrder();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Sales-order.png";
	// }

	@Override
	public String getHistoryToken() {
		return "salesOrder";
	}

	@Override
	public String getHelpToken() {
		return "new-sales";
	}

	@Override
	public String getText() {
		return messages.newSalesOrder();
	}
}
