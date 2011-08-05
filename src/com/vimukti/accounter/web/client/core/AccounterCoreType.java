/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Fernandez
 * 
 */
public enum AccounterCoreType implements IsSerializable {

	ACCOUNT("ClientAccount", "Account"),

	VENDOR("ClientVendor", "Vendor"),

	TAXAGENCY("ClientTAXAgency", "TAXAgency"),

	CUSTOMER("ClientCustomer", "Customer"),

	ITEM("ClientItem", "Item"),

	TAX_GROUP("ClientTAXGroup", "TAXGroup"),

	TAX_ITEM_GROUP("ClientTaxItemGroup", "TaxItemGroup"),

	BOX_1099("ClientBox1099", "Box1099"),

	TAX_CODE("ClientTAXCode", "TAXCode"),

	TAXITEM("ClientTAXItem", "TAXItem"),

	ITEM_TAX("ClientItemTax", "ItemTax"),

	CUSTOMER_GROUP("ClientCustomerGroup", "CustomerGroup"),

	VENDOR_GROUP("ClientVendorGroup", "VendorGroup"),

	PAYMENT_TERM("ClientPaymentTerms", "PaymentTerms"),

	SHIPPING_METHOD("ClientShippingMethod", "ShippingMethod"),

	SHIPPING_TERM("ClientShippingTerms", "ShippingTerms"),

	PRICE_LEVEL("ClientPriceLevel", "PriceLevel"),

	ITEM_GROUP("ClientItemGroup", "ItemGroup"),

	SALES_PERSON("ClientSalesPerson", "SalesPerson"),

	PAYEE("ClientPayee", "Payee"),

	CREDIT_RATING("ClientCreditRating", "CreditRating"),

	PAY_SALES_TAX("ClientPaySalesTax", "PaySalesTax"),

	PAYMENT_METHOD, COMPANY("ClientCompany", "Company"),

	TRANSACTION("ClientTransaction", "Transaction"),

	ADDRESS("ClientAddress", "Address"),

	BANK("ClientBank", "Bank"),

	COMPANY_PREFERENCES("ClientCompanyPreferences", "CompanyPreferences"),

	CONTACT("ClientContact", "Contact"),

	CREDITS_AND_PAYMENTS("ClientCreditsAndPayments", "CreditsAndPayments"),

	CURRENCY("ClientCurrency", "Currency"),

	EMAIL("ClientEmail", "Email"),

	ENTRY("ClientEntry", "Entry"),

	FAX("ClientFax", "Fax"),

	PAYSALESTAX_ENTRIES("ClientPaySalesTaxEntries", "PaySalesTaxEntries"),

	PHONE("ClientPhone", "Phone"),

	TAXRATES("ClientTaxRates", "TaxRates"),

	UNIT_OF_MEASURE("ClientUnitOfMeasure", "UnitOfMeasure"),

	USER_PREFERENCES("ClientUserPreferences", "UserPreferences"),

	JOURNALENTRY("ClientJournalEntry", "JournalEntry"),

	ESTIMATE("ClientEstimate", "Estimate"),

	TRANSACTION_MAKEDEPOSIT("ClientTransactionMakeDeposit",
			"TransactionMakeDeposit"),

	INVOICE("ClientInvoice", "Invoice"),

	CASHPURCHASE("ClientCashPurchase", "CashPurchase"),

	CASHSALES("ClientCashSales", "CashSales"),

	CUSTOMERCREDITMEMO("ClientCustomerCreditMemo", "CustomerCreditMemo"),

	CUSTOMERPREPAYMENT("ClientCustomerPrePayment", "CustomerPrePayment"),

	ENTERBILL("ClientEnterBill", "EnterBill"),

	ISSUEPAYMENT("ClientIssuePayment", "IssuePayment"),

	PAYBILL("ClientPayBill", "PayBill"),

	PAYSALESTAX("ClientPaySalesTax", "PaySalesTax"),

	PURCHASEORDER("ClientPurchaseOrder", "PurchaseOrder"),

	RECEIVEPAYMENT("ClientReceivePayment", "ReceivePayment"),

	SALESORDER("ClientSalesOrder", "SalesOrder"),

	TRANSFERFUND("ClientTransferFund", "TransferFund"),

	VENDORCREDITMEMO("ClientVendorCreditMemo", "VendorCreditMemo"),

	WRITECHECK("ClientWriteCheck", "WriteCheck"),

	MAKEDEPOSIT("ClientMakeDeposit", "MakeDeposit"),

	CREDITCARDCHARGE("ClientCreditCardCharge", "CreditCardCharge"),

	USER("ClientUser", "User"),

	FISCALYEAR("ClientFiscalYear", "FiscalYear"),

	COMMODITYCODE("ClientCommodityCode", "CommodityCode"),

	TRANSACTIONMAKEDEPOSITENTRIES("ClientTransactionMakeDepositEntries",
			"TransactionMakeDepositEntries"),

	ACCOUNTTRANSACTION("ClientAccountTransaction", "AccountTransaction"),

	VATRETURN("ClientVATReturn", "VATReturn"),

	BUDGET("ClientBudget", "Budget"),

	ITEMRECEIPT("ClientItemReceipt", "ItemReceipt"),

	EXPENSE("ClientExpense", "Expense"),

	PAYEXPENSE("ClientPayExpense", "PayExpense"),

	FIXEDASSETNOTE("ClientFixedAssetNote", "FixedAssetNote"),

