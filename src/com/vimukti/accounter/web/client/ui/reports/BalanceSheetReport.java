package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.serverreports.BalanceSheetServerReport;


public class BalanceSheetReport extends AbstractReportView<TrialBalance> {

	public BalanceSheetReport() {
		super(false, "");
		this.serverReport = new BalanceSheetServerReport(this);
		this.serverReport.setIshowGridFooter(false);
	}

	@Override
	public void OnRecordClick(TrialBalance record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (record.getAccountId() != 0) {
			UIUtils.runAction(record,
					ActionFactory.getTransactionDetailByAccountAction());
		} else {
			UIUtils.runAction(record,
					ActionFactory.getProfitAndLossAction());
		}

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_AS_OF;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getBalanceSheetReport(start, end, this);
	}



	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 112, "",
				"");

	}

	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 112, "",
				"");
	}
	
	@Override
	public void restoreView(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			isDatesArranged = false;
			return;
		}
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		toolbar.setEndDate(endDate);
		toolbar.setDefaultDateRange((String) map.get("selectedDateRange"));
		isDatesArranged = true;
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		ClientFinanceDate endDate = toolbar.getEndDate();
		map.put("selectedDateRange", selectedDateRange);
		map.put("endDate", endDate);
		return map;
	}
}
