package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Mandeep Singh
 */

public class TransactionDetailByTaxCodeAction extends Action {

	public TransactionDetailByTaxCodeAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getReportsMessages().report();
	}

	public TransactionDetailByTaxCodeAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getReportsMessages().report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					//					
					// AbstractReportView<TransactionDetailByTaxcode> report =
					// new TransactionDetailByTaxCodeReport();
					// MainFinanceWindow.getViewManager().showView(report, data,
					// isDependent, TransactionDetailByTaxCodeAction.this);
					Accounter
							.showInformation("This Report Not Yet Implemented");
				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// UIUtils.logError("Failed to Load Report...", t);
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().reports();
	}

	@Override
	public String getImageUrl() {
		return "/images/reports.png";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "transactionDetailByTaxCode";
	}

}
