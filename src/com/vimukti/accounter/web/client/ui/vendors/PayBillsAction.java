package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class PayBillsAction extends Action {

	protected PayBillView view;

	public PayBillsAction() {
		super();
		this.catagory = Global.get().Vendor();
	}

	@Override
	public void run() {

		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				view = PayBillView.getInstance();

				// UIUtils.setCanvas(view, getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, PayBillsAction.this);

			}

		});


	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().payBill();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/pay_bills.png";
	// }

	@Override
	public String getHistoryToken() {
		return "payBill";
	}

	@Override
	public String getHelpToken() {
		return "pay-bill";
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return messages.payBill();
	}

}
