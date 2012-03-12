package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientCompany;
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
import com.vimukti.accounter.web.client.ui.ShipToForm;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.SalesPersonCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.tables.CustomerItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class QuoteView extends AbstractCustomerTransactionView<ClientEstimate>
		implements IPrintableView {
	private SalesPersonCombo salesPersonCombo;
	private ShippingTermsCombo shippingTermsCombo;
	// private PriceLevelCombo priceLevelSelect;
	private TAXCodeCombo taxCodeSelect;
	private PaymentTermsCombo payTermsSelect;
	protected DateField quoteExpiryDate;
	private ArrayList<DynamicForm> listforms;

	private CustomerItemTransactionTable customerTransactionTable;
	private AddNewButton itemTableButton;

	protected ClientPriceLevel priceLevel;
	protected DateField deliveryDate;
	private List<ClientPaymentTerms> paymentTermsList;
	protected ClientSalesPerson salesPerson;
	protected ClientPaymentTerms paymentTerm;
	private TaxItemsForm vatTotalNonEditableText, salesTaxTextNonEditable;
	private AmountLabel netAmountLabel;

	private Double salesTax;
	private ShipToForm shipToAddress;
	private int type;
	private String title;
	private SelectCombo statusCombo;

	private DateField dueDateItem;

	private TextItem customerOrderText;

	public QuoteView(int type, String title) {
		super(ClientTransaction.TYPE_ESTIMATE);
		this.title = title;
		this.type = type;
		this.getElement().setId("QuoteView");
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
	}

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (customer == null) {
			return;
		}
		ClientCurrency currency = getCurrency(customer.getCurrency());

		// Job Tracking
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setEnabled(true);
			jobListCombo.setValue("");
			jobListCombo.setCustomer(customer);
		}
		if (this.getCustomer() != null && this.getCustomer() != customer) {
			ClientEstimate ent = this.transaction;

			if (ent != null && ent.getCustomer() == (customer.getID())) {
				this.customerTransactionTable.setAllRows(ent
						.getTransactionItems());
			} else if (ent != null
					&& !(ent.getCustomer() == (customer.getID()))) {
				this.customerTransactionTable.updateTotals();
			}
			this.customerTransactionTable.resetRecords();
			transaction.setTransactionItems(customerTransactionTable
					.getRecords());
			if (taxCodeSelect.getSelectedValue() != null) {
				customerTransactionTable.setTaxCode(taxCodeSelect
						.getSelectedValue().getID(), true);
			}
			if (currency != null) {
				transaction.setCurrency(currency.getID());
			}
			vatTotalNonEditableText.setTransaction(transaction);
			salesTaxTextNonEditable.setTransaction(transaction);

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
		// messages.none()))
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
			currencyWidget.setSelectedCurrencyFactorInWidget(currency,
					transactionDateItem.getDate().getDate());
		} else {
			currencyWidget.setSelectedCurrency(getBaseCurrency());
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(currency);
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}
	}

	@Override
	public void showMenu(Widget button) {
		setMenuItems(button,
		// messages.accounts(Global.get().Account()),
				messages.productOrServiceItem());
		// FinanceApplication.constants().comment(),
		// FinanceApplication.constants().VATItem());

	}

	private void shippingTermSelected(ClientShippingTerms shippingTerm2) {
		this.shippingTerm = shippingTerm2;
		if (shippingTerm != null && shippingTermsCombo != null) {
			shippingTermsCombo.setComboItem(getCompany().getShippingTerms(
					shippingTerm.getID()));
			shippingTermsCombo.setEnabled(!isInViewMode());
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

		if (transDate != null && paymentTerm != null) {
			ClientFinanceDate dueDate = Utility.getCalculatedDueDate(transDate,
					paymentTerm);
			if (dueDate != null) {
				dueDateItem.setValue(dueDate);
			}
		}
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

		salesPersonCombo.setEnabled(!isInViewMode());

	}

	@Override
	public ClientEstimate saveView() {
		ClientEstimate saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();
		saveOrUpdate(transaction);

	}

	private int getStatus(String status) {
		if (status.equals(messages.open())) {
			return ClientEstimate.STATUS_OPEN;
		} else if (status.equals(messages.accepted())) {
			return ClientEstimate.STATUS_ACCECPTED;
		} else if (status.equals(messages.closed())) {
			return ClientEstimate.STATUS_CLOSE;
		} else if (status.equals(messages.rejected())) {
			return ClientEstimate.STATUS_REJECTED;
		} else if (status.equals(messages.completed())) {
			return ClientTransaction.STATUS_COMPLETED;
		} else if (status.equals(messages.cancelled())) {
			return ClientTransaction.STATUS_CANCELLED;
		}
		return -1;
	}

	private String getStatusString(int status) {
		switch (status) {
		case ClientEstimate.STATUS_OPEN:
			return messages.open();
		case ClientEstimate.STATUS_ACCECPTED:
			if (type == ClientEstimate.SALES_ORDER) {
				return messages.completed();
			}
			return messages.accepted();
		case ClientEstimate.STATUS_CLOSE:
			return messages.closed();
		case ClientEstimate.STATUS_REJECTED:
			return messages.rejected();
		case ClientEstimate.STATUS_APPLIED:
		case ClientTransaction.STATUS_COMPLETED:
			if (type == ClientEstimate.SALES_ORDER) {
				return messages.completed();
			}
			return messages.closed();
		case ClientTransaction.STATUS_CANCELLED:
			return messages.cancelled();
		default:
			break;
		}
		return "";
	}

	@Override
	protected void createControls() {
		// setTitle(UIUtils.title(messages.quote()));
		Label lab1 = new Label(title);
		// + "(" + getTransactionStatus() + ")");
		lab1.setStyleName("label-title");
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
			locationCombo.setEnabled(!isInViewMode());
		}

		DynamicForm dateNoForm = new DynamicForm("datenumber-panel");
		if (!isTemplate) {
			dateNoForm.add(transactionDateItem, transactionNumber);
		}
		StyledPanel datepanel = new StyledPanel("datepanel");
//		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		// datepanel.setCellHorizontalAlignment(dateNoForm,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		// datepanel.getElement().getStyle().setPaddingRight(15, Unit.PX);

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");
//		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(messages.payeeName(Global.get()
				.customer()));
		contactCombo = createContactComboItem();
		billToTextArea = new TextAreaItem(messages.billTo(), "billToTextArea");
