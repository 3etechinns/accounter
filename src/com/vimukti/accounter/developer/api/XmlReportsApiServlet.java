package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.AccounterReportServiceImpl;

public class XmlReportsApiServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String DATE_PATTERN = "";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String methodName = getMethodName(req);
		SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);

		Date startDate = null;
		Date endDate = null;
		try {
			startDate = format.parse(req.getParameter("StartDate"));
			endDate = format.parse(req.getParameter("EndDate"));
		} catch (ParseException e) {
			throw new ServletException("Wrong date formate");
		}

		AccounterReportServiceImpl accounterReportServiceImpl = getAccounterReportServiceImpl();

		ClientFinanceDate clientFinanceStartDate = new ClientFinanceDate(
				startDate);
		ClientFinanceDate clientFinanceEndDate = new ClientFinanceDate(endDate);

		List<? extends BaseReport> result = null;

		if (methodName.equals("salesbycustomersummary")) {
			result = accounterReportServiceImpl.getSalesByCustomerSummary(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("payeestatements")) {
			int id = (Integer) req.getAttribute("Id");
			long transactionDate = (Long) req.getAttribute("TransactionDate");
			ClientFinanceDate fromDate = (ClientFinanceDate) req
					.getAttribute("FromDate");
			ClientFinanceDate toDate = (ClientFinanceDate) req
					.getAttribute("ToDate");
			int noOfDays = (Integer) req.getAttribute("NoOfDays");
			boolean isEnabledOfZeroBalBox = (Boolean) req
					.getAttribute("IsEnabledOfZeroBalBox");
			boolean isEnabledOfLessthanZeroBalBox = (Boolean) req
					.getAttribute("IsEnabledOfLessthanZeroBalBox");
			double lessThanZeroBalanceValue = (Double) req
					.getAttribute("LessThanZeroBalanceValue");
			boolean isEnabledOfNoAccountActivity = (Boolean) req
					.getAttribute("IsEnabledOfNoAccountActivity");
			boolean isEnabledOfInactiveCustomer = (Boolean) req
					.getAttribute("IsEnabledOfInactiveCustomer");

			accounterReportServiceImpl.getStatements(id, transactionDate,
					fromDate, toDate, noOfDays, isEnabledOfZeroBalBox,
					isEnabledOfLessthanZeroBalBox, lessThanZeroBalanceValue,
					isEnabledOfNoAccountActivity, isEnabledOfInactiveCustomer);

		} else if (methodName.equals("agedcreditors")) {
			result = accounterReportServiceImpl.getAgedCreditors(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("checkdetails")) {
			Long paymentmethod = (Long) req.getAttribute("PaymentMethod");
			result = accounterReportServiceImpl
					.getCheckDetailReport(paymentmethod,
							clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("depositdetail")) {
			result = accounterReportServiceImpl.getDepositDetail(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("expensereports")) {
			int status = (Integer) req.getAttribute("Status");
			result = accounterReportServiceImpl.getExpenseReportByType(status,
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("creditors")) {
			try {
				result = accounterReportServiceImpl.getCreditors(
						clientFinanceStartDate, clientFinanceEndDate);
			} catch (AccounterException e) {
				e.printStackTrace();
			}

		} else if (methodName.equals("ageddebtors")) {
			result = accounterReportServiceImpl.getAgedDebtors(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("amountsduetovendor")) {
			result = accounterReportServiceImpl.getAmountsDueToVendor(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salesbycustomerdetailbyname")) {
			String customerName = (String) req.getAttribute("Name");
			result = accounterReportServiceImpl.getSalesByCustomerDetailReport(
					customerName, clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("customertransactionhistory")) {
			result = accounterReportServiceImpl.getCustomerTransactionHistory(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("debitorslist")) {
			try {
				result = accounterReportServiceImpl.getDebitors(
						clientFinanceStartDate, clientFinanceEndDate);
			} catch (AccounterException e) {
				e.printStackTrace();
			}

		} else if (methodName.equals("mostprofitablecustomers")) {
			result = accounterReportServiceImpl.getMostProfitableCustomers(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("reversechargelist")) {
			String payeeName = (String) req.getAttribute("Name");
			result = accounterReportServiceImpl
					.getReverseChargeListDetailReport(payeeName,
							clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyitemdetail")) {
			result = accounterReportServiceImpl.getPurchasesByItemDetail(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("reversechargelistdetails")) {
			String payeeName = (String) req.getAttribute("Name");
			result = accounterReportServiceImpl
					.getReverseChargeListDetailReport(payeeName,
							clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("ecsaleslistdetails")) {
			String payeeName = (String) req.getAttribute("Name");
			result = accounterReportServiceImpl.getECSalesListDetailReport(
					payeeName, clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyitemsummary")) {
			result = accounterReportServiceImpl.getPurchasesByItemSummary(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyvendordetail")) {
			result = accounterReportServiceImpl.getPurchasesByVendorDetail(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("ecsaleslist")) {
			result = accounterReportServiceImpl.getECSalesListReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyvendorsummary")) {
			result = accounterReportServiceImpl.getPurchasesByVendorSummary(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salesbycustomerdetail")) {
			result = accounterReportServiceImpl.getSalesByCustomerDetailReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salesbyitemdetail")) {
			result = accounterReportServiceImpl.getSalesByItemDetail(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salesbyitemsummary")) {
			result = accounterReportServiceImpl.getSalesByItemSummary(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("transactiondetailbytaxitem")) {
			result = accounterReportServiceImpl.getTransactionDetailByTaxItem(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("trailbalance")) {
			result = accounterReportServiceImpl.getTrialBalance(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("vendortransactionhistory")) {
			result = accounterReportServiceImpl.getVendorTransactionHistory(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchasereportitems")) {
			result = accounterReportServiceImpl.getPurchaseReportItems(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salesreportitems")) {
			result = accounterReportServiceImpl.getSalesReportItems(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("transactionhistorycustomers")) {
			List<ClientCustomer> transactionHistoryCustomers = accounterReportServiceImpl
					.getTransactionHistoryCustomers(clientFinanceStartDate,
							clientFinanceEndDate);
			try {
				sendIAccountCoreResult(req, resp, transactionHistoryCustomers);
			} catch (Exception e) {
				throw new ServletException();
			}

		} else if (methodName.equals("vatitemdetails")) {
			String vatItemName = (String) req.getAttribute("Name");
			result = accounterReportServiceImpl.getVATItemDetailReport(
					vatItemName, clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("vatitemsummary")) {
			result = accounterReportServiceImpl.getVATItemSummaryReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("transactionhistoryvendors")) {
			List<ClientVendor> transactionHistoryVendors = accounterReportServiceImpl
					.getTransactionHistoryVendors(clientFinanceStartDate,
							clientFinanceEndDate);
			try {
				sendIAccountCoreResult(req, resp, transactionHistoryVendors);
			} catch (Exception e) {
				throw new ServletException();
			}

		} else if (methodName.equals("uncategorisedamounts")) {
			result = accounterReportServiceImpl.getUncategorisedAmountsReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salestaxliability")) {
			result = accounterReportServiceImpl.getSalesTaxLiabilityReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("vat100report")) {
			Long taxAgency = (Long) req.getAttribute("TaxAgency");
			result = accounterReportServiceImpl.getVAT100Report(taxAgency,
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("transactiondetailbyaccount")) {
			result = accounterReportServiceImpl.getTransactionDetailByAccount(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("priorreturnvatsummary")) {
			Long taxAgency = (Long) req.getAttribute("TaxAgency");
			result = accounterReportServiceImpl.getPriorReturnVATSummary(
					taxAgency, clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyitemdetailname")) {
			String itemName = (String) req.getAttribute("Name");
			result = accounterReportServiceImpl.getPurchasesByItemDetail(
					itemName, clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("priorvatreturnreport")) {
			Long taxAgency = (Long) req.getAttribute("TaxAgency");
			result = accounterReportServiceImpl.getPriorVATReturnReport(
					taxAgency, clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyvendordetailbyname")) {
			String vendorName = (String) req.getAttribute("Name");
			result = accounterReportServiceImpl.getPurchasesByVendorDetail(
					vendorName, clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("priorvatreturnvatdetailreport")) {
			result = accounterReportServiceImpl
					.getPriorVATReturnVATDetailReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("salesclosedorderlist")) {
			result = accounterReportServiceImpl.getSalesClosedOrderReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salescancelledorderreport")) {
			result = accounterReportServiceImpl.getSalesCancelledOrderReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salesbyitemdetailbyname")) {
			String itemName = (String) req.getAttribute("Name");
			result = accounterReportServiceImpl.getSalesByItemDetail(itemName,
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salesordelist")) {
			result = accounterReportServiceImpl.getSalesOrderReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salescompletedorder")) {
			result = accounterReportServiceImpl.getSalesCompletedOrderReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("ageddebtorsbydebitorname")) {
			String Name = (String) req.getAttribute("Name");
			result = accounterReportServiceImpl.getAgedDebtors(Name,
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salesopenorder")) {
			result = accounterReportServiceImpl.getSalesOpenOrderReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("agedCreditorsbydebitorname")) {
			String name = (String) req.getAttribute("Name");
			result = accounterReportServiceImpl.getAgedCreditors(name,
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchaseclosedorder")) {
			result = accounterReportServiceImpl.getPurchaseClosedOrderReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("transactiondetailbyaccountname")) {
			String accountName = (String) req.getAttribute("Name");
			result = accounterReportServiceImpl.getTransactionDetailByAccount(
					accountName, clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("minimumandmaximumtransactiondate")) {
			List<ClientFinanceDate> minimumAndMaximumTransactionDate = accounterReportServiceImpl
					.getMinimumAndMaximumTransactionDate();
			sendClentFinanceDateResult(req, resp,
					minimumAndMaximumTransactionDate);

		} else if (methodName.equals("purchaseorder")) {
			result = accounterReportServiceImpl.getPurchaseOrderReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchasecancelledorder")) {
			result = accounterReportServiceImpl
					.getPurchaseCancelledOrderReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("transactiondetailbytaxitemname")) {
			String taxItemName = (String) req.getAttribute("Name");
			result = accounterReportServiceImpl.getTransactionDetailByTaxItem(
					taxItemName, clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("balancesheetreport")) {
			result = accounterReportServiceImpl.getBalanceSheetReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchasecompletedlist")) {
			result = accounterReportServiceImpl
					.getPurchaseCompletedOrderReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("cashflowreport")) {
			result = accounterReportServiceImpl.getCashFlowReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchaseopenorderlist")) {
			result = accounterReportServiceImpl.getPurchaseOpenOrderReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("profitandlossreport")) {
			result = accounterReportServiceImpl.getProfitAndLossReport(
					clientFinanceStartDate, clientFinanceEndDate);
		}
		if (result != null) {
			sendBaseReportResult(req, resp, result);
		}

	}

	private void sendClentFinanceDateResult(HttpServletRequest req,
			HttpServletResponse resp,
			List<ClientFinanceDate> minimumAndMaximumTransactionDate) {
		// TODO Auto-generated method stub

	}

	private void sendIAccountCoreResult(HttpServletRequest req,
			HttpServletResponse resp, List<? extends IAccounterCore> list)
			throws Exception {
		XmlSerializationFactory factory = XmlSerializationFactory.getInstance();
		String string = factory.serializeList(list);
		sendResult(req, resp, string);

	}

	public void sendBaseReportResult(HttpServletRequest req,
			HttpServletResponse resp, List<? extends BaseReport> list) {
		XmlSerializationFactory factory = XmlSerializationFactory.getInstance();
		String string = factory.serializeReportsList(list);
		sendResult(req, resp, string);
	}

	private void sendResult(HttpServletRequest req, HttpServletResponse resp,
			String string) {
		ServletOutputStream outputStream;
		try {
			outputStream = resp.getOutputStream();
			outputStream.write(string.getBytes());
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private AccounterReportServiceImpl getAccounterReportServiceImpl() {
		// TODO
		return null;
	}

	private String getMethodName(HttpServletRequest req) {
		StringBuffer requestURL = req.getRequestURL();
		String url = requestURL.toString();
		String[] urlParts = url.split("/");
		String last = urlParts[urlParts.length - 1];
		return last.split("?")[0];
	}
}
