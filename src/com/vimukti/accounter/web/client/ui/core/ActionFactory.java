package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.banking.AccountRegisterAction;
import com.vimukti.accounter.web.client.ui.banking.BankingHomeAction;
import com.vimukti.accounter.web.client.ui.banking.BuyChecksAndFormsAction;
import com.vimukti.accounter.web.client.ui.banking.ChartsOfAccountsAction;
import com.vimukti.accounter.web.client.ui.banking.CreditCardChargeAction;
import com.vimukti.accounter.web.client.ui.banking.EnterPaymentsAction;
import com.vimukti.accounter.web.client.ui.banking.ImportBankFilesAction;
import com.vimukti.accounter.web.client.ui.banking.MakeDepositAction;
import com.vimukti.accounter.web.client.ui.banking.MatchTrasactionsAction;
import com.vimukti.accounter.web.client.ui.banking.NewBankAccountAction;
import com.vimukti.accounter.web.client.ui.banking.NewReconcileAccountAction;
import com.vimukti.accounter.web.client.ui.banking.PrintChecksAction;
import com.vimukti.accounter.web.client.ui.banking.ReconciliationsListAction;
import com.vimukti.accounter.web.client.ui.banking.ServicesOverviewAction;
import com.vimukti.accounter.web.client.ui.banking.SyncOnlinePayeesAction;
import com.vimukti.accounter.web.client.ui.banking.TransferFundsAction;
import com.vimukti.accounter.web.client.ui.banking.WriteChecksAction;
import com.vimukti.accounter.web.client.ui.combo.NewCurrencyAction;
import com.vimukti.accounter.web.client.ui.company.AccounterClassListAction;
import com.vimukti.accounter.web.client.ui.company.BudgetAction;
import com.vimukti.accounter.web.client.ui.company.ChangePasswordAction;
import com.vimukti.accounter.web.client.ui.company.ChartOfAccountsAction;
import com.vimukti.accounter.web.client.ui.company.CompanyHomeAction;
import com.vimukti.accounter.web.client.ui.company.CountryRegionListAction;
import com.vimukti.accounter.web.client.ui.company.CreditRatingListAction;
import com.vimukti.accounter.web.client.ui.company.CurrencyGroupListAction;
import com.vimukti.accounter.web.client.ui.company.CustomerGroupListAction;
import com.vimukti.accounter.web.client.ui.company.CustomersAction;
import com.vimukti.accounter.web.client.ui.company.DepreciationAction;
import com.vimukti.accounter.web.client.ui.company.EditProfileAction;
import com.vimukti.accounter.web.client.ui.company.FormLayoutsListAction;
import com.vimukti.accounter.web.client.ui.company.IntegrateWithBusinessContactManagerAction;
import com.vimukti.accounter.web.client.ui.company.ItemGroupListAction;
import com.vimukti.accounter.web.client.ui.company.ItemsAction;
import com.vimukti.accounter.web.client.ui.company.JournalEntriesAction;
import com.vimukti.accounter.web.client.ui.company.LocationGroupListAction;
import com.vimukti.accounter.web.client.ui.company.MakeActiveAction;
import com.vimukti.accounter.web.client.ui.company.MakeInActiveAction;
import com.vimukti.accounter.web.client.ui.company.ManageFiscalYearAction;
import com.vimukti.accounter.web.client.ui.company.ManageItemTaxAction;
import com.vimukti.accounter.web.client.ui.company.ManageSalesTaxGroupsAction;
import com.vimukti.accounter.web.client.ui.company.MergeCustomersAction;
import com.vimukti.accounter.web.client.ui.company.MergeFinancialAccountsAction;
import com.vimukti.accounter.web.client.ui.company.MergeItemsAction;
import com.vimukti.accounter.web.client.ui.company.MergeVendorsAction;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.company.NewBudgetAction;
import com.vimukti.accounter.web.client.ui.company.NewCashBasisJournalEntryAction;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.company.NewJournalEntryAction;
import com.vimukti.accounter.web.client.ui.company.NewSalesperSonAction;
import com.vimukti.accounter.web.client.ui.company.NewTAXAgencyAction;
import com.vimukti.accounter.web.client.ui.company.NewbankAction;
import com.vimukti.accounter.web.client.ui.company.PaySalesTaxAction;
import com.vimukti.accounter.web.client.ui.company.PayTypeListAction;
import com.vimukti.accounter.web.client.ui.company.PaymentTermListAction;
import com.vimukti.accounter.web.client.ui.company.PaymentsAction;
import com.vimukti.accounter.web.client.ui.company.PreferencesAction;
import com.vimukti.accounter.web.client.ui.company.PriceLevelListAction;
import com.vimukti.accounter.web.client.ui.company.PurchaseItemsAction;
import com.vimukti.accounter.web.client.ui.company.SalesPersonListsAction;
import com.vimukti.accounter.web.client.ui.company.ShippingMethodListAction;
import com.vimukti.accounter.web.client.ui.company.ShippingTermListAction;
import com.vimukti.accounter.web.client.ui.company.UserDetailsAction;
import com.vimukti.accounter.web.client.ui.company.UsersActivityListAction;
import com.vimukti.accounter.web.client.ui.company.VendorGroupListAction;
import com.vimukti.accounter.web.client.ui.customers.CreateStatementAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerPaymentsAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundsAction;
import com.vimukti.accounter.web.client.ui.customers.CustomersHomeAction;
import com.vimukti.accounter.web.client.ui.customers.InvoiceListViewAction;
import com.vimukti.accounter.web.client.ui.customers.InvoicesAction;
import com.vimukti.accounter.web.client.ui.customers.NewCashSaleAction;
import com.vimukti.accounter.web.client.ui.customers.NewCreditsAndRefundsAction;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerAction;
import com.vimukti.accounter.web.client.ui.customers.NewInvoiceAction;
import com.vimukti.accounter.web.client.ui.customers.NewQuoteAction;
import com.vimukti.accounter.web.client.ui.customers.QuotesAction;
import com.vimukti.accounter.web.client.ui.customers.ReceivePaymentAction;
import com.vimukti.accounter.web.client.ui.customers.ReceivedPaymentsAction;
import com.vimukti.accounter.web.client.ui.customers.RecurringsListAction;
import com.vimukti.accounter.web.client.ui.customers.SalesOrderAction;
import com.vimukti.accounter.web.client.ui.customers.SalesOrderListAction;
import com.vimukti.accounter.web.client.ui.customers.SalesPersonAction;
import com.vimukti.accounter.web.client.ui.customers.TaxDialogAction;
import com.vimukti.accounter.web.client.ui.fixedassets.DisposingRegisteredItemAction;
import com.vimukti.accounter.web.client.ui.fixedassets.HistoryListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.NewFixedAssetAction;
import com.vimukti.accounter.web.client.ui.fixedassets.PendingItemsListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.RegisteredItemsListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.SellingRegisteredItemAction;
import com.vimukti.accounter.web.client.ui.fixedassets.SoldDisposedFixedAssetsListAction;
import com.vimukti.accounter.web.client.ui.reports.APAgingDetailAction;
import com.vimukti.accounter.web.client.ui.reports.APAgingSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.ARAgingDetailAction;
import com.vimukti.accounter.web.client.ui.reports.ARAgingSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.BalanceSheetAction;
import com.vimukti.accounter.web.client.ui.reports.BudgetReportAction;
import com.vimukti.accounter.web.client.ui.reports.CashFlowStatementAction;
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.CustomerTransactionHistoryAction;
import com.vimukti.accounter.web.client.ui.reports.ECSalesListAction;
import com.vimukti.accounter.web.client.ui.reports.ECSalesListDetailAction;
import com.vimukti.accounter.web.client.ui.reports.ExpenseReportAction;
import com.vimukti.accounter.web.client.ui.reports.GLReportAction;
import com.vimukti.accounter.web.client.ui.reports.MISC1099TransactionDetailAction;
import com.vimukti.accounter.web.client.ui.reports.MostProfitableCustomersAction;
import com.vimukti.accounter.web.client.ui.reports.ProfitAndLossAction;
import com.vimukti.accounter.web.client.ui.reports.ProfitAndLossByLocationAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByItemDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByItemSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByVendorDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByVendorSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseClosedOrderAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseOpenOrderAction;
import com.vimukti.accounter.web.client.ui.reports.ReportsHomeAction;
import com.vimukti.accounter.web.client.ui.reports.ReverseChargeListAction;
import com.vimukti.accounter.web.client.ui.reports.ReverseChargeListDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByCustomerDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByCustomerSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByItemDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByItemSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByLocationDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByLocationSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.SalesClosedOrderAction;
import com.vimukti.accounter.web.client.ui.reports.SalesOpenOrderAction;
import com.vimukti.accounter.web.client.ui.reports.SalesTaxLiabilityAction;
import com.vimukti.accounter.web.client.ui.reports.StatementReportAction;
import com.vimukti.accounter.web.client.ui.reports.TAXItemExceptionDetailReport;
import com.vimukti.accounter.web.client.ui.reports.TaxItemDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.TransactionDetailByAccountAction;
import com.vimukti.accounter.web.client.ui.reports.TransactionDetailByTaxItemAction;
import com.vimukti.accounter.web.client.ui.reports.TrialBalanceAction;
import com.vimukti.accounter.web.client.ui.reports.VAT100ReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATDetailsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATItemSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATUncategorisedAmountsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VaTItemDetailAction;
import com.vimukti.accounter.web.client.ui.reports.VatExceptionDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.VendorTransactionHistoryAction;
import com.vimukti.accounter.web.client.ui.settings.AddMeasurementAction;
import com.vimukti.accounter.web.client.ui.settings.AutomaticSequenceAction;
import com.vimukti.accounter.web.client.ui.settings.ConversionBalancesAction;
import com.vimukti.accounter.web.client.ui.settings.ConversionDateAction;
import com.vimukti.accounter.web.client.ui.settings.CopyThemeAction;
import com.vimukti.accounter.web.client.ui.settings.CustomThemeAction;
import com.vimukti.accounter.web.client.ui.settings.DeleteThemeAction;
import com.vimukti.accounter.web.client.ui.settings.GeneralSettingsAction;
import com.vimukti.accounter.web.client.ui.settings.InventoryItemsAction;
import com.vimukti.accounter.web.client.ui.settings.InviteUserAction;
import com.vimukti.accounter.web.client.ui.settings.InvoiceBrandingAction;
import com.vimukti.accounter.web.client.ui.settings.MeasurementListAction;
import com.vimukti.accounter.web.client.ui.settings.NewBrandThemeAction;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentAction;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentsListAction;
import com.vimukti.accounter.web.client.ui.settings.StockSettingsAction;
import com.vimukti.accounter.web.client.ui.settings.UsersAction;
import com.vimukti.accounter.web.client.ui.settings.WareHouseItemsListAction;
import com.vimukti.accounter.web.client.ui.settings.WareHouseTransferAction;
import com.vimukti.accounter.web.client.ui.settings.WareHouseViewAction;
import com.vimukti.accounter.web.client.ui.settings.WarehouseListAction;
import com.vimukti.accounter.web.client.ui.settings.WarehouseTransferListAction;
import com.vimukti.accounter.web.client.ui.vat.AdjustTAXAction;
import com.vimukti.accounter.web.client.ui.vat.FileTAXAction;
import com.vimukti.accounter.web.client.ui.vat.ManageTAXCodesListAction;
import com.vimukti.accounter.web.client.ui.vat.NewTAXCodeAction;
import com.vimukti.accounter.web.client.ui.vat.NewVatItemAction;
import com.vimukti.accounter.web.client.ui.vat.PayTAXAction;
import com.vimukti.accounter.web.client.ui.vat.ReceiveVATAction;
import com.vimukti.accounter.web.client.ui.vat.TaxHistoryAction;
import com.vimukti.accounter.web.client.ui.vat.VatItemListAction;
import com.vimukti.accounter.web.client.ui.vendors.AwaitingAuthorisationAction;
import com.vimukti.accounter.web.client.ui.vendors.BillsAction;
import com.vimukti.accounter.web.client.ui.vendors.CashExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.CreditCardExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.EmployeeExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.EnterBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpenseClaimsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpensesAction;
import com.vimukti.accounter.web.client.ui.vendors.IssuePaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCashPurchaseAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCheckAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCreditMemoAction;
import com.vimukti.accounter.web.client.ui.vendors.NewItemReceiptAction;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;
import com.vimukti.accounter.web.client.ui.vendors.PayBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.Prepare1099MISCAction;
import com.vimukti.accounter.web.client.ui.vendors.PreviousClaimAction;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderAction;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderListAction;
import com.vimukti.accounter.web.client.ui.vendors.RecordExpensesAction;
import com.vimukti.accounter.web.client.ui.vendors.TDSPayAction;
import com.vimukti.accounter.web.client.ui.vendors.TDSVendorsListAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsListAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsHomeAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsListAction;

