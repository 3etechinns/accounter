package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.TransferFundsDialog;
import com.vimukti.accounter.web.client.ui.customers.CustomerView;
import com.vimukti.accounter.web.client.ui.customers.ReceivePaymentView;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;

/**
 * AccounterValidator is class used to validate all forms, fields & display
 * Errors& warnings, contains static methods to validate particular field or
 * form, any validation in accounter
 * 
 * @author kumar kasimala
 * 
 */
public class AccounterValidator {

	private static ClientCompany company;

	public static boolean validateAmount(Double amt) {
		if (DecimalUtil.isLessThan(amt, 0.00)
				|| DecimalUtil.isEquals(amt, 0.00)) {
			// throw new InvalidEntryException(AccounterErrorType.amount);
			return false;
		}
		return true;
	}

	public static boolean validateNagtiveAmount(Double amt) {
		if (DecimalUtil.isLessThan(amt, 0.00)) {
			return false;
		}
		return true;

	}

	// Account,Customer,VendorCreation:
	/**
	 * @param date
	 *            for Account AsOf , for Customer CustomerSince , for Vendor
	 *            VendorSince
	 * 
	 */
	public static void validateDateWithClosedFiscalYears(
			ClientFinanceDate asOfDate, List<ClientFiscalYear> fiscalYears) {
		for (ClientFiscalYear fiscalYear : fiscalYears) {
			if (fiscalYear.getStatus() == ClientFiscalYear.STATUS_CLOSE) {

			}
		}

	}

	// creating necessary fiscalYears

	// public static boolean createNecessaryFiscalYears(
	// final ClientFiscalYear fiscalYear1,
	// final ClientFinanceDate asOfDate, final AbstractBaseView view) {
	// List<ClientFiscalYear> openFiscalYears = getOpenFiscalYears();
	// ClientFiscalYear firstOPenFiscalYear = openFiscalYears.get(0);
	// ClientFiscalYear lastOPenFiscalYear = openFiscalYears
	// .get(openFiscalYears.size() - 1);
	// if (asOfDate.before(firstOPenFiscalYear.getStartDate())) {
	// return createFiscalYears(view, asOfDate);
	// } else if (asOfDate.after(lastOPenFiscalYear.getEndDate())) {
	// return createFiscalYears(view, asOfDate);
	// } else {
	// return true;
	// }
	// }

	public static boolean validateClosedFiscalYear(ClientFinanceDate asofDate) {
		List<ClientFiscalYear> closedFiscalYears = getClosedFiscalYears();
		for (ClientFiscalYear fiscalYear : closedFiscalYears) {
			if ((fiscalYear.getStartDate().getYear() + 1900) == (asofDate
					.getYear() + 1900)) {
				Accounter.showError(AccounterWarningType.CLOSED_FISCALYEAR);
				return false;
			}

		}
		return true;
	}

	private static List<ClientFiscalYear> getClosedFiscalYears() {
		List<ClientFiscalYear> fiscalyearlist = getCompany().getFiscalYears();
		List<ClientFiscalYear> closedFiscalYears = new ArrayList<ClientFiscalYear>();
		for (ClientFiscalYear fiscalyear : fiscalyearlist) {
			if (fiscalyear.getStatus() == ClientFiscalYear.STATUS_CLOSE) {
				closedFiscalYears.add(fiscalyear);
			}
		}
		return closedFiscalYears;
	}

	public static boolean isFixedAssetPurchaseDateWithinRange(
			ClientFinanceDate purchaseDate) {

		List<ClientFiscalYear> fiscalYears = getCompany().getFiscalYears();
		for (ClientFiscalYear firstFiscalYear : fiscalYears) {
			if (firstFiscalYear.getStatus() == ClientFiscalYear.STATUS_OPEN) {
				if (purchaseDate.after(firstFiscalYear.getStartDate())) {
					return true;
				}
			}
		}
		return false;

	}

	// public static boolean createFiscalYears(final AbstractBaseView view,
	// final ClientFinanceDate asofDate) {
	// Accounter.showWarning(AccounterWarningType.Create_FiscalYear,
	// AccounterType.WARNING, new ErrorDialogHandler() {
	//
	// @Override
	// public boolean onCancelClick() throws InvalidEntryException {
	// return false;
	// }
	//
	// @Override
	// public boolean onNoClick() throws InvalidEntryException {
	// // Accounter.stopExecution();
	// return true;
	//
	// }
	//
	// @Override
	// public boolean onYesClick() throws InvalidEntryException {
	// long convertedasOfDate = asofDate.getDate();
	// Accounter.createHomeService()
	// .changeFiscalYearsStartDateTo(
	// convertedasOfDate,
	// new AccounterAsyncCallback<Boolean>() {
	//
	// public void onException(
	// AccounterException caught) {
	// Accounter
	// .showError(Accounter
	// .constants()
	// .failedtoalterthefiscalyear());
	// }
	//
	// @SuppressWarnings("null")
	// public void onSuccess(Boolean result) {
	//
	// if (result != null || !result) {
	// Accounter
	// .showInformation(Accounter
	// .constants()
	// .fiscalYearcreated());
	// view.validationCount--;
	//
	// }
	//
	// }
	//
	// });
	// return true;
	//
	// }
	//
	// });
	// AbstractBaseView.warnOccured = true;
	// return false;
	// }

