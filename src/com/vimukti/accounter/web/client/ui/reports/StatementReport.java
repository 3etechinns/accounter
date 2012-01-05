package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.StatementServerReport;

public class StatementReport extends AbstractReportView<PayeeStatementsList> {
	public int precategory = 1001;
	public static long payeeId = 0;
	private final boolean isVendor;

	public StatementReport(boolean isVendor) {
		this.isVendor = isVendor;
		this.serverReport = new StatementServerReport(isVendor, this);
	}

	public StatementReport(boolean isVendor, long payeeId) {
		this.payeeId = payeeId;
		this.isVendor = isVendor;
		this.serverReport = new StatementServerReport(isVendor, this);
	}

	@Override
	public void init() {
		super.init();
		this.makeReportRequest(payeeId, toolbar.getStartDate(),
				toolbar.getEndDate());
		this.toolbar.setPayeeId(this.payeeId);
	}

	@Override
	public int getToolbarType() {
		if (isVendor)
			return TOOLBAR_TYPE_VENDOR;
		else
			return TOOLBAR_TYPE_CUSTOMER;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
	}

	@Override
	public void makeReportRequest(long payee, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		// resetReport(endDate, endDate);
		grid.clear();
		grid.addLoadingImagePanel();
		if (payee != 0) {
			payeeId = payee;
		}
		Accounter.createReportService().getStatements(isVendor, payeeId,
				startDate, endDate, this);

	}

	@Override
	public void OnRecordClick(PayeeStatementsList record) {
		record.setStartDate(toolbar.getEndDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactiontype(),
					record.getTransactionId());

	}

	@Override
	public void print() {

		if (payeeId == 0) {
			if (isVendor) {
				Accounter.showError(messages
						.pleaseSelect(Global.get().Vendor()));
			} else {
				Accounter.showError(messages.pleaseSelect(Global.get()
						.Customer()));
			}
		} else {
			if (isVendor) {
				UIUtils.generateReportPDF(
						Integer.parseInt(String.valueOf(startDate.getDate())),
						Integer.parseInt(String.valueOf(endDate.getDate())),
						167, "", "", payeeId);
			} else {
				UIUtils.generateReportPDF(
						Integer.parseInt(String.valueOf(startDate.getDate())),
						Integer.parseInt(String.valueOf(endDate.getDate())),
						150, "", "", payeeId);
			}
		}
	}

	@Override
	public void exportToCsv() {
		if (payeeId == 0) {
			if (isVendor) {
				Accounter.showError(messages
						.pleaseSelect(Global.get().Vendor()));
			} else {
				Accounter.showError(messages.pleaseSelect(Global.get()
						.Customer()));
			}
		} else {
			if (isVendor) {
				UIUtils.exportReport(
						Integer.parseInt(String.valueOf(startDate.getDate())),
						Integer.parseInt(String.valueOf(endDate.getDate())),
						167, "", "", payeeId);
			} else {
				UIUtils.exportReport(
						Integer.parseInt(String.valueOf(startDate.getDate())),
						Integer.parseInt(String.valueOf(endDate.getDate())),
						150, "", "", payeeId);
			}
		}
	}

	@Override
	public void printPreview() {

	}

	@Override
	public PayeeStatementsList getObject(PayeeStatementsList parent,
			PayeeStatementsList child) {
		return super.getObject(parent, child);
	}

	@Override
	public int sort(PayeeStatementsList obj1, PayeeStatementsList obj2, int col) {
		int ret = UIUtils.compareInt(obj1.getCategory(), obj2.getCategory());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 2:
			int num1 = UIUtils.isInteger(obj1.getTransactionNumber()) ? Integer
					.parseInt(obj1.getTransactionNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getTransactionNumber()) ? Integer
					.parseInt(obj2.getTransactionNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return UIUtils.compareTo(obj1.getTransactionNumber(),
						obj2.getTransactionNumber());

		case 1:
			return UIUtils.compareInt(obj1.getTransactiontype(),
					obj2.getTransactiontype());

		case 3:
			return UIUtils.compareDouble(obj1.getAgeing(), obj2.getAgeing());

		case 0:
			return UIUtils.compareTo(obj1.getTransactionDate(),
					obj2.getTransactionDate());

		case 4:
			return UIUtils.compareDouble(
					obj1.getTotal() * obj1.getCurrencyFactor(), obj2.getTotal()
							* obj1.getCurrencyFactor());

		}
		return 0;
	}

	@Override
	public String getDefaultDateRange() {
		return messages.thisMonth();
	}

	@Override
	public void restoreView(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			isDatesArranged = false;
			return;
		}
		ClientFinanceDate startDate = (ClientFinanceDate) map.get("startDate");
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		this.serverReport.setStartAndEndDates(startDate, endDate);
		toolbar.setEndDate(endDate);
		toolbar.setStartDate(startDate);
		toolbar.setDefaultDateRange((String) map.get("selectedDateRange"));
		long status1 = ((Long) map.get("statement"));
		StatementReport.payeeId = status1;
		isDatesArranged = true;
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		ClientFinanceDate startDate = toolbar.getStartDate();
		ClientFinanceDate endDate = toolbar.getEndDate();
		long status = StatementReport.payeeId;
		map.put("selectedDateRange", selectedDateRange);
		map.put("statement", status);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}

}
