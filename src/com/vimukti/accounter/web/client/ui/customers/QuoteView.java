package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.ShipToForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.SalesPersonCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.CustomerItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyFactorWidget;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class QuoteView extends AbstractCustomerTransactionView<ClientEstimate> {
	private SalesPersonCombo salesPersonCombo;
	private ShippingTermsCombo shippingTermsCombo;
	// private PriceLevelCombo priceLevelSelect;
	private TAXCodeCombo taxCodeSelect;
	private PaymentTermsCombo payTermsSelect;
	protected DateField quoteExpiryDate;
	private ArrayList<DynamicForm> listforms;
	private boolean locationTrackingEnabled;

	private CustomerItemTransactionTable customerTransactionTable;
	private AddNewButton itemTableButton;

	protected ClientPriceLevel priceLevel;
	protected DateField deliveryDate;
	private List<ClientPaymentTerms> paymentTermsList;
	protected ClientSalesPerson salesPerson;
	protected ClientPaymentTerms paymentTerm;
	private AmountLabel netAmountLabel, vatTotalNonEditableText,
			salesTaxTextNonEditable;

	private AmountLabel transactionTotalinBaseCurrency,
			transactionTotalinForeignCurrency;

	private Double salesTax;
	private ShipToForm shipToAddress;
	private int type;
	private String title;

	public QuoteView(int type, String title) {
		super(ClientTransaction.TYPE_ESTIMATE);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
		this.title = title;
		this.type = type;
	}

	private void initAllItems() {
		paymentTermsList = getCompany().getPaymentsTerms();
		payTermsSelect.initCombo(paymentTermsList);

		if (this.transaction != null) {
			this.quoteExpiryDate.setValue(new ClientFinanceDate(
					this.transaction.getExpirationDate()));
			this.deliveryDate.setValue(new ClientFinanceDate(this.transaction
					.getDeliveryDate()));
		}

	}

	public QuoteView(ClientCustomer customer) {
		super(ClientTransaction.TYPE_ESTIMATE);
		locationTrackingEnabled = ClientCompanyPreferences.get()
				.isLocationTrackingEnabled();
	}

	@Override
	protected void customerSelected(ClientCustomer customer) {

		ClientCurrency currency = getCurrency(customer.getCurrency());

		if (this.getCustomer() != null && this.getCustomer() != customer) {
			ClientEstimate ent = (ClientEstimate) this.transaction;

			if (ent != null && ent.getCustomer() == (customer.getID())) {
				this.customerTransactionTable.setAllRows(ent
						.getTransactionItems());
			} else if (ent != null
					&& !(ent.getCustomer() == (customer.getID()))) {
				this.customerTransactionTable.updateTotals();
			}
			this.customerTransactionTable.resetRecords();

			currencyWidget.setSelectedCurrency(currency);
		}
		super.customerSelected(customer);
		shippingTermSelected(shippingTerm);

		if (this.paymentTerm != null && payTermsSelect != null)
			payTermsSelect.setComboItem(this.paymentTerm);

		if (this.salesPerson != null && salesPersonCombo != null)
			salesPersonCombo.setComboItem(this.salesPerson);

		// if (this.taxCode != null
		// && taxCodeSelect != null
		// && taxCodeSelect.getValue() != ""
		// && !taxCodeSelect.getName().equalsIgnoreCase(
		// Accounter.constants().none()))
		// taxCodeSelect.setComboItem(this.taxCode);
		// if (this.priceLevel != null && priceLevelSelect != null)
		// priceLevelSelect.setComboItem(this.priceLevel);

		if (customer.getPhoneNo() != null)
			phoneSelect.setValue(customer.getPhoneNo());
		else
			phoneSelect.setValue("");
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {

			billToTextArea.setValue(getValidAddress(billingAddress));

		} else
			billToTextArea.setValue("");

		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		addresses.addAll(customer.getAddress());
		shipToAddress.setAddress(addresses);

		this.setCustomer(customer);

		if (customer != null) {
			customerCombo.setComboItem(customer);
		}

		long taxCode = customer.getTAXCode();
		if (taxCode != 0) {
			customerTransactionTable.setTaxCode(taxCode, false);
		}

		if (currency.getID() != 0) {
			currencyWidget.setSelectedCurrency(currency);
		} else {
			currencyWidget.setSelectedCurrency(getBaseCurrency());
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrencycode(currency);
			setCurrencyFactor(1.0);
			updateAmountsFromGUI();
			modifyForeignCurrencyTotalWidget();
		}
	}

	@Override
	public void showMenu(Widget button) {
		setMenuItems(button,
		// Accounter.messages().accounts(Global.get().Account()),
				Accounter.constants().productOrServiceItem());
		// FinanceApplication.constants().comment(),
		// FinanceApplication.constants().VATItem());

	}

	private void shippingTermSelected(ClientShippingTerms shippingTerm2) {
		this.shippingTerm = shippingTerm2;
		if (shippingTerm != null && shippingTermsCombo != null) {
			shippingTermsCombo.setComboItem(getCompany().getShippingTerms(
					shippingTerm.getID()));
			shippingTermsCombo.setDisabled(isInViewMode());
		}
	}

	@Override
	protected void paymentTermsSelected(ClientPaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
		if (this.paymentTerm != null && payTermsSelect != null) {

			payTermsSelect.setComboItem(getCompany().getPaymentTerms(
					paymentTerm.getID()));
		}
		ClientFinanceDate transDate = this.transactionDateItem.getEnteredDate();
		calculateDatesforPayterm(transDate);
	}

	private void calculateDatesforPayterm(ClientFinanceDate transDate) {
		if (transDate != null && this.paymentTerm != null) {
			ClientFinanceDate dueDate = Utility.getCalculatedDueDate(transDate,
					this.paymentTerm);
			if (dueDate != null) {
				quoteExpiryDate.setValue(dueDate);
			}
		}
	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson salesPerson2) {
		this.salesPerson = salesPerson2;
		if (salesPerson != null) {

			salesPersonCombo.setComboItem(getCompany().getSalesPerson(
					salesPerson.getID()));

		} else
			salesPersonCombo.setValue("");

		salesPersonCombo.setDisabled(isInViewMode());

	}

	@Override
	public void saveAndUpdateView() {

		ClientEstimate quote = transaction != null ? (ClientEstimate) transaction
				: new ClientEstimate();

		if (quoteExpiryDate.getEnteredDate() != null)
			quote.setExpirationDate(quoteExpiryDate.getEnteredDate().getDate());
		if (getCustomer() != null)
			quote.setCustomer(getCustomer());
		if (contact != null)
			quote.setContact(contact);
		if (phoneSelect.getValue() != null)
			quote.setPhone(phoneSelect.getValue().toString());

		if (deliveryDate.getEnteredDate() != null)
			quote.setDeliveryDate(deliveryDate.getEnteredDate().getDate());

		if (salesPerson != null)
			quote.setSalesPerson(salesPerson);

		if (priceLevel != null)
			quote.setPriceLevel(priceLevel);

		quote.setMemo(memoTextAreaItem.getValue().toString());

		if (billingAddress != null)
			quote.setAddress(billingAddress);

		if (shippingAddress != null) {
			quote.setShippingAdress(shippingAddress);
		}
		// quote.setReference(this.refText.getValue() != null ? this.refText
		// .getValue().toString() : "");
		quote.setPaymentTerm(Utility.getID(paymentTerm));
		quote.setNetAmount(getAmountInBaseCurrency(netAmountLabel.getAmount()));

		if (isTrackTax()) {
			if (vatinclusiveCheck != null) {
				quote.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
						.getValue());
			}
			if (salesTax == null)
				salesTax = 0.0D;
			quote.setTaxTotal(this.salesTax);
		}

		quote.setTotal(getAmountInBaseCurrency(transactionTotalinBaseCurrency
				.getAmount()));

		transaction = quote;

		super.saveAndUpdateView();

		saveOrUpdate((ClientEstimate) transaction);

	}

	@Override
	protected void createControls() {
		// setTitle(UIUtils.title(customerConstants.quote()));
		Label lab1 = new Label(title);
		// + "(" + getTransactionStatus() + ")");
		lab1.setStyleName(Accounter.constants().labelTitle());
		// lab1.setHeight("35px");

		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						setDateValues(date);

					}
				});

		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();
		if (locationTrackingEnabled) {
			locationCombo = createLocationCombo();
			locationCombo.setHelpInformation(true);
			locationCombo.setDisabled(isInViewMode());
		}

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		datepanel.getElement().getStyle().setPaddingRight(15, Unit.PX);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(Accounter.messages().payeeName(
				Global.get().customer()));
		contactCombo = createContactComboItem();
		billToTextArea = new TextAreaItem();
		billToTextArea.setWidth(100);
		billToTextArea.setTitle(Accounter.constants().billTo());
		billToTextArea.setDisabled(true);

		shipToCombo = createShipToComboItem();

		shipToCombo.setHelpInformation(true);

		shipToAddress = new ShipToForm(null);
		shipToAddress.getCellFormatter().getElement(0, 0).getStyle()
				.setVerticalAlign(VerticalAlign.TOP);

		// shipToAddress.getCellFormatter().getElement(0, 0)
		// .setAttribute(Accounter.constants().width(), "40px");
		shipToAddress.getCellFormatter().addStyleName(0, 1, "memoFormAlign");
		shipToAddress.businessSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						shippingAddress = shipToAddress.getAddress();
						if (shippingAddress != null)
							shipToAddress.setAddres(shippingAddress);
						else
							shipToAddress.addrArea.setValue("");
					}
				});

		if (transaction != null)
			shipToAddress.setDisabled(true);

		phoneSelect = new TextItem(customerConstants.phone());
		phoneSelect.setToolTip(Accounter.messages().phoneNumber(
				this.getAction().getCatagory()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(isInViewMode());

		custForm = UIUtils.form(Global.get().customer());
		custForm.setCellSpacing(5);
		// custForm.setWidth("100%");
		if (type == ClientEstimate.QUOTES) {
			custForm.setFields(customerCombo, contactCombo, phoneSelect,
					billToTextArea);
		} else {
			custForm.setFields(customerCombo);
		}
		// custForm.getCellFormatter().setWidth(0, 0, "150");
		custForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		custForm.setStyleName("align-form");

		DynamicForm phoneForm = UIUtils.form(customerConstants.phoneNumber());
		// phoneForm.setWidth("100%");
		phoneForm.setNumCols(2);
		phoneForm.setCellSpacing(3);
		salesPersonCombo = createSalesPersonComboItem();

		payTermsSelect = createPaymentTermsSelectItem();

		quoteExpiryDate = new DateField(customerConstants.expirationDate());
		quoteExpiryDate.setHelpInformation(true);
		quoteExpiryDate.setEnteredDate(getTransactionDate());
		// formItems.add(quoteExpiryDate);
		quoteExpiryDate.setDisabled(isInViewMode());

		deliveryDate = createTransactionDeliveryDateItem();
		deliveryDate.setEnteredDate(getTransactionDate());
		DynamicForm locationform = new DynamicForm();
		if (locationTrackingEnabled) {
			if (type == ClientEstimate.QUOTES) {
				phoneForm.setFields(locationCombo);
			} else {
				locationform.setFields(locationCombo);
			}
		}
		if (getPreferences().isSalesPersonEnabled()) {
			phoneForm.setFields(salesPersonCombo, payTermsSelect,
					quoteExpiryDate, deliveryDate);
		} else {
			phoneForm.setFields(payTermsSelect, quoteExpiryDate, deliveryDate);
		}
		phoneForm.setStyleName("align-form");
		// phoneForm.getCellFormatter().getElement(0, 0)
		// .setAttribute(Accounter.constants().width(), "203px");

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			if (type == ClientEstimate.QUOTES) {
				phoneForm.setFields(classListCombo);
			} else {
				locationform.setFields(classListCombo);
			}
			classListCombo.setDisabled(isInViewMode());
		}

		Label lab2 = new Label(customerConstants.productAndService());

		HorizontalPanel buttLabHLay = new HorizontalPanel();
		buttLabHLay.add(lab2);

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		taxCodeSelect = createTaxCodeSelectItem();

		salesTaxTextNonEditable = createSalesTaxNonEditableLabel();

		// priceLevelSelect = createPriceLevelSelectItem();
		// refText = createRefereceText();
		// refText.setWidth(100);
		netAmountLabel = createNetAmountLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();
		vatTotalNonEditableText = createVATTotalNonEditableLabel();

		transactionTotalinBaseCurrency = createTransactionTotalNonEditableLabel(getCompany()
				.getPrimaryCurrency());

		transactionTotalinForeignCurrency = createForeignCurrencyAmountLable(getCompany()
				.getPrimaryCurrency());

		customerTransactionTable = new CustomerItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), this) {

			@Override
			public void updateNonEditableItems() {
				QuoteView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return QuoteView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return QuoteView.this.isInViewMode();
			}
		};
		customerTransactionTable.setDisabled(isInViewMode());

		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});

		// final TextItem disabletextbox = new TextItem();
		// disabletextbox.setVisible(false);

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		prodAndServiceForm1.getCellFormatter().addStyleName(0, 0,
				"memoFormAlign");
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setFields(memoTextAreaItem);

		DynamicForm prodAndServiceForm2 = new DynamicForm();
		prodAndServiceForm2.setWidth("100%");
		prodAndServiceForm2.setNumCols(2);
		prodAndServiceForm2.setCellSpacing(5);

		DynamicForm vatForm = new DynamicForm();
		prodAndServiceForm2.addStyleName("boldtext");

		if (isTrackTax()) {
			prodAndServiceForm2.setFields(netAmountLabel);
			if (isTaxPerDetailLine()) {
				prodAndServiceForm2.setFields(vatTotalNonEditableText);
			} else {
				vatForm.setFields(taxCodeSelect, vatinclusiveCheck);
				prodAndServiceForm2.setFields(salesTaxTextNonEditable);
			}
		}

		prodAndServiceForm2.setFields(transactionTotalinBaseCurrency);
		if (isMultiCurrencyEnabled()) {
			prodAndServiceForm2.setFields(transactionTotalinForeignCurrency);
		}

		currencyWidget = createCurrencyFactorWidget();
		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setHorizontalAlignment(ALIGN_RIGHT);
		vPanel.setWidth("100%");

		vPanel.add(prodAndServiceForm2);

		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(vatForm);
		prodAndServiceHLay.add(prodAndServiceForm2);
		if (isTaxPerDetailLine()) {
			prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "30%");
		} else
			prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "");
		prodAndServiceHLay.setCellHorizontalAlignment(prodAndServiceForm2,
				ALIGN_RIGHT);

		VerticalPanel mainpanel = new VerticalPanel();
		mainpanel.setWidth("100%");
		mainpanel.add(vPanel);
		mainpanel.add(prodAndServiceHLay);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.setHorizontalAlignment(ALIGN_LEFT);

		leftVLay.add(custForm);
		if (getCompany().getPreferences().isDoProductShipMents())
			if (type == ClientEstimate.QUOTES)
				leftVLay.add(shipToAddress);
		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");
		rightVLay.setHorizontalAlignment(ALIGN_RIGHT);
		if (type == ClientEstimate.QUOTES) {
			rightVLay.add(phoneForm);
		} else {
			rightVLay.add(locationform);
		}
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			rightVLay.setCellHorizontalAlignment(currencyWidget,
					HasHorizontalAlignment.ALIGN_RIGHT);
			currencyWidget.setDisabled(isInViewMode());
		}
		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "50%");
		topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		VerticalPanel gridPanel = new VerticalPanel();

		gridPanel.add(customerTransactionTable);
		mainVLay.add(gridPanel);
		mainVLay.add(itemTableButton);
		mainVLay.add(mainpanel);
		gridPanel.setWidth("100%");

		// if (UIUtils.isMSIEBrowser()) {
		// resetFormView();
		// phoneForm.setWidth("60%");
		// }

		this.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(phoneForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(prodAndServiceForm2);
		settabIndexes();
		if (isMultiCurrencyEnabled()) {
			transactionTotalinForeignCurrency.hide();
		}
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();

		transaction
				.setTotal(getAmountInBaseCurrency(transactionTotalinBaseCurrency
						.getAmount()));

		transaction.setEstimateType(type);
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	protected void setDateValues(ClientFinanceDate date) {
		if (date != null) {
			super.setTransactionDate(date);
			deliveryDate.setEnteredDate(date);
			quoteExpiryDate.setValue(date);
			calculateDatesforPayterm(date);
			updateNonEditableItems();
		}
	}

	@Override
	protected void initMemoAndReference() {

		if (this.transaction != null) {

			String memo = ((ClientEstimate) transaction).getMemo();
			if (memo != null) {
				memoTextAreaItem.setValue(memo);
			}
		}

	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		if (transaction != null) {
			Double salesTaxAmout = ((ClientEstimate) transaction).getTaxTotal();
			if (salesTaxAmout != null) {
				salesTaxTextNonEditable
						.setAmount(getAmountInTransactionCurrency(salesTaxAmout));
			}

		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transaction != null) {
			Double transactionTotal = ((ClientEstimate) transaction).getTotal();
			if (transactionTotal != null) {
				transactionTotalinBaseCurrency
						.setAmount(getAmountInBaseCurrency(transactionTotal));
				transactionTotalinForeignCurrency
						.setAmount(getAmountInTransactionCurrency(transactionTotal));
			}

		}

	}

	@Override
	protected void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientEstimate());
		} else {

			if (currencyWidget != null) {
				if (transaction.getCurrency() > 1) {
					this.currency = getCompany().getCurrency(
							transaction.getCurrency());
				} else {
					this.currency = getCompany().getPrimaryCurrency();
				}
				this.currencyFactor = transaction.getCurrencyFactor();
				currencyWidget.setSelectedCurrency(this.currency);
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setDisabled(isInViewMode());
			}
			ClientCompany company = getCompany();
			this.setCustomer(company.getCustomer(transaction.getCustomer()));
			if (this.getCustomer() != null) {
				this.contacts = getCustomer().getContacts();
			}

			// customerSelected(FinanceApplication.getCompany().getCustomer(
			// estimate.getCustomer()));
			this.contact = transaction.getContact();

			this.phoneNo = transaction.getPhone();
			phoneSelect.setValue(this.phoneNo);
			this.billingAddress = transaction.getAddress();
			this.paymentTerm = company.getPaymentTerms(transaction
					.getPaymentTerm());
			this.priceLevel = company
					.getPriceLevel(transaction.getPriceLevel());
			this.salesPerson = company.getSalesPerson(transaction
					.getSalesPerson());
			initTransactionNumber();
			if (getCustomer() != null) {
				customerCombo.setComboItem(getCustomer());
			}
			// billToaddressSelected(this.billingAddress);
			if (billingAddress != null) {

				billToTextArea.setValue(getValidAddress(billingAddress));

			} else
				billToTextArea.setValue("");
			contactSelected(this.contact);
			paymentTermsSelected(this.paymentTerm);
			priceLevelSelected(this.priceLevel);
			salesPersonSelected(this.salesPerson);
			this.transactionItems = transaction.getTransactionItems();

			if (transaction.getDeliveryDate() != 0)
				this.deliveryDate.setValue(new ClientFinanceDate(transaction
						.getDeliveryDate()));
			if (transaction.getExpirationDate() != 0)
				this.quoteExpiryDate.setValue(new ClientFinanceDate(transaction
						.getExpirationDate()));

			if (transaction.getID() != 0) {
				setMode(EditMode.VIEW);
			}

			if (!isTaxPerDetailLine() && taxCodeSelect != null) {
				this.taxCodeSelect
						.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
			}

			memoTextAreaItem.setValue(transaction.getMemo());
			// refText.setValue(estimate.getReference());
			if (isTrackTax()) {
				netAmountLabel
						.setAmount(getAmountInTransactionCurrency(transaction
								.getNetAmount()));
				vatTotalNonEditableText.setValue(DataUtils
						.getAmountAsString(transaction.getTotal()
								- transaction.getNetAmount()));
			}
			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());
			}

			memoTextAreaItem.setDisabled(true);
			transactionTotalinBaseCurrency
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			transactionTotalinForeignCurrency
					.setAmount(getAmountInTransactionCurrency(transaction
							.getTotal()));

			customerTransactionTable.setDisabled(isInViewMode());
			transactionDateItem.setDisabled(isInViewMode());
			transactionNumber.setDisabled(isInViewMode());
			phoneSelect.setDisabled(isInViewMode());
			billToTextArea.setDisabled(isInViewMode());
			customerCombo.setDisabled(isInViewMode());
			payTermsSelect.setDisabled(isInViewMode());
			salesPersonCombo.setDisabled(isInViewMode());
			memoTextAreaItem.setDisabled(isInViewMode());
			contactCombo.setDisabled(isInViewMode());
			quoteExpiryDate.setDisabled(isInViewMode());
			deliveryDate.setDisabled(isInViewMode());
			taxCodeSelect.setDisabled(isInViewMode());
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		superinitTransactionViewData();
		initAllItems();
		initAccounterClass();
		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
	}

	private void initCustomers() {
		List<ClientCustomer> result = getCompany().getActiveCustomers();
		customerCombo.initCombo(result);
		customerCombo.setDisabled(isInViewMode());

	}

	private void superinitTransactionViewData() {

		initTransactionNumber();

		initCustomers();

		ArrayList<ClientSalesPerson> salesPersons = getCompany()
				.getActiveSalesPersons();

		salesPersonCombo.initCombo(salesPersons);

		ArrayList<ClientPriceLevel> priceLevels = getCompany().getPriceLevels();

		// priceLevelSelect.initCombo(priceLevels);

		initSalesTaxNonEditableItem();

		initTransactionTotalNonEditableItem();

		initMemoAndReference();

		initTransactionsItems();

	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {

		this.priceLevel = priceLevel;
		// if (priceLevel != null && priceLevelSelect != null) {
		//
		// priceLevelSelect.setComboItem(getCompany().getPriceLevel(
		// priceLevel.getID()));
		//
		// }
		// if (transaction == null && customerTransactionTable != null) {
		// customerTransactionTable.priceLevelSelected(this.priceLevel);
		// customerTransactionTable.updatePriceLevel();
		// }
		updateNonEditableItems();

	}

	@Override
	public void updateNonEditableItems() {
		if (customerTransactionTable == null)
			return;
		if (isTrackTax()) {
			netAmountLabel
					.setAmount(getAmountInTransactionCurrency(customerTransactionTable
							.getLineTotal()));
			vatTotalNonEditableText
					.setAmount(getAmountInTransactionCurrency(customerTransactionTable
							.getTotalTax()));
			setSalesTax(customerTransactionTable.getTotalTax());
		}
		setTransactionTotal(customerTransactionTable.getGrandTotal());
	}

	public void setTransactionTotal(Double transactionTotal) {
		if (transactionTotal == null)
			transactionTotal = 0.0D;
		transactionTotalinBaseCurrency.setAmount(transactionTotal);
		transactionTotalinForeignCurrency
				.setAmount(getAmountInTransactionCurrency(transactionTotal));
	}

	public void setSalesTax(Double salesTax) {
		if (salesTax == null)
			salesTax = 0.0D;
		this.salesTax = salesTax;
		salesTaxTextNonEditable
				.setAmount(getAmountInTransactionCurrency(salesTax));
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// 1. isValidDueOrDeliveryDate?

		if (!AccounterValidator.isValidDueOrDelivaryDates(
				this.quoteExpiryDate.getEnteredDate(), this.transactionDate)) {
			result.addError(this.quoteExpiryDate, Accounter.constants().the()
					+ " "
					+ customerConstants.expirationDate()
					+ " "
					+ " "
					+ Accounter.constants()
							.cannotbeearlierthantransactiondate());
		}
		result.add(customerTransactionTable.validateGrid());
		if (isTrackTax()) {
			if (!isTaxPerDetailLine()) {
				if (taxCodeSelect != null
						&& taxCodeSelect.getSelectedValue() == null) {
					result.addError(taxCodeSelect,
							accounterConstants.enterTaxCode());
				}

			}
		}
		return result;

	}

	public static QuoteView getInstance(int type, String title) {
		return new QuoteView(type, title);
	}

	@Override
	protected void onAddNew(String item) {
		super.onAddNew(item);
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.customerCombo.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void onEdit() {
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (transaction.getStatus() == QuoteListView.STATUS_ACCECPTED) {
					Accounter.showError(Accounter.constants()
							.thisQuoteAlreadyAccepted());
				} else if (caught instanceof InvocationException) {
					Accounter.showMessage(Accounter.constants()
							.sessionExpired());
				} else {
					int errorCode = ((AccounterException) caught)
							.getErrorCode();
					Accounter.showError(AccounterExceptions
							.getErrorString(errorCode));
				}
			}

			@Override
			public void onSuccess(Boolean result) {
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
		customerCombo.setDisabled(isInViewMode());
		if (getPreferences().isSalesPersonEnabled())
			salesPersonCombo.setDisabled(isInViewMode());
		payTermsSelect.setDisabled(isInViewMode());
		deliveryDate.setDisabled(isInViewMode());
		quoteExpiryDate.setDisabled(isInViewMode());
		taxCodeSelect.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		// priceLevelSelect.setDisabled(isInViewMode());
		customerTransactionTable.setDisabled(isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setDisabled(isInViewMode());
		}
		locationCombo.setDisabled(isInViewMode());
		classListCombo.setDisabled(isInViewMode());
		super.onEdit();
	}

	private void resetFormView() {

		// custForm.getCellFormatter().setWidth(0, 1, "200px");
		// custForm.setWidth("75%");
		// priceLevelSelect.setWidth("150px");
		// refText.setWidth("200px");

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		if (taxCode != null) {
			taxCodeSelect
					.setComboItem(getCompany().getTAXCode(taxCode.getID()));
			customerTransactionTable.setTaxCode(taxCode.getID(), true);
		} else
			taxCodeSelect.setValue("");
		updateNonEditableItems();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().quote();
	}

	@Override
	protected void initTransactionsItems() {
		if (transaction.getTransactionItems() != null
				&& !transaction.getTransactionItems().isEmpty())
			customerTransactionTable.setRecords(transaction
					.getTransactionItems());
	}

	@Override
	protected boolean isBlankTransactionGrid() {
		return customerTransactionTable.getAllRows().isEmpty();
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		customerTransactionTable.add(transactionItem);
	}

	@Override
	protected void refreshTransactionGrid() {
		customerTransactionTable.updateTotals();
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		return customerTransactionTable.getAllRows();
	}

	private void settabIndexes() {
		customerCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		phoneSelect.setTabIndex(3);
		billToTextArea.setTabIndex(4);
		transactionDateItem.setTabIndex(5);
		transactionNumber.setTabIndex(6);
		payTermsSelect.setTabIndex(7);
		quoteExpiryDate.setTabIndex(8);
		deliveryDate.setTabIndex(9);
		memoTextAreaItem.setTabIndex(10);
		// menuButton.setTabIndex(11);
		saveAndCloseButton.setTabIndex(12);
		saveAndNewButton.setTabIndex(13);
		cancelButton.setTabIndex(14);

	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		customerTransactionTable.add(item);
	}

	@Override
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		customerTransactionTable.updateAmountsFromGUI();
	}

	public void modifyForeignCurrencyTotalWidget() {
		if (currencyWidget.isShowFactorField()) {
			transactionTotalinForeignCurrency.hide();
		} else {
			transactionTotalinForeignCurrency.show();
			transactionTotalinForeignCurrency.setTitle(Accounter.messages()
					.currencyTotal(
							currencyWidget.getSelectedCurrency()
									.getFormalName()));
		}
	}
}
