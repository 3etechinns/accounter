package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.serverreports.VendorTransactionHistoryServerReport;

/**
 * @author kumar kasimala
 * 
 * 
 */

public class VendorTransactionHistoryReport extends
		AbstractReportView<TransactionHistory> {

	public VendorTransactionHistoryReport() {
		this.serverReport = new VendorTransactionHistoryServerReport(this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getVendorTransactionHistory(start, end,
				this);
	}

	@Override
	public void OnRecordClick(TransactionHistory record) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(getType(record),
					record.getTransactionId());
	}



	@Override
	public void onEdit() {

	}

	@Override
	public void print() {
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 129, "",
				"");
	}

	@Override
	public void printPreview() {

	}

	int getType(TransactionHistory record) {
		if (record.getType() == 11) {
			return (record.getMemo() != null && record.getMemo()
					.equalsIgnoreCase("supplier prepayment")) ? ClientTransaction.TYPE_VENDOR_PAYMENT
					: ClientTransaction.TYPE_PAY_BILL;
		}

		return record.getType();
	}

	public int sort(TransactionHistory obj1, TransactionHistory obj2, int col) {
		int ret = obj1.getName().toLowerCase()
				.compareTo(obj2.getName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 0:
			return obj1.getName().compareTo(obj2.getName());
		case 2:
			return UIUtils.compareInt(obj1.getType(), obj2.getType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 3:
			return UIUtils.compareInt(Integer.parseInt(obj1.getNumber()),
					Integer.parseInt(obj2.getNumber()));
			// case 3:
			// return obj1.getReference().toLowerCase().compareTo(
			// obj2.getReference().toLowerCase());
		case 4:
			return obj1.getAccount().toLowerCase()
					.compareTo(obj2.getAccount().toLowerCase());
		case 5:
			if (DecimalUtil.isEquals(obj1.getInvoicedAmount(), 0.0))
				return UIUtils.compareDouble(obj1.getPaidAmount(),
						obj2.getPaidAmount());
			else
				return UIUtils.compareDouble(obj1.getInvoicedAmount(),
						obj2.getInvoicedAmount());
		}
		return 0;
	}

	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 129, "",
				"");
	}
}