public class ActionFactory {
	public static AccounterConstants actionsConstants = Accounter.constants();

	private static AccounterConstants messages = actionsConstants;

	public static GeneralSettingsAction getGeneralSettingsAction() {
		return new GeneralSettingsAction(messages.generalSettings());
	}

	public static InventoryItemsAction getInventoryItemsAction() {
		return new InventoryItemsAction(actionsConstants.inventoryItems());
	}

	public static ConversionBalancesAction getConversionBalancesAction() {
		return new ConversionBalancesAction(
				actionsConstants.conversionBalance());
	}

	public static InvoiceBrandingAction getInvoiceBrandingAction() {
		return new InvoiceBrandingAction(actionsConstants.invoiceBranding());
	}

	public static NewBrandThemeAction getNewBrandThemeAction() {
		return new NewBrandThemeAction(messages.newBrandThemeLabel());

	}

	public static ConversionDateAction getConversionDateAction() {
		return new ConversionDateAction(actionsConstants.conversionDate());
	}

	public static AutomaticSequenceAction getAutomaticSequenceAction() {
		return new AutomaticSequenceAction(
				actionsConstants.automaticSequencing());
	}

	public static CustomThemeAction getCustomThemeAction() {
		return new CustomThemeAction(messages.newBrandThemeLabel());
	}

