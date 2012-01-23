package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.RealisedExchangeLossOrGain;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.client.ui.reports.RealisedExchangeLossesAndGainsReport;

public class RealisedExchangeLossesAndGainsServerReport extends
		AbstractFinaneReport<RealisedExchangeLossOrGain> {

	private double accountBalance = 0.00D;

	public RealisedExchangeLossesAndGainsServerReport(
			RealisedExchangeLossesAndGainsReport reportView) {
		this.reportView = reportView;
	}

	public RealisedExchangeLossesAndGainsServerReport(long startDate,
			long endDate, int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return getColunms();
	}

	@Override
	public String getTitle() {
		return messages.realisedExchangeLossesAndGains();
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.transactionType(),
				messages.transactionDate(), messages.name(),
				messages.currency(), messages.exchangeRate(),
				messages.realisedLossOrGain(), messages.balance() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT };
	}

	@Override
	public void processRecord(RealisedExchangeLossOrGain record) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getColumnData(RealisedExchangeLossOrGain record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return ReportUtility
					.getTransactionName(record.getTransactionType());
		case 1:
			return record.getTransactionDate();
		case 2:
			return record.getPayeeName();
		case 3:
			return record.getCurrency();
		case 4:
			return DataUtils.getAmountAsStringInCurrency(
					record.getExchangeRate(), null);
		case 5:
			return DataUtils.getAmountAsStringInCurrency(
					record.getRealisedLossOrGain(), null);
		case 6:
			this.accountBalance += record.getRealisedLossOrGain();
			return DataUtils.getAmountAsStringInCurrency(accountBalance, null);
		}
		return "";
	}

	@Override
	public ClientFinanceDate getStartDate(RealisedExchangeLossOrGain obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(RealisedExchangeLossOrGain obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
