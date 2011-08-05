package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.client.core.reports.VATItemSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.serverreports.VATItemDetailServerReport;


public class VATItemDetailReport extends AbstractReportView<VATItemDetail> {

	public VATItemDetailReport() {
		super();
		this.serverReport = new VATItemDetailServerReport(this);
	}

	@Override
	public void OnRecordClick(VATItemDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactionType(),
					record.getTransactionId());
	}

	@Override
	public void init(ViewManager manager) {
		super.init();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		VATItemSummary itemList = (VATItemSummary) data;
		Accounter.createReportService().getVATItemDetailReport(
				itemList.getName(), start, end, this);

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 139, "",
				"");

	}

	@Override
	public void printPreview() {

	}



	public int sort(VATItemDetail obj1, VATItemDetail obj2, int col) {
		int ret = obj1.getName().compareTo(obj2.getName());
		if (ret != 0) {
			return ret;
		}

		switch (col) {
		case 0:
			return UIUtils.compareInt(obj1.getTransactionType(),
					obj2.getTransactionType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 2:
			return obj1.getTransactionNumber().compareTo(
					obj2.getTransactionNumber());
		case 3:
			return obj1.getName().compareTo(obj2.getName());
		case 4:
			return obj1.getMemo().compareTo(obj2.getMemo());
		case 5:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		case 6:
			return UIUtils.compareDouble(obj1.getSalesPrice(),
					obj2.getSalesPrice());
		}
		return 0;
	}

	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 139, "",
				"");
	}

}