	public static UsersAction getUsersAction() {
		return new UsersAction(messages.users());
	}

	public static InviteUserAction getInviteUserAction() {
		return new InviteUserAction(messages.inviteUser());
	}

	public static DeleteThemeAction getDeleteThemeAction() {
		return new DeleteThemeAction(messages.users());
	}

	public static CopyThemeAction getCopyThemeAction() {
		return new CopyThemeAction(messages.copyTheme());
	}

	public static WareHouseViewAction getWareHouseViewAction() {
		return new WareHouseViewAction(actionsConstants.wareHouse());
	}

	public static WareHouseTransferAction getWareHouseTransferAction() {
		return new WareHouseTransferAction(messages.wareHouseTransfer());
	}

	public static WarehouseListAction getWarehouseListAction() {
		return new WarehouseListAction(messages.warehouseList());
	}

	public static WareHouseItemsListAction getWareHouseItemsListAction(
			long wareHouse) {
		return new WareHouseItemsListAction(wareHouse,
				messages.wareHouseItems());
	}

	public static WarehouseTransferListAction getWarehouseTransferListAction() {
		return new WarehouseTransferListAction(messages.warehouseTransferList());
	}

	// Banking action factory

	public static BankingHomeAction getBankingHomeAction() {
		return new BankingHomeAction(actionsConstants.bankingHome());
	}

	public static NewBankAccountAction getNewBankAccountAction() {
		return new NewBankAccountAction(Accounter.messages().newPayee(
				Global.get().Account()));
	}

	public static AccountRegisterAction getAccountRegisterAction() {
		return new AccountRegisterAction(Accounter.messages().accountRegister(
				Global.get().Account()));

	}

	public static WriteChecksAction getWriteChecksAction() {
		return new WriteChecksAction(actionsConstants.writeCheck());
	}

	public static WriteChecksAction getWriteChecksAction(
			ClientWriteCheck writeCheck,
			AccounterAsyncCallback<Object> callBackObject) {
		return new WriteChecksAction(actionsConstants.writeCheck(), writeCheck,
				callBackObject);
	}

	public static MakeDepositAction getMakeDepositAction() {
		return new MakeDepositAction(actionsConstants.makeDeposit());
	}

	public static MakeDepositAction getMakeDepositAction(
			ClientMakeDeposit makeDeposit,
			AccounterAsyncCallback<Object> callBackObject) {
		return new MakeDepositAction(actionsConstants.makeDeposit(),
				makeDeposit, callBackObject);
	}

	public static TransferFundsAction getTransferFundsAction() {
		return new TransferFundsAction(actionsConstants.transferFunds());
	}

	public static EnterPaymentsAction getEnterPaymentsAction() {
		return new EnterPaymentsAction(actionsConstants.enterPayments());
	}

	public static SyncOnlinePayeesAction getSyncOnlinePayeesAction() {
		return new SyncOnlinePayeesAction(actionsConstants.syncOnlinePayees());
	}

	public static ImportBankFilesAction getImportBankFilesAction() {
		return new ImportBankFilesAction(actionsConstants.importBankFiles());
	}

	public static CreditCardChargeAction getCreditCardChargeAction() {
		return new CreditCardChargeAction(actionsConstants.creditCardCharge());
	}

	public static PrintChecksAction getPrintChecksAction() {
		return new PrintChecksAction(actionsConstants.printChecks());
	}

	public static ChartsOfAccountsAction getChartsOfAccountsAction() {
		return new ChartsOfAccountsAction(Accounter.messages().chartOfAccounts(
				Global.get().Accounts()));
	}

	public static PaymentsAction getPaymentsAction(int category) {
		PaymentsAction action = new PaymentsAction(actionsConstants.payments(),
				category);
		return action;
	}

	public static MatchTrasactionsAction getMatchTrasactionsAction() {
		return new MatchTrasactionsAction(actionsConstants.matchTrasactions());
	}

	// ActionFactory

	public static CompanyHomeAction getCompanyHomeAction() {
		return new CompanyHomeAction(actionsConstants.home());
	}

	public static MakeActiveAction getMakeActiveAction() {
		return new MakeActiveAction(actionsConstants.makeActive());

	}

	public static MakeInActiveAction getMakeInActiveAction() {
		return new MakeInActiveAction(actionsConstants.makeInActive());

	}

	public static PreferencesAction getPreferencesAction() {
		return new PreferencesAction(actionsConstants.companyPreferences());
	}

	public static IntegrateWithBusinessContactManagerAction getIntegrateWithBusinessContactManagerAction() {
		return new IntegrateWithBusinessContactManagerAction(
				actionsConstants.integrateWithBusinessContactManager());
	}

	public static NewJournalEntryAction getNewJournalEntryAction() {
		return new NewJournalEntryAction(actionsConstants.newJournalEntry());
	}

	public static NewCashBasisJournalEntryAction getNewCashBasisJournalEntryAction() {
		return new NewCashBasisJournalEntryAction(
				actionsConstants.newCashBasisJournalEntry());
	}

	public static NewAccountAction getNewAccountAction() {
		return new NewAccountAction(Accounter.messages().newPayee(
				Global.get().Account()));
	}

	public static MergeCustomersAction getMergeCustomersAction() {
		return new MergeCustomersAction(Accounter.messages().mergeCustomers(
				Global.get().Customer()));
	}

	public static MergeVendorsAction getMergeVendorsAction() {
		return new MergeVendorsAction(Accounter.messages().mergeVendors(
				Global.get().vendor()));
	}

	public static MergeItemsAction getMergeItemsAction() {
		return new MergeItemsAction(actionsConstants.mergeItems());
	}

	public static MergeFinancialAccountsAction getMergeFinancialAccountsAction() {
		return new MergeFinancialAccountsAction(Accounter.messages()
				.mergeFinancialAccounts(Global.get().Accounts()));
	}

	public static ManageSalesTaxGroupsAction getManageSalesTaxGroupsAction() {
		String text;
		if (Accounter.getUser().canDoInvoiceTransactions())
			text = actionsConstants.manageSalesGroups();
		else
			text = actionsConstants.salesTaxGroups();
		return new ManageSalesTaxGroupsAction(text);
	}