//		billToTextArea.setWidth(100);
		billToTextArea.setDisabled(true);

		shipToCombo = createShipToComboItem();

		shipToAddress = new ShipToForm(null);

		// shipToAddress.getCellFormatter().getElement(0, 0)
		// .setAttribute(messages.width(), "40px");
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
			shipToAddress.setEnabled(false);

		phoneSelect = new TextItem(messages.phone(), "phoneSelect");
		phoneSelect.setToolTip(messages.phoneNumberOf(this.getAction()
				.getCatagory()));
//		phoneSelect.setWidth(100);
		phoneSelect.setEnabled(!isInViewMode());

		custForm = UIUtils.form(Global.get().customer());
		// custForm.setWidth("100%");
		if (type == ClientEstimate.QUOTES || type == ClientEstimate.SALES_ORDER) {
			custForm.add(customerCombo, contactCombo, phoneSelect,
					billToTextArea);
		} else {
			custForm.add(customerCombo);
		}
		// custForm.getCellFormatter().setWidth(0, 0, "150");
		custForm.setStyleName("align-form");

		DynamicForm phoneForm = UIUtils.form(messages.phoneNumber());
		// phoneForm.setWidth("100%");

		statusCombo = new SelectCombo(messages.status());
		statusCombo.initCombo(getStatusList());
		statusCombo.setSelectedItem(0);

		customerOrderText = new TextItem(messages.payeeOrderNo(Global.get()
				.customer()), "customerOrderText");
