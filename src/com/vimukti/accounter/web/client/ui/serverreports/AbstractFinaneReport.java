package com.vimukti.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;
import com.vimukti.accounter.web.client.ui.reports.ISectionHandler;
import com.vimukti.accounter.web.client.ui.reports.Section;

public abstract class AbstractFinaneReport<R> implements IFinanceReport<R> {

	protected int sectionDepth = 0;
	private String[] columns;

	protected List<R> records;

	protected ISectionHandler<R> handler;

	public boolean isVATDetailReport;
	public boolean isVATSummaryReport;

	protected List<Integer> columnstoHide = new ArrayList<Integer>();

	ReportGridTemplate<R> grid;

	private List<Section<R>> sections = new ArrayList<Section<R>>();

	protected boolean isShowTotal = true;
	private boolean ishowGridFooter = true;

	public ClientFinanceDate startDate;
	public ClientFinanceDate endDate;
	public ClientFinanceDate currentFiscalYearStartDate;
	public ClientFinanceDate currentFiscalYearEndDate;

	public static final int COLUMN_TYPE_TEXT = 1;
	public static final int COLUMN_TYPE_AMOUNT = 2;
	public static final int COLUMN_TYPE_DATE = 3;
	public static final int COLUMN_TYPE_NUMBER = 4;
	public static final int COLUMN_TYPE_PERCENTAGE = 5;

	private int generationType = 0;

	public int getGenerationtype() {
		return generationType;
	}

	public void setGenerationtype(int generationtype) {
		this.generationType = generationtype;
	}

	public IFinanceReport<R> reportView;
	protected String navigateObjectName;
	private int companyType;

	
	public AbstractFinaneReport() {
		this.columns = this.getColunms();
		if (generationType == 1001) {
			this.grid = new PDFReportGridTemplate<R>(columns, ishowGridFooter);
		} else {
			this.grid = new CSVReportGridTemplate<R>(columns, ishowGridFooter);
		}

		this.grid.setReportView(this);
	}

	@SuppressWarnings( { "unchecked" })
	public AbstractFinaneReport(long startDate, long endDate, int generationType) {
		this.startDate = new ClientFinanceDate(startDate);
		this.endDate = new ClientFinanceDate(endDate);
		this.generationType = generationType;
		this.columns = this.getColunms();
		if (generationType == 1001) {
			this.grid = new PDFReportGridTemplate<R>(columns, ishowGridFooter);
		} else {
			this.grid = new CSVReportGridTemplate<R>(columns, ishowGridFooter);
		}
		this.grid.setReportView(this);
	}

	void setReportGridTemplate(ReportGridTemplate<R> reportGridTemplate) {
		this.grid = reportGridTemplate;
	}

	/**
	 * Called when there is a problem in the server side in returning the data
	 * for the report view.
	 */
	@Override
	public void onFailure(Throwable caught) {
		ActionFactory.getReportsHomeAction();
	}

	/**
	 * This method will be called when the RPC method returns. This is a async
	 * call back.
	 */
	@Override
	public void onSuccess(List<R> result) {
		try {
			if (result != null && result.size() > 1) {
				removeAllRows();
				setFromAndToDate(result);
				initRecords(result);

			} else {
				removeAllRows();
				if (result != null && result.size() == 1)
					setFromAndToDate(result);
				endStatus();
			}
			showRecords();
		} catch (Exception e) {
			endStatus();
			System.err.println("EXCEPTION " + e);
		}
	}

