package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.vat.PayTAXView;

public class PaySalesTaxAction extends Action {

	public PaySalesTaxAction() {
		super();
		this.catagory = messages.company();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				// if (Accounter.getCompany().getAccountingType() == 1) {
				// MainFinanceWindow.getViewManager().showView(
				// new VATPaymentView(), data, false,
				// PaySalesTaxAction.this);
				// } else {
				// UIUtils.setCanvas(new PaySalesTaxView(),
				// getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(new PayTAXView(),
						data, false, PaySalesTaxAction.this);

				// }

			}

		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().paySalesTax();
	}

	@Override
	public String getHistoryToken() {
		return "paySalesTax";
	}

	@Override
	public String getHelpToken() {
		return "pay_sales-tax";
	}

	@Override
	public String getText() {
		String constant = null;
		constant = messages.payTax();
		return constant;
	}

}