	/**
	 * validations for all Transactions. The Transaction date should be with in
	 * the open fiscal year range.
	 * 
	 * @param transactionDate
	 * @return true
	 * @throws InvalidTransactionEntryException
	 */
	public static boolean validateTransactionDate(
			ClientFinanceDate transactionDate) {
		boolean validDate = false;
		List<ClientFiscalYear> openFiscalYears = getOpenFiscalYears();
		for (ClientFiscalYear openFiscalYear : openFiscalYears) {

			int before = transactionDate.compareTo(openFiscalYear
					.getStartDate());
			int after = transactionDate.compareTo(openFiscalYear.getEndDate());

			validDate = (before < 0 || after > 0) ? false : true;
			if (validDate)
				break;

		}

		return validDate;
	}

	public static boolean isInPreventPostingBeforeDate(
			ClientFinanceDate transactionDate) {
		return transactionDate.before(new ClientFinanceDate(Accounter
				.getCompany().getPreferences().getPreventPostingBeforeDate()));
	}

	public static List<ClientFiscalYear> getOpenFiscalYears() {
		List<ClientFiscalYear> fiscalYears = getCompany().getFiscalYears();

		List<ClientFiscalYear> openFiscalYears = new ArrayList<ClientFiscalYear>();
		for (ClientFiscalYear clientFiscalYear : fiscalYears) {
			if (clientFiscalYear.getStatus() == ClientFiscalYear.STATUS_OPEN)
				openFiscalYears.add(clientFiscalYear);

		}
		return openFiscalYears;
	}

	// /**
	// * checks whether the transaction grid is empty or not.
	// *
	// * @param transactionGrid
	// * @return
	// * @throws InvalidTransactionEntryException
	// */
	// public static boolean isBlankTransaction(FinanceGrid transactionGrid)
	// throws InvalidTransactionEntryException {
	// if (transactionGrid != null && transactionGrid.getRecords().size() == 0)
	// {
	// throw new InvalidTransactionEntryException(
	// AccounterErrorType.blankTransaction);
	// }
	// return true;
	//
	// }

	// public static boolean blankTransaction(FinanceGrid transactionGrid)
	// throws InvalidTransactionEntryException {
	// if (transactionGrid != null && transactionGrid.getRecords().size() == 0)
	// {
	// throw new InvalidTransactionEntryException(
	// AccounterErrorType.blankTransaction);
	// }
	// return true;
	//
	// }

	// this is to save or close the current view from viewManager.