	FIXEDASSETHISTORY("ClientFixedAssetHistory", "FixedAssetHistory"),

	CUSTOMERREFUND("ClientCustomerRefund", "CustomerRefund"), FIXEDASSET(
			"ClientFixedAsset", "FixedAsset"),

	VATBOX("ClientBox", "Box"),

	PAYVAT("ClientPayVAT", "PayVAT"), ITEMBACKUP("ClientItemBackUp",
			"ItemBackUp"),

	TRANSACTION_CREDITS_AND_PAYMENTS("ClientTransactionCreditsAndPayments",
			"TransactionCreditsAndPayments"),

	TRANSACTION_RECEIVEPAYMENT("ClientTransactionReceivePayment",
			"TransactionReceivePayment"),

	TRANSACTION_PAYBILL("ClientTransactionPayBill", "TransactionPayBill"),

	VATRETURNBOX("ClientVATReturnBox", "VATReturnBox"),

	DEPRECIATION("ClientDepreciation", "Depreciation"),

	DELETED, ERROR,

	TAXRATECALCULATION("ClientTAXRateCalculation", "TAXRateCalculation"),

	FINANCELOG("ClientFinanceLogger", "FinanceLogger"),

	RECEIVEVAT("ClientReceiveVAT", "ReceiveVAT"),

	TAXADJUSTMENT("ClientTAXAdjustment", "TAXAdjustment"),

	BRANDINGTHEME("ClientBrandingTheme", "BrandingTheme"),

	EMPLOYEE("ClientEmployee", "Employee"),

	WAREHOUSE("ClientWarehouse", "Warehouse"),

	MEASUREMENT("ClientMeasurement", "Measurement"),

	STOCK_TRANSFER("ClientStockTransfer", "StockTransfer");

	private String clientName;
	private String serverName;

	AccounterCoreType() {

	}

	AccounterCoreType(String clientSimpleName, String serverSimpleName) {

		this.clientName = clientSimpleName;
		this.serverName = serverSimpleName;
	}

	public static AccounterCoreType getObject(String value) {
		String upperCaseString = value.toUpperCase();
		AccounterCoreType type = AccounterCoreType.valueOf(upperCaseString);

		if (type == null) {
			if (upperCaseString.equals("TAXGROUP")) {
				return TAX_GROUP;
			} else if (upperCaseString.equals("TAXITEMGROUP")) {
				return TAX_ITEM_GROUP;
			} else if (upperCaseString.equals("BOX1099")) {
				return BOX_1099;
			} else if (upperCaseString.equals("TAXCODE")) {
				return TAX_CODE;
			} else if (upperCaseString.equals("ITEMTAX")) {
				return ITEM_TAX;
			} else if (upperCaseString.equals("CUSTOMERGROUP")) {
				return CUSTOMER_GROUP;
			} else if (upperCaseString.equals("VENDORGROUP")) {
				return VENDOR_GROUP;
			} else if (upperCaseString.equals("PAYMENTTERM")) {
				return PAYMENT_TERM;
			} else if (upperCaseString.equals("SHIPPINGMETHOD")) {
				return SHIPPING_METHOD;
			} else if (upperCaseString.equals("SHIPPINGTERM")) {
				return SHIPPING_TERM;
			} else if (upperCaseString.equals("PRICELEVEL")) {
				return PRICE_LEVEL;
			} else if (upperCaseString.equals("ITEMGROUP")) {
				return ITEM_GROUP;
			} else if (upperCaseString.equals("SALESPERSON")) {
				return SALES_PERSON;
			} else if (upperCaseString.equals("CREDITRATING")) {
				return CREDIT_RATING;
			} else if (upperCaseString.equals("PAYSALESTAX")) {
				return PAY_SALES_TAX;
			} else if (upperCaseString.equals("PAYMENTMETHOD")) {
				return PAYMENT_METHOD;
			} else if (upperCaseString.equals("COMPANYPREFERENCES")) {
				return COMPANY_PREFERENCES;
			} else if (upperCaseString.equals("CREDITSANDPAYMENTS")) {
				return CREDITS_AND_PAYMENTS;
			} else if (upperCaseString.equals("PAYSALESTAXENTRIES")) {
				return PAYSALESTAX_ENTRIES;
			} else if (upperCaseString.equals("UNITOFMEASURE")) {
				return UNIT_OF_MEASURE;
			} else if (upperCaseString.equals("USERPREFERENCES")) {
				return USER_PREFERENCES;
			} else if (upperCaseString.equals("TRANSACTIONMAKEDEPOSIT")) {
				return TRANSACTION_MAKEDEPOSIT;
			} else if (upperCaseString.equals("TRANSACTIONCREDITSANDPAYMENTS")) {
				return TRANSACTION_CREDITS_AND_PAYMENTS;
			} else if (upperCaseString.equals("TRANSACTIONRECEIVEPAYMENT")) {
				return TRANSACTION_RECEIVEPAYMENT;
			} else if (upperCaseString.equals("TRANSACTIONPAYBILL")) {
				return TRANSACTION_PAYBILL;
			}
		}

		return type;
	}

	public String getClientClassSimpleName() {

		return this.clientName;
	}

	public String getServerClassSimpleName() {

		return this.serverName;
	}

	public String getServerClassFullyQualifiedName() {

		return "com.vimukti.accounter.core." + this.serverName;
	}

}