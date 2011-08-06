package com.vimukti.accounter.web.client.ui.reports;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.serverreports.VAT100ServerReport;

public class VAT100Report extends AbstractReportView<VATSummary> {

	private String sectionName = "";

	private int row = -1;
	private long vatAgency;

	protected Double box3amount = 0.0D;
	protected Double box4amount = 0.0D;

	public VAT100Report() {
		super(false, Accounter.constants().noRecordsToShow());
		this.serverReport = new VAT100ServerReport(this);
	}

	@Override
	public void init() {
		super.init();
		toolbar.setDateRanageOptions(Accounter.constants().all(), Accounter
				.constants().thisWeek(), Accounter.constants().thisMonth(),
				Accounter.constants().lastWeek(), Accounter.constants()
						.lastMonth(),
				Accounter.constants().thisFinancialYear(), Accounter
						.constants().lastFinancialYear(), Accounter.constants()
						.thisFinancialQuarter(), Accounter.constants()
						.lastFinancialQuarter(), Accounter.constants()
						.financialYearToDate(), Accounter.constants()
						.lastVATQuarter(), Accounter.constants()
						.lastVATQuarterToDate(), Accounter.constants().custom());

		// Make rpc request for default VAT Agency and default DateRange
		List<ClientTAXAgency> vatAgencies = Accounter.getCompany()
				.getTaxAgencies();
		for (ClientTAXAgency vatAgency : vatAgencies) {
			if (vatAgency.getName().equalsIgnoreCase(
					Accounter.constants().hmCustomsExciseVAT())) {
				ClientFinanceDate date = new ClientFinanceDate();
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				ClientFinanceDate startDate = new ClientFinanceDate(
						date.getYear(), startMonth, 1);
				ClientFinanceDate start = startDate;
				ClientFinanceDate end = date;
				makeReportRequest(vatAgency.getID(), start, end);
				break;
			}
		}
	}

	@Override
	public void OnRecordClick(VATSummary record) {
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] {
				"",
				UIUtils.getDateByCompanyType(toolbar.getStartDate()) + "-"
						+ UIUtils.getDateByCompanyType(toolbar.getEndDate()) };
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE_VATAGENCY;
	}

	@Override
	public void makeReportRequest(long vatAgency, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		// row = -1;
		// this.sectionName = "";
		Accounter.createReportService().getVAT100Report(vatAgency, startDate,
				endDate, this);
		this.vatAgency = vatAgency;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 137, "",
				"", vatAgency);
	}

	@Override
	public void printPreview() {

	}

	@Override
	public int sort(VATSummary obj1, VATSummary obj2, int col) {

		// switch (col) {
		// case 0:
		// return obj1.getName().compareTo(obj2.getName());
		// case 1:
		// return UIUtils.compareDouble(obj1.getValue(), obj2.getValue());
		// }
		return 0;
	}

	public void exportToCsv() {

		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 137, "",
				"", vatAgency);
	}

}
