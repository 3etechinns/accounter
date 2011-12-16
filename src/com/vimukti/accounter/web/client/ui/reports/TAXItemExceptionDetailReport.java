package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TAXItemExceptionDetailReport extends Action {

	private ClientTAXReturn taxReturn;

	public TAXItemExceptionDetailReport() {
		super();
		this.catagory = Accounter.messages().report();
	}

	@Override
	public void run() {

		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean dependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				TAXitemExceptionReport report = new TAXitemExceptionReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						dependent, TAXItemExceptionDetailReport.this);
				if (taxReturn != null) {
					TaxAgencyStartDateEndDateToolbar toolbar = (TaxAgencyStartDateEndDateToolbar) report.toolbar;
					toolbar.fromItem.setDateWithNoEvent(new ClientFinanceDate(
							taxReturn.getPeriodStartDate()));
					toolbar.toItem.setDateWithNoEvent(new ClientFinanceDate(
							taxReturn.getPeriodEndDate()));
					ClientTAXAgency taxAgency = Accounter.getCompany()
							.getTaxAgency(taxReturn.getTAXAgency());
					toolbar.taxAgencyCombo.select(taxAgency);
					toolbar.selectedAgency = taxAgency;
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Report...", t);
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
		return "taxItemExceptionDetails";
	}

	@Override
	public String getHelpToken() {
		return "TaxItemExceptionDetails";
	}

	public void setTaxReturn(ClientTAXReturn taxReturn) {
		this.taxReturn = taxReturn;
	}

	@Override
	public String getText() {
		return messages.taxItemExceptionDetailReport();
	}
}