	// public static ManageSalesTaxCodesAction getManageSalesTaxCodesAction() {
	// String constant = actionsConstants.manageSalesTaxCodes();
	// return new ManageSalesTaxCodesAction(constant);
	// }

	public static ManageItemTaxAction getManageItemTaxAction() {
		return new ManageItemTaxAction(actionsConstants.manageItemTax());
	}

	public static PaySalesTaxAction getPaySalesTaxAction() {
		String constant = null;
		constant = actionsConstants.payTax();
		return new PaySalesTaxAction(constant);
	}

	public static SalesTaxLiabilityAction getViewSalesTaxLiabilityAction() {
		return new SalesTaxLiabilityAction(actionsConstants.salesTaxLiability());
	}

	// public static NewTaxAgencyAction getNewTaxAgencyAction() {
	// return new NewTaxAgencyAction(actionsConstants.newTaxAgency());
	// }

	public static CustomerGroupListAction getCustomerGroupListAction() {
		return new CustomerGroupListAction(Accounter.messages().payeeGroupList(
				Global.get().Customer()));
	}

	public static VendorGroupListAction getVendorGroupListAction() {
		return new VendorGroupListAction(Global.get().messages()
				.payeeGroupList(Global.get().Vendor()));
	}

	public static PaymentTermListAction getPaymentTermListAction() {
		return new PaymentTermListAction(actionsConstants.paymentTermList());
	}

	public static ShippingMethodListAction getShippingMethodListAction() {
		return new ShippingMethodListAction(
				actionsConstants.shippingMethodList());
	}

	public static ShippingTermListAction getShippingTermListAction() {
		return new ShippingTermListAction(actionsConstants.shippingTermList());
	}

	public static PriceLevelListAction getPriceLevelListAction() {
		return new PriceLevelListAction(actionsConstants.priceLevelList());
	}

	public static ItemGroupListAction getItemGroupListAction() {
		return new ItemGroupListAction(actionsConstants.itemGroupList());
	}

	public static CreditRatingListAction getCreditRatingListAction() {
		return new CreditRatingListAction(actionsConstants.creditRatingList());
	}

	public static CountryRegionListAction getCountryRegionListAction() {
		return new CountryRegionListAction(actionsConstants.countryRegionList());
	}

	public static CurrencyGroupListAction getCurrencyGroupListAction() {
		return new CurrencyGroupListAction(actionsConstants.currencyList());
	}

	public static FormLayoutsListAction getFormLayoutsListAction() {
		return new FormLayoutsListAction(actionsConstants.formLayoutsList());
	}

	public static PayTypeListAction getPayTypeListAction() {
		return new PayTypeListAction(actionsConstants.payTypeList());
	}

	public static ManageFiscalYearAction getManageFiscalYearAction() {
		return new ManageFiscalYearAction(actionsConstants.manageFiscalYear());
	}

	public static ChartOfAccountsAction getChartOfAccountsAction() {
		return new ChartOfAccountsAction(Accounter.messages()
				.accounterCategoryList(Global.get().Account()));
	}

	public static ChartOfAccountsAction getChartOfAccountsAction(int accountType) {
		return new ChartOfAccountsAction(Accounter.messages()
				.accounterCategoryList(Global.get().Account()), accountType);
	}

	public static SalesPersonListsAction getSalesPersonListAction() {
		return new SalesPersonListsAction(actionsConstants.salesPersons());
	}

	public static JournalEntriesAction getJournalEntriesAction() {
		return new JournalEntriesAction(actionsConstants.journalEntries());
	}

	public static NewbankAction getNewbankAction() {
		return new NewbankAction(actionsConstants.newBank());
	}

	public static ManageItemTaxAction getNewItemTaxAction() {
		return new ManageItemTaxAction(actionsConstants.newItemTax());
	}

	public static NewItemAction getNewItemAction(boolean forCustomer) {
		return new NewItemAction(actionsConstants.newItem(), forCustomer);
	}

	// public static NewItemAction getNewItemAction(ClientItem item,
	// AccounterAsyncCallback<Object> callback) {
	// return new NewItemAction(actionsConstants.newItem(),
	// }

	public static DepreciationAction getDepriciationAction() {
		return new DepreciationAction(actionsConstants.depreciation());

	}

	public static NewTAXAgencyAction getNewTAXAgencyAction() {
		String flag = actionsConstants.newTAXAgency();

		return new NewTAXAgencyAction(flag);
	}

	public static ManageSalesTaxItemsAction getManageSalesTaxItemsAction() {
		String constant;
		if (Accounter.getUser().canDoInvoiceTransactions())
			constant = actionsConstants.manageSalesItems();
		else
			constant = actionsConstants.salesTaxItems();
		return new ManageSalesTaxItemsAction(constant);
	}

	// public static NewTaxItemAction getTaxItemAction() {
	// return new NewTaxItemAction(FinanceApplication.constants()
	// .newTaxItem());
	// }

	public static AdjustTAXAction getAdjustTaxAction() {
		String flag = actionsConstants.taxAdjustment();
		return new AdjustTAXAction(flag);
	}

	public static ChangePasswordAction getChangePasswordAction() {
		return new ChangePasswordAction(actionsConstants.changePassword());

	}

	public static UserDetailsAction getUserDetailsAction() {
		return new UserDetailsAction(actionsConstants.userDetails());
	}

	// public static ForgetPasswordAction getForgetPasswordAction(){
	// return new ForgetPasswordAction(actionsConstants.forgetPassword());
	//
	// }

	// Customers actions Factory

	public static NewCustomerAction getNewCustomerAction() {
		return new NewCustomerAction(Accounter.messages().newPayee(
				Global.get().Customer()));
	}

	public static NewCustomerAction getNewCustomerAction(String quickAddText) {
		return new NewCustomerAction(Accounter.messages().newPayee(
				Global.get().Customer()), quickAddText);
	}

	public static NewVendorAction getNewVendorAction(String quickAddText) {
		return new NewVendorAction(Accounter.messages().newPayee(
				Global.get().vendor()), quickAddText);
	}

	public static NewCustomerAction getNewCustomerAction(
			ClientCustomer customer,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewCustomerAction(Accounter.messages().newPayee(
				Global.get().Customer()), customer, callBackObject);
	}

	public static CustomersHomeAction getCustomersHomeAction() {
		return new CustomersHomeAction(Accounter.messages().payeesHome(
				Global.get().Customer().trim()));
	}

	public static NewQuoteAction getNewQuoteAction(int type, String title) {
		return new NewQuoteAction(title, type);
	}

	public static NewQuoteAction getNewQuoteAction(ClientEstimate quote,
			AccounterAsyncCallback<Object> callBackObject, int type) {
		return new NewQuoteAction(actionsConstants.newQuote(), quote,
				callBackObject, type);
	}

