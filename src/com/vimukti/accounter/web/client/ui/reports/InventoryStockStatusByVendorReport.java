package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.core.ReportsGenerator;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.InventoryStockStatusDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.serverreports.InventoryStockStatusByVendorServerReport;

public class InventoryStockStatusByVendorReport extends
		AbstractReportView<InventoryStockStatusDetail> {

	public InventoryStockStatusByVendorReport(long id) {
		this.serverReport = new InventoryStockStatusByVendorServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getInventoryStockStatusByVendor(start,
				end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(InventoryStockStatusDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record, ActionFactory
				.getInventoryValuationDetailsAction(record.getItemId()));
	}

	@Override
	public void print() {
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())),
				REPORT_TYPE_INVENTORY_STOCK_STATUS_BYVENDOR, "", "");
	}

	@Override
	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())),
				REPORT_TYPE_INVENTORY_STOCK_STATUS_BYVENDOR, "", "");
	}
}