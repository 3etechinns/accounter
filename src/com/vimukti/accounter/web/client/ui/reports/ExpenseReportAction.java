package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class ExpenseReportAction extends Action {

	public ExpenseReportAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getReportsMessages().report();
	}

	public ExpenseReportAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getReportsMessages().report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					AbstractReportView<ExpenseList> report = new ExpenseReport();
					MainFinanceWindow.getViewManager().showView(report, data,
							isDependent, ExpenseReportAction.this);
				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// UIUtils.logError("Failed to Load Report...", t);
			}
		});

	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
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
		return "expenseReport";
	}

}
