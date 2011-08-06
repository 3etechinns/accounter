package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.PaySalesTaxView;
import com.vimukti.accounter.web.client.ui.VATPaymentView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class PaySalesTaxAction extends Action {

	public PaySalesTaxAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to load PaySalesTax..", t);
				return;
			}

			public void onCreated() {

				try {
					if (Accounter.getCompany().getAccountingType() == 1) {
						MainFinanceWindow.getViewManager().showView(
								new VATPaymentView(), data, false,
								PaySalesTaxAction.this);
					} else {
						// UIUtils.setCanvas(new PaySalesTaxView(),
						// getViewConfiguration());
						MainFinanceWindow.getViewManager().showView(
								new PaySalesTaxView(), data, false,
								PaySalesTaxAction.this);
					}
				} catch (Throwable t) {
					onCreateFailed(t);
				}

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

}
