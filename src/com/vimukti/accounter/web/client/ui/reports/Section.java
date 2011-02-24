package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.ui.serverreports.BalanceSheetServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ProfitAndLossServerReport;

public class Section<R> {

	private IFinanceReport<R> financeReport;

	public String title, footer;

	String[] titles, footers;
	int[] sumColumnsIndexes;

	public Object[] data = null;
	public boolean isaddFooter = true, ishowGridFooter;

	ISectionHandler<R> handler;

	public Section(String title, String footer, int[] sumColums,
			int colsLength, IFinanceReport<R> report) {
		this.title = title;
		this.footer = footer;
		this.financeReport=report;
		data = new Object[colsLength];
		this.sumColumnsIndexes = sumColums;
		this.handler = report.getSectionHanlder();
	}

	public Section(String[] titles, String[] footers, int[] sumColums,
			int colsLength, IFinanceReport<R> report) {
		this.titles = titles;
		this.footers = footers;
		this.financeReport=report;
		data = new Object[colsLength];
		if (!financeReport.isServerSide()) {
			updateTitleandFooterValuesonClient();
		} else {
			updateTitleandFooterValuesonServer();
		}
		this.sumColumnsIndexes = sumColums;
		this.handler = report.getSectionHanlder();
	}

	private void updateTitleandFooterValuesonServer() {
		if (financeReport instanceof BalanceSheetServerReport
				|| financeReport instanceof ProfitAndLossServerReport) {
			this.title = ((titles[0] == "" || titles[0] == null) ? titles[1]
					: titles[0]);
			this.footer = ((footers[0] == "" || footers[0] == null) ? footers[1]
					: footers[0]);
		}
	}

	public void startSection() {

		if (handler != null) {
			handler.OnSectionAdd(this);
		}
		if (titles == null) {
			titles = new String[] { title };
		}
		if (isTitleEmpty())

			financeReport.addRow(null, 0, titles, true, false, false);
	}

	public void update(Object[] values) {
		for (int index : sumColumnsIndexes) {
			this.data[index] = sum((this.data[index] == ""
					|| this.data[index] == null ? 0.0 : this.data[index]),
					(values[index] == "" || values[index] == null ? 0.0
							: values[index]));
		}
	}

	private boolean isTitleEmpty() {
		for (String str : titles) {
			if (str == null)
				continue;
			str = str.trim();
			if (str.length() > 0)
				return true;
		}
		return false;
	}

	private double sum(Object a, Object b) {
		double da = a == null ? 0.0 : (Double) a;
		double db = b == null ? 0.0 : (Double) b;
		return da + db;
	}

	public void endSection() {
		updateFooterInData();

		if (handler != null) {
			handler.OnSectionEnd(this);
		}
		if (ishowGridFooter)
			financeReport.addFooter(data);

		if (isaddFooter)
			financeReport.addRow(null, 2, data, true, true, true);
	}

	private void updateFooterInData() {
		if (footers == null) {
			footers = new String[] { footer };
		}
		int j = 0;
		for (String ft : footers) {
			// if (ft != null & ft.equals("")) {
			data[j] = ft;
			j++;
			// }
		}
	}

	private void updateTitleandFooterValuesonClient() {
		if (financeReport instanceof com.vimukti.accounter.web.client.ui.reports.BalanceSheetReport
				|| financeReport instanceof com.vimukti.accounter.web.client.ui.reports.ProfitAndLossReport) {
			this.title = ((titles[0] == "" || titles[0] == null) ? titles[1]
					: titles[0]);
			this.footer = ((footers[0] == "" || footers[0] == null) ? footers[1]
					: footers[0]);
		}
	}
}