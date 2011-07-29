package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseClosedOrderServerReport;


public class PurchaseClosedOrderReport extends
		AbstractReportView<OpenAndClosedOrders> {

	public PurchaseClosedOrderReport() {
		this.serverReport = new PurchaseClosedOrderServerReport(this);
	}

	@Override
	public void init() {
		super.init();
		toolbar.setDateRanageOptions("", Accounter.constants().present(),
				Accounter.constants().lastMonth(), Accounter.constants()
						.last3Months(), Accounter.constants().last6Months(),
				Accounter.constants().lastYear(), Accounter.constants()
						.untilEndOfYear(), Accounter.constants().custom());
	}

	@Override
	public void OnRecordClick(OpenAndClosedOrders record) {

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getPurchaseClosedOrderReport(start,
				end, this);

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 135, "",
				"");

	}

	@Override
	public void printPreview() {

	}

	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 135, "",
				"");
	}
}
