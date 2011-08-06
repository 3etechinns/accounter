package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ShipToForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.SalesOrderGrid;

public class SalesOrderView extends
		AbstractCustomerTransactionView<ClientSalesOrder> {

	private Double payments = 0.0;

	private Double balanceDue = 0.0;
	private DateField dueDateItem;
	private LabelItem quoteLabel;
	private SalesQuoteListDialog dialog;
	private long selectedEstimateId;

	private ArrayList<DynamicForm> listforms;
	private TextItem customerOrderText;
	private Label lab1;
	private ArrayList<ClientEstimate> selectedSalesOrders;
	private String OPEN = Accounter.constants().open();
	private String COMPLETED = Accounter.constants().completed();
	private String CANCELLED = Accounter.constants().cancelled();
	private TextAreaItem billToTextArea;
	private ShipToForm shipToAddress;

	public SalesOrderView() {
		super(ClientTransaction.TYPE_SALES_ORDER, CUSTOMER_TRANSACTION_GRID);
	}

	@Override
	protected void createControls() {
		// setTitle(UIUtils.title(FinanceApplication.constants()
		// .salesOrder()));
		LinkItem emptylabel = new LinkItem();
		emptylabel.setLinkTitle("");
		emptylabel.setShowTitle(false);

		lab1 = new Label(Accounter.constants().salesOrder());
		lab1.setStyleName(Accounter.constants().labelTitle());
		// lab1.setHeight("35px");
		statusSelect = new SelectCombo(Accounter.constants().status());

		selectComboList = new ArrayList<String>();
		selectComboList.add(OPEN);
		selectComboList.add(COMPLETED);
		selectComboList.add(CANCELLED);
		statusSelect.initCombo(selectComboList);
		statusSelect.setComboItem(OPEN);
		statusSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (statusSelect.getSelectedValue() != null)
							statusSelect.setComboItem(selectItem);

					}
				});
		statusSelect.setRequired(true);
		statusSelect.setDisabled(isEdit);

		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(Accounter.constants().orderNo());
		transactionNumber.setWidth(50);

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.addStyleName("date-number");
		dateNoForm.setFields(statusSelect, transactionDateItem);
		forms.add(dateNoForm);

		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("99%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		customerCombo = new CustomerCombo(Accounter.constants().customer(),
				true);
		customerCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					@Override
					public void selectedComboBoxItem(ClientCustomer selectItem) {
						customerSelected(selectItem);

					}

				});

		customerCombo.setRequired(true);
		customerCombo.setHelpInformation(true);
		customerCombo.setDisabled(isEdit);
		formItems.add(customerCombo);

		customerCombo.setWidth(100);
		quoteLabel = new LabelItem();
		quoteLabel.setValue(Accounter.constants().quotes());
		quoteLabel.setWidth("100%");
		quoteLabel.addStyleName("falseHyperlink");
		quoteLabel.setShowTitle(false);
		quoteLabel.setDisabled(isEdit);
		quoteLabelListener();
		contactCombo = createContactComboItem();
		contactCombo.setWidth(100);

		billToTextArea = new TextAreaItem();
		billToTextArea.setWidth(100);
		billToTextArea.setTitle(Accounter.constants().billTo());
		billToTextArea.setDisabled(true);

		shipToCombo = createShipToComboItem();
		shipToAddress = new ShipToForm(null);
		shipToAddress.getCellFormatter().getElement(0, 0).getStyle()
				.setVerticalAlign(VerticalAlign.TOP);
		shipToAddress.getCellFormatter().setWidth(0, 0, "40px");
		shipToAddress.getCellFormatter().addStyleName(0, 1, "memoFormAlign");
		shipToAddress.businessSelect.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				shippingAddress = shipToAddress.getAddress();
				if (shippingAddress != null)
					shipToAddress.setAddres(shippingAddress);
				else
					shipToAddress.addrArea.setValue("");
			}
		});
		if (isEdit)
			shipToAddress.businessSelect.setDisabled(true);

		phoneSelect = new TextItem(customerConstants.phone());
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(isEdit);

		custForm = UIUtils.form(customerConstants.billingAddress());
		custForm.setNumCols(3);
		// custForm.setWidth("50%");
		custForm.setFields(customerCombo, quoteLabel, contactCombo, emptylabel,
				phoneSelect, emptylabel, billToTextArea, emptylabel);
		custForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		custForm.getCellFormatter().setWidth(0, 1, "180px");
		custForm.getCellFormatter().setWidth(0, 0, "225px");
		forms.add(custForm);

		customerOrderText = new TextItem(Accounter.constants()
				.customerOrderNo());
		customerOrderText.setWidth(50);
		customerOrderText.setColSpan(1);
		customerOrderText.setDisabled(isEdit);

		salesPersonCombo = createSalesPersonComboItem();

		// salesPersonCombo = new SalesPersonCombo(FinanceApplication
		// .constants().salesPerson(), false);
		// salesPersonCombo.setDisabled(isEdit);
		// salesPersonCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientSalesPerson>() {
		//
		// public void selectedComboBoxItem(
		// ClientSalesPerson selectItem) {
		//
		// salesPersonSelected(selectItem);
		//
		// }
		//
		// });

		payTermsSelect = createPaymentTermsSelectItem();

		// payTermsSelect = new PaymentTermsCombo(FinanceApplication
		// .constants().paymentTerms(), false);
		// payTermsSelect.setDisabled(isEdit);
		// payTermsSelect
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {
		//
		// public void selectedComboBoxItem(
		// ClientPaymentTerms selectItem) {
		//
		// paymentTermsSelected(selectItem);
		//
		// }
		//
		// });

		shippingTermsCombo = createShippingTermsCombo();

		// shippingTermsCombo = new ShippingTermsCombo("Shipping Terms ",
		// false);
		// shippingTermsCombo.setDisabled(isEdit);
		// shippingTermsCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientShippingTerms>() {
		//
		// public void selectedComboBoxItem(
		// ClientShippingTerms selectItem) {
		//
		// shippingTermSelected(selectItem);
		//
		// }
		//
		// });

		shippingMethodsCombo = createShippingMethodCombo();

		// shippingMethodsCombo = new ShippingMethodsCombo(
		// FinanceApplication.constants().shippingMethod(),false);
		// shippingMethodsCombo.setDisabled(isEdit);
		// shippingMethodsCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientShippingMethod>() {
		//
		// public void selectedComboBoxItem(
		// ClientShippingMethod selectItem) {
		// shippingMethodSelected(selectItem);
		// }
		//
		// });

		dueDateItem = createDueDateItem();

		DynamicForm termsForm = new DynamicForm();
		termsForm.setWidth("100%");
		termsForm.setIsGroup(true);
		termsForm.setGroupTitle(customerConstants.terms());
		termsForm.setNumCols(2);
		if (ClientCompanyPreferences.get().isSalesPersonEnabled()) {
			termsForm.setFields(transactionNumber, customerOrderText,
					salesPersonCombo, payTermsSelect, shippingTermsCombo,
					shippingMethodsCombo, dueDateItem);
		} else {
			termsForm.setFields(transactionNumber, customerOrderText,
					payTermsSelect, shippingTermsCombo, shippingMethodsCombo,
					dueDateItem);
		}
		termsForm.getCellFormatter().setWidth(0, 0, "230px");
		forms.add(termsForm);

		Label lab2 = new Label(customerConstants.productAndService());

		memoTextAreaItem = createMemoTextAreaItem();
		// refText = createRefereceText();
		// refText.setWidth(100);

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setNumCols(2);
		prodAndServiceForm1.setFields(memoTextAreaItem);
		prodAndServiceForm1.getCellFormatter().addStyleName(0, 0,
				"memoFormAlign");
		forms.add(prodAndServiceForm1);

		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();

		priceLevelSelect = createPriceLevelSelectItem();
		taxCodeSelect = createTaxCodeSelectItem();

		paymentsNonEditableText = new AmountLabel(customerConstants.payments());
		paymentsNonEditableText.setDisabled(true);
		paymentsNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");

		balanceDueNonEditableText = new AmountLabel(
				customerConstants.balanceDue());
		balanceDueNonEditableText.setDisabled(true);
		balanceDueNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");
		salesTaxTextNonEditable = createSalesTaxNonEditableLabel();

		netAmountLabel = createNetAmountLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();

		vatTotalNonEditableText = createVATTotalNonEditableLabel();

		customerTransactionGrid = getGrid();
		customerTransactionGrid.setTransactionView(this);
		customerTransactionGrid.isEnable = false;
		customerTransactionGrid.init();
		customerTransactionGrid.setCanEdit(true);
		customerTransactionGrid.setDisabled(isEdit);
		customerTransactionGrid.setWidth("99.5%");
		customerTransactionGrid.setHeight("250px");
		customerTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);

		DynamicForm prodAndServiceForm2 = new DynamicForm();
		prodAndServiceForm2.setWidth("100%");
		prodAndServiceForm2.setNumCols(4);

		TextItem dummyItem = new TextItem("");
		dummyItem.setVisible(false);
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			prodAndServiceForm2.setFields(dummyItem, netAmountLabel, dummyItem,
					vatTotalNonEditableText, dummyItem,
					transactionTotalNonEditableText);
			prodAndServiceForm2.setStyleName("invoice-total");
		} else {
			prodAndServiceForm2.setFields(taxCodeSelect,
					salesTaxTextNonEditable, dummyItem,
					transactionTotalNonEditableText);
			prodAndServiceForm2.setStyleName("tax-form");
		}

		forms.add(prodAndServiceForm2);

		HorizontalPanel panel = new HorizontalPanel();
		panel.setHorizontalAlignment(ALIGN_RIGHT);
		panel.add(createAddNewButton());
		panel.getElement().getStyle().setMarginTop(8, Unit.PX);

		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");
		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(prodAndServiceForm2);
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "30%");
		} else
			prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "50%");
		prodAndServiceHLay.setCellHorizontalAlignment(prodAndServiceForm2,
				ALIGN_RIGHT);

		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");
		vpanel.setHorizontalAlignment(ALIGN_RIGHT);
		vpanel.add(panel);

		menuButton.setType(AccounterButton.ADD_BUTTON);

		vpanel.add(prodAndServiceHLay);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setHorizontalAlignment(ALIGN_LEFT);
		// leftVLay.setWidth("100%");
		leftVLay.add(custForm);
		if (ClientCompanyPreferences.get().isDoProductShipMents())
			leftVLay.add(shipToAddress);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setHorizontalAlignment(ALIGN_RIGHT);
		// rightVLay.setWidth("100%");
		rightVLay.add(termsForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "42%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		// mainVLay.add(lab2);

		mainVLay.add(customerTransactionGrid);
		// mainVLay.add(createAddNewButton());
		mainVLay.add(vpanel);

		if (UIUtils.isMSIEBrowser()) {
			resetFormView();
			termsForm.getCellFormatter().setWidth(0, 1, "230px");
			termsForm.setWidth("90%");
		}
		this.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(termsForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(prodAndServiceForm2);

	}

	private void quoteLabelListener() {
		if (!isEdit) {
			quoteLabel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					getEstimates();
				}
			});
		}
	}

	public void resetFormView() {
		custForm.getCellFormatter().setWidth(0, 1, "200px");
		custForm.setWidth("94%");
		shipToAddress.getCellFormatter().setWidth(0, 1, "100");
		shipToAddress.getCellFormatter().setWidth(0, 2, "200");
		statusSelect.setWidth("150px");
		// refText.setWidth("200px");
	}

	public AbstractTransactionGrid<ClientTransactionItem> getGrid() {

		// if (getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US)
		// return new SalesOrderUSGrid();
		// else
		// return new SalesOrderUKGrid();
		return new SalesOrderGrid();

	}

	private void initDueDate() {

		// if (transactionObject != null) {
		// ClientInvoice invoice = (ClientInvoice) transactionObject;
		// if (invoice.getDueDate() != 0) {
		// dueDateItem.setEnteredDate(new Date(invoice.getDueDate()));
		// } else if (invoice.getPaymentTerm() != null) {
		// ClientPaymentTerms terms = FinanceApplication.getCompany()
		// .getPaymentTerms(invoice.getPaymentTerm());
		// Date transactionDate = this.transactionDateItem
		// .getEnteredDate();
		// Date dueDate = new Date(invoice.getDueDate());
		// dueDate = Utility.getCalculatedDueDate(transactionDate, terms);
		// if (dueDate != null) {
		// dueDateItem.setEnteredDate(dueDate);
		// }
		//
		// }
		//
		// } else
		dueDateItem.setEnteredDate(new ClientFinanceDate());

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientSalesOrder());
			initCustomers();
			initPriceLevels();
			initTaxItemGroups();
			initSalesTaxNonEditableItem();
			initTransactionTotalNonEditableItem();
			initMemoAndReference();
			initPaymentTerms();
			initShippingTerms();
			initShippingMethod();
			initDueDate();
		} else {

			ClientCompany company = getCompany();
			this.setCustomer(company.getCustomer(transaction.getCustomer()));
			this.billingAddress = transaction.getBillingAddress();

			this.contact = transaction.getContact();
			this.addressListOfCustomer = getCustomer().getAddress();

			if (billingAddress != null) {
				billToTextArea.setValue(billingAddress.getAddress1() + "\n"
						+ billingAddress.getStreet() + "\n"
						+ billingAddress.getCity() + "\n"
						+ billingAddress.getStateOrProvinence() + "\n"
						+ billingAddress.getZipOrPostalCode() + "\n"
						+ billingAddress.getCountryOrRegion());

			}
			this.shippingAddress = transaction.getShippingAdress();
			List<ClientAddress> addresses = new ArrayList<ClientAddress>();
			if (getCustomer() != null)
				addresses.addAll(getCustomer().getAddress());
			shipToAddress.setListOfCustomerAdress(addresses);
			if (shippingAddress != null) {
				shipToAddress.businessSelect.setValue(shippingAddress
						.getAddressTypes().get(shippingAddress.getType()));
				shipToAddress.setAddres(shippingAddress);
			}

			// this.priceLevel = company.getPriceLevel(salesOrderToBeEdited
			// .getPriceLevel());
			shippingMethodSelected(company.getShippingMethod(transaction
					.getShippingMethod()));
			this.paymentTerm = company.getPaymentTerms(transaction
					.getPaymentTerm());
			shippingTermSelected(company.getShippingTerms(transaction
					.getShippingTerm()));
			this.transactionItems = transaction.getTransactionItems();
			// this.taxCode =
			// getTaxItemGroupForTransactionItems(this.transactionItems);

			customerSelected(this.getCustomer());
			int status = transaction.getStatus();
			switch (status) {
			case ClientTransaction.STATUS_OPEN:
				statusSelect.setComboItem(OPEN);
				break;
			case ClientTransaction.STATUS_COMPLETED:
				statusSelect.setComboItem(COMPLETED);
				break;
			case ClientTransaction.STATUS_CANCELLED:
				statusSelect.setComboItem(CANCELLED);
			default:
				break;
			}

			if (transaction.getPhone() != null)
				phoneNo = transaction.getPhone();
			if (getCustomer().getPhoneNo().isEmpty())
				phoneSelect.setValue(phoneNo);

			contactSelected(this.contact);

			// billToaddressSelected(this.billingAddress);
			// shipToAddressSelected(shippingAddress);

			customerOrderText.setValue(transaction.getCustomerOrderNumber());
			paymentTermsSelected(this.paymentTerm);
			// priceLevelSelected(this.priceLevel);
			salesPersonSelected(company.getSalesPerson(transaction
					.getSalesPerson()));
			shippingMethodSelected(this.shippingMethod);
			shippingTermSelected(this.shippingTerm);
			taxCodeSelected(this.taxCode);
			dueDateItem.setEnteredDate(new ClientFinanceDate(transaction
					.getDueDate()));

			memoTextAreaItem.setValue(transaction.getMemo());
			// refText.setValue(salesOrderToBeEdited.getReference());

			if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				netAmountLabel.setAmount(transaction.getNetAmount());
				vatTotalNonEditableText.setAmount(transaction.getTotal()
						- transaction.getNetAmount());
			} else if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
				this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
				if (taxCode != null) {
					this.taxCodeSelect
							.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
				}
				this.salesTaxTextNonEditable.setAmount(transaction
						.getSalesTaxAmount());
				this.transactionTotalNonEditableText.setAmount(transaction
						.getTotal());
			}
			customerTransactionGrid.setCanEdit(false);
		}
		initTransactionNumber();
		initSalesPersons();
	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		if (isEdit) {
			Double salesTaxAmout = ((ClientSalesOrder) transaction)
					.getSalesTaxAmount();
			setSalesTax(salesTaxAmout);

		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (isEdit) {
			Double transactionTotal = ((ClientSalesOrder) transaction)
					.getTotal();
			setTransactionTotal(transactionTotal);

		}

	}

	private void initPayments() {

		// if (isEdit) {
		//
		// ClientInvoice invoice = (ClientInvoice) transaction;
		//
		// // setPayments(invoice.getPayments());
		// Double payment = invoice.getPayments();
		// if (payment == null)
		// payment = 0.0D;
		// this.payments = payment;
		// paymentsNonEditableText.setAmount(payment);
		// }

	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();

		super.saveAndUpdateView();

		saveOrUpdate(transaction);
	}

	protected void updateTransaction() {
		super.updateTransaction();
		if (statusSelect.getSelectedValue().equals(OPEN))
			transaction.setStatus(ClientTransaction.STATUS_OPEN);
		else if (statusSelect.getSelectedValue().equals(COMPLETED))
			transaction.setStatus(ClientTransaction.STATUS_COMPLETED);
		else if (statusSelect.getSelectedValue().equals(CANCELLED))
			transaction.setStatus(ClientTransaction.STATUS_CANCELLED);
		if (getCustomer() != null)
			transaction.setCustomer(getCustomer().getID());
		if (contact != null)
			transaction.setContact(contact);
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());
		if (billingAddress != null)
			transaction.setBillingAddress(billingAddress);
		if (shippingAddress != null)
			transaction.setShippingAdress(shippingAddress);

		if (customerOrderText.getValue() != null)
			transaction.setCustomerOrderNumber(customerOrderText.getValue()
					.toString());
		if (salesPerson != null)
			transaction.setSalesPerson(salesPerson.getID());
		if (paymentTerm != null)
			transaction.setPaymentTerm(paymentTerm.getID());
		if (shippingTerm != null)
			transaction.setShippingTerm(shippingTerm.getID());
		if (shippingMethod != null)
			transaction.setShippingMethod(shippingMethod.getID());
		if (dueDateItem.getEnteredDate() != null)
			transaction.setDueDate(dueDateItem.getEnteredDate().getDate());

		if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
			if (taxCode != null) {
				for (ClientTransactionItem record : customerTransactionGrid
						.getRecords()) {
					record.setTaxItemGroup(taxCode.getID());

				}
			}
			transaction.setSalesTaxAmount(this.salesTax);
		} else if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			transaction.setNetAmount(netAmountLabel.getAmount());
			// transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
			// .getValue());
		}

		transaction.setTotal(transactionTotalNonEditableText.getAmount());

		transaction.setMemo(getMemoTextAreaItem());
		// transaction.setReference(getRefText());
		if (selectedEstimateId != 0)
			transaction.setEstimate(selectedEstimateId);
	}

	@Override
	protected void customerSelected(final ClientCustomer customer) {

		if (customer != null) {
			if (this.getCustomer() != null
					&& !this.getCustomer().equals(customer)
					&& transaction == null)
				customerTransactionGrid.removeAllRecords();
			selectedSalesOrders = new ArrayList<ClientEstimate>();
			this.setCustomer(customer);
			super.customerSelected(customer);
			customerCombo.setComboItem(customer);
			// if (transactionObject == null)
			// getEstimates();
			if (customer.getPhoneNo() != null)
				phoneSelect.setValue(customer.getPhoneNo());
			else
				phoneSelect.setValue("");
			this.addressListOfCustomer = customer.getAddress();
			billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
			if (billingAddress != null) {
				billToTextArea.setValue(billingAddress.getAddress1() + "\n"
						+ billingAddress.getStreet() + "\n"
						+ billingAddress.getCity() + "\n"
						+ billingAddress.getStateOrProvinence() + "\n"
						+ billingAddress.getZipOrPostalCode() + "\n"
						+ billingAddress.getCountryOrRegion());

			} else
				billToTextArea.setValue("");
			List<ClientAddress> addresses = new ArrayList<ClientAddress>();
			addresses.addAll(customer.getAddress());
			shipToAddress.setAddress(addresses);
		}
	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson person) {
		salesPerson = person;
		if (salesPerson != null) {

			salesPersonCombo.setComboItem(getCompany().getSalesPerson(
					salesPerson.getID()));

		}
		salesPersonCombo.setDisabled(isEdit);

	}

	@Override
	protected void paymentTermsSelected(ClientPaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
		if (this.paymentTerm != null && payTermsSelect != null) {

			payTermsSelect.setComboItem(getCompany().getPaymentTerms(
					paymentTerm.getID()));
		}
		ClientFinanceDate transDate = this.transactionDateItem.getEnteredDate();

		if (transDate != null && paymentTerm != null) {
			ClientFinanceDate dueDate = Utility.getCalculatedDueDate(transDate,
					paymentTerm);
			if (dueDate != null) {
				dueDateItem.setValue(dueDate);
			}
		}

	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {
		// this.priceLevel = priceLevel;
		// if (priceLevel != null && priceLevelSelect != null) {
		//
		// priceLevelSelect.setComboItem(FinanceApplication.getCompany()
		// .getPriceLevel(priceLevel.getID()));
		//
		// }
		// if (transactionObject == null && customerTransactionGrid != null) {
		// customerTransactionGrid.priceLevelSelected(priceLevel);
		// customerTransactionGrid.updatePriceLevel();
		// }
		// updateNonEditableItems();

	}

	protected DateField createDueDateItem() {

		DateField dateItem = new DateField(Accounter.constants().dueDate());
		dateItem.setTitle(Accounter.constants().dueDate());
		dateItem.setColSpan(1);

		dateItem.setDisabled(isEdit);

		formItems.add(dateItem);

		return dateItem;

	}

	@Override
	public void setTransactionDate(ClientFinanceDate transactionDate) {
		super.setTransactionDate(transactionDate);
		if (this.transactionDateItem != null
				&& this.transactionDateItem.getValue() != null)
			;
		updateNonEditableItems();
	}

	@Override
	public void updateNonEditableItems() {
		if (customerTransactionGrid == null)
			return;
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			Double taxableLineTotal = customerTransactionGrid
					.getTaxableLineTotal();

			if (taxableLineTotal == null)
				return;

			Double salesTax = taxCode != null ? Utility.getCalculatedSalesTax(
					transactionDateItem.getEnteredDate(),
					taxableLineTotal,
					getCompany().getTAXItemGroup(
							taxCode.getTAXItemGrpForSales())) : 0;

			setSalesTax(salesTax);

			setTransactionTotal(customerTransactionGrid.getTotal()
					+ this.salesTax);
		} else {
			if (customerTransactionGrid.getGrandTotal() != null
					&& customerTransactionGrid.getTotalValue() != null) {
				netAmountLabel.setAmount(customerTransactionGrid
						.getGrandTotal());
				vatTotalNonEditableText.setAmount(customerTransactionGrid
						.getTotalValue()
						- customerTransactionGrid.getGrandTotal());
				setTransactionTotal(customerTransactionGrid.getTotalValue());
			}
		}
		// Double payments = this.paymentsNonEditableText.getAmount();
		// setBalanceDue((this.transactionTotal - payments));
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		result.add(FormItem.validate(statusSelect));
		result.add(super.validate());
		return result;
	}

	private void getEstimates() {
		if (this.rpcUtilService == null)
			return;
		if (getCustomer() == null) {
			Accounter.showError(Accounter.constants().pleaseSelectCustomer());
		} else {
			this.rpcUtilService.getEstimates(getCustomer().getID(),
					new AccounterAsyncCallback<List<ClientEstimate>>() {

						public void onException(AccounterException caught) {
							// Accounter.showError(FinanceApplication
							// .constants()
							// .noQuotesForCustomer()
							// + " " + customer.getName());
							return;

						}

						public void onSuccess(List<ClientEstimate> result) {

							if (result == null)
								onFailure(new Exception());

							if (result.size() > 0) {
								showQuotesDialog(result);
							} else {
								showQuotesDialog(result);
							}

						}

					});

		}
	}

	protected void showQuotesDialog(List<ClientEstimate> result) {
		if (result == null)
			return;

		List<ClientEstimate> filteredList = new ArrayList<ClientEstimate>();
		filteredList.addAll(result);

		for (ClientEstimate record : result) {
			for (ClientEstimate estimate : selectedSalesOrders) {
				if (estimate.getID() == record.getID())
					filteredList.remove(record);
			}
		}

		if (dialog == null) {
			dialog = new SalesQuoteListDialog(this, filteredList);
		}

		dialog.setQuoteList(filteredList);
		dialog.show();

		if (filteredList.isEmpty()) {
			dialog.grid
					.addEmptyMessage(Accounter.constants().noRecordsToShow());
		}

	}

	public void selectedQuote(ClientEstimate selectedEstimate) {
		if (selectedEstimate == null)
			return;
		for (ClientTransactionItem record : this.customerTransactionGrid
				.getRecords()) {
			for (ClientTransactionItem salesRecord : selectedEstimate
					.getTransactionItems())
				if (record.getReferringTransactionItem() == salesRecord.getID())
					customerTransactionGrid.deleteRecord(record);

		}
		// if (dialog.preCustomer == null || dialog.preCustomer !=
		// this.customer) {
		// dialog.preCustomer = this.customer;
		// } else {
		// return;
		// }

		if (selectedSalesOrders != null)
			selectedSalesOrders.add(selectedEstimate);

		List<ClientTransactionItem> itemsList = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : selectedEstimate
				.getTransactionItems()) {
			if (item.getLineTotal() - item.getInvoiced() <= 0) {
				continue;
			}
			ClientTransactionItem clientItem = new ClientTransactionItem();
			if (item.getLineTotal() != 0.0) {
				clientItem.setDescription(item.getDescription());
				clientItem.setType(item.getType());
				clientItem.setAccount(item.getAccount());
				clientItem.setItem(item.getItem());
				clientItem.setVatItem(item.getVatItem());
				clientItem.setVATfraction(item.getVATfraction());
				// clientItem.setVatCode(item.getVatCode());
				clientItem.setTaxCode(item.getTaxCode());
				clientItem.setDescription(item.getDescription());
				clientItem.setQuantity(item.getQuantity());
				clientItem.setUnitPrice(item.getUnitPrice());
				clientItem.setDiscount(item.getDiscount());
				clientItem.setLineTotal(item.getLineTotal()
						- item.getInvoiced());
				clientItem.setTaxable(item.isTaxable());
				clientItem.setReferringTransactionItem(item.getID());
				itemsList.add(clientItem);
			}

		}
		selectedEstimateId = selectedEstimate.getID();
		orderNum = selectedEstimate.getNumber();
		customerTransactionGrid.setAllTransactions(itemsList);
		// if (selectedEstimate == null)
		// return;
		//
		// selectedSalesOrders.add(selectedEstimate);
		//
		// ClientSalesOrder convertedSalesOrder = new ClientSalesOrder(
		// selectedEstimate);
		//
		// selectedEstimateId = selectedEstimate.getID();
		//
		// if (convertedSalesOrder == null) {
		// Accounter.showError(FinanceApplication.constants()
		// .couldNotLoadQuote());
		// return;
		// }
		//
		// this.transactionObject = convertedSalesOrder;
		//
		// // initTransactionViewData(convertedSalesOrder);
		// this.transactionItems = convertedSalesOrder.getTransactionItems();
		// customerTransactionGrid.setAllTransactions(transactionItems);

	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		if (core.getID() == this.shippingTermsCombo.getSelectedValue().getID()) {
			this.shippingTermsCombo
					.addItemThenfireEvent((ClientShippingTerms) core);
		}
		if (core.getID() == this.shippingMethodsCombo.getSelectedValue()
				.getID()) {
			this.shippingMethodsCombo
					.addItemThenfireEvent((ClientShippingMethod) core);
		}

	}

	public void onEdit() {
		if (transaction.getStatus() == ClientTransaction.STATUS_COMPLETED)
			Accounter.showError(Accounter.constants()
					.completedSalesOrdercantbeedited());
		else {
			AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

				@Override
				public void onException(AccounterException caught) {
					Accounter.showError(caught.getMessage());
				}

				@Override
				public void onSuccess(Boolean result) {
					// if (statusSelect.getValue().equals(COMPLETED))
					// Accounter
					// .showError("Completed sales order can't be edited.");
					if (result)
						enableFormItems();
				}

			};

			AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
					.getType());
			this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);
		}
	}

	protected void enableFormItems() {
		isEdit = false;
		statusSelect.setDisabled(isEdit);
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		ClientTransactionItem item = new ClientTransactionItem();
		if (!DecimalUtil.isEquals(item.getInvoiced(), 0)) {
			customerCombo.setDisabled(isEdit);
		} else {
			customerCombo.setDisabled(true);
		}
		taxCodeSelect.setDisabled(isEdit);
		customerOrderText.setDisabled(isEdit);
		customerTransactionGrid.setDisabled(false);
		quoteLabel.setDisabled(isEdit);

		quoteLabelListener();
		if (ClientCompanyPreferences.get().isSalesPersonEnabled())
			salesPersonCombo.setDisabled(isEdit);
		shippingTermsCombo.setDisabled(isEdit);
		payTermsSelect.setDisabled(isEdit);
		shippingMethodsCombo.setDisabled(isEdit);
		dueDateItem.setDisabled(isEdit);
		shipToAddress.businessSelect.setDisabled(isEdit);
		customerTransactionGrid.setCanEdit(true);
		super.onEdit();
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {

			taxCodeSelect
					.setComboItem(getCompany().getTAXCode(taxCode.getID()));
			customerTransactionGrid.setTaxCode(taxCode.getID());
		} else
			taxCodeSelect.setValue("");
		// updateNonEditableItems();

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().salesOrder();
	}

	@Override
	protected void initMemoAndReference() {
		// TODO Auto-generated method stub

	}

}
