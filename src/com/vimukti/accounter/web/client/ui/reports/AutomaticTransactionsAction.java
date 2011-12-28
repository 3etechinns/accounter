package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class AutomaticTransactionsAction extends
		Action<TransactionDetailByAccount> {

	public AutomaticTransactionsAction() {
		this.catagory = messages.report();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				AbstractReportView<TransactionDetailByAccount> report = new AutomaticTransactionsReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, AutomaticTransactionsAction.this);

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
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {
		return "automaticTransactions";
	}

	@Override
	public String getHelpToken() {
		return "automatic-transactions";
	}

	@Override
	public String getText() {
		return messages.automaticTransactions();
	}

}
