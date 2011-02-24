package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.SalesTaxLiability;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class SalesTaxLiabilityServerReport extends
		AbstractFinaneReport<SalesTaxLiability> {

	private String sectionName = "";

	public SalesTaxLiabilityServerReport(
			IFinanceReport<SalesTaxLiability> reportView) {
		this.reportView = reportView;
	}

	public SalesTaxLiabilityServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(SalesTaxLiability record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getTaxItemName();
//			return record.getTaxCodeName();
		case 1:
			return record.getTaxRate() + " %";
		case 2:
			return record.getTaxCollected();
		case 3:
			return record.getTotalSales();
		case 4:
			return record.getNonTaxable();
		case 5:
			return record.getNonTaxableOther();
		case 6:
			return record.getTaxable();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { "TaxCode ", "Tax Rate", "Tax Collected",
				"Total Sales", "Non-taxable", "Non-taxable-other", "Taxable" };

	}

	@Override
	public String getTitle() {
		return "SalesTax Liability";
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// try {
		// onSuccess(this.financeTool.getSalesTaxLiabilityReport(start, end));
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void processRecord(SalesTaxLiability record) {
		if (sectionDepth == 0) {
			addSection("", "Total", new int[] { 2, 3, 4, 5, 6 });
		} else if (sectionDepth == 1) {
			// First time
			this.sectionName = record.getTaxAgencyName();
			addSection(sectionName, "", new int[0]);
		} else if (sectionDepth == 2) {
			// Inside fist section
			addSection("Begining Balance", "Total", new int[] { 2, 3, 4, 5, 6 });
		} else if (sectionDepth == 3) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getTaxAgencyName())) {
				endSection();
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientFinanceDate getEndDate(SalesTaxLiability obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(SalesTaxLiability obj) {
		return obj.getStartDate();
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { "TaxCode ", "Tax Rate", "Tax Collected",
				"Total Sales", "Non-taxable", "Non-taxable-other", "Taxable" };
	}

}
