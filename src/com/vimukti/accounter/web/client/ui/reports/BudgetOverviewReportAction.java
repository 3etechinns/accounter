package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class BudgetOverviewReportAction extends Action {

	protected BudgetOverviewReport report;


	public BudgetOverviewReportAction() {
		super();
		this.catagory = Accounter.messages().report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				report = new BudgetOverviewReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, BudgetOverviewReportAction.this);

			}

		});

	}

	// @Override
	// public ParentCanvas getView() {
	// // its not using any where return null;
	// return this.report;
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
		return "budgetOverviewReport";
	}

	@Override
	public String getHelpToken() {
		return "budget-report";
	}

	@Override
	public String getText() {
		return messages.budgetOverview();
	}

}