	public String dateFormat(ClientFinanceDate date) {
		try {
			if (date == null)
				return "";
			DateTimeFormat dateFormatter = DateTimeFormat
					.getFormat("dd/MM/yyyy");
			String format = dateFormatter.format(date.getDateAsObject());
			return format;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setFromAndToDate(List<R> result) {
		try {
			R obj = result.get(result.size() - 1);
			startDate = getStartDate(obj);
			endDate = getEndDate(obj);
			setHeaderTitle();
			if (startDate != null || endDate != null) {
				result.remove(result.size() - 1);
			}
			this.records = result;
		} catch (Exception e) {
			System.err.println(e);
		}

	}

	@Override
	public void initRecords(List<R> records) {
		removeAllRows();
		this.records = records;

		for (R record : records) {
			processRecord(record);
			Object[] values = new Object[this.columns.length];
			for (int x = 0; x < this.columns.length; x++) {
				values[x] = getColumnData(record, x);
			}
			updateTotals(values);
			addRow(record, 2, values, false, false, false);
		}
		endAllSections();
		sections.clear();
		endStatus();
		showRecords();
	}

	protected void setHeaderTitle() {
		String[] dynamiccolumns = getDynamicHeaders();
		if (dynamiccolumns != null) {
			for (int index = 0; index < dynamiccolumns.length; index++) {

				grid.setHeaderText(dynamiccolumns[index], index);
			}
		}
	}

	/**
	 * This method is to close all sections in reverse Order
	 */
	public void endAllSections() {
		try {
			for (int i = this.sections.size() - 1; i >= 0; i--) {
				if (i == 1) {
					sections.get(0).isaddFooter = false;
					endSection();
				} else
					endSection();
			}
		} catch (Exception e) {
		}
	}

	private void endStatus() {
	}

	@Override
	public void updateTotals(Object[] values) {
		for (Section<R> sec : this.sections) {
			sec.update(values);
		}
	}

	/**
	 * This method creates a section
	 * 
	 * @param sectionTitle
	 *            section title
	 * @param footerTitle
	 *            footerTitle if available, null otherwise.
	 * @param sumColumns
	 *            indexes of the columns that needs to totalled
	 */
	public void addSection(String sectionTitle, String footerTitle,
			int[] sumColumns) {
		Section<R> s = new Section<R>(sectionTitle, footerTitle, sumColumns,
				columns.length, this);
		s.startSection();
		sections.add(s);
		sectionDepth++;
	}

	public void addSection(String[] sectionTitles, String[] footerTitles,
			int[] sumColumns) {
		Section<R> s = new Section<R>(sectionTitles, footerTitles, sumColumns,
				columns.length, this);
		s.startSection();
		sections.add(s);
		sectionDepth++;
	}

	/**
	 * Report Title
	 * 
	 * @return
	 */

	public abstract String getTitle();

	/**
	 * @return all column names in order
	 */
	public abstract String[] getColunms();

	/**
	 * 
	 * @return all column types in the same order as column names
	 */
	public abstract int[] getColumnTypes();

	/**
	 * Check and pre-process the report before it is added to the report view.
	 * You may want to check and create sections depending on the requirement of
	 * your report.
	 * 
	 * @param record
	 */
	public abstract void processRecord(R record);

	public abstract Object getColumnData(R record, int columnIndex);

	public abstract ClientFinanceDate getStartDate(R obj);

	public abstract ClientFinanceDate getEndDate(R obj);

	public boolean isWiderReport() {
		return false;
	}

	public void endSection() {
		try {
			this.sectionDepth--;
			if (sectionDepth >= 0 && !sections.isEmpty()) {
				Section<R> s = sections.remove(sectionDepth);
				s.endSection();
			}

		} catch (Exception e) {
			// do nothing
		}
	}

	public boolean isIshowGridFooter() {
		return ishowGridFooter;
	}

	public void setIshowGridFooter(boolean ishowGridFooter) {
		this.ishowGridFooter = ishowGridFooter;
		this.ishowGridFooter = ishowGridFooter;
	}

	public String getDefaultDateRange() {
		return "All";
	}

	public int getColumnWidth(int index) {
		return -1;
	}

	public void refresh() {
		if (reportView == null) {
			resetVariables();
			this.initRecords(records);
		} else {
			this.reportView.refresh();
		}
	}

	public List<Integer> getColumnstoHide() {
		return columnstoHide;
	}

	public void setColumnstoHide(List<Integer> columnstoHide) {
		this.columnstoHide = columnstoHide;
	}

	@Override
	public void addFooter(Object[] values) {
		if (reportView == null) {
			this.grid.addFooter(values);
		} else {
			this.reportView.addFooter(values);
		}
	}

	@Override
	public void addRow(R record, int depth, Object[] values, boolean bold,
			boolean underline, boolean border) {
		if (reportView == null) {
			this.grid.addRow(record, depth, values, bold, underline, border);
		} else {
			this.reportView.addRow(record, depth, values, bold, underline,
					border);
		}
	}

	@Override
	public ISectionHandler<R> getSectionHanlder() {
		return this.handler;
	}

	@Override
	public boolean isServerSide() {
		return true;
	}

	public ClientFinanceDate getStartDate() {
		return startDate;
	}

	public ClientFinanceDate getEndDate() {
		return endDate;
	}

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	@Override
	public void setHandler(ISectionHandler<R> handler) {
		this.handler = handler;
	}

	public void setRecords(List<R> records) {
		this.records = records;
	}

	/**
	 * In this method we will just make the GUI control that holds all data
	 * visible
	 */
	public void showRecords() {
		try {
		} catch (Exception e) {
		}
	}

	/**
	 * In this method you need to call the RPC method by passing 'this' as
	 * callback.
	 */
	public abstract void makeReportRequest(long start, long end);

	public void makeReportRequest(String vatAgency, ClientFinanceDate endDate) {

	}

	public void makeReportRequest(String vatAgency,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

	}

	/**
	 * This method is used for Sales and Purchase order reports by sending the
	 * status.This method is override in the SalesOpenOrderReport and
	 * PurchaseOpenOrderReport
	 */
	public void makeReportRequest(int status, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

	}

	public void initData() {
	}

	protected String getPreviousReportDateRange(Object object) {
		return "";
	}

	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return null;
	}

	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return null;
	}