	// public static NewItemAction getNewItemAction(ClientItem item,
	// AccounterAsyncCallback<Object> callBackObject) {
	// return new NewItemAction(actionsConstants.newItem(),
	// }

	public static TaxDialogAction getTaxAction() {
		return new TaxDialogAction(actionsConstants.tax());
	}

	public static NewInvoiceAction getNewInvoiceAction() {
		return new NewInvoiceAction(actionsConstants.newInvoice());
	}

	public static NewInvoiceAction getNewInvoiceAction(ClientInvoice invoice,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewInvoiceAction(actionsConstants.newInvoice(), invoice,
				callBackObject);
	}

	public static NewCashSaleAction getNewCashSaleAction() {
		return new NewCashSaleAction(actionsConstants.newCashSale());
	}

	public static NewCashSaleAction getNewCashSaleAction(
			ClientCashSales cashSales,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewCashSaleAction(actionsConstants.newCashSale(), cashSales,
				callBackObject);
	}

	public static NewCreditsAndRefundsAction getNewCreditsAndRefundsAction() {
		return new NewCreditsAndRefundsAction(actionsConstants.newCreditNotes());
	}

	public static NewCreditsAndRefundsAction getNewCreditsAndRefundsAction(
			ClientCustomerCreditMemo creditMemo,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewCreditsAndRefundsAction(
				actionsConstants.newCreditsAndRefunds(), creditMemo,
				callBackObject);
	}

	public static ReceivePaymentAction getReceivePaymentAction() {
		return new ReceivePaymentAction(actionsConstants.receivePayment());
	}

	public static ReceivePaymentAction getReceivePaymentAction(
			ClientReceivePayment receivePayment,
			AccounterAsyncCallback<Object> callBackObject) {
		return new ReceivePaymentAction(actionsConstants.receivePayment(),
				receivePayment, callBackObject);
	}

	public static CustomerRefundAction getCustomerRefundAction() {
		return new CustomerRefundAction(Accounter.messages().customerRefund(
				Global.get().Customer()));
	}

	// public static CustomerRefundAction getCustomerRefundAction(
	// ClientCustomerRefund customerRefund,
	// AccounterAsyncCallback<Object> callBackObject) {
	// return new CustomerRefundAction(actionsConstants.customerRefund(),
	// callBackObject);
	// }

	public static CreateStatementAction getCreateStatementAction() {
		return new CreateStatementAction(actionsConstants.createStatement());
	}

	public static CustomersAction getCustomersAction() {
		return new CustomersAction(Accounter.messages().payees(
				Global.get().Customer()));
	}

	public static ItemsAction getItemsAction(boolean customer, boolean vendor) {
		if (customer) {
			return new ItemsAction(actionsConstants.items(), Global.get()
					.customer());
		} else if (vendor) {
			return new ItemsAction(actionsConstants.items(), Global.get()
					.vendor());
		} else
			return new ItemsAction(actionsConstants.items(), Accounter
					.messages().bothCustomerAndVendor(Global.get().Customer(),
							Global.get().Vendor()));
	}

	public static QuotesAction getQuotesAction(String title, int type) {
		return new QuotesAction(title, type);
	}

	public static ReceivedPaymentsAction getReceivedPaymentsAction() {
		return new ReceivedPaymentsAction(actionsConstants.receivedPayments());
	}

	public static InvoicesAction getInvoicesAction(String viewType) {
		return new InvoicesAction(actionsConstants.invoices(), viewType);
	}

	public static CustomerRefundsAction getCustomerRefundsAction() {
		return new CustomerRefundsAction(Accounter.messages().customerRefunds(
				Global.get().Customer()));
	}

	public static SalesOrderAction getSalesOrderAction() {
		return new SalesOrderAction(actionsConstants.newSalesOrder());
	}

	public static SalesOrderListAction getSalesOrderListAction() {
		return new SalesOrderListAction(actionsConstants.salesOrderList());
	}

	public static NewSalesperSonAction getNewSalesperSonAction() {
		return new NewSalesperSonAction(actionsConstants.newSalesPerson());
	}

	public static SalesPersonAction getSalesPersonAction() {
		return new SalesPersonAction(actionsConstants.salesPersons());
	}

	public static CustomerPaymentsAction getNewCustomerPaymentAction() {
		return new CustomerPaymentsAction(Accounter.messages().payeePrePayment(
				Global.get().Customer()));
	}

	public static BrandingThemeComboAction getBrandingThemeComboAction() {
		return new BrandingThemeComboAction(
				actionsConstants.brandingThemeCombo());
	}

	public static EmailViewAction getEmailViewAction() {
		return new EmailViewAction(actionsConstants.email());
	}

	public static InvoiceListViewAction getInvoiceListViewAction() {
		return new InvoiceListViewAction("");
	}

	public static PaymentDialogAction getPaymentDialogAction() {
		return new PaymentDialogAction(actionsConstants.payments());
	}

	// Fixed Assests action factory

	public static NewFixedAssetAction getNewFixedAssetAction() {
		return new NewFixedAssetAction(actionsConstants.newFixedAsset());
	}

	public static SellingRegisteredItemAction getSellingRegisteredItemAction() {
		return new SellingRegisteredItemAction(
				actionsConstants.sellingRegisteredItem());
	}

	public static DisposingRegisteredItemAction getDiposingRegisteredItemAction() {
		return new DisposingRegisteredItemAction(
				actionsConstants.disposingRegisteredItem());
	}

	public static PendingItemsListAction getPendingItemsListAction() {
		return new PendingItemsListAction(actionsConstants.pendingItemsList());

	}

	public static Action getRegisteredItemsListAction() {
		return new RegisteredItemsListAction(
				actionsConstants.registeredItemsList());
	}

	public static Action getSoldDisposedListAction() {
		return new SoldDisposedFixedAssetsListAction(
				actionsConstants.soldDisposedItems());
	}

	public static Action getHistoryListAction() {
		return new HistoryListAction(actionsConstants.historyList());
	}

	// Purchase order action

	// public static PurchaseOrderAction getPurchaseOrderAction() {
	// return new PurchaseOrderAction("Purchase Order",
	// }

	// public static PurchaseOrderListAction getPurchaseOrderListAction() {
	// return new PurchaseOrderListAction("PurchaseOrder List",
	// }

	public static PurchaseOpenOrderAction getPurchaseOpenOrderListAction() {
		return new PurchaseOpenOrderAction(
				actionsConstants.purchaseOrderReport());
	}

	// reports action factory

	public static ReportsHomeAction getReportsHomeAction() {
		return new ReportsHomeAction(actionsConstants.reportsHome());
	}

	public static VendorsListAction getVendorListAction() {
		return new VendorsListAction(Global.get().messages()
				.payeesList(Global.get().Vendor()));
	}