//		customerOrderText.setWidth(50);
		customerOrderText.setEnabled(!isInViewMode());

		shippingTermsCombo = createShippingTermsCombo();

		shippingMethodsCombo = createShippingMethodCombo();

		salesPersonCombo = createSalesPersonComboItem();

		payTermsSelect = createPaymentTermsSelectItem();
		dueDateItem = createDueDateItem();
		quoteExpiryDate = new DateField(messages.expirationDate(),
				"quoteExpiryDate");
		quoteExpiryDate.setEnteredDate(getTransactionDate());
		// formItems.add(quoteExpiryDate);
		quoteExpiryDate.setEnabled(!isInViewMode());

		deliveryDate = createTransactionDeliveryDateItem();
		deliveryDate.setEnteredDate(getTransactionDate());
		DynamicForm locationform = new DynamicForm("locationform");
		jobListCombo = createJobListCombo();
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setEnabled(false);
			if (type == ClientEstimate.QUOTES
					|| type == ClientEstimate.SALES_ORDER) {
				phoneForm.add(jobListCombo);
			} else {
				locationform.add(jobListCombo);
			}
		}
		if (locationTrackingEnabled) {
			if (type == ClientEstimate.QUOTES) {
				phoneForm.add(locationCombo);
			} else {
				locationform.add(locationCombo);
			}
		}
		if (getPreferences().isSalesPersonEnabled()) {
			if (isTemplate) {
				if (type == ClientEstimate.SALES_ORDER) {
					phoneForm.add(statusCombo, dueDateItem, customerOrderText,
							shippingTermsCombo, shippingMethodsCombo,
							payTermsSelect);
				} else {
					phoneForm
							.add(statusCombo, salesPersonCombo, payTermsSelect);
				}

			} else {
				if (type == ClientEstimate.SALES_ORDER) {
					phoneForm.add(statusCombo, dueDateItem, customerOrderText,
							shippingTermsCombo, shippingMethodsCombo,
							payTermsSelect);
				} else {
					phoneForm.add(statusCombo, salesPersonCombo,
							payTermsSelect, quoteExpiryDate, deliveryDate);
				}
			}
		} else {
			if (isTemplate) {
				if (type == ClientEstimate.SALES_ORDER) {
					phoneForm.add(statusCombo, dueDateItem, customerOrderText,
							shippingTermsCombo, shippingMethodsCombo,
							payTermsSelect);
				} else {
					phoneForm.add(statusCombo, payTermsSelect);
				}
			} else {
				if (type == ClientEstimate.SALES_ORDER) {
					phoneForm.add(statusCombo, dueDateItem, customerOrderText,
							shippingTermsCombo, shippingMethodsCombo,
							payTermsSelect);
				} else {
					phoneForm.add(statusCombo, payTermsSelect, quoteExpiryDate,
							deliveryDate);
				}
			}
		}
		phoneForm.setStyleName("align-form");
		// phoneForm.getCellFormatter().getElement(0, 0)
		// .setAttribute(messages.width(), "203px");

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			if (type == ClientEstimate.QUOTES
					|| type == ClientEstimate.SALES_ORDER) {
				phoneForm.add(classListCombo);
			} else {
				locationform.add(classListCombo);
			}
			classListCombo.setEnabled(!isInViewMode());
		}

		Label lab2 = new Label(messages.productAndService());

		StyledPanel buttLabHLay = new StyledPanel("buttLabHLay");
		buttLabHLay.add(lab2);

		memoTextAreaItem = createMemoTextAreaItem();
		// memoTextAreaItem.setWidth(100);
		taxCodeSelect = createTaxCodeSelectItem();

		salesTaxTextNonEditable = new TaxItemsForm();// createSalesTaxNonEditableLabel();

		// priceLevelSelect = createPriceLevelSelectItem();
		// refText = createRefereceText();
		// refText.setWidth(100);
		netAmountLabel = createNetAmountLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();
		vatTotalNonEditableText = new TaxItemsForm();// createVATTotalNonEditableLabel();

		transactionTotalBaseCurrencyText = createTransactionTotalNonEditableLabel(getCompany()
				.getPrimaryCurrency());

		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPrimaryCurrency());

		currencyWidget = createCurrencyFactorWidget();

		customerTransactionTable = new CustomerItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), this) {

			@Override
			public void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
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

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getAmount() != null
						&& discountField.getAmount() != 0) {
					row.setDiscount(discountField.getAmount());
				}
				QuoteView.this.updateNonEditableItems();
			}
		};
		customerTransactionTable.setEnabled(!isInViewMode());

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

		DynamicForm prodAndServiceForm1 = new DynamicForm("prodAndServiceForm1");