	public static void saveOrClose(final AbstractBaseView view,
			final ViewManager viewManager) {
		Accounter.showWarning(AccounterWarningType.saveOrClose,
				AccounterType.WARNINGWITHCANCEL, new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() throws InvalidEntryException {

						return true;
					}

					@Override
					public boolean onNoClick() throws InvalidEntryException {

						return true;
					}

					@Override
					public boolean onYesClick() throws InvalidEntryException {

						return true;
					}

				});
	}

	// set the selected income account as default Income account for Service
	// Item
	public static void defaultIncomeAccountServiceItem(
			final ClientAccount selectItem, ClientAccount defaultIncomeAccount) {
		company = getCompany();
		if (defaultIncomeAccount != null)
			if (!(defaultIncomeAccount.equals(selectItem))) {
				Accounter.showWarning(
						AccounterWarningType.default_IncomeAccount,
						AccounterType.WARNING, new ErrorDialogHandler() {

							@Override
							public boolean onCancelClick()
									throws InvalidEntryException {

								return false;
							}

							@Override
							public boolean onNoClick()
									throws InvalidEntryException {

								return true;
							}

							@Override
							public boolean onYesClick()
									throws InvalidEntryException {
								if (company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
									company.setUkServiceItemDefaultIncomeAccount(selectItem
											.getName());
								else
									company.setServiceItemDefaultIncomeAccount(selectItem
											.getName());
								return true;
							}

						});
			}

	}

	// Set the selected income account as default Income account for
	// Non-Inventory
	// Item

	public static void defaultIncomeAccountNonInventory(
			final ClientAccount selectItem, ClientAccount defaultIncomeAccount) {
		company = getCompany();
		if (!(defaultIncomeAccount.equals(selectItem))) {
			Accounter.showWarning(
					AccounterWarningType.default_IncomeAccountNonInventory,
					AccounterType.WARNING, new ErrorDialogHandler() {

						@Override
						public boolean onCancelClick()
								throws InvalidEntryException {

							return false;
						}

						@Override
						public boolean onNoClick() throws InvalidEntryException {

							return true;
						}

						@Override
						public boolean onYesClick()
								throws InvalidEntryException {
							if (company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
								company.setUkNonInventoryItemDefaultIncomeAccount(selectItem
										.getName());
							else
								company.setNonInventoryItemDefaultIncomeAccount(selectItem
										.getName());
							return true;
						}

					});
		}

	}

	// set the selected income account as default Expense account for Service
	// Item
	public static void defaultExpenseAccountServiceItem(
			final ClientAccount selectItem, ClientAccount defaultExpenseAccount) {

		company = getCompany();
		if (defaultExpenseAccount != null)
			if (!(defaultExpenseAccount.equals(selectItem))) {
				Accounter.showWarning(
						AccounterWarningType.default_ExpenseAccount,
						AccounterType.WARNING, new ErrorDialogHandler() {

							@Override
							public boolean onCancelClick()
									throws InvalidEntryException {

								return false;
							}

							@Override
							public boolean onNoClick()
									throws InvalidEntryException {

								return true;
							}

							@Override
							public boolean onYesClick()
									throws InvalidEntryException {
								if (company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
									company.setUkServiceItemDefaultExpenseAccount(selectItem
											.getName());
								else
									company.setServiceItemDefaultExpenseAccount(selectItem
											.getName());
								return true;
							}

						});
			}

	}

	// Set the selected Expense Account as default Expense Account for
	// Non-Inventory
	// Item

	public static void defaultExpenseAccountNonInventory(
			final ClientAccount selectExpAccount,
			ClientAccount defaultExpAccount) {
		company = getCompany();
		if (defaultExpAccount != null)
			if (!(defaultExpAccount.equals(selectExpAccount))) {
				Accounter
						.showWarning(
								AccounterWarningType.default_ExpenseAccountNonInventory,
								AccounterType.WARNING,
								new ErrorDialogHandler() {

									@Override
									public boolean onCancelClick()
											throws InvalidEntryException {

										return false;
									}

									@Override
									public boolean onNoClick()
											throws InvalidEntryException {

										return true;
									}

									@Override
									public boolean onYesClick()
											throws InvalidEntryException {
										if (company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
											company.setUkNonInventoryItemDefaultExpenseAccount(selectExpAccount
													.getName());
										else
											company.setNonInventoryItemDefaultExpenseAccount(selectExpAccount
													.getName());
										return true;
									}

								});
			}

	}

	public static boolean validate_IncomeAccount(final AbstractBaseView view,
			ClientAccount income_account) {
		if (income_account.getType() != ClientAccount.TYPE_INCOME) {
			return false;
			// Accounter.showWarning(
			// AccounterWarningType.different_IncomeAccountType,
			// AccounterType.WARNING, new ErrorDialogHandler() {
			//
			// @Override
			// public boolean onCancelClick()
			// throws InvalidEntryException {
			//
			// return false;
			// }
			//
			// @Override
			// public boolean onNoClick() throws InvalidEntryException {
			// // Accounter.stopExecution();
			// return true;
			// }
			//
			// @Override
			// public boolean onYesClick()
			// throws InvalidEntryException {
			// view.validationCount--;
			// return true;
			// }
			//
			// });
			// AbstractBaseView.warnOccured = true;
		} else
			// view.validationCount--;

			return false;
	}

	public static boolean validate_ExpenseAccount(ClientAccount expense_account) {
		if (expense_account.getType() != ClientAccount.TYPE_EXPENSE
				&& expense_account.getType() != ClientAccount.TYPE_COST_OF_GOODS_SOLD) {
			return false;
			// Accounter.showWarning(
			// AccounterWarningType.different_ExpenseAccountType,
			// AccounterType.WARNING, new ErrorDialogHandler() {
			//
			// @Override
			// public boolean onCancelClick()
			// throws InvalidEntryException {
			//
			// return false;
			// }
			//
			// @Override
			// public boolean onNoClick() throws InvalidEntryException {
			// // Accounter.stopExecution();
			// return true;
			// }
			//
			// @Override
			// public boolean onYesClick()
			// throws InvalidEntryException {
			// view.validationCount--;
			// return true;
			// }
			//
			// });
			// AbstractBaseView.warnOccured = true;
		}
		// else
		// view.validationCount--;
		return true;

	}

	public static boolean duplicate_itemName() throws InvalidEntryException {
		Accounter.showError(AccounterErrorType.duplicate_ItemName);
		return false;
	}

	public static boolean item_Must_Sell_Or_Buy() throws InvalidEntryException {
		Accounter.showError(AccounterErrorType.Item_BuyOrSell);
		return false;
	}

	public static boolean item_Edit_ISellThisItem(ClientItem item,
			boolean isISellThisItemCurrent) throws InvalidEntryException {

		if (item.isISellThisItem() != isISellThisItemCurrent) {
			Accounter.showError(AccounterErrorType.isItemSoldTrue);
			return false;
		}
		return true;

	}

	public static boolean item_Edit_IPurchaseThisItem(ClientItem item,
			boolean isIBuyThisItemCurrnent) throws InvalidEntryException {
		if (item.isIBuyThisItem() != isIBuyThisItemCurrnent) {
			Accounter.showError(AccounterErrorType.isItemPurchaseTrue);
			return false;
		}
		return true;
	}

	public static boolean validate_TaxAgency_PaymentTerm(
			ClientPaymentTerms paymentTerm) throws InvalidEntryException {
		if (paymentTerm.getDiscountPercent() > 0) {
			Accounter
					.showError(AccounterErrorType.taxAgency_Discount_PaymentTerm);
			return false;
		}
		return true;

	}

	/**
	 * validates whether the account passed is a TaxAgency Account or not.
	 * 
	 * @param Account
	 * @return
	 * @throws InvalidTransactionEntryException
	 */
	public static boolean validate_TaxAgency_FinanceAcount(
			ClientAccount financeAccount) {
		if (financeAccount == null)
			return true;
		// List<ClientTaxAgency> taxAgencies = FinanceApplication.getCompany()
		// .getActiveTaxAgencies();
		// for (ClientTaxAgency taxAgency : taxAgencies) {
		// if (taxAgency.getLiabilityAccount() == financeAccount.getID())
		// {
		// throw new InvalidTransactionEntryException(financeAccount
		// .getName()
		// + AccounterErrorType.taxAgency_FinanceAcount);
		//
		// }
		// }
		return true;
	}

	public static void void_Transaction() {
		Accounter.showError(AccounterErrorType.taxAgency_FinanceAcount);
		// Accounter.stopExecution();

	}

	public static void canVoidOrEdit(ClientTransaction transaction) {
		Accounter.showError(AccounterErrorType.canVoidOrEdit);
		// Accounter.stopExecution();

	}

	public static boolean cannotUsePurchaseItem(ClientItem item)
			throws InvalidTransactionEntryException {

		if (!item.isIBuyThisItem()) {
			Accounter.showError(AccounterErrorType.cannotUsePurchaseItem);
			return false;
			// throw new InvalidTransactionEntryException(
			// AccounterErrorType.cannotUsePurchaseItem);

		} else
			return true;
	}

	public static boolean cannotUseSalesItem(ClientItem item)
			throws InvalidTransactionEntryException {

		if (!item.isISellThisItem()) {
			Accounter.showError(AccounterErrorType.cannotUseSalesItem);
			return false;
			// throw new InvalidTransactionEntryException(
			// AccounterErrorType.cannotUseSalesItem);

		} else
			return true;

	}

	public static void validate_UnitPrice(Double price) {

		if (DecimalUtil.isLessThan(price, 0.00)) {
			Accounter.showError(AccounterErrorType.unitPrice);
			// Accounter.stopExecution();
		}

	}

	public static void validate_DiscountAmount(Double discountAmount) {

		if (DecimalUtil.isLessThan(discountAmount, 0.00)) {
			Accounter.showError(AccounterErrorType.discountAmount);
			// Accounter.stopExecution();
		}

	}

	public static boolean validate_LineTotal(Double total,
			boolean ifAmountIsNegative) {

		if (DecimalUtil.isLessThan(total, 0.00)) {
			Accounter.showError(AccounterErrorType.lineTotalAmount);
			// Accounter.stopExecution();
			return false;
		}
		return true;

	}

	/**
	 * This Method Validates the Due,Delivary and Expire Dates Not Earlier than
	 * Transaction Date
	 * 
	 * @return
	 */
	public static boolean validate_dueOrDelivaryDates(
			ClientFinanceDate dueorDelivaryDate,
			ClientFinanceDate transactionDate) {

		if (dueorDelivaryDate.before(transactionDate)) {
			if (!UIUtils.isdateEqual(dueorDelivaryDate, transactionDate)) {
				return false;
			}

		}
		return true;
	}

	/**
	 * this is for Receive payment only. this method distributes the given
	 * amount to all possible records in the grid.
	 * 
	 * @param view
	 * @param amountToDistribute
	 */
	public static <T> void distributePaymentToOutstandingInvoices(
			final ReceivePaymentView view, final Double amountToDistribute) {

		Accounter.showWarning(AccounterErrorType.distributePayments,

		AccounterType.WARNINGWITHCANCEL, new ErrorDialogHandler() {

			@Override
			public boolean onCancelClick() throws InvalidEntryException {
				return true;
			}

			@Override
			public boolean onNoClick() throws InvalidEntryException {
				return true;
			}

			@Override
			public boolean onYesClick() throws InvalidEntryException {

				Double amount = amountToDistribute;
				double updatedValue = 0.0D;
				// FIXME--need to check the code
				for (ClientTransactionReceivePayment trprecord : view.gridView
						.getRecords()) {
					if (!view.gridView.isSelected(trprecord))
						view.gridView.selectRow(view.gridView
								.indexOf(trprecord));

					try {
						if (!DecimalUtil.isGreaterThan(
								trprecord.getAmountDue(), amount)) {
							trprecord.setPayment(trprecord.getAmountDue());
							updatedValue += trprecord.getAmountDue();
							amount -= trprecord.getAmountDue();
						} else {
							trprecord.setPayment(amount);
							updatedValue += amount;
							amount = 0D;
						}
						view.gridView.updateData(trprecord);
						// view.gridView.updateFooterValues(DataUtils
						// .getAmountAsString(updatedValue), 8);
						if (!DecimalUtil.isGreaterThan(amount, 0D))
							break;
					} catch (Exception e) {
						Accounter.showError(Accounter.constants()
								.accountervalidatorError());
						return false;
					}

				}
				view.recalculateGridAmounts();

				view.calculateUnusedCredits();

				return true;
			}
		});
	}

	/**
	 * in Receivepayment, the total payments should not exceed the amount
	 * received. this method checks that validation.
	 * 
	 * @param amount
	 * @param paymentsTotal
	 * @return
	 */
	public static boolean validateRecievePaymentAmount(Double amount,
			Double paymentsTotal) {
		if (DecimalUtil.isGreaterThan(paymentsTotal, amount)) {
			// Accounter.showError(AccounterErrorType.recievePayment_TotalAmount);
			// Accounter.stopExecution();
			return false;
		}

		return true;

	}

	public static boolean validate_Receive_Payment(double amountDue,
			double totalValue, String errormessg) {
		if (DecimalUtil.isLessThan(totalValue, 0.00)) {
			Accounter.showError(AccounterErrorType.INVALID_NEGATIVE_AMOUNT);
			// Accounter.stopExecution();
			return false;
		} else if (DecimalUtil.isGreaterThan(totalValue, amountDue)
				|| DecimalUtil.isEquals(totalValue, 0)) {
			Accounter.showError(errormessg);
			// Accounter.stopExecution();
			return false;
		}
		return true;
	}

	public static boolean validate_MakeDeposit_CashBackAccount(
			ClientAccount cashbackAccount) throws InvalidEntryException {
		if (cashbackAccount == null) {
			Accounter.showError(AccounterErrorType.makeDeposit_CashBackAccount);
			return false;
		}
		return true;

	}

	public static boolean validate_MakeDeposit_CashBackAmount(double amount,
			double totalDepositAmount) {

		if (!DecimalUtil.isEquals(amount, 0.0)
				&& DecimalUtil.isGreaterThan(amount, totalDepositAmount)) {
			return false;
		}
		return true;

	}

	public static boolean validate_MakeDeposit_accountcombo(
			ClientAccount selectedDepositInAccount,
			AbstractTransactionGrid gridView) {
		List<ClientTransactionMakeDeposit> selectedRecords = gridView
				.getSelectedRecords();

		for (ClientTransactionMakeDeposit rec : selectedRecords) {
			if (rec.getAccount() == (selectedDepositInAccount.getID())) {
				return false;
			}
		}

		return true;

	}

	public static boolean validate_TransferFunds(ClientAccount from,
			ClientAccount to) {
		if (from.getID() == to.getID()) {
			return false;
		}
		return true;

	}

	public static boolean validate_TransferFromAccount(
			ClientAccount fromAccount, Double transferAmount,
			final TransferFundsDialog dialog) {
		if (!fromAccount.isIncrease()
				&& DecimalUtil.isLessThan(
						(fromAccount.getTotalBalance() - transferAmount), 0.00)) {
			// Accounter.showWarning(AccounterWarningType.transferFromAccount,
			// AccounterType.WARNING, new ErrorDialogHandler() {
			//
			// @Override
			// public boolean onCancelClick()
			// throws InvalidEntryException {
			//
			// return false;
			// }
			//
			// @Override
			// public boolean onNoClick() throws InvalidEntryException {
			// // Accounter.stopExecution();
			// return true;
			// }
			//
			// @Override
			// public boolean onYesClick()
			// throws InvalidEntryException {
			// dialog.isValidatedTransferAmount = true;
			// return true;
			//
			// }
			// });
			return false;
		} else {
			dialog.isValidatedTransferAmount = true;
			return true;
		}

	}

	// /**
	// * validates the transaction grid in all transactions.
	// *
	// * @param transactionGrid
	// * @param transactionDomain
	// * @return
	// * @throws InvalidTransactionEntryException
	// */
	// public static <T> boolean validateTransactionGrid(
	// FinanceGrid<T> transactionGrid, int tranctionDomain)
	// throws InvalidTransactionEntryException {
	//
	// if (transactionGrid == null)
	// return true;
	//
	// if (isBlankTransaction(transactionGrid)) {
	//
	// switch (tranctionDomain) {
	// case FinanceGrid.CUSTOMER_TRANSACTION:
	// case FinanceGrid.VENDOR_TRANSACTION:
	//
	// return validateGrid(transactionGrid, tranctionDomain);
	//
	// default:
	// break;
	// }
	// }
	//
	// return true;
	//
	// }

	// private static <T> boolean validateGrid(FinanceGrid<T> transactionGrid,
	// int transactionDomain) throws InvalidTransactionEntryException {
	//
	// ClientItem item;
	// ClientAccount account;
	// for (T record : transactionGrid.getRecords()) {
	//
	// ClientTransactionItem transactionItem = (ClientTransactionItem) record;
	//
	// // validation of transaction Item ,if it is an item(whether the
	// // account in that item is taxAgency related or not).
	// if (transactionItem.getType() == 1) {
	// item = FinanceApplication.getCompany().getItem(
	// transactionItem.getItem());
	// if (item == null)
	// throw new InvalidTransactionEntryException(
	// "Item is null in "
	// // + (transactionGrid.getGrid()
	// // .getRecordIndex(record) + 1)
	// + " row");
	// long accountId = (transactionDomain == FinanceGrid.CUSTOMER_TRANSACTION)
	// ? item
	// .getIncomeAccount()
	// : item.getExpenseAccount();
	// try {
	// validate_TaxAgency_FinanceAcount(FinanceApplication
	// .getCompany().getAccount(accountId));
	// } catch (InvalidTransactionEntryException e) {
	//
	// throw new InvalidTransactionEntryException(item.getName()
	// + " (Item) -->" + e.getMessage());
	// }
	//
	// }// validation of transaction Item ,if it is an account(whether
	// // the
	// // account is taxAgency related or not).
	// else if (transactionItem.getType() == 4) {
	// account = FinanceApplication.getCompany().getAccount(
	// transactionItem.getAccount());
	// if (account == null)
	// throw new InvalidTransactionEntryException(
	// "Account is null in "
	// // + (transactionGrid.getGrid()
	// // .getRecordIndex(record) + 1)
	// + " row");
	// validate_TaxAgency_FinanceAcount(account);
	// }
	//
	// }
	// //
	// // for (IsSerializable record : transactionGrid.getRecords()) {
	// //
	// // TransactionItemRecord transactionItem = (TransactionItemRecord)
	// // record;
	// //
	// // // validation of transaction Item ,if it is an item(whether the
	// // // account in that item is taxAgency related or not).
	// // if (transactionItem.getTransactionItemType() == 1) {
	// // item = transactionItem.getItem();
	// // if (item == null)
	// // throw new InvalidTransactionEntryException(
	// // "Item is null in "
	// // // + (transactionGrid.getGrid()
	// // // .getRecordIndex(record) + 1)
	// // + " row");
	// // long accountId = (transactionDomain ==
	// // FinanceGrid.CUSTOMER_TRANSACTION) ? item
	// // .getIncomeAccount()
	// // : item.getExpenseAccount();
	// // try {
	// // validate_TaxAgency_FinanceAcount(FinanceApplication
	// // .getCompany().getAccount(accountId));
	// // } catch (InvalidTransactionEntryException e) {
	// //
	// // throw new InvalidTransactionEntryException(item.getName()
	// // + " (Item) -->" + e.getMessage());
	// // }
	// //
	// // }// validation of transaction Item ,if it is an account(whether
	// // // the
	// // // account is taxAgency related or not).
	// // else if (transactionItem.getTransactionItemType() == 4) {
	// // account = transactionItem.getAccount();
	// // if (account == null)
	// // throw new InvalidTransactionEntryException(
	// // "Account is null in "
	// // // + (transactionGrid.getGrid()
	// // // .getRecordIndex(record) + 1)
	// // + " row");
	// // validate_TaxAgency_FinanceAcount(account);
	// // }
	// //
	// // }
	//
	// return true;
	// }

	/**
	 * verifies whether the object is null or not.
	 * 
	 * @param object
	 * @return true
	 * @throws InvalidTransactionEntryException
	 */
	public static boolean isNull(Object object)
			throws InvalidTransactionEntryException {
		if (object == null) {
			throw new InvalidTransactionEntryException(Accounter.constants()
					.fieldError());

		}

		return true;
	}

	public static boolean isNull(Object... objects)
			throws InvalidTransactionEntryException {
		for (Object object : objects)
			if (object == null)
				throw new InvalidTransactionEntryException(
						AccounterErrorType.REQUIRED_FIELDS);
		return true;

	}

	public static boolean isNullValue(Object object) {
		if (object == null || object.equals(" ")) {
			return false;
		}
		return true;

	}

	public static boolean validateGridLineTotal(Double lineTotal)
			throws InvalidEntryException {
		if (DecimalUtil.isLessThan(lineTotal, 0.00)) {
			throw new InvalidEntryException(AccounterErrorType.lineTotal);
		}
		return false;

	}

	public static boolean validateGridUnitPrice(Double unitPrice)
			throws InvalidTransactionEntryException {
		if (DecimalUtil.isLessThan(unitPrice, 0.00)) {
			// BaseView.errordata.setHTML("<li> " + AccounterErrorType.unitPrice
			// + ".");
			// BaseView.commentPanel.setVisible(true);
			// MainFinanceWindow.getViewManager().appendError(
			// AccounterErrorType.unitPrice);
			// Accounter.showError(AccounterErrorType.unitPrice);
			// Accounter.stopExecution();
			return false;
		} else {
			// BaseView.errordata.setHTML("");
			// BaseView.commentPanel.setVisible(false);

		}
		return true;

	}

	public static boolean validateGridQuantity(int quantity)
			throws InvalidTransactionEntryException {
		if (DecimalUtil.isLessThan(quantity, 0.00)) {
			// BaseView.errordata.setHTML("<li> " + AccounterErrorType.quantity
			// + ".");
			// BaseView.commentPanel.setVisible(true);
			// MainFinanceWindow.getViewManager().appendError(
			// AccounterErrorType.quantity);
			// Accounter.showError(AccounterErrorType.quantity);
			// Accounter.stopExecution();
			return false;
		} else {
			// BaseView.errordata.setHTML("");
			// BaseView.commentPanel.setVisible(false);

		}
		return true;

	}

	public static boolean validateCustomerRefundAmount(Double amount,
			ClientAccount payFromAccount) {

		if (!payFromAccount.isIncrease()
				&& (DecimalUtil.isLessThan(
						(payFromAccount.getTotalBalance() - amount), 0.0))) {
			// Accounter.showWarning(
			// AccounterWarningType.INVALID_CUSTOMERREFUND_AMOUNT,
			// AccounterType.WARNING, new ErrorDialogHandler() {
			//
			// @Override
			// public boolean onCancelClick()
			// throws InvalidEntryException {
			// return false;
			// }
			//
			// @Override
			// public boolean onNoClick() throws InvalidEntryException {
			// // Accounter.stopExecution();
			// return true;
			// }
			//
			// @Override
			// public boolean onYesClick()
			// throws InvalidEntryException {
			// view.validationCount--;
			// return true;
			// }
			//
			// });
			// AbstractBaseView.warnOccured = true;

			return false;
		}
		return true;

	}

	public static boolean validateFormItem(FormItem item, boolean isDialog)
			throws InvalidTransactionEntryException {
		if (!item.validate()) {

			Accounter.messages().pleaseEnter(item.getTitle());
			// throw new InvalidTransactionEntryException(
			// "Required fields are shown in bold.Those fields should be filled!!");

		}

		return true;
	}

	// /**
	// * checks whether the transaction grid is empty or not.
	// *
	// * @param transactionGrid
	// * @return
	// * @throws InvalidTransactionEntryException
	// */

	public static boolean isBlankTransaction(
			AbstractTransactionGrid<?> transactionGrid) {
		if (transactionGrid != null && transactionGrid.getRecords().isEmpty()) {
			return true;
		}
		return false;

	}

	public static boolean validate_Radiovalue(Object object) {
		if (object == null) {
			return false;
		}
		return true;
	}

	/**
	 * this method Validates the selling Date is before than purchase Date
	 * 
	 * @param purchaseDate
	 * @param sellingDate
	 * @param constant
	 * @return
	 * @throws InvalidTransactionEntryException
	 */

	public static boolean validateSellorDisposeDate(
			ClientFinanceDate purchaseDate, ClientFinanceDate sellingDate) {
		if (sellingDate.before(purchaseDate)) {
			if (!UIUtils.isdateEqual(sellingDate, purchaseDate))
				return false;
		}

		return true;
	}

	public static boolean isSellorBuyCheck(CheckboxItem sellCheck,
			CheckboxItem buyCheck) {
		if (!sellCheck.isChecked() && !buyCheck.isChecked()) {
			return false;
		}
		return true;
	}

	/**
	 * This Method Validates the TransactionItems in TransactionGrid with Valid
	 * Itemnames or not
	 * 
	 * @param value
	 * @param itemName
	 * @return
	 * @throws InvalidTransactionEntryException
	 */

	public static boolean validateGridItem(Object value) {
		if (value == null || value == "") {
			// throw new InvalidTransactionEntryException(Accounter.messages()
			// .pleaseEnter(itemName));
			return false;
		}
		return true;
	}

	public static boolean validate_ZeroAmount(Double amount) {
		if (DecimalUtil.isEquals(amount, 0.00)) {
			return false;
		}
		return true;
	}

	public static boolean validatePurchaseDate(ClientFinanceDate transactionDate) {
		boolean validDate = true;
		List<ClientFiscalYear> openFiscalYears = getOpenFiscalYears();
		for (ClientFiscalYear openFiscalYear : openFiscalYears) {

			int before = transactionDate.compareTo(openFiscalYear
					.getStartDate());
			int after = transactionDate.compareTo(openFiscalYear.getEndDate());

			validDate = (before < 0 || after > 0) ? false : true;
			if (validDate)
				break;

		}

		return validDate;

	}

	/**
	 * In Write Check, the amount should not exceed the Line Total. this method
	 * checks that validation.
	 * 
	 * @param amount
	 * @param total
	 * @return
	 */
	public static boolean validateWriteCheckAmount(double amount, double total) {
		if (!DecimalUtil.isEquals(total, amount)) {
			Accounter.showError(AccounterErrorType.writeCheck_TotalAmount);
			// Accounter.stopExecution();
			return false;
		}

		return true;

	}

	public static boolean isPriorAsOfDate(ClientFinanceDate asOfDate) {

		ClientFinanceDate companyStartDate = new ClientFinanceDate(getCompany()
				.getPreferences().getPreventPostingBeforeDate());
		if (asOfDate.before(companyStartDate)) {
			// throw new
			// InvalidEntryException(AccounterErrorType.prior_asOfDate);
			return true;
			// return false;
		}
		return false;

	}

	public static boolean validatePayment() {
		Accounter.showError("");
		return false;

	}

	public static boolean validatePayBill() {
		Accounter.showError(Accounter.constants()
				.youcantsavepaybillwith0amount());
		return false;
	}

	// public static boolean isBlankTransactionGrid(
	// AbstractTransactionGrid transactionGrid)
	// throws InvalidTransactionEntryException {
	// if (transactionGrid != null && transactionGrid.getRecords().isEmpty()) {
	// throw new InvalidTransactionEntryException(
	// AccounterErrorType.selectTransaction);
	// }
	// return true;
	//
	// }

	// public static boolean validateReceivePaymentGrid(
	// AbstractTransactionGrid<?> transactionGrid) {
	// if (transactionGrid == null || transactionGrid.getRecords().isEmpty()
	// || transactionGrid.getSelectedRecords().size() == 0) {
	// return false;
	// } else if (!transactionGrid.validateGrid()) {
	// return false;
	// }
	//
	// return true;
	//
	// }

	public static boolean isAmountTooLarge(Double amount)
			throws InvalidEntryException {
		if (DecimalUtil.isGreaterThan(amount, 1000000000000.00)) {
			throw new InvalidEntryException(AccounterErrorType.AMOUNTEXCEEDS);
		}
		return false;

	}

	public static boolean isAmountNegative(Double amount)
			throws InvalidEntryException {
		if (DecimalUtil.isLessThan(amount, 0.00)) {
			throw new InvalidEntryException(
					AccounterErrorType.INVALID_NEGATIVE_AMOUNT);
		}
		return false;

	}

	/**
	 * @return
	 */
	private static ClientCompany getCompany() {
		return Accounter.getCompany();
	}

}