package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Mandeep Singh
 */

public class TransactionDetailByTaxCodeAction extends Action {

	public TransactionDetailByTaxCodeAction(String text) {
		super(text);
		this.catagory = Accounter.constants().report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				//
				// AbstractReportView<TransactionDetailByTaxcode> report =
				// new TransactionDetailByTaxCodeReport();
				// MainFinanceWindow.getViewManager().showView(report, data,
				// isDependent, TransactionDetailByTaxCodeAction.this);
				Accounter.showInformation(Accounter.constants()
						.thisReportNotYetImplemented());

			}

		 
		});

	}

	// @Override
	// public ParentCanvas getView() {
	// // not required for this class
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {

		return "transactionDetailByTaxCode";
	}

}
