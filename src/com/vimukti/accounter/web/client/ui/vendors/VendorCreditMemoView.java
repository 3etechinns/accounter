package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class VendorCreditMemoView extends
		AbstractVendorTransactionView<ClientVendorCreditMemo> {
	private DynamicForm vendorForm;
	private ArrayList<DynamicForm> listforms;
	private VendorAccountTransactionTable vendorAccountTransactionTable;
	private VendorItemTransactionTable vendorItemTransactionTable;
	private AddNewButton accountTableButton, itemTableButton;
	private final boolean locationTrackingEnabled;
	private DisclosurePanel accountsDisclosurePanel, itemsDisclosurePanel;

	private VendorCreditMemoView() {
		super(ClientTransaction.TYPE_VENDOR_CREDIT_MEMO);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		if (vendor == null) {
			return;
		}
		if (this.getVendor() != null && this.getVendor() != vendor) {
			ClientVendorCreditMemo ent = this.transaction;

			if (ent != null && ent.getVendor() == vendor.getID()) {
				this.vendorAccountTransactionTable
						.setRecords(getAccountTransactionItems(ent
								.getTransactionItems()));
				this.vendorItemTransactionTable
						.setRecords(getItemTransactionItems(ent
								.getTransactionItems()));
			} else if (ent != null && ent.getVendor() != vendor.getID()) {
				this.vendorAccountTransactionTable.resetRecords();
				this.vendorAccountTransactionTable.updateTotals();
				this.vendorItemTransactionTable.updateTotals();
			}
		}

		long currency = vendor.getCurrency();
		if (currency != 0) {
			ClientCurrency clientCurrency = getCompany().getCurrency(currency);
			currencyWidget.setSelectedCurrencyFactorInWidget(clientCurrency,
					transactionDateItem.getValue().getDate());
		} else {
			ClientCurrency clientCurrency = getCompany().getPrimaryCurrency();
			if (clientCurrency != null) {
				currencyWidget.setSelectedCurrency(clientCurrency);
			}
		}
		if (isMultiCurrencyEnabled()) {
			super.setCurrency(getCompany().getCurrency(vendor.getCurrency()));
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}

		if (vendor.getPhoneNo() != null)
			phoneSelect.setValue(vendor.getPhoneNo());
		else
			phoneSelect.setValue("");
		super.vendorSelected(vendor);

	}

	@Override
	public void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientVendorCreditMemo());
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
			super.vendorSelected(getCompany()
					.getVendor(transaction.getVendor()));
			contactSelected(transaction.getContact());
			phoneSelect.setValue(transaction.getPhone());
			transactionNumber.setValue(transaction.getNumber());
			if (getPreferences().isTrackPaidTax()) {
				if (getPreferences().isTaxPerDetailLine()) {
					netAmount.setAmount(transaction.getNetAmount());
					vatTotalNonEditableText.setTransaction(transaction);
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

			transactionTotalNonEditableText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			foreignCurrencyamountLabel.setAmount(transaction.getTotal());
			initAccounterClass();
		}

		if (transaction.getTransactionItems() != null) {
			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					this.discountField.setAmount(getdiscount(transaction
							.getTransactionItems()));
				}
			}
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		initMemoAndReference();
		super.initTransactionViewData();
		accountsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ACCOUNT, true));
		itemsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ITEM, false));
		updateAmountsFromGUI();
	}

	public void resetElements() {
		this.setVendor(null);
		this.contact = null;
		this.phoneNo = null;
		setMemoTextAreaItem("");
		// setRefText("");

	}

	@Override
	public void createControls() {

		Label lab1 = new Label(messages.payeeCredit(Global.get().Vendor())
				+ "(" + getTransactionStatus() + ")");

		lab1.setStyleName("label-title");
		if (transaction == null
				|| transaction.getStatus() == ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
			lab1 = new Label(messages.payeeCredit(Global.get().Vendor()));

		else
			lab1 = new Label(messages.payeeCredit(Global.get().Vendor()) + "("
					+ getTransactionStatus() + ")");

		lab1.setStyleName("label-title");
		// lab1.setHeight("50px");
		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(messages.creditNoteNo());

		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		if (isTemplate) {
			dateNoForm.setFields(transactionNumber);
		} else {
			dateNoForm.setFields(transactionDateItem, transactionNumber);
		}

		VerticalPanel datepanel = new VerticalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		vendorCombo = createVendorComboItem(messages.payeeName(Global.get()
				.Vendor()));

		contactCombo = createContactComboItem();
		// if (this.isEdit)
		// FIXME--need to disable the form
		// vendorForm.setDisabled(true);

		phoneSelect = new TextItem(messages.phone());
		phoneSelect.setToolTip(messages.phoneNumberOf(this.getAction()
				.getCatagory()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setDisabled(isInViewMode());
		phoneSelect.setWidth(100);

		if (this.isInViewMode()) {
			// FiXME--The form need to be disabled
			// phoneForm.setDisabled(true);
		}

		netAmount = new AmountLabel(messages.netAmount());
		netAmount.setDefaultValue("£0.00");
		netAmount.setDisabled(true);

		transactionTotalNonEditableText = createTransactionTotalNonEditableItem(getCompany()
				.getPrimaryCurrency());

		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPrimaryCurrency());

		vatTotalNonEditableText = new TaxItemsForm();// createVATTotalNonEditableItem();

		// Label lab2 = new Label(messages.itemsAndExpenses());
		// menuButton = createAddNewButton();
		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackTax() && isTrackPaidTax(), isTaxPerDetailLine(),
				isTrackDiscounts(), isDiscountPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				VendorCreditMemoView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return VendorCreditMemoView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return VendorCreditMemoView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getAmount() != 0) {
					row.setDiscount(discountField.getAmount());
				}
				VendorCreditMemoView.this.updateNonEditableItems();
			}
		};

		vendorAccountTransactionTable.setDisabled(isInViewMode());
		vendorAccountTransactionTable.getElement().getStyle()
				.setMarginTop(10, Unit.PX);

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
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				VendorCreditMemoView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return VendorCreditMemoView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return VendorCreditMemoView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getAmount() != 0) {
					row.setDiscount(discountField.getAmount());
				}
				VendorCreditMemoView.this.updateNonEditableItems();
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
		currencyWidget = createCurrencyFactorWidget();
		FlowPanel itemsFlowPanel = new FlowPanel();
		itemsDisclosurePanel = new DisclosurePanel("Itemize by Product/Service");
		itemsFlowPanel.add(vendorItemTransactionTable);
		itemsFlowPanel.add(itemTableButton);
		itemsDisclosurePanel.setContent(itemsFlowPanel);
		itemsDisclosurePanel.setWidth("100%");

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");

		vendorForm = UIUtils.form(Global.get().vendor());
		// vendorForm.setWidth("50%");
		vendorForm.setFields(vendorCombo, contactCombo, phoneSelect);

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			vendorForm.setFields(classListCombo);
		}

		// vendorForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "190px");

		leftVLay.add(vendorForm);

		VerticalPanel rightVLay = new VerticalPanel();
		DynamicForm locationForm = new DynamicForm();
		if (locationTrackingEnabled) {
			locationForm.setFields(locationCombo);
			locationForm.getElement().getStyle().setFloat(Float.RIGHT);
			rightVLay.add(locationForm);
			rightVLay.setWidth("100%");
		}
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			rightVLay.setCellHorizontalAlignment(currencyWidget, ALIGN_RIGHT);
			currencyWidget.setDisabled(isInViewMode());
		}

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);
		vatinclusiveCheck = getVATInclusiveCheckBox();
		DynamicForm memoForm = new DynamicForm();
		// memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);
		VerticalPanel totalForm = new VerticalPanel();
		totalForm.setWidth("100%");
		totalForm.setStyleName("boldtext");
		// netAmount.setWidth((netAmount.getMainWidget().getOffsetWidth() + 100)
		// + "px");

		taxCodeSelect = createTaxCodeSelectItem();

		HorizontalPanel bottomLayout = new HorizontalPanel();
		bottomLayout.setWidth("100%");
		leftVLay.add(vendorForm);
		VerticalPanel rightVLay1 = new VerticalPanel();
		rightVLay1.setHorizontalAlignment(ALIGN_RIGHT);
		rightVLay1.setWidth("100%");
		HorizontalPanel topHLay1 = new HorizontalPanel();
		topHLay1.addStyleName("fields-panel");
		topHLay1.setWidth("100%");
		topHLay1.add(leftVLay);
		topHLay1.add(rightVLay);
		topHLay1.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

		HorizontalPanel bottomLayout1 = new HorizontalPanel();
		bottomLayout1.setWidth("100%");

		VerticalPanel bottomPanel = new VerticalPanel();
		bottomPanel.setWidth("100%");

		DynamicForm transactionTotalForm = new DynamicForm();
		transactionTotalForm.setNumCols(2);
		discountField = getDiscountField();

		DynamicForm taxForm = new DynamicForm();
		if (isTrackTax() && isTrackPaidTax()) {
			DynamicForm netAmountForm = new DynamicForm();
			netAmountForm.setNumCols(2);
			netAmountForm.setFields(netAmount);

			totalForm.add(netAmountForm);
			totalForm.add(vatTotalNonEditableText);
			totalForm.setCellHorizontalAlignment(netAmountForm, ALIGN_RIGHT);

			if (isMultiCurrencyEnabled()) {
				transactionTotalForm.setFields(transactionTotalNonEditableText,
						foreignCurrencyamountLabel);
			} else {
				transactionTotalForm.setFields(transactionTotalNonEditableText);
			}

			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setWidth("100%");
			vPanel.setHorizontalAlignment(ALIGN_RIGHT);
			vPanel.add(totalForm);

			bottomLayout1.add(memoForm);
			if (!isTaxPerDetailLine()) {
				taxForm.setItems(taxCodeSelect, vatinclusiveCheck);
				bottomLayout1.add(taxForm);
			}
			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					taxForm.setFields(discountField);
					bottomLayout1.add(taxForm);
				}
			}
			bottomLayout1.add(totalForm);
			bottomLayout1.setCellWidth(totalForm, "30%");

			bottomPanel.add(vPanel);
			bottomPanel.add(bottomLayout1);
		} else {
			memoForm.setStyleName("align-form");
			bottomLayout1.add(memoForm);
			if (isMultiCurrencyEnabled()) {
				transactionTotalForm.setFields(transactionTotalNonEditableText,
						foreignCurrencyamountLabel);
			} else {
				transactionTotalForm.setFields(transactionTotalNonEditableText);
			}
			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					taxForm.setFields(discountField);
					bottomLayout1.add(taxForm);
				}
			}

			bottomLayout1.add(totalForm);

			bottomPanel.add(bottomLayout1);

		}

		totalForm.add(transactionTotalForm);

		totalForm.setCellHorizontalAlignment(vatTotalNonEditableText,
				ALIGN_RIGHT);
		totalForm.setCellHorizontalAlignment(transactionTotalForm, ALIGN_RIGHT);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(voidedPanel);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay1);

		mainVLay.add(accountsDisclosurePanel);
		mainVLay.add(itemsDisclosurePanel);
		mainVLay.add(bottomPanel);

		// if (UIUtils.isMSIEBrowser())
		// resetFormView();

		this.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);

		listforms.add(memoForm);

		listforms.add(vatCheckform);
		listforms.add(transactionTotalForm);
		if (isMultiCurrencyEnabled()) {
			foreignCurrencyamountLabel.hide();
		}

		settabIndexes();
	}

	@Override
	public void initMemoAndReference() {

		if (this.isInViewMode()) {

			ClientVendorCreditMemo vendorCreditMemo = transaction;

			if (vendorCreditMemo != null) {
				memoTextAreaItem.setDisabled(true);
				setMemoTextAreaItem(vendorCreditMemo.getMemo());
				// setRefText(vendorCreditMemo.getReference());

			}
		}
	}

	@Override
	public ClientVendorCreditMemo saveView() {
		ClientVendorCreditMemo saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();

		super.saveAndUpdateView();

		saveOrUpdate(transaction);

	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Vendor
		if (vendor != null)
			transaction.setVendor(getVendor().getID());

		// Setting Contact
		if (contact != null)
			transaction.setContact(contact);

		// Setting Phone
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());

		// Setting Total
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal());

		// Setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// transaction.setReference(getRefText());

		transaction.setNetAmount(netAmount.getAmount());
		// itemReceipt.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());

		if (vatinclusiveCheck != null)
			transaction.setAmountsIncludeVAT(vatinclusiveCheck.getValue());
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());

		if (discountField.getAmount() != 0.0 && transactionItems != null) {
			for (ClientTransactionItem item : transactionItems) {
				item.setDiscount(discountField.getAmount());
			}

		}
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// 1. is Valid transaction date?
		// 2. is in prevent posting before date?
		// 3. vendorForm valid?
		// 4. isBlank transaction?
		// 5. is vendor transaction grid valid?
		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDate,
		// messages.invalidateTransactionDate());
		// }

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, messages.invalidateDate());
		}
		result.add(vendorForm.validate());
		if (getAllTransactionItems().isEmpty()) {
			result.addError(vendorAccountTransactionTable,
					messages.blankTransaction());
		} else {
			result.add(vendorAccountTransactionTable.validateGrid());
			result.add(vendorItemTransactionTable.validateGrid());
		}
		return result;
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

		transactionTotalNonEditableText
				.setAmount(getAmountInBaseCurrency(grandTotal));
		foreignCurrencyamountLabel.setAmount(grandTotal);

		netAmount.setAmount(lineTotal);
		if (getPreferences().isTrackPaidTax()) {
			if ((transaction.getTransactionItems() != null && transaction
					.getTransactionItems().isEmpty()) || !isInViewMode()) {
				transaction.setTransactionItems(vendorAccountTransactionTable
						.getAllRows());
				transaction.getTransactionItems().addAll(
						vendorItemTransactionTable.getAllRows());
			}
			vatTotalNonEditableText.setTransaction(transaction);
		}
	}

	// @Override
	// public void setViewConfiguration(ViewConfiguration viewConfiguration)
	// throws Exception {
	//
	// super.setViewConfiguration(viewConfiguration);
	//
	// if (isEdit && (!transactionObject.isVendorCreditMemo()))
	// throw new Exception("Unable to load the Required CashPurchase....");
	//
	// if (viewConfiguration.isInitWithPayee()) {
	// ClientPayee payee = viewConfiguration.getPayeeObject();
	//
	// if (payee == null || (!payee.isVendor()))
	// throw new Exception("Required Vendor Could Not be Loaded...");
	// }
	// }

	public static VendorCreditMemoView getInstance() {

		return new VendorCreditMemoView();

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
		this.vendorCombo.setFocus();
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
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				Accounter.showError(caught.getMessage());
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
		vendorCombo.setDisabled(isInViewMode());
		transactionDateItem.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		vendorAccountTransactionTable.setDisabled(isInViewMode());
		vendorItemTransactionTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		discountField.setDisabled(isInViewMode());
		phoneSelect.setDisabled(isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
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

	@Override
	protected Double getTransactionTotal() {
		return this.transactionTotalNonEditableText.getAmount();
	}

	private void resetFormView() {
		// vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		// vendorForm.setWidth("40%");
	}

	@Override
	protected String getViewTitle() {
		return messages.payeeCredit(Global.get().Vendor());
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {
			taxCodeSelect.setComboItem(taxCode);
			vendorAccountTransactionTable.setTaxCode(taxCode.getID(), true);
			vendorItemTransactionTable.setTaxCode(taxCode.getID(), true);
		} else
			taxCodeSelect.setValue("");
	}

	@Override
	protected void addAllRecordToGrid(
			List<ClientTransactionItem> transactionItems) {
		vendorAccountTransactionTable
				.setRecords(getAccountTransactionItems(transactionItems));
		vendorItemTransactionTable
				.setRecords(getItemTransactionItems(transactionItems));
	}

	@Override
	protected void removeAllRecordsFromGrid() {
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		vendorAccountTransactionTable.add(transactionItem);
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(vendorAccountTransactionTable.getRecords());
		list.addAll(vendorItemTransactionTable.getRecords());
		return list;
	}

	@Override
	protected void refreshTransactionGrid() {

		vendorAccountTransactionTable.updateTotals();
		vendorItemTransactionTable.updateTotals();
	}

	private void settabIndexes() {
		vendorCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		phoneSelect.setTabIndex(3);
		transactionDateItem.setTabIndex(4);
		transactionNumber.setTabIndex(5);
		memoTextAreaItem.setTabIndex(6);
		// menuButton.setTabIndex(7);
		saveAndCloseButton.setTabIndex(8);
		saveAndNewButton.setTabIndex(9);
		cancelButton.setTabIndex(10);

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
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		vendorAccountTransactionTable.updateAmountsFromGUI();
		vendorItemTransactionTable.updateAmountsFromGUI();
	}

	public void modifyForeignCurrencyTotalWidget() {
		if (currencyWidget.isShowFactorField()) {
			foreignCurrencyamountLabel.hide();
		} else {
			foreignCurrencyamountLabel.show();
			foreignCurrencyamountLabel.setTitle(messages
					.currencyTotal(currencyWidget.getSelectedCurrency()
							.getFormalName()));
		}
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected void updateDiscountValues() {

		if (discountField.getAmount() != null) {
			vendorItemTransactionTable.setDiscount(discountField.getAmount());
			vendorAccountTransactionTable
					.setDiscount(discountField.getAmount());
		} else {
			discountField.setAmount(0d);
		}
	}
}