	/**
	 * Reset Variables in Report,
	 */
	public void resetVariables() {
		// implemented in subclass
	}

	@Override
	public List<R> getRecords() {
		return this.records;
	}

	@Override
	public void removeAllRows() {
		if (reportView == null) {
			this.grid.removeAllRows();
		} else {
			this.reportView.removeAllRows();
		}
	}

	public ReportGridTemplate<R> getGridTemplate() {
		return this.grid;
	}

	// public void setFinaceTool(FinanceTool financeTool) {
	// this.financeTool = financeTool;
	// }

	public void setNavigatedObjectName(String navigateObjectName) {
		this.navigateObjectName = navigateObjectName;
	}

	public String getDateByCompanyType(ClientFinanceDate date) {
		if (this.reportView == null) {
			return "";
		}
		if (date == null) {
			return "";
		}
		if (this.companyType == ClientCompany.ACCOUNTING_TYPE_UK) {
			DateTimeFormat dateFormatter = DateTimeFormat
					.getFormat("MM/dd/yyyy");
			String format = dateFormatter.format(date.getDateAsObject());
			return format;
		} else if (this.companyType == ClientCompany.ACCOUNTING_TYPE_US) {
			DateTimeFormat dateFormatter = DateTimeFormat
					.getFormat("dd/MM/yyyy");
			String format = dateFormatter.format(date.getDateAsObject());
			return format;
		} else {
			DateTimeFormat dateFormatter = DateTimeFormat
					.getFormat("dd/MM/yyyy");
			String format = dateFormatter.format(date.getDateAsObject());
			return format;
		}

	}