	public static BalanceSheetAction getBalanceSheetAction() {
		return new BalanceSheetAction(actionsConstants.balanceSheet());
	}

	public static CashFlowStatementAction getCashFlowStatementAction() {
		return new CashFlowStatementAction(actionsConstants.cashFlowReport());
	}

	public static TrialBalanceAction getTrialBalanceAction() {
		return new TrialBalanceAction(actionsConstants.trialBalance());
	}

	public static TransactionDetailByAccountAction getTransactionDetailByAccountAction() {
		return new TransactionDetailByAccountAction(Accounter.messages()
				.transactionDetailByAccount(Global.get().Account()));
	}

	public static GLReportAction getGlReportAction() {
		return new GLReportAction(actionsConstants.generalLedgerReport());
	}

	public static SalesTaxLiabilityAction getSalesTaxLiabilityAction() {
		return new SalesTaxLiabilityAction(actionsConstants.salesTaxLiability());
	}

	public static TransactionDetailByTaxItemAction getTransactionDetailByTaxItemAction() {
		return new TransactionDetailByTaxItemAction(
				actionsConstants.transactionDetailByTaxItem());
	}

	// public static YtdProfitComparedToLastYearAction
	// getYtdProfitComparedToLastYearAction() {
	// return new YtdProfitComparedToLastYearAction(
	// }

	public static ARAgingDetailAction getArAgingDetailAction() {
		return new ARAgingDetailAction(actionsConstants.arAgeingDetail());
	}

	public static CustomerTransactionHistoryAction getCustomerTransactionHistoryAction() {
		return new CustomerTransactionHistoryAction(Accounter.messages()
				.payeeTransactionHistory(Global.get().Customer()));
	}

	public static MostProfitableCustomersAction getMostProfitableCustomersAction() {
		return new MostProfitableCustomersAction(Accounter.messages()
				.mostProfitableCustomers(Global.get().customer()));
	}

	public static SalesByCustomerSummaryAction getSalesByCustomerSummaryAction() {
		return new SalesByCustomerSummaryAction(Accounter.messages()
				.salesByCustomerSummary(Global.get().customer()));
	}

	public static SalesByCustomerDetailAction getSalesByCustomerDetailAction() {
		return new SalesByCustomerDetailAction(Accounter.messages()
				.salesByCustomerDetail(Global.get().Customer()));
	}

	public static SalesByItemSummaryAction getSalesByItemSummmaryAction() {
		return new SalesByItemSummaryAction(
				actionsConstants.salesByItemSummary());
	}

	public static SalesByItemDetailAction getSalesByItemDetailAction() {
		return new SalesByItemDetailAction(actionsConstants.salesByItemDetail());
	}

	// public static YtdSalesComparedToLastYearAction
	// getYtdSalesComparedToLastYearAction() {
	// return new YtdSalesComparedToLastYearAction(
	// }

	public static APAgingDetailAction getAorpAgingDetailAction() {
		return new APAgingDetailAction(actionsConstants.apAgeingDetail());
	}

	public static VendorTransactionHistoryAction getVendorTransactionHistoryAction() {
		return new VendorTransactionHistoryAction(Global.get().messages()
				.payeeTransactionHistory(Global.get().Vendor()));
	}

	// public static AmountsDueToVendorsAction getAmountsDueToVendorsAction() {
	// return new AmountsDueToVendorsAction("Amounts Due To"
	// + UIUtils.getVendorString("Suppliers", "Vendors"),
	// }

	public static ProfitAndLossAction getProfitAndLossAction() {
		return new ProfitAndLossAction(actionsConstants.profitAndLoss());
	}

	public static SalesByItemSummaryAction getSalesByItemSummaryAction() {
		return new SalesByItemSummaryAction(
				actionsConstants.salesByItemSummary());
	}

	public static PurchaseByVendorSummaryAction getPurchaseByVendorSummaryAction() {
		return new PurchaseByVendorSummaryAction(Global.get().messages()
				.purchaseByVendorSummary(Global.get().Vendor()));
	}

	public static PurchaseByVendorDetailsAction getPurchaseByVendorDetailAction() {
		return new PurchaseByVendorDetailsAction(Global.get().messages()
				.purchaseByVendorDetail(Global.get().Vendor()));
	}

	public static PurchaseByItemSummaryAction getPurchaseByItemSummaryAction() {
		return new PurchaseByItemSummaryAction(
				actionsConstants.purchaseByItemSummary());
	}

	public static PurchaseByItemDetailsAction getPurchaseByItemAction() {
		return new PurchaseByItemDetailsAction(
				actionsConstants.purchaseByItemDetail());
	}

	public static PurchaseOpenOrderAction getPurchaseOpenOrderAction() {
		return new PurchaseOpenOrderAction(
				actionsConstants.purchaseOrderReport());
	}

	public static PurchaseClosedOrderAction getPurchaseClosedOrderAction() {
		return new PurchaseClosedOrderAction(
				actionsConstants.purchaseClosedOrder());
	}

	public static SalesOpenOrderAction getSalesOpenOrderAction() {
		return new SalesOpenOrderAction(actionsConstants.salesOrderReport());
	}

	public static SalesClosedOrderAction getSalesCloseOrderAction() {
		return new SalesClosedOrderAction(actionsConstants.salesCloseOrder());
	}

	public static VATDetailsReportAction getVATDetailsReportAction() {
		return new VATDetailsReportAction(actionsConstants.vatDetail());
	}

	public static VATSummaryReportAction getVATSummaryReportAction() {
		return new VATSummaryReportAction(actionsConstants.priorVATReturns());
	}

	public static VAT100ReportAction getVAT100ReportAction() {
		return new VAT100ReportAction(actionsConstants.vat100());
	}

	public static VATUncategorisedAmountsReportAction getVATUncategorisedAmountsReportAction() {
		return new VATUncategorisedAmountsReportAction(
				actionsConstants.uncategorisedVATAmounts());
	}

	public static VATItemSummaryReportAction getVATItemSummaryReportAction() {
		return new VATItemSummaryReportAction(actionsConstants.vatItemSummary());
	}

	public static ECSalesListAction getECSalesListAction() {
		return new ECSalesListAction(actionsConstants.ecSalesList());
	}

	public static ECSalesListDetailAction getECSalesListDetailAction() {
		return new ECSalesListDetailAction(
				actionsConstants.ecSalesListDetailReport());
	}

	public static ReverseChargeListAction getReverseChargeListAction() {
		return new ReverseChargeListAction(actionsConstants.reverseChargeList());
	}

	public static ReverseChargeListDetailAction getReverseChargeListDetailAction() {
		return new ReverseChargeListDetailAction(
				actionsConstants.reverseChargeListDetailReport());
	}

	public static VaTItemDetailAction getVaTItemDetailAction() {
		return new VaTItemDetailAction(actionsConstants.vatItemDetailReport());
	}

