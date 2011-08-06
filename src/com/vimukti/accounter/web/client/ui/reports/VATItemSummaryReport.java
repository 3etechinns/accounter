package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.VATItemSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.serverreports.VATItemSummaryServerReport;

public class VATItemSummaryReport extends AbstractReportView<VATItemSummary> {

	public VATItemSummaryReport() {
		super(false, Accounter.constants().noRecordsToShow());
		this.serverReport = new VATItemSummaryServerReport(this);
	}

	@Override
	public void OnRecordClick(VATItemSummary record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record, ActionFactory.getVaTItemDetailAction());

	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getVATItemSummaryReport(start, end,
				this);

	}

	@Override
	public void processRecord(VATItemSummary record) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 141, "",
				"");

	}

	@Override
	public void printPreview() {

	}

	public int sort(VATItemSummary obj1, VATItemSummary obj2, int col) {

		switch (col) {
		case 0:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());
		case 1:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		}
		return 0;
	}

	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 141, "",
				"");
	}

}
