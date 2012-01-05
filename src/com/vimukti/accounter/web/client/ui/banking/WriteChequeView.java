package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.PayeeCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;

public class WriteChequeView extends
		AbstractBankTransactionView<ClientWriteCheck> {

	private PayFromAccountsCombo bankAccSelect;

	private PayeeCombo paytoSelect;
	private AmountField balText, amtText;
	private DynamicForm bankAccForm;

	private HorizontalPanel labelLayout;
	public AmountLabel netAmount;

	TaxItemsForm vatTotalNonEditableText;
	private TextAreaItem addrArea;
	private DynamicForm payForm;

	protected ClientAccount selectBankAcc;
	protected ClientAccount selectPayToMethod;
	protected boolean isClose;

	private CheckboxItem toprintCheck;
	protected ClientCustomer selectedCustomer;
	protected ClientVendor selectedVendor;

	protected ClientSalesPerson selectedSalesPerson;
	protected ClientAccount selectedBalance;

	protected ClientPayee payee;
	private VerticalPanel mainVLay, nHPanel;

	private LinkedHashMap<String, ClientAddress> billToAddress;

	protected ClientTAXAgency selectedTaxAgency;

	private DateField date;

	// private DynamicForm amtForm;

	private final ClientCompany company;
	private List<ClientAccount> payFromAccounts;

	private final String checkNo = ClientWriteCheck.IS_TO_BE_PRINTED;

	private DynamicForm numForm;

	private ArrayList<DynamicForm> listforms;

	private VerticalPanel vatPanel;
	private VerticalPanel amountPanel;
	private VerticalPanel vPanel;

	private VendorAccountTransactionTable transactionVendorAccountTable;
	private AddNewButton accountTableButton;
	private DisclosurePanel vendorAccountsDisclosurePanel;

	private TAXCodeCombo taxCodeSelect;

	private boolean isAmountChange;
	private AmountLabel unassignedAmount;
	private double previousValue = 0.00D;

	private HorizontalPanel unassignedAmountPanel;

	// private CurrencyFactorWidget currencyWidget;

	private WriteChequeView() {
		super(ClientTransaction.TYPE_WRITE_CHECK);
		this.company = getCompany();

	}

	public static WriteChequeView getInstance() {

		return new WriteChequeView();
	}

	// private void getPayFromAccounts() {
	// payFromAccounts = new ArrayList<ClientAccount>();
	// for (ClientAccount account : FinanceApplication.getCompany()
	// .getAccounts()) {
	// if (account.getType() == ClientAccount.TYPE_CASH
	// || account.getType() == ClientAccount.TYPE_BANK
	// || account.getType() == ClientAccount.TYPE_CREDIT_CARD
	// || account.getType() == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
	// || account.getType() == ClientAccount.TYPE_LONG_TERM_LIABILITY) {
	//
	// payFromAccounts.add(account);
	// }
	//
	// }
	// bankAccSelect.initCombo(payFromAccounts);
	// }
	// public void updateTotals() {
	// // if (transaction == null) {
	// transactionVendorTable.setVisible(true);
	// transactionVendorTable.setDisabled(isInViewMode());
	// changeGrid(transactionVendorTable);
	// transactionVendorTable.updateTotals();
	// // }
	// }

	protected void updateAddressAndGrid() {
		// Set<Address> add = null;
		long currency = getBaseCurrency().getID();

		if (payee instanceof ClientCustomer) {
			selectedCustomer = (ClientCustomer) payee;
			addressList = selectedCustomer.getAddress();
			currency = selectedCustomer.getCurrency();
			// if (transaction == null) {
			// customerAccountsDisclosurePanel.setVisible(true);
			// customerItemsDisclosurePanel.setVisible(true);
			// changeGrid(customerAccountsDisclosurePanel,
			// customerItemsDisclosurePanel);
			// }
		} else if (payee instanceof ClientVendor) {

			selectedVendor = (ClientVendor) payee;
			addressList = selectedVendor.getAddress();
			currency = selectedVendor.getCurrency();
			// vendorAccountsDisclosurePanel.setVisible(true);
			// vendorItemsDisclosurePanel.setVisible(true);
			// changeGrid(vendorAccountsDisclosurePanel,
			// vendorItemsDisclosurePanel);

		} else if (payee instanceof ClientTAXAgency) {
			selectedTaxAgency = (ClientTAXAgency) payee;
			addressList = selectedTaxAgency.getAddress();
			// vendorAccountsDisclosurePanel.setVisible(true);
			// vendorItemsDisclosurePanel.setVisible(true);
			// changeGrid(vendorAccountsDisclosurePanel,
			// vendorItemsDisclosurePanel);

			/*
			 * The selected TaxAgency required in the taxagency grid to allow
			 * only the taxcodes available fot this taxagency
			 */
			// taxAgencyGrid.setSelectedTaxAgency(selectedTaxAgency);
			// taxAgencyGrid.filteredTaxCodes();
			// if (transactionObject == null) {
			// taxAgencyGrid.setVisible(true);
			// changeGrid(taxAgencyGrid);
			// }

		}
		ClientCurrency clientCurrency = getCompany().getCurrency(currency);
		currencyWidget.setSelectedCurrency(clientCurrency);

		String formalName = currencyWidget.getSelectedCurrency()
				.getFormalName();

		transactionVendorAccountTable.updateTotals();
		this.transactionVendorAccountTable.updateAmountsFromGUI();

		foreignCurrencyamountLabel.setTitle(messages.currencyTotal(formalName));

		transactionTotalBaseCurrencyText.setTitle(messages
				.currencyTotal(getBaseCurrency().getFormalName()));

		amtText.setCurrency(clientCurrency);
		// getAddreses(add);
		if (isInViewMode()) {
			if (transaction.getAddress() != null)
				billToaddressSelected(getAddressById(transaction.getAddress()
						.getID()));
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(clientCurrency);
			setCurrencyFactor(transaction.getCurrencyFactor());
			updateAmountsFromGUI();
			modifyForeignCurrencyTotalWidget();
		}

		initBillToCombo();

	}

	@Override
	protected void reload() {
		if (!saveAndClose)
			try {
				ActionFactory.getWriteChecksAction().run(null, true);
			} catch (Throwable e) {

				// //UIUtils.logError("Failed to load Write Check View", e);
			}

	}

	// public void changeGrid(DisclosurePanel gridView, DisclosurePanel
	// gridView2) {
	// setMenuRequired(true);
	// // mainVLay.remove(vendorGrid);
	// // mainVLay.add(vendorGrid);
	// // } else if (gridView instanceof TaxAgencyTransactionGrid) {
	// //
	// // TaxAgencyTransactionGrid taxAgencyGrid = (TaxAgencyTransactionGrid)
	// // gridView;
	// // setMenuRequired(false);
	// // // mainVLay.remove(taxAgencyGrid);
	// // // mainVLay.add(taxAgencyGrid);
	// // }Ou
	// if (transactionVendorAccountTable != null)
	// mainVLay.remove(vendorAccountsDisclosurePanel);
	// if (transactionVendorItemTable != null)
	// mainVLay.remove(vendorItemsDisclosurePanel);
	// // if (taxAgencyGrid != null)
	// // mainVLay.remove(taxAgencyGrid);
	// if (transactionCustomerAccountTable != null)
	// mainVLay.remove(customerAccountsDisclosurePanel);
	// if (transactionCustomerItemTable != null)
	// mainVLay.remove(customerItemsDisclosurePanel);
	// mainVLay.add(gridView);
	// // mainVLay.add(accountTableButton);
	// mainVLay.add(gridView2);
	// // mainVLay.add(itemTableButton);
	// mainVLay.add(vPanel);
	// if (getCompany().getPreferences().isRegisteredForVAT()) {
	// // It should be like thid only,becoz vatPanel is getting add befor
	// // the gird.So,we need to remove n add after grid
	// mainVLay.remove(vatPanel);
	// mainVLay.add(vatPanel);
	// }
	// // mainVLay.redraw();
	// }

	protected void updateBalance() {
		if (selectBankAcc != null) {
			balText.setAmount(selectBankAcc.getTotalBalance());
		}
	}

	protected void getAddreses(Set<ClientAddress> allAddress) {
		ClientAddress toBeShown = null;

		if (isInViewMode() && transaction.getAddress() != null) {
			addressList = payee.getAddress();
			toBeShown = getAddressById(transaction.getAddress().getID());
		} else {
			for (ClientAddress to : allAddress) {
				if (to.getType() == ClientAddress.TYPE_BUSINESS) {
					toBeShown = to;
					break;
				}
			}
		}

		if (toBeShown != null) {
			billToAddress.put("1", toBeShown);
			String toToSet = new String();
			if (toBeShown.getStreet() != null) {
				toToSet = toBeShown.getStreet().toString() + ",\n";
			}
			if (toBeShown.getCity() != null) {
				toToSet += toBeShown.getCity().toString() + ",\n";
			}

			if (toBeShown.getStateOrProvinence() != null) {
				toToSet += toBeShown.getStateOrProvinence() + ",\n";
			}
			if (toBeShown.getZipOrPostalCode() != null) {
				toToSet += toBeShown.getZipOrPostalCode() + ",\n";
			}
			if (toBeShown.getCountryOrRegion() != null) {
				toToSet += toBeShown.getCountryOrRegion();
			}

			addrArea.setValue(toToSet);
		}
	}

	public void initBankaccountCombo() {
		payFromAccounts = bankAccSelect.getAccounts();

		bankAccSelect.initCombo(payFromAccounts);
		if (isInViewMode() || transaction.getBankAccount() != 0) {

			selectBankAcc = this.company.getAccount(transaction
					.getBankAccount());

		}
		// else if (takenPaySalesTax != null) {
		//
		// selectBankAcc = getCompany().getAccount(
		// takenPaySalesTax.getPayFrom());
		//
		// }
		if (selectBankAcc != null) {

			bankAccSelect.setComboItem(selectBankAcc);
			updateBalance();
		}

		bankAccSelect.setDisabled(isInViewMode());
		// bankAccSelect.setShowDisabled(false);

	}

	public void initPayToCombo() {
		List<ClientPayee> payees = getCompany().getActivePayees();

		if (payees != null) {

			paytoSelect.initCombo(payees);

			if (isInViewMode() || transaction.getPayToType() != 0) {
				ClientCustomer customer = null;
				if (transaction.getPayToType() == ClientPayee.TYPE_CUSTOMER) {
					customer = getCompany().getCustomer(
							transaction.getCustomer());
					payee = customer;
					paytoSelect.setComboItem(customer);
				} else if (transaction.getPayToType() == ClientPayee.TYPE_VENDOR) {
					ClientVendor vendor2 = getCompany().getVendor(
							transaction.getVendor());
					payee = vendor2;
					paytoSelect.setComboItem(vendor2);
				} else if (transaction.getPayToType() == ClientPayee.TYPE_TAX_AGENCY) {
					ClientTAXAgency taxAgency = getCompany().getTaxAgency(
							transaction.getTaxAgency());
					payee = taxAgency;
					paytoSelect.setComboItem(taxAgency);
				}
				paytoSelect.setDisabled(isInViewMode());
				transactionVendorAccountTable
						.setRecords(getAccountTransactionItems(transaction
								.getTransactionItems()));
				return;
			}

			newPayToMethod();
		}
	}

	protected void newPayToMethod() {
		if (isInViewMode()) {
			switch (transaction.getPayToType()) {
			case ClientWriteCheck.TYPE_VENDOR:
				paytoSelect.setComboItem(getCompany().getVendor(
						transaction.getVendor()));
				payee = this.company.getVendor(transaction.getVendor());

				break;
			case ClientWriteCheck.TYPE_CUSTOMER:
				ClientCompany clientCompany = this.company;
				paytoSelect.setComboItem(clientCompany.getCustomer(transaction
						.getCustomer()));
				payee = clientCompany.getCustomer(transaction.getCustomer());

				break;
			case ClientWriteCheck.TYPE_TAX_AGENCY:
				paytoSelect.setComboItem(getCompany().getTaxAgency(
						transaction.getTaxAgency()));
				payee = this.company.getTaxAgency(transaction.getTaxAgency());
				break;
			}
			paytoSelect.setDisabled(isInViewMode());
			paytoSelect.setDisabled(false);
			updateAddressAndGrid();

		}

	}

	private void setDisableFields() {
		if (isInViewMode()) {
			payForm.setDisabled(true);
			bankAccForm.setDisabled(true);
		}

	}

	// @Override
	// protected void initTransactionNumber() {
	//
	// if (transactionObject != null) {
	//
	// transactionNumber = transactionObject.getNumber();
	// return;
	// }
	//
	// AccounterAsyncCallback<String> transactionNumberCallback = new
	// AccounterAsyncCallback<String>() {
	//
	// public void onException(AccounterException caught) {
	// Accounter.showError(FinanceApplication.constants()
	// .failedToGetTransactionNumber());
	//
	// }
	//
	// public void onSuccess(String result) {
	// if (result == null) {
	// onFailure(new Exception());
	// }
	//
	// transactionNumber = result;
	// }
	//
	// };
	//
	// this.rpcUtilService.getNextTransactionNumber(transactionType,
	// transactionNumberCallback);
	//
	// }

	// private void resetElements() {
	// selectBankAcc = null;
	// selectedBalance = null;
	// selectedCustomer = null;
	// selectedSalesPerson = null;
	// selectedTaxAgency = null;
	// selectedVendor = null;
	// selectPayToMethod = null;
	// writeCheckTaken = null;
	// addressList = null;
	// billingAddress = null;
	// billToCombo.setDisabled(isEdit);
	// amtText.setValue(""+UIUtils.getCurrencySymbol() +"0.00");
	// text.setValue(Utility.getNumberInWords("0.00"));
	// date.setValue(new Date());
	// setMemoTextAreaItem("");
	//
	// if (transactionCustomerGrid != null)
	// mainVLay.remove(transactionCustomerGrid);
	//
	// if (taxAgencyGrid != null)
	// mainVLay.remove(taxAgencyGrid);
	// taxAgencyGrid = new TaxAgencyTransactionGrid();
	// transactionCustomerGrid = new CustomerTransactionGrid();
	// if (taxAgencyGrid != null)
	// mainVLay.remove(taxAgencyGrid);
	// taxAgencyGrid = new TaxAgencyTransactionGrid();
	// transactionCustomerGrid = new CustomerTransactionGrid();
	// transactionVendorGrid = new VendorTransactionGrid();
	// vendorTransactionGrid.setTransactionView(this);
	// mainVLay.add(transactionCustomerGrid);

	// }

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (isInViewMode()) {
			Double transactionTotal = transaction.getTotal();
			if (transactionTotal != null && !isAmountChange) {
				amtText.setAmount(transactionTotal.doubleValue());
			}

		}

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		result.add(DynamicForm.validate(payForm));
		result.add(DynamicForm.validate(bankAccForm));

		// FIXME Need to validate grids.
		if (transactionVendorAccountTable.getAllRows().isEmpty()) {
			result.addError(transactionVendorAccountTable,
					messages.blankTransaction());
		} else {
			result.add(transactionVendorAccountTable.validateGrid());
		}

		// if (!validateAmount()) {
		// result.addError(memoTextAreaItem,
		// messages.transactiontotalcannotbe0orlessthan0());
		// }

		if (isTrackTax()) {
			if (!isTaxPerDetailLine()) {
				if (taxCodeSelect != null
						&& taxCodeSelect.getSelectedValue() == null) {
					result.addError(taxCodeSelect, messages.enterTaxCode());
				}

			}
		}
		if (unassignedAmountPanel.isVisible()) {
			result.addError(unassignedAmountPanel,
					messages.amountAndTotalShouldEqual());
		}

		ClientAccount bankAccount = bankAccSelect.getSelectedValue();
		// check if the currency of accounts is valid or not
		if (bankAccount != null) {
			ClientCurrency bankCurrency = getCurrency(bankAccount.getCurrency());
			if (bankCurrency != getBaseCurrency() && bankCurrency != currency) {
				result.addError(bankAccSelect,
						messages.selectProperBankAccount());
			}
		}

		return result;
	}

	private boolean validateAmount() {
		double total = 0.0;
		total += transactionVendorAccountTable.getGrandTotal();
		return AccounterValidator.isPositiveAmount(total);
	}

	public ClientWriteCheck saveView() {
		ClientWriteCheck saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {

		if (isInViewMode()) {
			updatePaySalesTax();
			return;
		}

		updateTransaction();

		saveOrUpdate(transaction);

	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type of the write check
		transaction.setType(ClientTransaction.TYPE_WRITE_CHECK);

		transaction.setCheckNumber(checkNo);

		transaction.setTransactionDate(transactionDate.getDate());

		// Setting Bank account
		selectBankAcc = bankAccSelect.getSelectedValue();
		if (selectBankAcc != null) {
			transaction.setBankAccount(selectBankAcc.getID());
		}

		// setting paymentmethod
		transaction.setPaymentMethod(messages.check());

		// Setting Address
		// transaction.setAddress(billingAddress);

		// Setting Transactions
		// FIXME Need to assign transaction Items from to tables.
		if (payee != null) {
			// In Edit mode If payee was changed to customer to vendor or
			// anything else
			// previous object should become null;
			transaction.setCustomer(0);
			transaction.setVendor(0);
			transaction.setTaxAgency(0);

			switch (payee.getType()) {
			case ClientPayee.TYPE_CUSTOMER:
				transaction.setCustomer(selectedCustomer.getID());
				transaction.setPayToType(ClientWriteCheck.TYPE_CUSTOMER);
				break;
			case ClientPayee.TYPE_VENDOR:
				transaction.setVendor(selectedVendor.getID());
				transaction.setPayToType(ClientWriteCheck.TYPE_VENDOR);
				break;

			case ClientPayee.TYPE_TAX_AGENCY:
				transaction.setTaxAgency(selectedTaxAgency.getID());
				transaction.setPayToType(ClientWriteCheck.TYPE_TAX_AGENCY);

				break;
			}
		}
		transaction.setTotal(amtText.getAmount());
		transaction.setAmount(amtText.getAmount());
		transaction.setInWords(amtText.getValue().toString());

		// Setting Date
		if (date != null)
			transaction.setDate(date.getValue().getDate());
		// setting transactoin number
		transaction.setNumber(transactionNumber.getValue().toString());

		// setting checknumber
		transaction.setCheckNumber(checkNo);

		// setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		if (vatinclusiveCheck != null) {
			transaction.setAmountsIncludeVAT(vatinclusiveCheck.getValue());
		}

		if (isTrackDiscounts()) {
			if (discountField.getAmount() != 0.0 && transactionItems != null) {
				for (ClientTransactionItem item : transactionItems) {
					item.setDiscount(discountField.getAmount());
				}
			}
		}

		if (toprintCheck.getValue() != null) {
			transaction.setToBePrinted(toprintCheck.getValue());

		}
		if (currency != null) {
			transaction.setCurrency(currency.getID());
			transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
		}
	}

	private void updatePaySalesTax() {
		transactionSuccess(transaction);

	}

	@Override
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();

		// setTitle(bankingConstants.writeCheck());
		Label titleLabel = new Label(messages.writeCheck() + "("
				+ getTransactionStatus() + ")");
		titleLabel.removeStyleName("gwt-Label");
		titleLabel.addStyleName("label-title");

		transactionNumber = createTransactionNumberItem();
		locationCombo = createLocationCombo();
		locationCombo.setHelpInformation(isInViewMode());
		date = createTransactionDateItem();
		date.setShowTitle(false);
		date.setColSpan(2);
		date.setDisabled(isInViewMode());

		numForm = new DynamicForm();
		numForm.setNumCols(6);
		numForm.addStyleName("datenumber-panel");
		if (isTemplate) {
			numForm.setFields(transactionNumber);
		} else {
			numForm.setFields(date, transactionNumber);
		}

		nHPanel = new VerticalPanel();
		nHPanel.setCellHorizontalAlignment(numForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		nHPanel.add(numForm);

		labelLayout = new HorizontalPanel();
		labelLayout.setWidth("100%");
		labelLayout.add(nHPanel);
		labelLayout.setCellHorizontalAlignment(nHPanel,
				HasHorizontalAlignment.ALIGN_RIGHT);

		balText = new AmountField(messages.balance(), this, getBaseCurrency());
		balText.setWidth(100);
		balText.setDisabled(true);

		bankAccSelect = new PayFromAccountsCombo(messages.bankAccount());
		// bankAccSelect.setWidth(100);
		bankAccSelect.setRequired(true);
		bankAccSelect.setDisabled(isInViewMode());
		bankAccSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectBankAcc = selectItem;
						balText.setAmount(selectBankAcc
								.getTotalBalanceInAccountCurrency());
						balText.setCurrency(getCurrency(selectItem
								.getCurrency()));
					}

				});
		bankAccSelect.setDefaultPayFromAccount();

		bankAccForm = new DynamicForm();
		if (locationTrackingEnabled)
			bankAccForm.setFields(locationCombo);
		bankAccForm.setFields(bankAccSelect, balText);

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			bankAccForm.setFields(classListCombo);
		}
		// bankAccForm.getCellFormatter().setWidth(0, 0, "232px");
		// forms.add(bankAccForm);

		paytoSelect = new PayeeCombo(messages.payTo());
		// paytoSelect.setWidth(100);
		// paytoSelect.setRequired(true);
		paytoSelect.setDisabled(isInViewMode());
		paytoSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPayee>() {
					@Override
					public void selectedComboBoxItem(ClientPayee selectItem) {
						amtText.setValue("0.00");
						if (payee != null) {

							transactionVendorAccountTable.resetRecords();
							transaction
									.setTransactionItems(transactionVendorAccountTable
											.getRecords());
							if (taxCodeSelect.getSelectedValue() != null) {
								transactionVendorAccountTable.setTaxCode(
										taxCodeSelect.getSelectedValue()
												.getID(), true);
							}
							vatTotalNonEditableText.setTransaction(transaction);

							// } else if (payee instanceof ClientTAXAgency)
							// {
							// taxAgencyGrid.removeAllRecords();
						}

						if (isInViewMode() && payee != null) {
							if (payee.getType() != selectItem.getType()) {
								Accounter.showError(messages
										.youcannotchangeaCustomertypetoVendortype(
												Global.get().customer(), Global
														.get().Vendor()));
								paytoSelect.setComboItem(payee);
							} else {

								PayToSelected(selectItem);

								payee = selectItem;
							}
						} else {
							payee = selectItem;
						}
						updateAddressAndGrid();

					}

				});

		amtText = new AmountField(messages.amount(), this, getBaseCurrency());
		amtText.setWidth(100);
		amtText.setAmount(0.00);
		amtText.setDisabled(isInViewMode());
		amtText.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if ((amtText.getAmount() != 0)
						&& !(DecimalUtil.isEquals(previousValue,
								amtText.getAmount()))) {
					previousValue = amtText.getAmount();
					isAmountChange = true;
					validateAmountAndTotal();
				}
			}
		});

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		memoTextAreaItem.setDisabled(false);

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		toprintCheck = new CheckboxItem(messages.toBePrinted());
		toprintCheck.setDisabled(false);
		toprintCheck.setValue(true);

		payForm = new DynamicForm();
		// payForm.setWidth("100%");
		payForm.setFields(paytoSelect, amtText);
		// payForm.getCellFormatter().setWidth(0, 0, "170px");

		currencyWidget = createCurrencyFactorWidget();

		VerticalPanel currencyPanel = new VerticalPanel();
		// currencyPanel.setWidth("100%");
		currencyPanel.add(bankAccForm);
		currencyPanel.setCellHorizontalAlignment(bankAccForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		if (isMultiCurrencyEnabled()) {
			currencyPanel.add(currencyWidget);
			currencyWidget.setDisabled(isInViewMode());
			currencyPanel.setCellHorizontalAlignment(currencyWidget,
					HasHorizontalAlignment.ALIGN_RIGHT);
		}

		HorizontalPanel accPanel = new HorizontalPanel();
		accPanel.addStyleName("fields-panel");
		accPanel.setWidth("100%");
		accPanel.add(payForm);
		accPanel.add(currencyPanel);
		accPanel.setCellHorizontalAlignment(currencyPanel,
				HasHorizontalAlignment.ALIGN_RIGHT);

		VerticalPanel topHLay = new VerticalPanel();
		topHLay.setWidth("100%");
		topHLay.add(labelLayout);
		topHLay.add(accPanel);

		vatPanel = new VerticalPanel();
		amountPanel = new VerticalPanel();
		vatPanel.setWidth("100%");
		amountPanel.setWidth("100%");
		vatinclusiveCheck = getVATInclusiveCheckBox();
		foreignCurrencyamountLabel = createTransactionTotalNonEditableLabel(getBaseCurrency());

		transactionTotalBaseCurrencyText = createTransactionTotalNonEditableLabel(getBaseCurrency());

		vatTotalNonEditableText = new TaxItemsForm();

		netAmount = new AmountLabel(messages.netAmount());

		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setWidth("100%");
		bottomPanel.add(memoForm);
		VerticalPanel totalForm = new VerticalPanel();

		DynamicForm netAmountForm = new DynamicForm();
		netAmountForm.setNumCols(2);
		netAmountForm.setFields(netAmount);
		totalForm.add(netAmountForm);
		discountField = getDiscountField();

		DynamicForm form = new DynamicForm();
		if (isTrackTax()) {
			totalForm.add(vatTotalNonEditableText);
			if (!isTaxPerDetailLine()) {
				taxCodeSelect = createTaxCodeSelectItem();
				// taxCodeSelect.setVisible(isInViewMode());
				form.setFields(taxCodeSelect);
				vatPanel.setCellHorizontalAlignment(form, ALIGN_CENTER);
				vatPanel.add(form);
				vatPanel.add(form);
				vatPanel.setCellHorizontalAlignment(form, ALIGN_RIGHT);
			}
			if (isTrackPaidTax()) {
				form.setFields(vatinclusiveCheck);
				form.addStyleName("boldtext");
			}
		}

		if (isTrackDiscounts()) {
			if (!isDiscountPerDetailLine()) {
				form.setFields(discountField);
				vatPanel.add(form);
			}
		}

		DynamicForm transactionTotalForm = new DynamicForm();
		transactionTotalForm.setNumCols(2);

		transactionTotalForm.setFields(foreignCurrencyamountLabel);

		if (isMultiCurrencyEnabled()) {
			transactionTotalForm.setFields(transactionTotalBaseCurrencyText);
		}
		totalForm.add(transactionTotalForm);
		totalForm.addStyleName("boldtext");
		totalForm.setCellHorizontalAlignment(netAmountForm, ALIGN_RIGHT);
		totalForm.setCellHorizontalAlignment(vatTotalNonEditableText,
				ALIGN_RIGHT);
		totalForm.setCellHorizontalAlignment(transactionTotalForm, ALIGN_RIGHT);

		unassignedAmountPanel = new HorizontalPanel();

		amountPanel.add(unassignedAmountPanel);
		amountPanel.add(totalForm);

		Button recalculateButton = new Button(messages.recalculate());

		recalculateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isAmountChange = false;
				updateNonEditableItems();
				hideUnassignedFields();
			}

		});

		DynamicForm unassignedAmountForm = new DynamicForm();
		unassignedAmount = new AmountLabel(messages.unassignedAmount());
		unassignedAmountForm.setFields(unassignedAmount);

		unassignedAmountPanel.add(recalculateButton);
		unassignedAmountPanel.add(unassignedAmountForm);

		amountPanel.setCellHorizontalAlignment(totalForm, ALIGN_RIGHT);
		amountPanel.setHorizontalAlignment(ALIGN_RIGHT);
		hideUnassignedFields();

		mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");

		// For Editing writeCheck or PaySalesTax.
		// if (transactionObject != null) {
		// transactionItems = transactionObject.getTransactionItems();
		// if (takenPaySalesTax != null) {
		// paytoSelect.setRequired(false);
		// memoTextAreaItem.setDisabled(true);
		// toprintCheck.setDisabled(true);
		// transactionNumber = takenPaySalesTax.getNumber();
		// amtText.setAmount(takenPaySalesTax.getTotal());
		// date.setValue(takenPaySalesTax.getDate());
		// text.setValue(Utility.getNumberInWords(String
		// .valueOf(takenPaySalesTax.getTotal())));
		//
		// setGridType(TAXAGENCY_TRANSACTION_GRID);
		// taxAgencyGrid = new TaxAgencyTransactionGrid();
		// taxAgencyGrid.setTransactionView(this);
		// taxAgencyGrid.setCanEdit(true);
		// taxAgencyGrid.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		// taxAgencyGrid.init();
		// taxAgencyGrid.setVisible(true);
		// taxAgencyGrid.setTransactionPaySalesTax(takenPaySalesTax
		// .getTransactionPaySalesTax());
		// taxAgencyGrid.setVisible(false);
		// taxAgencyGrid = new TaxAgencyTransactionGrid();
		// taxAgencyGrid.init();
		// taxAgencyGrid.setVisible(true);
		// // taxAgencyGrid.setTransactionPaySalesTax(takenPaySalesTax
		// // .getTransactionPaySalesTax());
		// taxAgencyGrid.setVisible(true);
		// mainVLay.add(lab1);
		// nText
		// .setValue(writeCheckTaken.isToBePrinted() ? bankingConstants
		// .toBePrinted()
		// : transactionNumber != null ? transactionNumber
		// : "");
		// mainVLay.add(labelLayout);
		// mainVLay.add(topHLay);
		// mainVLay.add(taxAgencyGrid);
		//
		// }
		// if{

		transactionVendorAccountTable = new VendorAccountTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				WriteChequeView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return WriteChequeView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return WriteChequeView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getAmount() != null
						&& discountField.getAmount() != 0) {
					row.setDiscount(discountField.getAmount());
				}
				WriteChequeView.this.updateNonEditableItems();
			}
		};

		transactionVendorAccountTable.setDisabled(isInViewMode());

		accountTableButton = new AddNewButton();
		accountTableButton.setEnabled(!isInViewMode());
		accountTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAccount();
			}
		});

		FlowPanel vendorAccountFlowPanel = new FlowPanel();
		vendorAccountsDisclosurePanel = new DisclosurePanel(
				messages.ItemizebyAccount());
		vendorAccountFlowPanel.add(transactionVendorAccountTable);
		vendorAccountFlowPanel.add(accountTableButton);
		vendorAccountsDisclosurePanel.setContent(vendorAccountFlowPanel);
		vendorAccountsDisclosurePanel.setOpen(true);
		vendorAccountsDisclosurePanel.setWidth("100%");

		if (isInViewMode()) {
			transactionItems = transaction.getTransactionItems();
			transactionNumber.setValue(transaction.getNumber());

			amtText.setAmount(transaction.getTotal());
			memoTextAreaItem.setValue(transaction.getMemo());
			date.setValue(transaction.getDate());
			toprintCheck.setValue(transaction.isToBePrinted());
			toprintCheck.setDisabled(true);
			// nText.setValue(writeCheckTaken.isToBePrinted() ? bankingConstants
			// .toBePrinted()
			// : transactionNumber != null ? transactionNumber : "");
			// }

		}
		mainVLay.add(titleLabel);
		mainVLay.add(voidedPanel);
		mainVLay.add(topHLay);
		mainVLay.add(vendorAccountsDisclosurePanel);

		vPanel = new VerticalPanel();
		vPanel.setWidth("100%");
		// vPanel.add(createAddNewButton());
		// menuButton.getElement().getStyle().setMargin(5, Unit.PX);

		// HorizontalPanel bottomPanel = new HorizontalPanel();
		// bottomPanel.setWidth("100%");
		// bottomPanel.add(memoForm);
		// if (isTrackTax()) {
		// if (!isTaxPerDetailLine()) {
		// taxCodeSelect = createTaxCodeSelectItem();
		// // taxCodeSelect.setVisible(isInViewMode());
		// DynamicForm form = new DynamicForm();
		// form.setFields(taxCodeSelect);
		// bottomPanel.add(form);
		// }
		// }
		// // if (getCompany().getPreferences().isRegisteredForVAT()) {
		bottomPanel.add(vatPanel);
		bottomPanel.add(amountPanel);

		// }

		vPanel.add(bottomPanel);
		bottomPanel.setCellHorizontalAlignment(memoForm, ALIGN_LEFT);
		vPanel.getElement().getStyle().setMarginTop(8, Unit.PX);

		mainVLay.add(vPanel);

		this.setWidth("100%");
		this.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(numForm);

		listforms.add(bankAccForm);
		listforms.add(payForm);
		// listforms.add(amtForm);

		settabIndexes();

		if (isMultiCurrencyEnabled() && !isInViewMode()) {
			transactionTotalBaseCurrencyText.hide();
		}

	}

	// protected void setCheckNumber() {
	//
	// rpcUtilService.getNextCheckNumber(selectBankAcc.getID(),
	// new AccounterAsyncCallback<Long>() {
	//
	// public void onFailure(Throwable t) {
	// // //UIUtils.logError(
	// // "Failed to get the next check number!!", t);
	// nText.setValue(bankingConstants.toBePrinted());
	// checkNo = ClientWriteCheck.IS_TO_BE_PRINTED;
	// return;
	// }
	//
	// public void onSuccess(Long result) {
	// if (result == null)
	// onFailure(null);
	//
	// checkNo = String.valueOf(result);
	// nText.setValue(result.toString());
	//
	// }
	//
	// });
	//
	// }

	//
	// private void setDisableStaeForFormItems() {
	//
	// for (FormItem formItem : formItems) {
	//
	// if (formItem != null)
	// formItem.setDisabled(isEdit);
	//
	// }
	//
	// }

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setDisabled(isInViewMode());
		setMemoTextAreaItem(transaction.getMemo());

	}

	// @Override
	// public void setViewConfiguration(ViewConfiguration viewConfiguration)
	// throws Exception {
	//
	// super.setViewConfiguration(viewConfiguration);
	// if (isEdit) {
	// if (!transactionObject.isWriteCheck()) {
	//
	// if (!transactionObject.isPaySalesTax())
	// throw new Exception("Failed to load....");
	// else {
	// takenPaySalesTax = (ClientPaySalesTax) transactionObject;
	// transactionType = ClientTransaction.TYPE_PAY_SALES_TAX;
	//
	// }
	// }
	//
	// }
	//
	// }

	@Override
	public void updateNonEditableItems() {

		// if (vendorAccountTransactionTable == null
		// || vendorItemTransactionTable == null) {
		// return;
		// }
		// double lineTotal = vendorAccountTransactionTable.getLineTotal()
		// + vendorItemTransactionTable.getLineTotal();
		// double grandTotal = vendorAccountTransactionTable.getGrandTotal()
		// + vendorItemTransactionTable.getGrandTotal();
		//
		// netAmount.setAmount(lineTotal);
		// if (getCompany().getPreferences().isTrackPaidTax()) {
		// vatTotalNonEditableText.setAmount(grandTotal - lineTotal);
		// }
		// transactionTotalNonEditableText
		// .setAmount(getAmountInBaseCurrency(grandTotal));
		// foreignCurrencyamountLabel.setAmount(grandTotal);
		//
		if (transactionVendorAccountTable == null) {
			return;
		}
		double total = transactionVendorAccountTable.getGrandTotal();

		if (!isAmountChange) {
			this.amtText.setAmount(total);
			previousValue = amtText.getAmount();
		}
		double grandTotal = transactionVendorAccountTable.getLineTotal();
		if (getPreferences().isTrackPaidTax()) {
			if (transaction.getTransactionItems() != null && !isInViewMode()) {
				transaction.setTransactionItems(transactionVendorAccountTable
						.getAllRows());
			}
			vatTotalNonEditableText.setTransaction(transaction);
		}
		netAmount.setAmount(grandTotal);

		foreignCurrencyamountLabel.setAmount(total);

		transactionTotalBaseCurrencyText
				.setAmount(getAmountInBaseCurrency(total));

		if (amtText.getAmount() == 0) {
			amtText.setAmount(foreignCurrencyamountLabel.getAmount());
			previousValue = amtText.getAmount();
		}
		if (isAmountChange)
			validateAmountAndTotal();

	}

	private void validateAmountAndTotal() {
		unassignedAmount.setAmount(amtText.getAmount()
				- foreignCurrencyamountLabel.getAmount());
		if (unassignedAmount.getAmount() != 0) {
			showUnassignedFields();
		} else {
			hideUnassignedFields();
		}
	}

	// @Override
	// public void onDraw() {
	// this.nText.setDisabled(true);
	// }

	private void showUnassignedFields() {
		unassignedAmountPanel.setVisible(true);
	}

	private void hideUnassignedFields() {
		unassignedAmountPanel.setVisible(false);
	}

	@Override
	public void showMenu(Widget button) {
		// if (payee != null) {
		// switch (payee.getType()) {
		// case ClientWriteCheck.TYPE_CUSTOMER:
		// case ClientWriteCheck.TYPE_VENDOR:
		// case ClientWriteCheck.TYPE_TAX_AGENCY:
		// setMenuItems(button, messages.accounts(
		// Global.get().Account()), Accounter.constants()
		// .productItem()
		// FinanceApplication.constants().comment(),
		// FinanceApplication.constants()
		// .salesTax()
		// );
		// break;
		// case ClientWriteCheck.TYPE_VENDOR:
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US)
		// setMenuItems(FinanceApplication.constants()
		// .nominalCodeItem(), FinanceApplication
		// .constants().product()
		// // FinanceApplication.constants().comment()
		// );
		// else
		// setMenuItems(FinanceApplication.constants()
		// .nominalCodeItem(), FinanceApplication
		// .constants().product(),
		// FinanceApplication.constants().VATItem(),
		// FinanceApplication.constants().comment());
		// break;
		// case ClientWriteCheck.TYPE_TAX_AGENCY:
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US)
		// setMenuItems(FinanceApplication.constants()
		// .nominalCodeItem(), FinanceApplication
		// .constants().product()
		// // FinanceApplication.constants().comment()
		// );
		// else
		// setMenuItems(FinanceApplication.constants()
		// .nominalCodeItem(), FinanceApplication
		// .constants().product(),
		// FinanceApplication.constants().VATItem(),
		// FinanceApplication.constants().comment());
		// }
		// } else
		setMenuItems(button, messages.Account(),
				messages.productOrServiceItem());
		// FinanceApplication.constants().comment(),
		// FinanceApplication.constants().salesTax()
		// );

	}

	@Override
	protected void onAddNew(String item) {
		// FIXME Need to add new transaction item to appropriate grid.
		ClientTransactionItem transactionItem = new ClientTransactionItem();
		if (payee != null) {
			if (payee.getType() == ClientWriteCheck.TYPE_CUSTOMER) {
				if (item.equals(messages.Accounts())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
				} else if (item.equals(messages.productOrServiceItem())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ITEM);

				} else if (item.equals(messages.comment())) {
					transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
				}
				// else if (item.equals("Sales Tax")) {
				// transactionItem
				// .setType(ClientTransactionItem.TYPE_SALESTAX);
				// }
				// transactionCustomerTable.add(transactionItem);
			} else if (payee.getType() == ClientWriteCheck.TYPE_VENDOR
					|| payee.getType() == ClientWriteCheck.TYPE_TAX_AGENCY) {
				if (item.equals(messages.Accounts())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
				} else if (item.equals(messages.productOrServiceItem())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
				} else if (item.equals(messages.comment())) {
					transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
				}
				// transactionVendorTable.add(transactionItem);
			}
		} else {
			if (item.equals(messages.Accounts())) {
				transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
			} else if (item.equals(messages.productOrServiceItem())) {
				transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
			} else if (item.equals(messages.comment())) {
				transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
			}
			// else if (item.equals(Accounter.constants().salesTax())) {
			// transactionItem.setType(ClientTransactionItem.TYPE_SALESTAX);
			// }
			// transactionCustomerTable.add(transactionItem);
		}
	}

	@Override
	public void onAddNew() {
		// ClientTransactionItem transactionItem = new ClientTransactionItem();
		// taxAgencyGrid.addData(transactionItem);
	}

	@Override
	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.paytoSelect.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				Accounter.showError(AccounterExceptions
						.getErrorString(errorCode));
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		date.setDisabled(isInViewMode());
		paytoSelect.setDisabled(isInViewMode());
		// billToCombo.setDisabled(isInViewMode());
		amtText.setDisabled(isInViewMode());
		toprintCheck.setDisabled(isInViewMode());
		bankAccSelect.setDisabled(isInViewMode());
		if (transactionVendorAccountTable != null)
			transactionVendorAccountTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		memoTextAreaItem.setDisabled(false);
		discountField.setDisabled(isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		if (isTrackTax()) {
			if (!isTaxPerDetailLine()) {
				taxCodeSelect.setDisabled(isInViewMode());
			}
		}
		if (currencyWidget != null) {
			currencyWidget.setDisabled(isInViewMode());
		}

		super.onEdit();

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	public void PayToSelected(ClientPayee selectItem) {

		ClientCurrency payeeCurrency = getCompany().getCurrency(
				selectItem.getCurrency());

		amtText.setCurrency(payeeCurrency);

		// ClientWriteCheck check = (ClientWriteCheck) this.transaction;

		// FIXME Need to set transaction items.
		// if (selectItem instanceof ClientCustomer) {
		// transactionCustomerTable.removeAllRecords();
		// if (check.getCustomer() == selectItem.getID()) {
		// transactionCustomerTable
		// .setRecords(check.getTransactionItems());
		// }
		//
		// } else if (selectItem instanceof ClientVendor) {
		// transactionVendorTable.removeAllRecords();
		// if (check.getVendor() == selectItem.getID()) {
		// transactionVendorTable.setRecords(check.getTransactionItems());
		// }
		//
		// } else if (selectItem instanceof ClientTAXAgency) {
		// transactionVendorTable.removeAllRecords();
		// if (check.getTaxAgency() == selectItem.getID()) {
		// transactionVendorTable.setRecords(check.getTransactionItems());
		// }
		//
		// }

		if (currency.getID() != 0) {
			currencyWidget.setSelectedCurrency(payeeCurrency);
		} else {
			currencyWidget.setSelectedCurrency(getBaseCurrency());
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(currency);
			setCurrencyFactor(1.0);
			updateAmountsFromGUI();
			modifyForeignCurrencyTotalWidget();
		}

	}

	@Override
	protected String getViewTitle() {
		return messages.writeCheck();
	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientWriteCheck());
		} else {

			if (currencyWidget != null) {
				this.currency = getCompany().getCurrency(
						transaction.getCurrency());
				this.currencyFactor = transaction.getCurrencyFactor();
				if (currency != null)
					currencyWidget.setSelectedCurrency(this.currency);
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
			}
			// if (getPreferences().isTrackPaidTax()) {
			// netAmount.setAmount(transaction.getNetAmount());
			// vatTotalNonEditableText.setAmount(transaction.getTotal()
			// - transaction.getNetAmount());
			// }
			if (isTrackTax()) {
				if (isTaxPerDetailLine()) {
					netAmount.setAmount(transaction.getNetAmount());
					vatTotalNonEditableText.setTransaction(transaction);
				} else {
					this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
					if (taxCode != null) {
						taxCodeSelected(taxCode);
					}
				}
			}

			if (transaction.getTransactionItems() != null) {
				if (isTrackDiscounts()) {
					if (!isDiscountPerDetailLine()) {
						this.discountField
								.setAmount(getdiscount(this.transactionItems));
					}
				}
			}

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());

			}
			initMemoAndReference();
			initAccounterClass();
		}
		initTransactionNumber();
		initPayToCombo();

		setDisableFields();
		initBankaccountCombo();
		updateAddressAndGrid();
		amtText.setAmount(transaction.getAmount());
		if (transaction.getAmount() != 0) {
			validateAmountAndTotal();
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		vendorAccountsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ACCOUNT, true));
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(transactionVendorAccountTable.getAllRows());
		return list;

	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		// FIXME
		// if (ClientWriteCheck.TYPE_CUSTOMER == transaction.getPayToType()) {
		// transactionCustomerTable.add(transactionItem);
		// } else {
		// transactionVendorTable.add(transactionItem);
		// }
	}

	@Override
	protected void refreshTransactionGrid() {
		if (transactionVendorAccountTable != null) {
			transactionVendorAccountTable.updateTotals();
		}
	}

	private void settabIndexes() {
		paytoSelect.setTabIndex(1);
		amtText.setTabIndex(3);
		date.setTabIndex(4);
		transactionNumber.setTabIndex(5);
		bankAccSelect.setTabIndex(6);
		balText.setTabIndex(7);
		memoTextAreaItem.setTabIndex(8);
		vatinclusiveCheck.setTabIndex(9);
		saveAndCloseButton.setTabIndex(11);
		saveAndNewButton.setTabIndex(12);
		cancelButton.setTabIndex(13);

	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		transactionVendorAccountTable.add(item);
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {
			taxCodeSelect.setComboItem(taxCode);
			transactionVendorAccountTable.setTaxCode(taxCode.getID(), true);
		} else {
			taxCodeSelect.setValue("");
		}

	}

	@Override
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		this.transactionVendorAccountTable.updateAmountsFromGUI();
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		// TODO Auto-generated method stub
	}

	public void modifyForeignCurrencyTotalWidget() {
		if (currencyWidget.isShowFactorField()) {
			transactionTotalBaseCurrencyText.hide();
		} else {
			transactionTotalBaseCurrencyText.show();
			transactionTotalBaseCurrencyText.setTitle(messages
					.currencyTotal(getBaseCurrency().getFormalName()));
		}
	}

	@Override
	protected void updateDiscountValues() {

		if (discountField.getAmount() != null) {
			transactionVendorAccountTable
					.setDiscount(discountField.getAmount());
		} else {
			discountField.setAmount(0d);
		}
	}

}