	public static ARAgingSummaryReportAction getArAgingSummaryReportAction() {
		return new ARAgingSummaryReportAction(
				actionsConstants.arAgeingSummary());
	}

	public static Action getAorpAgingSummaryReportAction() {
		return new APAgingSummaryReportAction(
				actionsConstants.apAgeingSummary());
	}

	public static ExpenseReportAction getExpenseReportAction() {
		return new ExpenseReportAction(actionsConstants.expenseReport());
	}

	public static DepositDetailAction getDetailReportAction() {
		return new DepositDetailAction(actionsConstants.depositDetail());
	}

	public static CheckDetailReportAction getCheckDetailReport() {
		return new CheckDetailReportAction(actionsConstants.checkDetail(), "");
	}

	public static Action getStatementReport() {
		return new StatementReportAction(Accounter.messages().payeeStatement(
				Global.get().Customer()));
	}

	// sales order action factory

	// public static SalesOrderAction getSalesOrderAction() {
	// return new SalesOrderAction("Sales Order",
	// "/images/Sales-order.png");
	// }

	// public static SalesOrderListAction getSalesOrderListAction() {
	// return new SalesOrderListAction("SalesOrder List",
	// "/images/Sales-order-list.png");
	// }

	// public static SalesOpenOrderAction getSalesOpenOrderAction() {
	// return new SalesOpenOrderAction("SalesOrder Report",
	// "/images/icons/report/reports.png");
	// }

	// vat action factory

	public static NewVatItemAction getNewVatItemAction() {
		String flag = actionsConstants.newTaxItem();
		return new NewVatItemAction(flag);
	}

	public static FileTAXAction getFileTAXAction() {
		return new FileTAXAction(actionsConstants.fileTAX());
	}

	public static VatItemListAction getVatItemListAction() {
		return new VatItemListAction(actionsConstants.taxItemsList());
	}

	// public static VatGroupAction getVatGroupAction() {
	// return new VatGroupAction(actionsConstants.newVATGroup());
	// }

	public static Action getTAXCodeListAction() {
		return new ManageTAXCodesListAction(actionsConstants.taxCodesList());
	}

	public static NewTAXCodeAction getNewTAXCodeAction() {
		return new NewTAXCodeAction(actionsConstants.newTaxCode());
	}

	// public static Action getManageVATCodeAction() {
	// return new ManageVATCodeAction("Manage VAT");
	// }

	// public static NewTAXAgencyAction getNewTAXAgencyAction() {
	// return new NewTAXAgencyAction(actionsConstants.newVATAgency());
	// }

	// public static ManageVATGroupListAction getManageVATGroupListAction() {
	// return new ManageVATGroupListAction(actionsConstants.vatGroupList());
	// }

	public static AdjustTAXAction getVatAdjustmentAction() {
		return new AdjustTAXAction(actionsConstants.vatAdjustment());

	}

	public static PayTAXAction getpayTAXAction() {
		return new PayTAXAction(actionsConstants.payTax());

	}

	public static Action getreceiveVATAction() {
		return new ReceiveVATAction(actionsConstants.tAXRefund());
	}

	public static VendorsHomeAction getVendorsHomeAction() {
		return new VendorsHomeAction(Global.get().messages()
				.payeesHome(Global.get().Vendor().trim()));
	}

	public static NewVendorAction getNewVendorAction() {
		return new NewVendorAction(Global.get().messages()
				.newPayee(Global.get().Vendor()));
	}

	public static NewVendorAction getNewVendorAction(ClientVendor vendor,
			AccounterAsyncCallback<Object> callback) {
		return new NewVendorAction(Global.get().messages()
				.newPayee(Global.get().Vendor()));
	}

	public static PurchaseItemsAction getPurchaseItemsAction() {
		return new PurchaseItemsAction(actionsConstants.items(), Global.get()
				.Vendor());
	}

	// public static NewItemAction getNewItemAction() {
	// return new NewItemAction(actionsConstants.newItem(),
	// "/images/icons/vendors/new_item.png", null);
	// }
	//
	// public static NewItemAction getNewItemAction(Item item,
	// AccounterAsyncCallback<Object> callback, AbstractBaseView view) {
	// return new NewItemAction(actionsConstants.newItem(),
	// "/images/icons/vendors/new_item.png", view, item, callback);
	// }

	public static NewCashPurchaseAction getNewCashPurchaseAction() {
		return new NewCashPurchaseAction(actionsConstants.newCashPurchase());
	}

	public static NewCashPurchaseAction getNewCashPurchaseAction(
			ClientCashPurchase cashPurchase,
			AccounterAsyncCallback<Object> callback) {
		return new NewCashPurchaseAction(actionsConstants.newCashPurchase(),
				cashPurchase, callback);
	}

	public static NewCreditMemoAction getNewCreditMemoAction() {
		return new NewCreditMemoAction(Global.get().messages()
				.payeeCredit(Global.get().Vendor()));
	}

	public static NewCreditMemoAction getNewCreditMemoAction(
			ClientVendorCreditMemo vendorCreditMemo,
			AccounterAsyncCallback<Object> callBack) {
		return new NewCreditMemoAction(actionsConstants.newCreditMemo(),
				vendorCreditMemo, callBack);
	}

	public static NewCheckAction getNewCheckAction() {
		return new NewCheckAction(actionsConstants.newCheck());
	}

	public static EnterBillsAction getEnterBillsAction() {
		return new EnterBillsAction(actionsConstants.enterBill());
	}

	public static PayBillsAction getPayBillsAction() {
		return new PayBillsAction(actionsConstants.payBill());
	}

	public static IssuePaymentsAction getIssuePaymentsAction() {
		return new IssuePaymentsAction(actionsConstants.issuePayment());
	}

	public static VendorPaymentsAction getNewVendorPaymentAction() {
		return new VendorPaymentsAction(Global.get().messages()
				.payeePrePayment(Global.get().Vendor()));
	}

	public static RecordExpensesAction getRecordExpensesAction() {
		return new RecordExpensesAction(actionsConstants.recordExpenses());
	}

	public static ServicesOverviewAction getServicesOverviewAction() {
		return new ServicesOverviewAction(actionsConstants.servicesOverview());
	}

	public static BuyChecksAndFormsAction getBuyChecksAndFormsAction() {
		return new BuyChecksAndFormsAction(
				actionsConstants.buyChecksAndForms(), "");
	}

	public static VendorsListAction getVendorsAction() {
		return new VendorsListAction(Global.get().messages()
				.payees(Global.get().Vendor()));
	}

	// public static Item getItemAction() {
	// return new ItemListAction(actionsConstants.items());
	// }

	public static BillsAction getBillsAction() {
		return new BillsAction(actionsConstants.billsAndItemReceipts());
	}

	public static ExpensesAction getExpensesAction(String viewType) {
		return new ExpensesAction(actionsConstants.recordExpenses(), viewType);
	}