//		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.add(memoTextAreaItem);

		StyledPanel nonEditablePanel = new StyledPanel("nonEditablePanel");

		DynamicForm totalForm = new DynamicForm("totalForm");

		DynamicForm netAmountForm = new DynamicForm("netAmountForm");

		DynamicForm vatForm = new DynamicForm("vatForm");
		nonEditablePanel.addStyleName("boldtext");

		discountField = getDiscountField();

		if (isTrackTax()) {
			netAmountForm.add(netAmountLabel);
			nonEditablePanel.add(netAmountForm);
			if (isTaxPerDetailLine()) {
				nonEditablePanel.add(vatTotalNonEditableText);
			} else {
				vatForm.add(taxCodeSelect);
				nonEditablePanel.add(salesTaxTextNonEditable);
			}
			vatForm.add(vatinclusiveCheck);
		}

		if (isTrackDiscounts()) {
			if (!isDiscountPerDetailLine()) {
				vatForm.add(discountField);
				nonEditablePanel.add(vatForm);
			}
		}
		totalForm.add(transactionTotalBaseCurrencyText);
		if (isMultiCurrencyEnabled()) {
			totalForm.add(foreignCurrencyamountLabel);
		}

		StyledPanel prodAndServiceHLay = new StyledPanel("prodAndServiceHLay");
//		prodAndServiceHLay.setWidth("100%");

		StyledPanel vPanel = new StyledPanel("vPanel");
		// vPanel.setHorizontalAlignment(ALIGN_RIGHT);
		// vPanel.setWidth("100%");

		// vPanel.add(prodAndServiceForm2);
		nonEditablePanel.add(totalForm);

		vPanel.add(nonEditablePanel);

		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(vatForm);
		prodAndServiceHLay.add(nonEditablePanel);

		// if (isTaxPerDetailLine()) {
		// prodAndServiceHLay.setCellWidth(nonEditablePanel, "30%");
		// } else
		// prodAndServiceHLay.setCellWidth(nonEditablePanel, "");
		// prodAndServiceHLay.setCellHorizontalAlignment(nonEditablePanel,
		// ALIGN_RIGHT);

		StyledPanel mainpanel = new StyledPanel("mainpanel");
		mainpanel.add(vPanel);
		mainpanel.add(prodAndServiceHLay);

		StyledPanel leftVLay = new StyledPanel("leftVLay");

		leftVLay.add(custForm);
		if (getCompany().getPreferences().isDoProductShipMents())
			if (type == ClientEstimate.QUOTES)
				leftVLay.add(shipToAddress);
		StyledPanel rightVLay = new StyledPanel("rightVLay");
		if (type == ClientEstimate.QUOTES || type == ClientEstimate.SALES_ORDER) {
			rightVLay.add(phoneForm);
		} else {
			rightVLay.add(locationform);
		}
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			currencyWidget.setEnabled(!isInViewMode());
		}
		StyledPanel topHLay = new StyledPanel("topHLay");
		topHLay.addStyleName("fields-panel");
//		topHLay.setWidth("100%");
		// topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		// topHLay.setCellWidth(leftVLay, "50%");
		// topHLay.setCellWidth(rightVLay, "50%");
		// topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab1);
		mainVLay.add(voidedPanel);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		StyledPanel gridPanel = new StyledPanel("gridPanel");

		gridPanel.add(customerTransactionTable);
		mainVLay.add(gridPanel);
		mainVLay.add(itemTableButton);
		mainVLay.add(mainpanel);

		// if (UIUtils.isMSIEBrowser()) {
		// resetFormView();
		// phoneForm.setWidth("60%");
		// }

		this.add(mainVLay);

