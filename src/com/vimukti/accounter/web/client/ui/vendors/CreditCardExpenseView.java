package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.AbstractBankTransactionView;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.ContactCombo;
import com.vimukti.accounter.web.client.ui.combo.CreditCardAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CreditCardExpenseView extends
		AbstractBankTransactionView<ClientCreditCardCharge> {

	protected List<String> selectedComboList;
	protected DateField date, delivDate;;
	protected TextItem cheqNoText;
	AmountField totText;
	List<String> idPhoneNumberForContacts = new ArrayList<String>();
	List<String> idNamesForContacts = new ArrayList<String>();

	protected DynamicForm vendorForm, addrForm, phoneForm, termsForm, memoForm;
	protected SelectCombo payMethSelect;
	protected CreditCardAccountCombo creditCardCombo;

	protected String selectPaymentMethod;

	private DynamicForm totForm;

	private HorizontalPanel botPanel;
	HorizontalPanel totPanel;

	private VerticalPanel leftVLay;

	private ArrayList<DynamicForm> listforms;
	protected Label titlelabel;
	// protected TextAreaItem billToAreaItem;
	private List<ClientAccount> listOfAccounts;

	private final boolean locationTrackingEnabled;

	private VendorAccountTransactionTable vendorAccountTransactionTable;
	private VendorItemTransactionTable vendorItemTransactionTable;
	private AddNewButton accountTableButton, itemTableButton;
	private DisclosurePanel accountsDisclosurePanel, itemsDisclosurePanel;

	private TAXCodeCombo taxCodeSelect;

	public CreditCardExpenseView() {

		super(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
	}

	@Override
	protected void createControls() {
		String vendorString = null;

		vendorString = messages.payeeName(Global.get().Vendor());

		vendorCombo = new VendorCombo(vendorString, true) {
			@Override
			public void initCombo(List<ClientVendor> list) {
				List<ClientVendor> ccVendors = new ArrayList<ClientVendor>();
				for (ClientVendor vdr : list) {
					// if (vdr.getVendorGroup() != 0) {
					// ClientVendorGroup vendorGrougp = Accounter.getCompany()
					// .getVendorGroup(vdr.getVendorGroup());
					// if (vendorGrougp.getName().equals(
					// AccounterClientConstants.CREDIT_CARD_COMPANIES)) {
					ccVendors.add(vdr);
					// }
					// }
				}
				super.initCombo(ccVendors);
			}

			@Override
			public void onAddNew() {
				NewVendorAction action = ActionFactory.getNewVendorAction();

				action.setCallback(new ActionCallback<ClientVendor>() {

					@Override
					public void actionResult(ClientVendor result) {
						if (result.getName() != null)
							addItemThenfireEvent(result);

					}
				});
				action.setOpenedFrom(NewVendorAction.FROM_CREDIT_CARD_EXPENSE);
				action.run(null, true);

			}
		};
		vendorCombo.setHelpInformation(true);
		vendorCombo.setRequired(false);
		if (!isTaxPerDetailLine())
			taxCodeSelect = createTaxCodeSelectItem();
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						selectedVendor = selectItem;

						vendorCombo.setComboItem(selectItem);
						addPhonesContactsAndAddress();
						long code = selectedVendor.getTAXCode();
						if (taxCodeSelect != null) {
							if (code == 0)
								code = Accounter.getCompany()
										.getDefaultTaxCode();
							taxCodeSelect.setComboItem(getCompany().getTAXCode(
									code));
						}
						ClientCurrency currency = getCurrency(selectedVendor
								.getCurrency());
						if (currency.getID() != 0) {
							currencyWidget.setSelectedCurrencyFactorInWidget(
									currency, transactionDateItem.getDate()
											.getDate());
						} else {
							currencyWidget
									.setSelectedCurrency(getBaseCurrency());
						}
						modifyForeignCurrencyTotalWidget();
					}

				});

		String listString[] = new String[] {
				messages.cash(),
				UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
						.messages().check()), messages.creditCard(),
				messages.directDebit(), messages.masterCard(),
				messages.onlineBanking(), messages.standingOrder(),
				messages.switchMaestro() };

		if (isInViewMode()) {
			ClientCreditCardCharge creditCardCharge = transaction;
			vendorCombo.setComboItem(getCompany().getVendor(
					creditCardCharge.getVendor()));
			vendorCombo.setDisabled(true);
		}
		// vendorForm.setFields(vendorCombo, contactNameSelect, phoneSelect,
		// billToAreaItem);
		// vendorForm.getCellFormatter().setWidth(0, 0, "180px");
		// vendorForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		// VerticalPanel verticalPanel = (VerticalPanel) vendorForm.getParent();
		// vendorForm.removeFromParent();
		// verticalPanel.add(vendorForm);
		// verticalPanel.setSpacing(10);

		titlelabel = new Label(messages.creditCardExpense());
		titlelabel.removeStyleName("gwt-Label");
		titlelabel.addStyleName(messages.labelTitle());
		// titlelabel.setHeight("35px");
		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(8);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();

		VerticalPanel regPanel = new VerticalPanel();
		regPanel.setCellHorizontalAlignment(dateNoForm, ALIGN_RIGHT);
		regPanel.add(dateNoForm);
		regPanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(regPanel);
		labeldateNoLayout.setCellHorizontalAlignment(regPanel, ALIGN_RIGHT);

		contactCombo = new ContactCombo(messages.contact(), true);
		contactCombo.setHelpInformation(true);
		contactCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientContact>() {

					@Override
					public void selectedComboBoxItem(ClientContact selectItem) {

						contactSelected(selectItem);

					}

				});
		contactCombo.addNewContactHandler(new ValueCallBack<ClientContact>() {

			@Override
			public void execute(ClientContact value) {
				addContactToVendor(value);
			}
		});
		contactCombo.setDisabled(isInViewMode());

		// formItems.add(contactCombo);

		// contactNameSelect = new
		// SelectCombo(messages.contactName());
		// contactNameSelect.setHelpInformation(true);
		// contactNameSelect
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<String>() {
		//
		// @Override
		// public void selectedComboBoxItem(String selectItem) {
		// contactNameSelect.setSelected(selectItem);
		//
		// int i = 0;
		// while (i < idNamesForContacts.size()) {
		// String s = idNamesForContacts.get(i);
		// if (s.equals(selectItem))
		// phoneSelect.setValue(idPhoneNumberForContacts
		// .get(i));
		//
		// i++;
		// }
		//
		// }
		// });
		// contactNameSelect.setWidth(100);
		// formItems.add(contactNameSelect);
		// billToCombo = createBillToComboItem();

		// billToAreaItem = new TextAreaItem(messages.billTo());
		// billToAreaItem.setWidth(100);
		// billToAreaItem.setDisabled(true);
		// formItems.add(billToCombo);
		phoneSelect = new TextItem(messages.phone());
		phoneSelect.setToolTip(messages.phoneNumberOf(this.getAction()
				.getCatagory()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		// formItems.add(phoneSelect);

		vendorForm = UIUtils.form(messages.vendor());
		vendorForm.setWidth("100%");
		vendorForm.setFields(vendorCombo, contactCombo, phoneSelect
		/* billToAreaItem */);
		vendorForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		// vendorForm.getCellFormatter().setWidth(0, 0, "180px");

		payMethSelect = new SelectCombo(messages.paymentMethod());
		payMethSelect.setRequired(true);
		payMethSelect.setWidth(100);
		List<String> paymentMthds = new ArrayList<String>();
		paymentMthds.add(messages.creditCard());
		payMethSelect.initCombo(paymentMthds);
		payMethSelect.setDefaultToFirstOption(true);
		payMethSelect.setDisabled(true);
		// payMethSelect.setComboItem(UIUtils
		// .getpaymentMethodCheckBy_CompanyType(messages
		// .check()));

		creditCardCombo = createCreditCardItem();
		creditCardCombo.setWidth(100);
		creditCardCombo.setPopupWidth("510px");
		creditCardCombo.setTitle(messages.payFrom());
		payFromAccount = 0;
		creditCardCombo.setColSpan(0);
		// formItems.add(payFrmSelect)
		cheqNoText = new TextItem(messages.chequeNo());

		cheqNoText.setHelpInformation(true);
		cheqNoText.setDisabled(isInViewMode());
		cheqNoText.setWidth(100);
		// formItems.add(cheqNoText);

		delivDate = new DateField(messages.deliveryDate());
		delivDate.setHelpInformation(true);
		delivDate.setColSpan(1);
		delivDate.setValue(new ClientFinanceDate());
		// formItems.add(delivDate);

		termsForm = UIUtils.form(messages.terms());
		termsForm.setWidth("100%");
		if (locationTrackingEnabled)
			termsForm.setFields(locationCombo);
		termsForm.setFields(creditCardCombo, payMethSelect, delivDate);
		// termsForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "203px");
		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			termsForm.setFields(classListCombo);
		}

		netAmount = new AmountLabel(messages.netAmount());
		netAmount.setDefaultValue(String.valueOf(0.00));
		netAmount.setDisabled(true);

		transactionTotalBaseCurrencyText = createTransactionTotalNonEditableLabel(getBaseCurrency());

		transactionTotalTransactionCurrencyText = createTransactionTotalNonEditableLabel(getBaseCurrency());

		vatTotalNonEditableText = createVATTotalNonEditableLabel();

		// vatinclusiveCheck = new CheckboxItem(messages
		// .amountIncludesVat());
		vatinclusiveCheck = getVATInclusiveCheckBox();

		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackTax() && isTrackPaidTax(), isTaxPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CreditCardExpenseView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CreditCardExpenseView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CreditCardExpenseView.this.isInViewMode();
			}
		};

		vendorAccountTransactionTable.setDisabled(isInViewMode());

		accountTableButton = new AddNewButton();
		accountTableButton.setEnabled(!isInViewMode());
		accountTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAccount();
			}
		});
		FlowPanel accountFlowPanel = new FlowPanel();
		accountsDisclosurePanel = new DisclosurePanel("Itemize by Account");
		accountFlowPanel.add(vendorAccountTransactionTable);
		accountFlowPanel.add(accountTableButton);
		accountsDisclosurePanel.setContent(accountFlowPanel);
		accountsDisclosurePanel.setOpen(true);
		accountsDisclosurePanel.setWidth("100%");
		vendorItemTransactionTable = new VendorItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CreditCardExpenseView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CreditCardExpenseView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CreditCardExpenseView.this.isInViewMode();
			}
		};

		vendorItemTransactionTable.setDisabled(isInViewMode());

		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});

		vatinclusiveCheck = getVATInclusiveCheckBox();
		FlowPanel itemsFlowPanel = new FlowPanel();
		itemsDisclosurePanel = new DisclosurePanel("Itemize by Product/Service");
		itemsFlowPanel.add(vendorItemTransactionTable);
		itemsFlowPanel.add(itemTableButton);
		itemsDisclosurePanel.setContent(itemsFlowPanel);
		itemsDisclosurePanel.setWidth("100%");
		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		memoTextAreaItem.setDisabled(false);

		// refText = new TextItem(messages.reference());
		//
		// refText.setWidth(100);
		// refText.setDisabled(false);

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);

		DynamicForm totalForm = new DynamicForm();
		totalForm.setNumCols(2);
		totalForm.setWidth("100%");
		totalForm.setStyleName("boldtext");
		// totText = new AmountField(FinanceApplication.constants()
		// .total());
		// totText.setWidth(100);

		totForm = new DynamicForm();
		totForm.setWidth("100%");
		totForm.addStyleName("unused-payments");
		totForm.getElement().getStyle().setMarginTop(10, Unit.PX);

		botPanel = new HorizontalPanel();
		botPanel.setWidth("100%");

		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");
		currencyWidget = createCurrencyFactorWidget();
		if (isTrackTax()) {
			if (isMultiCurrencyEnabled()) {
				totalForm.setFields(netAmount, vatTotalNonEditableText,
						transactionTotalBaseCurrencyText,
						transactionTotalTransactionCurrencyText);
			} else {
				totalForm.setFields(netAmount, vatTotalNonEditableText,
						transactionTotalBaseCurrencyText);
			}
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setHorizontalAlignment(ALIGN_RIGHT);
			vPanel.setWidth("100%");
			vPanel.add(totalForm);

			botPanel.add(memoForm);
			if (!isTaxPerDetailLine()) {
				DynamicForm form = new DynamicForm();
				form.setFields(taxCodeSelect, vatinclusiveCheck);
				botPanel.add(form);
			}
			botPanel.add(totalForm);
			botPanel.setCellWidth(totalForm, "30%");

			bottompanel.add(vPanel);
			bottompanel.add(botPanel);

		} else {
			totForm.setFields(transactionTotalBaseCurrencyText);

			if (isMultiCurrencyEnabled())
				totForm.setFields(transactionTotalTransactionCurrencyText);

			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setWidth("100%");
			hPanel.add(memoForm);
			hPanel.setCellHorizontalAlignment(memoForm, ALIGN_LEFT);
			hPanel.add(totForm);
			hPanel.setCellHorizontalAlignment(totForm, ALIGN_RIGHT);

			VerticalPanel vpanel = new VerticalPanel();
			vpanel.setWidth("100%");
			vpanel.add(hPanel);

			bottompanel.add(vpanel);
		}

		leftVLay = new VerticalPanel();
		// leftVLay.setWidth("80%");
		leftVLay.add(vendorForm);

		VerticalPanel rightHLay = new VerticalPanel();
		// rightHLay.setWidth("80%");
		rightHLay.setCellHorizontalAlignment(termsForm, ALIGN_RIGHT);
		rightHLay.add(termsForm);
		if (isMultiCurrencyEnabled()) {
			rightHLay.add(currencyWidget);
			rightHLay.setCellHorizontalAlignment(currencyWidget,
					HasHorizontalAlignment.ALIGN_RIGHT);
			currencyWidget.setDisabled(isInViewMode());
		}

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.setSpacing(20);
		topHLay.add(rightHLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightHLay, "50%");
		topHLay.setCellHorizontalAlignment(rightHLay, ALIGN_RIGHT);

		VerticalPanel vLay1 = new VerticalPanel();
		// vLay1.add(lab2);
		// vLay1.add(addButton);
		// multi currency combo
		vLay1.add(accountsDisclosurePanel);
		vLay1.add(itemsDisclosurePanel);
		// vLay1.add(createAddNewButton());
		// menuButton.getElement().getStyle().setMargin(5, Unit.PX);
		vLay1.setWidth("100%");
		vLay1.add(bottompanel);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(titlelabel);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		mainVLay.add(vLay1);

		this.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(termsForm);
		listforms.add(memoForm);
		listforms.add(vatCheckform);
		listforms.add(totalForm);
		listforms.add(totForm);

		if (isInViewMode()) {
			creditCardCombo.setComboItem(getCompany().getAccount(
					transaction.getPayFrom()));
		}

	}

	private CreditCardAccountCombo createCreditCardItem() {
		CreditCardAccountCombo creditCardAccountCombo = new CreditCardAccountCombo(
				Accounter.messages().creditCard());
		creditCardAccountCombo.setHelpInformation(true);
		creditCardAccountCombo.setRequired(true);
		// payFrmSelect.setWidth("*");
		creditCardAccountCombo.setColSpan(3);

		// payFrmSelect.setWidth("*");
		// payFrmSelect.setWrapTitle(false);
		creditCardAccountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						payFromAccountSelected(selectItem.getID());
						// selectedAccount = (Account) selectItem;
						// adjustBalance();

					}

				});
		creditCardAccountCombo.setDisabled(isInViewMode());
		// payFrmSelect.setShowDisabled(false);
		return creditCardAccountCombo;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDateItem, messages.invalidateDate());
		}

		// if (vendorCombo.getSelectedValue() == null)
		// result.addError(
		// vendorCombo,
		// messages.pleaseSelectVendor(
		// Global.get().vendor()));
		result.add(vendorAccountTransactionTable.validateGrid());
		result.add(vendorItemTransactionTable.validateGrid());
		if (creditCardCombo.getSelectedValue() == null) {
			result.addError(creditCardCombo,
					messages.pleaseSelect(messages.payFrom()));
		} else {
			ClientAccount bankAccount = creditCardCombo.getSelectedValue();
			// check if the currency of accounts is valid or not
			if (bankAccount != null) {
				ClientCurrency bankCurrency = getCurrency(bankAccount
						.getCurrency());
				if (!(bankCurrency.equals(getBaseCurrency()))
						&& !(bankCurrency.equals(currency))) {
					result.addError(creditCardCombo,
							messages.selectProperBankAccount());
				}
			}
		}

		if (isTrackTax()) {
			if (!isTaxPerDetailLine()) {
				if (taxCodeSelect != null
						&& taxCodeSelect.getSelectedValue() == null) {
					result.addError(taxCodeSelect, messages.enterTaxCode());
				}
			}
		}

		return result;
	}

	private void resetFormView() {
		// vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		// refText.setWidth("200px");
	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientCreditCardCharge());
			resetElements();
			initpayFromAccountCombo();
		} else {

			if (currencyWidget != null) {
				this.currency = getCompany().getCurrency(
						transaction.getCurrency());
				this.currencyFactor = transaction.getCurrencyFactor();
				currencyWidget.setSelectedCurrency(this.currency);
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setDisabled(isInViewMode());
			}
			// if (vendor != null) {
			// vendorNameSelect.setComboItem(vendor);
			// phoneSelect.setValue(vendor.getPhoneNo());
			// }
			transactionDateItem.setValue(transaction.getDate());
			contact = transaction.getContact();
			if (contact != null) {
				contactCombo.setValue(contact.getName());
			}
			transactionDateItem.setValue(transaction.getDate());
			transactionDateItem.setDisabled(isInViewMode());
			transactionNumber.setValue(transaction.getNumber());
			transactionNumber.setDisabled(isInViewMode());
			delivDate.setValue(new ClientFinanceDate(transaction
					.getDeliveryDate()));
			delivDate.setDisabled(isInViewMode());
			phoneSelect.setValue(transaction.getPhone());
			if (getPreferences().isTrackPaidTax()) {
				if (getPreferences().isTaxPerDetailLine()) {
					netAmount
							.setAmount(getAmountInTransactionCurrency(transaction
									.getNetAmount()));
					vatTotalNonEditableText
							.setAmount(getAmountInTransactionCurrency(transaction
									.getTotal() - transaction.getNetAmount()));
				} else {
					this.taxCode = getTaxCodeForTransactionItems(transaction
							.getTransactionItems());
					if (taxCode != null) {
						this.taxCodeSelect.setComboItem(taxCode);
					}
				}
				if (vatinclusiveCheck != null) {
					setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());
				}
			}
			transactionTotalBaseCurrencyText
					.setAmount(getAmountInTransactionCurrency(transaction
							.getTotal()));

			transactionTotalTransactionCurrencyText
					.setAmount(getAmountInTransactionCurrency(transaction
							.getTotal()));

			if (transaction.getPayFrom() != 0)
				payFromAccountSelected(transaction.getPayFrom());
			creditCardCombo.setComboItem(getCompany()
					.getAccount(payFromAccount));
			creditCardCombo.setDisabled(isInViewMode());
			cheqNoText.setDisabled(isInViewMode());
			cheqNoText.setValue(transaction.getCheckNumber());
			paymentMethodSelected(transaction.getPaymentMethod());
			payMethSelect.setComboItem(transaction.getPaymentMethod());
			payMethSelect.setDisabled(isInViewMode());
			cheqNoText.setDisabled(isInViewMode());
			vendorAccountTransactionTable
					.setRecords(getAccountTransactionItems(transaction
							.getTransactionItems()));
			vendorItemTransactionTable
					.setRecords(getItemTransactionItems(transaction
							.getTransactionItems()));
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		initMemoAndReference();
		initTransactionNumber();
		addVendorsList();
		initAccounterClass();
		accountsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ACCOUNT, true));
		itemsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ITEM, false));
		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
	}

	private void initpayFromAccountCombo() {

		// listOfAccounts = Utility.getPayFromAccounts(FinanceApplication
		// .getCompany());
		// getPayFromAccounts();
		listOfAccounts = creditCardCombo.getAccounts();

		creditCardCombo.initCombo(listOfAccounts);
		creditCardCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		creditCardCombo.setAccounts();
		creditCardCombo.setDisabled(isInViewMode());

		account = creditCardCombo.getSelectedValue();

		if (account != null)
			creditCardCombo.setComboItem(account);
	}

	private void addVendorsList() {
		List<ClientVendor> result = getCompany().getActiveVendors();
		if (result != null) {
			initVendorsList(result);

		}
	}

	protected void initVendorsList(List<ClientVendor> result) {
		// First identify existing selected vendor
		for (ClientVendor vendor : result) {
			if (isInViewMode())
				if (vendor.getID() == transaction.getVendor()) {
					selectedVendor = vendor;
				}
		}
		vendorCombo.initCombo(result);

		if (isInViewMode()) {
			if (selectedVendor != null) {
				vendorCombo.setComboItem(selectedVendor);
				billToaddressSelected(selectedVendor.getSelectedAddress());
			}
			addPhonesContactsAndAddress();
		}
		vendorCombo.setDisabled(isInViewMode());
	}

	protected void addPhonesContactsAndAddress() {
		// Set<Address> allAddress = selectedVendor.getAddress();
		if (selectedVendor != null) {
			addressList = selectedVendor.getAddress();
			initBillToCombo();
			// billToCombo.setDisabled(isEdit);
			Set<ClientContact> allContacts;
			allContacts = selectedVendor.getContacts();
			Iterator<ClientContact> it = allContacts.iterator();
			// List<String> phones = new ArrayList<String>();
			ClientContact primaryContact = null;

			int i = 0;
			while (it.hasNext()) {
				ClientContact contact = it.next();
				if (contact.isPrimary())
					primaryContact = contact;
				idNamesForContacts.add(contact.getName());
				idPhoneNumberForContacts.add(contact.getBusinessPhone());
				// phones.add(contact.getBusinessPhone());
				i++;
			}

			contactCombo.initCombo(new ArrayList<ClientContact>(allContacts));
			contactCombo.setComboItem(primaryContact);
		}
		// phoneSelect.initCombo(phones);

		// ClientVendor cv = FinanceApplication.getCompany().getVendor(
		// creditCardChargeTaken.getVendor());
		if (transaction.getContact() != null)
			contactCombo.setSelected(transaction.getContact().getName());
		if (transaction.getPhone() != null)
			// FIXME check and fix the below code
			phoneSelect.setValue(transaction.getPhone());

		contactCombo.setDisabled(isInViewMode());
		phoneSelect.setDisabled(isInViewMode());
		return;
	}

	private void resetElements() {
		selectedVendor = null;
		// transaction = null;
		billingAddress = null;
		addressList = null;
		// billToCombo.setDisabled(isEdit);
		paymentMethod = UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
				.messages().check());
		payFromAccount = 0;
		// phoneSelect.setValueMap("");
		setMemoTextAreaItem("");
		// refText.setValue("");
		cheqNoText.setValue("");

	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type
		transaction.setType(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);

		// setting date
		if (transactionDateItem != null)

			transaction.setDate((transactionDateItem.getValue()).getDate());
		// setting number
		if (transactionNumber != null)
			transaction.setNumber(transactionNumber.getValue().toString());
		ClientVendor vendor = vendorCombo.getSelectedValue();
		if (vendor != null)
			transaction.setVendor(vendor.getID());
		// setting contact
		if (contact != null) {
			transaction.setContact(contact);
		}
		// if (contactNameSelect.getValue() != null) {
		// // ClientContact contact = getContactBasedOnId(contactNameSelect
		// // .getValue().toString());
		// transaction
		// .setContact(getContactBasedOnId(contactNameSelect
		// .getValue().toString()));
		//
		// }

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress(billingAddress);

		// setting phone
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());

		// Setting payment method

		transaction.setPaymentMethod(payMethSelect.getSelectedValue());

		// Setting pay from
		if (creditCardCombo.getSelectedValue() != null)
			payFromAccount = creditCardCombo.getSelectedValue().getID();
		if (payFromAccount != 0)
			transaction.setPayFrom(getCompany().getAccount(payFromAccount)
					.getID());

		// setting check no
		if (cheqNoText.getValue() != null)
			transaction.setCheckNumber(cheqNoText.getValue().toString());

		if (vatinclusiveCheck != null) {
			transaction.setAmountsIncludeVAT(vatinclusiveCheck.getValue());
		}

		// setting delivery date
		transaction.setDeliveryDate(UIUtils.toDate(delivDate.getValue()));

		// setting total
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal());
		// setting memo
		transaction.setMemo(getMemoTextAreaItem());
		// setting ref
		// transaction.setReference(UIUtils.toStr(refText.getValue()));
		taxCodeSelected(taxCode);
		if (selectedVendor != null) {

			// setting vendor
			transaction.setVendor(selectedVendor.getID());

			// setting contact
			if (contact != null) {
				transaction.setContact(contact);
			}
		}
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	public void createAlterObject() {
		saveOrUpdate(transaction);

	}

	/*
	 * @Override public ValidationResult validate() { ValidationResult result =
	 * super.validate();
	 * 
	 * if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
	 * result.addError(transactionDate, messages.invalidateTransactionDate()); }
	 * 
	 * if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
	 * result.addError(transactionDate, messages.invalidateTransactionDate()); }
	 * 
	 * result.add(vendorForm.validate()); result.add(termsForm.validate()); if
	 * (AccounterValidator.isBlankTransaction(vendorTransactionGrid)) {
	 * result.addError(vendorTransactionGrid, messages.blankTransaction()); }
	 * result.add(vendorTransactionGrid.validateGrid()); return result; }
	 */

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
		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		// payMethSelect.setDisabled(isEdit);
		if (paymentMethod.equals(messages.check())
				|| paymentMethod.equals(messages.cheque())) {
			cheqNoText.setDisabled(isInViewMode());
		} else {
			cheqNoText.setDisabled(!isInViewMode());
		}
		delivDate.setDisabled(isInViewMode());
		// billToCombo.setDisabled(isEdit);
		vendorCombo.setDisabled(isInViewMode());
		contactCombo.setDisabled(isInViewMode());
		phoneSelect.setDisabled(isInViewMode());
		creditCardCombo.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		vendorAccountTransactionTable.setDisabled(isInViewMode());
		vendorItemTransactionTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		if (taxCodeSelect != null)
			taxCodeSelect.setDisabled(isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setDisabled(isInViewMode());
		}
		super.onEdit();
	}

	@Override
	public void showMenu(Widget button) {
		setMenuItems(button, messages.Accounts(),
				messages.productOrServiceItem());
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();

		if (getPreferences().isTrackPaidTax())
			transaction.setNetAmount(getAmountInBaseCurrency(netAmount
					.getAmount()));
		// creditCardCharge.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());

		super.saveAndUpdateView();

		createAlterObject();
	}

	@Override
	protected String getViewTitle() {
		return messages.creditCardExpense();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected void initMemoAndReference() {
		if (isInViewMode()) {
			memoTextAreaItem.setDisabled(true);
			setMemoTextAreaItem(transaction.getMemo());
		}
	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNonEditableItems() {
		if (vendorAccountTransactionTable == null
				|| vendorItemTransactionTable == null) {
			return;
		}
		double lineTotal = vendorAccountTransactionTable.getLineTotal()
				+ vendorItemTransactionTable.getLineTotal();
		double grandTotal = vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal();

		transactionTotalBaseCurrencyText.setAmount(grandTotal);
		transactionTotalTransactionCurrencyText
				.setAmount(getAmountInTransactionCurrency(grandTotal));
		netAmount.setAmount(getAmountInTransactionCurrency(lineTotal));
		if (getPreferences().isTrackPaidTax()) {
			vatTotalNonEditableText
					.setAmount(getAmountInTransactionCurrency(grandTotal
							- lineTotal));
		}

	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	public ClientVendor getSelectedVendor() {
		return selectedVendor;
	}

	/**
	 * @param value
	 */
	protected void addContactToVendor(final ClientContact contact) {
		final ClientVendor selectedVendor = vendorCombo.getSelectedValue();
		if (selectedVendor == null) {
			return;
		}
		selectedVendor.addContact(contact);
		AccounterAsyncCallback<Long> asyncallBack = new AccounterAsyncCallback<Long>() {

			@Override
			public void onException(AccounterException caught) {
				caught.printStackTrace();
			}

			@Override
			public void onResultSuccess(Long result) {
				selectedVendor.setVersion(selectedVendor.getVersion() + 1);
				contactSelected(contact);
			}

		};
		Accounter.createCRUDService().update(selectedVendor, asyncallBack);
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		vendorAccountTransactionTable.add(transactionItem);
	}

	@Override
	protected void refreshTransactionGrid() {
		vendorAccountTransactionTable.updateTotals();
		vendorItemTransactionTable.updateTotals();
	}

	@Override
	public void setFocus() {
		this.vendorCombo.setFocus();

	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		vendorAccountTransactionTable.add(item);
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		vendorItemTransactionTable.add(item);
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(vendorAccountTransactionTable.getRecords());
		list.addAll(vendorItemTransactionTable.getRecords());
		return list;
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {

		if (taxCodeSelect == null) {
			return;
		}

		this.taxCode = taxCode;
		if (taxCode != null) {
			taxCodeSelect.setComboItem(taxCode);
			vendorAccountTransactionTable.setTaxCode(taxCode.getID(), true);
			vendorItemTransactionTable.setTaxCode(taxCode.getID(), true);
		} else {
			taxCodeSelect.setValue("");
		}

	}

	@Override
	public void updateAmountsFromGUI() {
		vendorAccountTransactionTable.updateAmountsFromGUI();
		vendorItemTransactionTable.updateAmountsFromGUI();
		modifyForeignCurrencyTotalWidget();
	}

	public void modifyForeignCurrencyTotalWidget() {
		if (currencyWidget.isShowFactorField()) {
			transactionTotalTransactionCurrencyText.hide();
		} else {
			transactionTotalTransactionCurrencyText.show();
			transactionTotalTransactionCurrencyText.setTitle(Accounter
					.messages().currencyTotal(
							currencyWidget.getSelectedCurrency()
									.getFormalName()));
		}
	}
}