	public static VendorPaymentsListAction getVendorPaymentsAction() {

		return new VendorPaymentsListAction(Global.get().messages()
				.payeePayment(Global.get().Vendor()));
	}

	public static PurchaseOrderAction getPurchaseOrderAction() {
		return new PurchaseOrderAction(actionsConstants.purchaseOrder());
	}

	public static PurchaseOrderListAction getPurchaseOrderListAction() {
		return new PurchaseOrderListAction(actionsConstants.purchaseOrderList());
	}

	public static NewItemReceiptAction getItemReceiptAction() {
		return new NewItemReceiptAction(actionsConstants.itemReceipt());
	}

	public static CashExpenseAction CashExpenseAction() {
		return new CashExpenseAction(actionsConstants.cashExpense());
	}

	public static EmployeeExpenseAction EmployeeExpenseAction() {
		return new EmployeeExpenseAction(actionsConstants.employeeExpense());
	}

	public static CreditCardExpenseAction CreditCardExpenseAction() {
		return new CreditCardExpenseAction(actionsConstants.creditCardCharge());
	}

	public static AwaitingAuthorisationAction getAwaitingAuthorisationAction() {
		return new AwaitingAuthorisationAction(
				actionsConstants.awaitingAuthorisation());

	}

	public static PreviousClaimAction getPreviousClaimAction() {
		return new PreviousClaimAction(actionsConstants.previousClaims());

	}

	public static ExpenseClaimsAction getExpenseClaimsAction(int selectedTab) {
		return new ExpenseClaimsAction(actionsConstants.expenseClaims(),
				selectedTab);
	}

	public static MeasurementListAction getMeasurementsAction() {
		return new MeasurementListAction(messages.measurement());
	}

	public static AddMeasurementAction getAddMeasurementAction() {
		return new AddMeasurementAction(messages.addMeasurementName());
	}

	public static NewCurrencyAction getNewCurrencyAction() {
		return new NewCurrencyAction(actionsConstants.newCurrency());
	}

	public static Action getEditProfileAction() {
		return new EditProfileAction(actionsConstants.editProfile());
	}

	public static Action getRecurringsListAction() {
		return new RecurringsListAction("Recurrings List action");
	}

	public static Prepare1099MISCAction getPrepare1099MISCAction() {
		return new Prepare1099MISCAction(
				actionsConstants.prepare1099MiscForms());
	}

	public static SalesByLocationDetailsAction getSalesByLocationDetailsAction(
			boolean isLocation) {
		String actionsting = Accounter.messages().getSalesByLocationDetails(
				Global.get().Location());
		if (!isLocation) {
			actionsting = Accounter.constants().salesByClassDetails();
		}
		return new SalesByLocationDetailsAction(actionsting, isLocation);
	}

	public static SalesByLocationSummaryAction getSalesByLocationSummaryAction(
			boolean isLocation) {
		String actionsting = Accounter.messages().salesByLocationSummary(
				Global.get().Location());
		if (!isLocation) {
			actionsting = Accounter.constants().salesByClassSummary();
		}
		return new SalesByLocationSummaryAction(actionsting, isLocation);
	}

	public static ProfitAndLossByLocationAction getProfitAndLossByLocationAction(
			boolean isLocation) {
		String actionstring = actionsConstants.profitAndLoss() + " By "
				+ Accounter.messages().location(Global.get().Location());
		if (!isLocation) {
			actionstring = actionsConstants.profitAndLossbyClass();
		}
		return new ProfitAndLossByLocationAction(actionstring, isLocation);
	}

	public static BudgetAction getBudgetActions() {
		return new BudgetAction(actionsConstants.budgetView());
	}

	public static NewBudgetAction getNewBudgetAction() {
		return new NewBudgetAction(Accounter.constants().newBudget());
	}

	public static UsersActivityListAction getUsersActivityListAction() {
		return new UsersActivityListAction(
				actionsConstants.usersActivityLogTitle());
	}

	public static TDSVendorsListAction getTDSVendorsAction(boolean isTDSView) {
		return new TDSVendorsListAction(actionsConstants.tdsVendorsList(),
				isTDSView);
	}

	public static NewReconcileAccountAction getNewReconciliationAction() {
		return new NewReconcileAccountAction(Accounter.constants()
				.Reconciliation());
	}

	public static ReconciliationsListAction getReconciliationsListAction() {
		return new ReconciliationsListAction(
				actionsConstants.ReconciliationsList());
	}

	public static LocationGroupListAction getLocationGroupListAction() {
		return new LocationGroupListAction(actionsConstants.locationGroupList());
	}

	public static AccounterClassListAction getAccounterClassGroupListAction() {
		return new AccounterClassListAction(
				actionsConstants.accounterClassList());
	}

	public static TDSPayAction getpayTDSAction() {
		return new TDSPayAction(actionsConstants.payTDS());
	}

	public static MISC1099TransactionDetailAction getMisc1099TransactionDetailAction() {
		return new MISC1099TransactionDetailAction(Accounter.messages()
				.MISC1099TransactionDetailByVendor(Global.get().Vendor()));
	}

	public static StockSettingsAction getStockSettingsAction() {
		return new StockSettingsAction(actionsConstants.stockSettings());
	}

	public static StockAdjustmentAction getStockAdjustmentAction() {
		return new StockAdjustmentAction(actionsConstants.stockAdjustment());
	}

	public static StockAdjustmentsListAction getStockAdjustmentsListAction() {
		return new StockAdjustmentsListAction(
				actionsConstants.stockAdjustments());
	}

	public static BudgetReportAction getBudgetReportsAction(int i) {
		switch (i) {
		case 1:
			return new BudgetReportAction(actionsConstants.accountVScustom(), i);
		case 2:
			return new BudgetReportAction(actionsConstants.accountVSmonths(), i);
		case 3:
			return new BudgetReportAction(actionsConstants.accountVSquaters(),
					i);
		case 4:
			return new BudgetReportAction(actionsConstants.accountVSyears(), i);
		default:
			return new BudgetReportAction(actionsConstants.accountVScustom(), i);
		}

	}

	public static TaxHistoryAction getTaxHistoryAction() {
		return new TaxHistoryAction(actionsConstants.taxHistory());
	}

	public static VatExceptionDetailReportAction getVATExceptionDetailsReportAction() {
		return new VatExceptionDetailReportAction(
				actionsConstants.vatExceptionDetail());
	}

	public static TaxItemDetailReportAction getTaxItemDetailReportAction() {
		return new TaxItemDetailReportAction(
				actionsConstants.taxItemDetailReport());
	}

	public static TAXItemExceptionDetailReport getTaxItemExceptionDetailReportAction() {
		return new TAXItemExceptionDetailReport(
				actionsConstants.taxItemExceptionDetailReport());
	}
}