	// public static final int MEMO_OPENING_BALANCE = 0;
	// public static final int TYPE_CASH_SALES = 1;
	// public static final int TYPE_CASH_PURCHASE = 2;
	// public static final int TYPE_CREDIT_CARD_CHARGE = 3;
	// public static final int TYPE_CUSTOMER_CREDIT_MEMO = 4;
	// public static final int TYPE_CUSTOMER_REFUNDS = 5;
	// public static final int TYPE_ENTER_BILL = 6;
	// public static final int TYPE_ESTIMATE = 7;
	//
	// public static final int TYPE_INVOICE = 8;
	// public static final int TYPE_ISSUE_PAYMENT = 9;
	// public static final int TYPE_MAKE_DEPOSIT = 10;
	// public static final int TYPE_PAY_BILL = 11;
	// public static final int TYPE_RECEIVE_PAYMENT = 12;
	// public static final int TYPE_TRANSFER_FUND = 13;
	// public static final int TYPE_VENDOR_CREDIT_MEMO = 14;
	// public static final int TYPE_WRITE_CHECK = 15;
	// public static final int TYPE_JOURNAL_ENTRY = 16;
	// public static final int TYPE_PAY_SALES_TAX = 17;
	// public static final int TYPE_EXPENSE = 18;
	// public static final int TYPE_PAY_EXPENSE = 19;
	// public static final int TYPE_VAT_RETURN = 20;
	//
	// public static final int TYPE_SALES_ORDER = 21;
	// public static final int TYPE_PURCHASE_ORDER = 22;
	// public static final int TYPE_ITEM_RECEIPT = 23;
	//
	// public static final int TYPE_ADJUST_VAT_RETURN = 24;
	// public static final int TYPE_PAY_VAT = 30;
	//
	// public static final int TYPE_CASH_EXPENSE = 26;
	// public static final int TYPE_CREDIT_CARD_EXPENSE = 27;
	// public static final int TYPE_EMPLOYEE_EXPENSE = 28;
	// public static final int TYPE_CUSTOMER_PREPAYMENT = 29;
	//
	// public static final int TYPE_VENDOR_PAYMENT = 25;
	// public static final int TYPE_RECEIVE_VAT = 31;
	//
	// public String getTransactionName(int transactionType) {
	//
	// String transactionName = null;
	// switch (transactionType) {
	// case MEMO_OPENING_BALANCE:
	// transactionName = "Opening Balance";
	// break;
	// case TYPE_CASH_SALES:
	// transactionName = "Cash Sale";
	// break;
	// case TYPE_CASH_PURCHASE:
	// transactionName = "Cash Purchase";
	// break;
	// case TYPE_CREDIT_CARD_CHARGE:
	// transactionName = "Credit Card Charge";
	// break;
	// case TYPE_CUSTOMER_CREDIT_MEMO:
	// transactionName = "Customer Credit";
	// break;
	// case TYPE_CUSTOMER_REFUNDS:
	// transactionName = "Customer Refund";
	// break;
	// case TYPE_ENTER_BILL:
	// transactionName = getVendorString("Supplier Bill", "Vendor Bill");
	// break;
	// case TYPE_ESTIMATE:
	// transactionName = "Quote";
	// break;
	// case TYPE_INVOICE:
	// transactionName = "Invoice";
	// break;
	// case TYPE_ISSUE_PAYMENT:
	// transactionName = "Issue Payment";
	// break;
	// case TYPE_MAKE_DEPOSIT:
	// transactionName = "Deposit/Transfer Funds";
	// break;
	// case TYPE_PAY_BILL:
	// transactionName = getVendorString("Supplier Payment",
	// "Vendor Payment");
	// break;
	// case TYPE_VENDOR_PAYMENT:
	// transactionName = getVendorString("Supplier Prepayment",
	// "Vendor Prepayment");
	// break;
	// case TYPE_RECEIVE_PAYMENT:
	// transactionName = "Customer Payment";
	// break;
	// case TYPE_TRANSFER_FUND:
	// transactionName = "Transfer Fund";
	// break;
	// case TYPE_VENDOR_CREDIT_MEMO:
	// transactionName = getVendorString("Supplier Credit",
	// "Vendor Credit");
	// break;
	// case TYPE_WRITE_CHECK:
	// transactionName = "Check";
	// break;
	// case TYPE_JOURNAL_ENTRY:
	// transactionName = "Journal Entry";
	// break;
	// case TYPE_PAY_SALES_TAX:
	// transactionName = "Pay Sales Tax";
	// break;
	// case TYPE_RECEIVE_VAT:
	// transactionName = "Receive VAT ";
	// break;
	// case TYPE_SALES_ORDER:
	// transactionName = "Sales Order";
	// break;
	// case TYPE_PURCHASE_ORDER:
	// transactionName = "Purchase Order";
	// break;
	// case TYPE_ITEM_RECEIPT:
	// transactionName = "Item Receipt";
	// break;
	// case TYPE_CASH_EXPENSE:
	// transactionName = "Cash Expense";
	// break;
	// case TYPE_EMPLOYEE_EXPENSE:
	// transactionName = "Employee Expense";
	// break;
	// case TYPE_CREDIT_CARD_EXPENSE:
	// transactionName = "Credit Card Expense";
	// break;
	// case TYPE_VAT_RETURN:
	// transactionName = "VAT Return ";
	// break;
	// case TYPE_PAY_VAT:
	// transactionName = "Pay VAT";
	// break;
	// case TYPE_CUSTOMER_PREPAYMENT:
	// transactionName = "Customer PrePayment";
	// }
	// return transactionName;
	// }

	public int getCompanyType() {
		return companyType;
	}

	@Override
	public void setCompanyType(int companyType) {
		this.companyType = companyType;
	}

	@Override
	public ClientFinanceDate getCurrentFiscalYearStartDate() {
		if (this.reportView != null) {
			return this.reportView.getCurrentFiscalYearStartDate();
		}
		return this.currentFiscalYearStartDate;
	}

	@Override
	public ClientFinanceDate getCurrentFiscalYearEndDate() {
		if (this.reportView != null) {
			return this.reportView.getCurrentFiscalYearEndDate();
		}
		return this.currentFiscalYearEndDate;
	}

	public void setCurrentFiscalYearStartDate(
			ClientFinanceDate currentFiscalYearStartDate) {
		this.currentFiscalYearStartDate = currentFiscalYearStartDate;
	}

	public void setCurrentFiscalYearEndDate(
			ClientFinanceDate currentFiscalYearEndDate) {
		this.currentFiscalYearEndDate = currentFiscalYearEndDate;
	}

	public String getVendorString(String forUk, String forUs) {
		return companyType == ClientCompany.ACCOUNTING_TYPE_UK ? forUk : forUs;
	}

	@Override
	public void setStartAndEndDates(long startDate2, long endDate2) {
		this.startDate = new ClientFinanceDate(startDate2);
		this.endDate = new ClientFinanceDate(endDate2);
	}
}