//		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(phoneForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(totalForm);
		listforms.add(netAmountForm);
		settabIndexes();
		if (isMultiCurrencyEnabled()) {
			foreignCurrencyamountLabel.hide();
		}
	}

	protected DateField createDueDateItem() {

		DateField dateItem = new DateField(messages.dueDate(), "dateItem");
		dateItem.setToolTip(messages.selectDateUntilDue(this.getAction()
				.getViewName()));
		dateItem.setTitle(messages.dueDate());

		dateItem.setEnabled(!isInViewMode());

		// formItems.add(dateItem);

		return dateItem;

	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
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
		quote.setNetAmount(netAmountLabel.getAmount());

		if (isTrackTax()) {
			setAmountIncludeTAX();
			if (salesTax == null)
				salesTax = 0.0D;
			quote.setTaxTotal(this.salesTax);
		}
		if (isTrackDiscounts()) {
			if (discountField.getAmount() != 0.0 && transactionItems != null) {
				for (ClientTransactionItem item : transactionItems) {
					item.setDiscount(discountField.getAmount());
				}
			}
		}
		quote.setTotal(foreignCurrencyamountLabel.getAmount());

		String selectedValue = statusCombo.getSelectedValue();
		quote.setStatus(getStatus(selectedValue));

		transaction = quote;
		transaction.setTotal(foreignCurrencyamountLabel.getAmount());
		if (getPreferences().isJobTrackingEnabled()) {
			if (jobListCombo.getSelectedValue() != null)
				transaction.setJob(jobListCombo.getSelectedValue().getID());
		}
		transaction.setEstimateType(type);
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
		transaction.setCustomerOrderNumber(customerOrderText.getValue());
		transaction.setShippingTerm(shippingTerm == null ? 0 : shippingTerm
				.getID());
		if (shippingMethod != null)
			transaction.setShippingMethod(shippingMethod.getID());
		if (dueDateItem.getEnteredDate() != null)
			transaction.setDueDate(dueDateItem.getEnteredDate().getDate());
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

			String memo = transaction.getMemo();
			if (memo != null) {
				memoTextAreaItem.setValue(memo);
			}
		}

	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		if (transaction != null) {
			Double salesTaxAmout = transaction.getTaxTotal();
			// if (salesTaxAmout != null) {
			// salesTaxTextNonEditable.setAmount(salesTaxAmout);
			// }
			salesTaxTextNonEditable.setTransaction(transaction);

		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transaction != null) {
			Double transactionTotal = transaction.getTotal();
			if (transactionTotal != null) {
				transactionTotalBaseCurrencyText
						.setAmount(getAmountInBaseCurrency(transactionTotal));
				foreignCurrencyamountLabel.setAmount(transactionTotal);
			}

		}

	}

	@Override
	protected void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientEstimate());
			if (type == ClientEstimate.SALES_ORDER) {
				dueDateItem.setEnteredDate(new ClientFinanceDate());
			}
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
				currencyWidget.setEnabled(!isInViewMode());
			}
			ClientCompany company = getCompany();
			initTransactionsItems();
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
			dueDateItem.setEnteredDate(new ClientFinanceDate(transaction
					.getDueDate()));
			this.shippingTerm = company.getShippingTerms(transaction
					.getShippingTerm());
			if (this.shippingTerm != null) {
				shippingTermsCombo.setComboItem(shippingTerm);
			}
			this.shippingMethod = company.getShippingMethod(transaction
					.getShippingMethod());
			if (shippingMethod != null) {
				shippingMethodsCombo.setComboItem(shippingMethod);
			}
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

			statusCombo.setComboItem(getStatusString(transaction.getStatus()));
			customerOrderText.setValue(transaction.getCustomerOrderNumber());
			memoTextAreaItem.setValue(transaction.getMemo());
			// refText.setValue(estimate.getReference());
			if (isTrackTax()) {
				if (!isTaxPerDetailLine() && taxCodeSelect != null) {
					this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
					if (taxCode != null) {
						this.taxCodeSelect.setComboItem(taxCode);
						taxCodeSelected(taxCode);
					} else {
						if (getCustomer() != null) {
							this.taxCode = getCompany().getTAXCode(
									getCustomer().getTAXCode());
							if (taxCode != null) {
								this.taxCodeSelect.setComboItem(taxCode);
								taxCodeSelected(taxCode);
							}
						}
					}
				}
				netAmountLabel.setAmount(transaction.getNetAmount());
				// vatTotalNonEditableText.setValue(DataUtils
				// .getAmountAsString(transaction.getTotal()
				// - transaction.getNetAmount()));
				vatTotalNonEditableText.setTransaction(transaction);
			}
			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(isAmountIncludeTAX());
			}
			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					this.discountField
							.setAmount(getdiscount(this.transactionItems));
				}
			}
			memoTextAreaItem.setDisabled(true);
			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			foreignCurrencyamountLabel.setAmount(transaction.getTotal());

			customerTransactionTable.setEnabled(!isInViewMode());
			transactionDateItem.setEnabled(!isInViewMode());
			transactionNumber.setEnabled(!isInViewMode());
			phoneSelect.setEnabled(!isInViewMode());
			billToTextArea.setEnabled(!isInViewMode());
			customerCombo.setEnabled(!isInViewMode());
			payTermsSelect.setEnabled(!isInViewMode());
			salesPersonCombo.setEnabled(!isInViewMode());
			memoTextAreaItem.setEnabled(!isInViewMode());
			contactCombo.setEnabled(!isInViewMode());
			quoteExpiryDate.setEnabled(!isInViewMode());
			deliveryDate.setEnabled(!isInViewMode());
			taxCodeSelect.setEnabled(!isInViewMode());
			statusCombo.setEnabled(!isInViewMode());
			customerOrderText.setEnabled(!isInViewMode());
			statusCombo.initCombo(getStatusList());
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		if (getPreferences().isJobTrackingEnabled()) {
			jobSelected(Accounter.getCompany().getjob(transaction.getJob()));
		}
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
		customerCombo.setEnabled(!isInViewMode());

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
		if (transaction == null && customerTransactionTable != null) {
			customerTransactionTable.setPricingLevel(priceLevel);
			// customerTransactionTable.updatePriceLevel();
		}
		updateNonEditableItems();

	}

	@Override
	public void updateNonEditableItems() {
		if (customerTransactionTable == null)
			return;
		ClientTAXCode tax = taxCodeSelect.getSelectedValue();
		if (tax != null) {
			for (ClientTransactionItem item : customerTransactionTable
					.getRecords()) {
				item.setTaxCode(tax.getID());
			}
		}
		if (isTrackTax()) {
			netAmountLabel.setAmount(customerTransactionTable.getLineTotal());
			// vatTotalNonEditableText.setAmount(customerTransactionTable
			// .getTotalTax());
			if (transaction.getTransactionItems() != null && !isInViewMode()) {
				transaction.setTransactionItems(customerTransactionTable
						.getAllRows());
			}
			if (currency != null) {
				transaction.setCurrency(currency.getID());
			}
			vatTotalNonEditableText.setTransaction(transaction);

			setSalesTax(customerTransactionTable.getTotalTax());
		}
		setTransactionTotal(customerTransactionTable.getGrandTotal());
	}

	public void setTransactionTotal(Double transactionTotal) {
		if (transactionTotal == null)
			transactionTotal = 0.0D;
		transactionTotalBaseCurrencyText
				.setAmount(getAmountInBaseCurrency(transactionTotal));
		foreignCurrencyamountLabel.setAmount(transactionTotal);
	}

	public void setSalesTax(Double salesTax) {
		if (salesTax == null)
			salesTax = 0.0D;
		this.salesTax = salesTax;
		if ((transaction.getTransactionItems() != null && transaction
				.getTransactionItems().isEmpty()) && !isInViewMode()) {
			transaction.setTransactionItems(customerTransactionTable
					.getAllRows());
		}
		salesTaxTextNonEditable.setTransaction(transaction);
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// 1. isValidDueOrDeliveryDate?
		if (type == ClientEstimate.SALES_ORDER
				&& !AccounterValidator.isValidDueOrDelivaryDates(
						this.dueDateItem.getDate(), getTransactionDate())) {
			result.addError(this.dueDateItem,
					messages.the() + " " + messages.dueDate() + " " + " "
							+ messages.cannotbeearlierthantransactiondate());
		}
		if (!AccounterValidator.isValidDueOrDelivaryDates(
				this.quoteExpiryDate.getEnteredDate(), this.transactionDate)) {
			result.addError(
					this.quoteExpiryDate,
					messages.the() + " " + messages.expirationDate() + " "
							+ " "
							+ messages.cannotbeearlierthantransactiondate());
		}
		result.add(customerTransactionTable.validateGrid());
		if (isTrackTax()) {
			if (!isTaxPerDetailLine()) {
				if (taxCodeSelect != null
						&& taxCodeSelect.getSelectedValue() == null) {
					result.addError(taxCodeSelect,
							messages.pleaseSelect(messages.taxCode()));
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

	@Override
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
				if (transaction.getStatus() == ClientEstimate.STATUS_ACCECPTED) {
					Accounter.showError(messages.thisQuoteAlreadyAccepted());
				} else if (caught instanceof InvocationException) {
					Accounter.showMessage(messages.sessionExpired());
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
		transactionDateItem.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		customerCombo.setEnabled(!isInViewMode());
		if (getPreferences().isSalesPersonEnabled())
			salesPersonCombo.setEnabled(!isInViewMode());
		payTermsSelect.setEnabled(!isInViewMode());
		deliveryDate.setEnabled(!isInViewMode());
		quoteExpiryDate.setEnabled(!isInViewMode());
		taxCodeSelect.setEnabled(!isInViewMode());
		memoTextAreaItem.setEnabled(!isInViewMode());
		// priceLevelSelect.setEnabled(!isInViewMode());
		customerTransactionTable.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		discountField.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		if (locationCombo != null) {
			locationCombo.setEnabled(!isInViewMode());
		}
		if (classListCombo != null) {
			classListCombo.setEnabled(!isInViewMode());
		}
		statusCombo.setEnabled(!isInViewMode());
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setEnabled(!isInViewMode());
			if (customer != null) {
				jobListCombo.setCustomer(customer);
			}
		}
		customerOrderText.setEnabled(!isInViewMode());
		shippingTermsCombo.setEnabled(!isInViewMode());
		dueDateItem.setEnabled(!isInViewMode());
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
		ArrayList<ClientBrandingTheme> themesList = Accounter.getCompany()
				.getBrandingTheme();
		if (themesList.size() > 1) {
			// if there are more than one branding themes, then show branding
			// theme combo box
			ActionFactory.getBrandingThemeComboAction().run(transaction, false);
		} else {
			// if there is only one branding theme
			ClientBrandingTheme clientBrandingTheme = themesList.get(0);
			UIUtils.downloadAttachment(transaction.getID(),
					ClientTransaction.TYPE_ESTIMATE,
					clientBrandingTheme.getID());
		}

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
		if (type == ClientEstimate.CHARGES) {
			return messages.charge();
		} else if (type == ClientEstimate.CREDITS) {
			return messages.credit();
		} else if (type == ClientEstimate.SALES_ORDER) {
			return messages.salesOrder();
		}
		return messages.quote();
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
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(12);
		if (saveAndNewButton != null)
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
		String formalName = currencyWidget.getSelectedCurrency()
				.getFormalName();
		if (currencyWidget.isShowFactorField()) {
			foreignCurrencyamountLabel.hide();
		} else {
			foreignCurrencyamountLabel.show();
			foreignCurrencyamountLabel.setTitle(messages
					.currencyTotal(formalName));
		}
		netAmountLabel.setTitle(messages.currencyNetAmount(formalName));
	}

	public List<String> getStatusList() {
		ArrayList<String> statuses = new ArrayList<String>();
		statuses.add(messages.open());
		if (type != ClientEstimate.SALES_ORDER) {
			statuses.add(messages.accepted());
			statuses.add(messages.closed());
			if (transaction == null
					|| transaction.getStatus() == ClientEstimate.STATUS_REJECTED
					|| transaction.getSaveStatus() != ClientTransaction.STATUS_DRAFT) {
				statuses.add(messages.rejected());
			}
		} else {
			statuses.add(messages.completed());
			statuses.add(messages.cancelled());
		}

		return statuses;
	}

	@Override
	public boolean canPrint() {
		EditMode mode = getMode();
		if (type == ClientEstimate.QUOTES) {
			if (mode == EditMode.CREATE || mode == EditMode.EDIT
					|| data.getSaveStatus() == ClientTransaction.STATUS_DRAFT) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean canExportToCsv() {
		return false;
	}

	@Override
	protected void updateDiscountValues() {

		if (discountField.getAmount() != null) {
			customerTransactionTable.setDiscount(discountField.getAmount());
		} else {
			discountField.setAmount(0d);
		}
	}
}
