package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.TaxItemCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.edittable.tables.TransactionPayBillTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyComboWidget;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyFactorWidget;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class PayBillView extends AbstractTransactionBaseView<ClientPayBill> {

	AmountField endBalText;
	DateField date;
	DateField dueDate;
	AccounterConstants accounterConstants = Accounter.constants();
	protected String vendorPaymentMethod;
	private TransactionPayBillTable grid;
	protected AmountField cashDiscountTextItem;
	protected AmountField creditTextItem;
	public AmountLabel unUsedCreditsText, amountLabel;

	TaxItemCombo tdsCombo;
	protected SelectCombo vendorPaymentMethodCombo;
	protected List<PayBillTransactionList> paybillTransactionList;
	protected List<PayBillTransactionList> filterList;
	protected List<PayBillTransactionList> tempList;
	private ClientFinanceDate dueDateOnOrBefore;
	private DynamicForm payForm, filterForm;
	private int size;
	// public double transactionTotal = 0.0d;
	public double totalAmountDue = 0.0d;
	public double totalCashDiscount = 0.0d;
	public double totalOriginalAmount = 0.0d;
	public double totalOrginalAmt = 0.0D, totalDueAmt = 0.0D,
			totalPayment = 0.0D, cashDiscount = 0.0d;
	private ArrayList<DynamicForm> listforms;
	private VerticalPanel gridLayout;

	// Upate
	protected PayFromAccountsCombo payFromCombo;
	protected ClientAccount payFromAccount;
	protected ClientVendor vendor;
	protected List<ClientVendor> vendors;
	protected VendorCombo vendorCombo;
	private boolean locationTrackingEnabled;
	private AmountLabel tdsPayableAmount;
	private AmountLabel amountToVendor;
	private double toBeSetEndingBalance;

	private CurrencyFactorWidget currencyWidget;
	private ClientCurrency vendorCurrency;

	public PayBillView() {
		super(ClientTransaction.TYPE_PAY_BILL);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
	}

	/*
	 * This method invoked when all records in grid are de-selected.It resets
	 * the non-editable fields
	 */
	public void resetTotlas() {
		amountLabel.setAmount(getAmountInTransactionCurrency(0.0));
		toBeSetEndingBalance = getAmountInTransactionCurrency(payFromCombo
				.getSelectedValue() != null ? payFromCombo.getSelectedValue()
				.getTotalBalance() : 0.0);
		endBalText.setAmount(toBeSetEndingBalance);
	}

	/*
	 * This method invoked eachtime when there is a change in records and it
	 * updates the noneditable amount fields
	 */
	public void adjustAmountAndEndingBalance() {
		List<ClientTransactionPayBill> selectedRecords = grid
				.getSelectedRecords();
		double toBeSetAmount = 0.0;
		for (ClientTransactionPayBill rec : selectedRecords) {
			toBeSetAmount += rec.getPayment();
		}
		if (this.transaction != null) {
			Double totalPayment = getAmountInTransactionCurrency(toBeSetAmount);
			Double tdsTotal = getAmountInTransactionCurrency(grid.getTDSTotal());
			amountToVendor.setAmount(totalPayment);
			tdsPayableAmount.setAmount(tdsTotal);
			amountLabel.setAmount(totalPayment + tdsTotal);

			if (payFromAccount != null) {
				if (isInViewMode()) {
					endBalText
							.setAmount(getAmountInTransactionCurrency(payFromAccount
									.getTotalBalance()));
				} else {
					if (payFromAccount.isIncrease()) {
						toBeSetEndingBalance = payFromAccount.getTotalBalance()
								+ amountToVendor.getAmount();
					} else {
						toBeSetEndingBalance = payFromAccount.getTotalBalance()
								- amountToVendor.getAmount();
					}
				}
			}
		}
	}

	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type of Enter Bill
		transaction.setType(ClientTransaction.TYPE_PAY_BILL);
		transaction.setPayBillType(ClientPayBill.TYPE_PAYBILL);

		// Setting Accounts Payable
		transaction.setAccountsPayable(getCompany()

		// transaction.setTaxItem(taxItem)
				.getAccountsPayableAccount());

		// Setting Date
		transaction.setDate(date.getEnteredDate().getDate());

		// Setting Pay From
		transaction.setPayFrom(payFromAccount);

		// Setting payment method
		transaction.setPaymentMethod(paymentMethodCombo.getSelectedValue());

		// Setting Bill due or before
		if (dueDate.getEnteredDate() != null)
			transaction.setBillDueOnOrBefore(dueDate.getEnteredDate());
		// Setting Vendor
		if (getVendor() != null)
			transaction.setVendor(getVendor());
		// Setting Amount

		transaction.setTotal(getAmountInBaseCurrency(amountLabel.getAmount()));

		if (getPreferences().isTDSEnabled() && vendor.isTdsApplicable()) {
			ClientTAXItem taxItem = tdsCombo.getSelectedValue();
			if (taxItem != null) {
				transaction.setTdsTaxItem(taxItem);
			}

		}

		// Setting ending Balance
		transaction
				.setEndingBalance(getAmountInBaseCurrency(toBeSetEndingBalance));

		transaction.setMemo(memoTextAreaItem.getValue().toString());
		if (getPreferences().isTDSEnabled() && getVendor().isTdsApplicable()) {
			transaction.setTdsTotal(grid.getTDSTotal());
		}
		// Setting Transactions
		List<ClientTransactionPayBill> selectedRecords = grid
				.getSelectedRecords();
		List<ClientTransactionPayBill> transactionPayBill = new ArrayList<ClientTransactionPayBill>();
		for (ClientTransactionPayBill tpbRecord : selectedRecords) {
			PayBillTransactionList payBillTX = paybillTransactionList.get(grid
					.indexOf(tpbRecord));

			if (payBillTX.getType() == ClientTransaction.TYPE_ENTER_BILL) {
				tpbRecord.setEnterBill(payBillTX.getTransactionId());
			} else if (payBillTX.getType() == ClientTransaction.TYPE_MAKE_DEPOSIT) {
				tpbRecord.setTransactionMakeDeposit(payBillTX
						.getTransactionId());
			} else if (payBillTX.getType() == ClientTransaction.TYPE_JOURNAL_ENTRY) {
				tpbRecord.setJournalEntry(payBillTX.getTransactionId());
			}
			tpbRecord.setAccountsPayable(getCompany()
					.getAccountsPayableAccount());
			tpbRecord.setPayBill(transaction);

			// ClientAccount cashAcc = getCompany().getAccountByName(
			// gridView.getAttribute(Accounter.constants().cashAccount(),
			// gridView.indexOf(tpbRecord)));
			// if (cashAcc != null)
			// tpbRecord.setDiscountAccount(cashAcc.getID());

			List<ClientTransactionCreditsAndPayments> trpList = grid
					.getCreditsAndPaymentsDialiog() != null ? grid
					.getCreditsAndPaymentsDialiog().getTransactionCredits(
							tpbRecord)
					: new ArrayList<ClientTransactionCreditsAndPayments>();
			if (trpList != null)
				for (ClientTransactionCreditsAndPayments temp : trpList) {
					temp.setTransactionPayBill(tpbRecord);
				}
			tpbRecord.setTransactionCreditsAndPayments(trpList);
			if (tpbRecord.getTempCredits() != null) {
				tpbRecord.getTempCredits().clear();
			}

			transactionPayBill.add(tpbRecord);
		}
		transaction.setTransactionPayBill(transactionPayBill);

		transaction
				.setUnUsedCredits(getAmountInBaseCurrency(this.unUsedCreditsText
						.getAmount()));

		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	private void initListGrid() {
		grid = new TransactionPayBillTable(!isInViewMode(), this) {

			@Override
			protected void updateFootervalues(ClientTransactionPayBill row,
					boolean canEdit) {
				PayBillView.this.updateFootervalues(row, canEdit);
			}

			@Override
			protected void setUnUsedCreditsTextAmount(Double totalBalances) {
				PayBillView.this.setUnUsedCredits(totalBalances);
			}

			@Override
			protected void resetTotlas() {
				PayBillView.this.resetTotlas();
			}

			@Override
			protected void deleteTotalPayment(ClientTransactionPayBill obj) {
				PayBillView.this.deleteTotalPayment(obj);
			}

			@Override
			protected void updateTotalPayment(ClientTransactionPayBill obj) {
				PayBillView.this.updateTotalPayment(obj);
			}

			@Override
			protected void calculateUnusedCredits() {
				PayBillView.this.calculateUnusedCredits();
			}

			@Override
			protected void adjustPaymentValue(
					ClientTransactionPayBill selectedObject) {
				PayBillView.this.adjustPaymentValue(selectedObject);
			}

			@Override
			protected void adjustAmountAndEndingBalance() {
				PayBillView.this.adjustAmountAndEndingBalance();
			}

			@Override
			protected boolean isInViewMode() {
				return PayBillView.this.isInViewMode();
			}
		};
		grid.setDisabled(isInViewMode());
	}

	/**
	 * @param obj
	 */
	protected void updateTotalPayment(ClientTransactionPayBill obj) {
		ClientTransaction transactionObject = getTransactionObject();
		// paybillView.gettrantransactionTotal = 0.0;
		transactionObject.setTotal(0.0);
		for (ClientTransactionPayBill rec : grid.getSelectedRecords()) {
			transactionObject.setTotal(transactionObject.getTotal()
					+ rec.getPayment());
			totalCashDiscount += rec.getCashDiscount();
		}
		adjustPaymentValue(obj);
	}

	/*
	 * This method invoked each time when there is a change in cashdicount or
	 * credits or on record's paymentvalue change
	 */
	public void adjustPaymentValue(ClientTransactionPayBill rec) {
		Double amountDue = rec.getAmountDue();
		Double cashDiscount = rec.getCashDiscount();
		Double credit = rec.getAppliedCredits();
		Double payments = amountDue - (cashDiscount + credit);

		// (rec).setPayment(payments);
		(rec).setCashDiscount(cashDiscount);
		(rec).setAppliedCredits(credit);

		grid.update(rec);
		adjustAmountAndEndingBalance();
	}

	/*
	 * This method adds the records(bills & makedeposits(newlycreated rec's)) to
	 * the grid when vendor changed
	 */
	private void addGridRecords(List<PayBillTransactionList> billsList) {
		clearGrid();

		if (billsList != null) {
			totalOrginalAmt = 0.0d;
			totalDueAmt = 0.0d;
			totalPayment = 0.0d;
			cashDiscount = 0.0d;
			List<ClientTransactionPayBill> records = new ArrayList<ClientTransactionPayBill>();
			for (PayBillTransactionList curntRec : billsList) {
				ClientTransactionPayBill record = new ClientTransactionPayBill();

				record.setAmountDue(curntRec.getAmountDue());
				record.setDummyDue(curntRec.getAmountDue());

				record.setBillNumber(curntRec.getBillNumber());

				record.setCashDiscount(curntRec.getCashDiscount());

				record.setAppliedCredits(curntRec.getCredits());

				record.setDiscountDate(curntRec.getDiscountDate() != null ? curntRec
						.getDiscountDate().getDate() : 0);

				record.setDueDate(curntRec.getDueDate() != null ? curntRec
						.getDueDate().getDate() : 0);

				record.setOriginalAmount(curntRec.getOriginalAmount());

				// record.setPayment(curntRec.getPayment());
				ClientVendor vendor = getCompany().getVendorByName(
						curntRec.getVendorName());
				if (vendor != null)
					record.setVendor(vendor.getID());

				totalOrginalAmt += record.getOriginalAmount();
				totalDueAmt += record.getAmountDue();
				totalPayment += record.getPayment();
				cashDiscount += record.getCashDiscount();

				records.add(record);
			}
			// gridView.updateFooterValues(FinanceApplication.constants()
			// .subTotal(), 1);
			// if (!isEdit)
			// gridView.updateFooterValues(DataUtils
			// .getAmountAsString(totalOrginalAmt), 2);
			// if (!isEdit)
			// gridView.updateFooterValues(DataUtils
			// .getAmountAsString(totalDueAmt), 3);
			//
			// gridView.updateFooterValues(DataUtils
			// .getAmountAsString(totalPayment), isEdit ? 6 : 7);
			// if (!isEdit)
			// gridView.updateFooterValues(DataUtils
			// .getAmountAsString(cashDiscount), 5);
			grid.setRecords(records);
			size = records.size();
		}
	}

	public void updateFooterValues() {
		/* if no records are there */
		totalOrginalAmt = 0.0D;
		totalDueAmt = 0.0D;
		cashDiscount = 0.0D;
		// if (!isEdit)
		// gridView.updateFooterValues(DataUtils
		// .getAmountAsString(totalOrginalAmt), 2);
		// if (!isEdit)
		// gridView.updateFooterValues(DataUtils
		// .getAmountAsString(totalDueAmt), 3);
		// gridView.updateFooterValues(DataUtils.getAmountAsString(totalPayment),
		// isEdit ? 6 : 7);
		// if (!isEdit)
		// gridView.updateFooterValues(DataUtils
		// .getAmountAsString(cashDiscount), 5);
	}

	private ClientVendor getVendorById(long id) {
		for (ClientVendor temp : vendors) {
			if (temp.getID() == id)
				return temp;
		}
		return null;
	}

	@Override
	protected void createControls() {

		listforms = new ArrayList<DynamicForm>();

		Label lab = new Label(Accounter.constants().payBill());
		lab.removeStyleName("gwt-Label");
		lab.addStyleName(Accounter.constants().labelTitle());

		locationCombo = createLocationCombo();
		locationCombo.setHelpInformation(true);

		// lab.setHeight("50px");
		date = new DateField(Accounter.constants().date());
		date.setToolTip(Accounter.messages().selectDateWhenTransactioCreated(
				this.getAction().getViewName()));
		date.setHelpInformation(true);
		// date.setUseTextField(true);
		date.setEnteredDate(new ClientFinanceDate());
		setTransactionDate(new ClientFinanceDate());
		date.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate changedDate) {
				if (changedDate != null) {
					try {
						ClientFinanceDate newDate = date.getValue();
						if (newDate != null)
							setTransactionDate(newDate);
					} catch (Exception e) {
						Accounter.showError(Accounter.constants()
								.invalidTransactionDate());
						setTransactionDate(new ClientFinanceDate());
						date.setEnteredDate(getTransactionDate());
					}

				}

			}

		});
		transactionNumber = createTransactionNumberItem();

		vendorCombo = createVendorComboItem(messages.payeeName(Global.get()
				.Vendor()));
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						grid.isAlreadyOpened = false;

						vendorSelected(selectItem);
					}
				});

		payFromCombo = createPayFromCombo(Accounter.constants().payFrom());
		payFromCombo.setPopupWidth("500px");
		paymentMethodCombo = createPaymentMethodSelectItem();
		// paymentMethodCombo.setWidth(100);
		paymentMethodCombo.setDefaultValue(Accounter.constants()
				.onlineBanking());

		dueDate = new DateField(Accounter.constants()
				.filterByBilldueonorbefore());
		dueDate.setHelpInformation(true);
		dueDate.setValue(new ClientFinanceDate());
		// dueDate.setUseTextField(true);
		// dueDate.setWidth(100);

		dueDate.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (paybillTransactionList != null) {
					// if (event.getSource() != null) {
					// dueDateOnOrBefore = ((DateField) event.getSource())
					// .getValue();
					dueDateOnOrBefore = dueDate.getValue();
					clearGrid();
					filterGrid();
					// }
				}

			}
		});

		vendorPaymentMethodCombo = createPaymentMethodSelectItem();
		vendorPaymentMethodCombo.setRequired(false);
		vendorPaymentMethodCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (vendorPaymentMethodCombo.getSelectedValue() != null)
					vendorPaymentMethod = ((SelectItem) event.getSource())
							.getValue().toString();
				if (paybillTransactionList != null) {
					clearGrid();
					filterGrid();
				}

			}
		});

		// filterForm = new DynamicForm();
		// filterForm.setIsGroup(true);
		// filterForm.setGroupTitle(Accounter.constants().Filter());
		// filterForm.setFields(dueDate);

		DynamicForm dateForm = new DynamicForm();
		dateForm.setNumCols(6);
		dateForm.setStyleName("datenumber-panel");
		dateForm.setFields(date, transactionNumber);

		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateForm);
		datepanel.setCellHorizontalAlignment(dateForm, ALIGN_RIGHT);

		payForm = new DynamicForm();
		// payForm.setWidth("80%");
		payForm.setIsGroup(true);
		payForm.setGroupTitle(Accounter.constants().payment());
		payForm.setFields(vendorCombo, payFromCombo, paymentMethodCombo,
				dueDate);

		endBalText = new AmountField(Accounter.constants().endingBalance(),
				this, getBaseCurrency());
		endBalText.setHelpInformation(true);
		endBalText.setWidth(100);
		endBalText.setValue("" + UIUtils.getCurrencySymbol() + "0.00");
		endBalText.setDisabled(true);
		tdsCombo = new TaxItemCombo(Accounter.constants().tds(),
				ClientTAXItem.TAX_TYPE_TDS);
		tdsCombo.setDisabled(isInViewMode());
		tdsCombo.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {

			@Override
			public void selectedComboBoxItem(ClientTAXItem selectItem) {
				grid.setTds(selectItem);
			}
		});

		DynamicForm balForm = new DynamicForm();
		// balForm.setWidth("100%");
		balForm.setIsGroup(true);
		if (locationTrackingEnabled)
			balForm.setFields(locationCombo);
		balForm.setGroupTitle(Accounter.constants().balances());
		balForm.setFields(endBalText, tdsCombo);
		if (!getCompany().getPreferences().isTDSEnabled()) {
			tdsCombo.setVisible(false);
		}
		// if (getPreferences().isClassTrackingEnabled()
		// && getPreferences().isClassOnePerTransaction()) {
		// classListCombo = createAccounterClassListCombo();
		// balForm.setFields(classListCombo);
		// }

		Label lab1 = new Label(Accounter.constants().billsDue());

		initListGrid();

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth("100%");

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		DynamicForm totalForm = new DynamicForm();
		totalForm.setWidth("100%");
		totalForm.setStyleName("boldtext");
		unUsedCreditsText = new AmountLabel(Accounter.constants()
				.unusedCredits());
		unUsedCreditsText.setDisabled(true);
		amountLabel = new AmountLabel(Accounter.messages().currencyTotal(
				getBaseCurrency().getFormalName()));
		amountLabel.setDisabled(true);
		currencyWidget = createCurrencyFactorWidget();
		this.tdsPayableAmount = new AmountLabel(constants.tdsAmount());
		tdsPayableAmount.setDisabled(true);

		this.amountToVendor = new AmountLabel(messages.payeePayment(Global
				.get().Vendor()));
		amountToVendor.setDisabled(true);

		DynamicForm textForm = new DynamicForm();
		textForm.setNumCols(2);
		textForm.setStyleName("unused-payments");
		textForm.setFields(unUsedCreditsText, amountToVendor, tdsPayableAmount,
				amountLabel);
		if (!getPreferences().isTDSEnabled()) {
			amountToVendor.setVisible(false);
			tdsPayableAmount.setVisible(false);
		}

		HorizontalPanel bottompanel = new HorizontalPanel();
		bottompanel.setWidth("100%");
		bottompanel.add(memoForm);
		bottompanel.setCellHorizontalAlignment(memoForm,
				HasHorizontalAlignment.ALIGN_LEFT);
		bottompanel.add(textForm);
		bottompanel.setCellHorizontalAlignment(textForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(payForm);
		VerticalPanel vpPanel = new VerticalPanel();
		vpPanel.setCellHorizontalAlignment(balForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		vpPanel.add(balForm);
		if (isMultiCurrencyEnabled()) {
			vpPanel.add(currencyWidget);
			vpPanel.setCellHorizontalAlignment(currencyWidget,
					HasHorizontalAlignment.ALIGN_RIGHT);
			currencyWidget.setDisabled(isInViewMode());
		}

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(vpPanel);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(vpPanel, "50%");
		topHLay.setCellHorizontalAlignment(vpPanel, ALIGN_RIGHT);

		HorizontalPanel bottomAmtsLayout = new HorizontalPanel();

		bottomAmtsLayout.setWidth("100%");
		gridLayout = new VerticalPanel();
		gridLayout.add(lab1);
		gridLayout.add(grid);
		gridLayout.add(bottomAmtsLayout);
		gridLayout.add(bottompanel);
		gridLayout.setWidth("100%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab);
		mainVLay.add(datepanel);
		mainVLay.add(topHLay);
		mainVLay.add(gridLayout);

		this.add(mainVLay);
		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(payForm);
		listforms.add(balForm);
		listforms.add(textForm);
		setTabindexes();

	}

	private void setUnUsedCredits(Double unusedCredits) {
		unUsedCreditsText
				.setAmount(getAmountInTransactionCurrency(unusedCredits));
	}

	public void calculateUnusedCredits() {

		Double totalCredits = 0D;
		List<ClientCreditsAndPayments> updatedCustomerCreditsAndPayments = grid
				.getUpdatedCustomerCreditsAndPayments();
		if (updatedCustomerCreditsAndPayments != null) {
			for (ClientCreditsAndPayments credit : updatedCustomerCreditsAndPayments) {
				totalCredits += credit.getBalance();
			}
		}

		this.unUsedCreditsText
				.setAmount(getAmountInTransactionCurrency(totalCredits));

	}

	protected void vendorSelected(final ClientVendor vendor) {
		
		 vendorCurrency = getCompany().getCurrency(vendor.getCurrency());
		
		endBalText.setCurrency(vendorCurrency);
		amountLabel
				.setTitle(Accounter.messages().currencyTotal(vendorCurrency
								.getFormalName()));

		if (vendor == null) {
			paybillTransactionList = null;
			return;
		}

		this.setVendor(vendor);

		if (!isInViewMode()) {
			if (isTDSEnable()) {
				tdsCombo.setVisible(true);
				amountToVendor.setVisible(true);
				tdsPayableAmount.setVisible(true);
				ClientTAXItem taxItem = getCompany().getTAXItem(
						vendor.getTaxItemCode());
				if (taxItem != null) {
					tdsCombo.setComboItem(taxItem);
				}
			} else {
				tdsCombo.setVisible(false);
				amountToVendor.setVisible(false);
				tdsPayableAmount.setVisible(false);
			}
		}

		/*
		 * resetting the crdits dialog's refernce,so that a new object will
		 * created for opening credits dialog
		 */
		grid.setCreditsAndPaymentsDialiog(null);
		grid.setCreditsStack(null);
		grid.initCreditsAndPayments(vendor);
		grid.reDraw();

		if (transaction.id == 0) {
			grid.addLoadingImagePanel();
			getTransactionPayBills(vendor);
		}
		initTransactionTotalNonEditableItem();
		long currency = vendor.getCurrency();
		if (currency != 0) {
			ClientCurrency clientCurrency = getCompany().getCurrency(currency);
			if (clientCurrency != null) {
				currencyWidget.setSelectedCurrency(clientCurrency);
			}
		} else {
			ClientCurrency clientCurrency = getCompany().getPreferences()
					.getPrimaryCurrency();
			if (clientCurrency != null) {
				currencyWidget.setSelectedCurrency(clientCurrency);
			}
		}
		if (isMultiCurrencyEnabled()) {
			super.setCurrencycode(getCompany()
					.getCurrency(vendor.getCurrency()));
			setCurrencyFactor(1.0);
			updateAmountsFromGUI();
		}

	}

	private void getTransactionPayBills(ClientVendor vendor) {
		this.rpcUtilService
				.getTransactionPayBills(
						vendor.getID(),
						new AccounterAsyncCallback<ArrayList<PayBillTransactionList>>() {

							public void onException(AccounterException caught) {
								grid.addEmptyMessage(Accounter.constants()
										.noRecordsToShow());
							}

							public void onResultSuccess(
									ArrayList<PayBillTransactionList> result) {

								paybillTransactionList = result;
								if (result.size() > 0) {
									addGridRecords(result);
									dueDateOnOrBefore = dueDate.getValue();
									clearGrid();
									filterGrid();
								} else {
									grid.addEmptyMessage(Accounter.constants()
											.noRecordsToShow());
									updateFooterValues();
								}
							}

						});

	}

	protected void accountSelected(ClientAccount account) {

		if (account == null)
			return;
		this.payFromAccount = account;
		endBalText.setAmount(payFromAccount.getCurrentBalance());
		adjustAmountAndEndingBalance();
	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			ClientPayBill clientPayBill = new ClientPayBill();
			clientPayBill.setAmountIncludeTDS(true);
			setData(clientPayBill);
		} else {
			grid.showTDS(transaction.getTdsTaxItem() != null);
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
			paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
			paymentMethodCombo.setDisabled(isInViewMode());

			this.transactionItems = transaction.getTransactionItems();

			payFromCombo.setComboItem(getCompany().getAccount(
					transaction.getPayFrom()));

			date.setValue(transaction.getDate());
			date.setDisabled(true);
			accountSelected(getCompany().getAccount(transaction.getPayFrom()));

			dueDate.setValue(new ClientFinanceDate(transaction
					.getBillDueOnOrBefore()));
			dueDate.setDisabled(true);

			transactionNumber.setValue(transaction.getNumber());
			transactionNumber.setDisabled(isInViewMode());

			this.setVendor(getCompany().getVendor(transaction.getVendor()));
			vendorSelected(getCompany().getVendor(transaction.getVendor()));
			if (tdsCombo != null && transaction.getTdsTaxItem() != null) {
				tdsCombo.setComboItem(transaction.getTdsTaxItem());
			}

			if (isTDSEnable()) {
				tdsCombo.setVisible(true);
			}
			amountLabel.setAmount(getAmountInTransactionCurrency(transaction
					.getNetAmount()));
			endBalText.setAmount(getAmountInTransactionCurrency(transaction
					.getEndingBalance()));
			initListGridData(this.transaction.getTransactionPayBill());
			initTransactionTotalNonEditableItem();
			memoTextAreaItem.setDisabled(true);
			initAccounterClass();
		}
		// super.initTransactionViewData();
		initVendors();
		initTransactionTotalNonEditableItem();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		try {
			this.initMemoAndReference();
		} catch (Exception e) {
			System.err.println(e);
		}

		initPayFromAccounts();
		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
	}

	private void initListGridData(List<ClientTransactionPayBill> list) {
		int count = 0;
		double totalOrgAmt = 0.0;
		// double totalDueAmount = 0.0;
		// double cashDiscount = 0.0;
		// double credits = 0.0;
		// double payment = 0.0;
		// grid.setRecords(list);
		for (ClientTransactionPayBill receivePayment : list) {
			totalOrgAmt += receivePayment.getOriginalAmount();
			this.grid.add(receivePayment);
			// this.grid.selectRow(count);
			count++;
		}
	}

	protected void initTransactionTotalNonEditableItem() {
		if (transaction == null)
			return;
		ClientPayBill paybill = ((ClientPayBill) transaction);
		Double unusedCredits = paybill.getUnUsedCredits();
		setUnUsedCredits(unusedCredits);
	}

	@Override
	public void updateNonEditableItems() {

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		if (vendorCombo.getSelectedValue() == null) {
			vendorCombo.setValue("");
		}

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateDate());
		}
		ValidationResult payFormValidationResult = payForm.validate();
		if (payFormValidationResult.haveErrors()
				|| payFormValidationResult.haveWarnings()) {
			result.add(payFormValidationResult);
			return result;
		}

		if (filterForm != null) {
			result.add(filterForm.validate());
			return result;
		}
		if (!isInViewMode()) {
			if (grid.getAllRows().isEmpty()) {
				result.addError(
						grid,
						Accounter.messages().noBillsAreAvailableFirstAddABill(
								Global.get().Vendor()));
			}

			else {

				result.add(grid.validateGrid());
			}
		}

		if (isTDSEnable()) {
			ClientTAXItem selectedValue = tdsCombo.getSelectedValue();
			if (selectedValue == null) {
				result.addError(tdsCombo, constants.pleaseSelectTDS());
			}
		}
		ClientAccount bankAccount = payFromCombo.getSelectedValue();
		// check if the currency of accounts is valid or not
		if (bankAccount != null) {
			ClientCurrency bankCurrency = getCurrency(bankAccount.getCurrency());
			if (bankCurrency != getBaseCurrency() && bankCurrency != currency) {
				result.addError(payFromCombo,
						accounterConstants.selectProperBankAccount());
			}
		}
		return result;
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();
		saveOrUpdate(transaction);

	}

	public static PayBillView getInstance() {
		return new PayBillView();
	}

	protected void clearGrid() {
		grid.removeAllRecords();
	}

	protected void filterGrid() {
		filterList = new ArrayList<PayBillTransactionList>();
		tempList = new ArrayList<PayBillTransactionList>();

		filterList.addAll(paybillTransactionList);

		if (dueDateOnOrBefore != null) {
			for (PayBillTransactionList cont : filterList) {
				if (cont.getDueDate().before(dueDateOnOrBefore)
						|| cont.getDueDate().equals(
								UIUtils.stringToDate(UIUtils
										.dateToString(dueDateOnOrBefore))))
					tempList.add(cont);

			}
			filterList.clear();
			filterList.addAll(tempList);
			tempList.clear();
		}

		if (getVendor() != null) {

			for (PayBillTransactionList cont : filterList) {
				if (getVendor().getName().toString()
						.equalsIgnoreCase(cont.getVendorName().toString())) {

					tempList.add(cont);
				}
			}
			filterList.clear();
			filterList.addAll(tempList);
			tempList.clear();
		}

		List<ClientTransactionPayBill> records = new ArrayList<ClientTransactionPayBill>();
		for (PayBillTransactionList cont : filterList) {
			ClientTransactionPayBill record = new ClientTransactionPayBill();

			record.setAmountDue(cont.getAmountDue());
			record.setDummyDue(cont.getAmountDue());
			record.setBillNumber(cont.getBillNumber());
			record.setCashDiscount(cont.getCashDiscount());
			record.setAppliedCredits(cont.getCredits());
			if (cont.getDiscountDate() != null)
				record.setDiscountDate(cont.getDiscountDate().getDate());
			if (cont.getDueDate() != null)
				record.setDueDate(cont.getDueDate().getDate());

			record.setOriginalAmount(cont.getOriginalAmount());
			record.setPayment(cont.getPayment());
			records.add(record);
		}

		grid.setRecords(records);
		size = records.size();
		if (size == 0)
			grid.addEmptyMessage(Accounter.constants().noRecordsToShow());
	}

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
		// Nothing To Do

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// Nothing TO DO

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	public void onEdit() {
		if (!transaction.isVoid()) {
			Accounter.showWarning(AccounterWarningType.PAYBILL_EDITING,
					AccounterType.WARNING, new ErrorDialogHandler() {

						@Override
						public boolean onYesClick() {
							voidTransaction();
							return true;
						}

						private void voidTransaction() {
							AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

								@Override
								public void onException(
										AccounterException caught) {
									Accounter.showError(Accounter.constants()
											.failedtovoidPayBill());

								}

								@Override
								public void onResultSuccess(Boolean result) {
									if (result) {
										enableFormItems();
									} else

										onFailure(new Exception());
								}

							};
							if (isInViewMode()) {
								AccounterCoreType type = UIUtils
										.getAccounterCoreType(transaction
												.getType());
								rpcDoSerivce.voidTransaction(type,
										transaction.id, callback);
							}

						}

						@Override
						public boolean onNoClick() {

							return true;
						}

						@Override
						public boolean onCancelClick() {

							return true;
						}
					});
		} else if (transaction.isVoid() || transaction.isDeleted())
			Accounter
					.showError(Accounter.constants().failedtovoidTransaction());
	}

	public void updateFootervalues(ClientTransactionPayBill obj, boolean canEdit) {
		ClientTransaction transactionObject = this.getTransactionObject();
		if (canEdit) {
			transactionObject.setTotal(0.0);
			this.totalCashDiscount = 0.0;
			for (ClientTransactionPayBill rec : grid.getSelectedRecords()) {
				transactionObject.setTotal(transactionObject.getTotal()
						+ rec.getPayment());
				this.totalCashDiscount += rec.getCashDiscount();
			}
		} else {
			transactionObject.setTotal(0.0);
			this.totalCashDiscount = 0.0;
			this.totalOriginalAmount = 0.0;
			for (ClientTransactionPayBill rec : grid.getRecords()) {
				this.totalOriginalAmount += rec.getOriginalAmount();
				transactionObject.setTotal(transactionObject.getTotal()
						+ rec.getPayment());
				this.totalCashDiscount += rec.getCashDiscount();
			}
		}
	}

	private void enableFormItems() {

		setMode(EditMode.EDIT);
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		date.setDisabled(isInViewMode());
		vendorCombo.setDisabled(isInViewMode());
		paymentMethodCombo.setDisabled(isInViewMode());
		dueDate.setDisabled(isInViewMode());
		grid.setDisabled(isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setDisabled(isInViewMode());
		}

		super.onEdit();
		grid.removeFromParent();
		initListGrid();
		gridLayout.insert(grid, 2);
		getTransactionPayBills(this.getVendor());
		memoTextAreaItem.setDisabled(isInViewMode());
		transaction = new ClientPayBill();
		data = transaction;
		tdsCombo.setDisabled(isInViewMode());
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	protected Double getTransactionTotal() {
		return getAmountInBaseCurrency(this.amountLabel.getAmount());
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().payBills();
	}

	// Update methods

	public ClientVendor getVendor() {
		return vendor;
	}

	public void setVendor(ClientVendor vendor) {
		this.vendor = vendor;
	}

	public VendorCombo createVendorComboItem(String title) {

		VendorCombo vendorCombo = new VendorCombo(title != null ? title
				: Global.get().Vendor());
		vendorCombo.setHelpInformation(true);
		vendorCombo.setRequired(true);
		vendorCombo.setDisabled(isInViewMode());
		// vendorCombo.setShowDisabled(false);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						vendorSelected(selectItem);

					}

				});

		// vendorCombo.setShowDisabled(false);
		return vendorCombo;

	}

	public void deleteTotalPayment(ClientTransactionPayBill rec) {
		ClientTransaction transactionObject = getTransactionObject();
		// paybillView.transactionTotal -= rec.getPayment();
		transactionObject.setTotal(transactionObject.getTotal()
				- rec.getPayment());
		this.totalCashDiscount -= rec.getCashDiscount();

		/* updating payment column's footer */
		// this.updateFooterValues(DataUtils
		// .getAmountAsString(paybillView.transactionTotal), canEdit ? 7
		// : 6);
		// if (!DecimalUtil.isLessThan(paybillView.totalCashDiscount, 0))
		// this.updateFooterValues(DataUtils
		// .getAmountAsString(paybillView.totalCashDiscount),
		// canEdit ? 5 : 3);
	}

	public PayFromAccountsCombo createPayFromCombo(String title) {

		PayFromAccountsCombo payFromCombo = new PayFromAccountsCombo(title);
		payFromCombo.setHelpInformation(true);
		payFromCombo.setRequired(true);
		payFromCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						accountSelected(selectItem);
						// selectedAccount = (Account) selectItem;
						// adjustBalance();

					}

				});
		payFromCombo.setDisabled(isInViewMode());
		// payFromCombo.setShowDisabled(false);
		// formItems.add(payFromCombo);
		return payFromCombo;
	}

	protected void initVendors() {

		if (vendorCombo == null)
			return;
		List<ClientVendor> result = getCompany().getActiveVendors();
		vendors = result;

		vendorCombo.initCombo(result);
		vendorCombo.setDisabled(isInViewMode());

		if (getVendor() != null)
			vendorCombo.setComboItem(getVendor());
	}

	protected void initPayFromAccounts() {
		// getPayFromAccounts();
		// payFromCombo.initCombo(payFromAccounts);
		// payFromCombo.setAccountTypes(UIUtils
		// .getOptionsByType(AccountCombo.payFromCombo));
		payFromCombo.setAccounts();
		payFromCombo.setDisabled(isInViewMode());
		payFromAccount = payFromCombo.getSelectedValue();
		if (payFromAccount != null)
			payFromCombo.setComboItem(payFromAccount);
	}

	@Override
	protected void refreshTransactionGrid() {

	}

	private void setTabindexes() {
		vendorCombo.setTabIndex(1);
		payFromCombo.setTabIndex(2);
		vendorPaymentMethodCombo.setTabIndex(3);
		dueDate.setTabIndex(4);
		date.setTabIndex(5);
		transactionNumber.setTabIndex(6);
		endBalText.setTabIndex(8);
		memoTextAreaItem.setTabIndex(9);
		saveAndCloseButton.setTabIndex(10);
		saveAndNewButton.setTabIndex(11);
		cancelButton.setTabIndex(12);

	}

	@Override
	public void updateAmountsFromGUI() {
		this.grid.updateAmountsFromGUI();

	}

	private boolean isTDSEnable() {
		if (vendor != null) {
			return (getPreferences().isTDSEnabled() && vendor.isTdsApplicable())
					|| transaction.getTdsTaxItem() != null;
		} else {
			return getPreferences().isTDSEnabled()
					|| transaction.getTdsTaxItem() != null;
		}
	}
}
