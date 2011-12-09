package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayTAX;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class VATPaymentView extends AbstractTransactionBaseView<ClientPayTAX> {
	private AmountField vatBalance, amount, endingBalanceText;
	private TextItem referenceNo, checkNoText, memo, vatAgency;
	private AddressForm addressForm;
	private AccountCombo payFromAccCombo;
	private CheckboxItem toBePrinted;

	private DynamicForm vatAgencyForm;
	private DynamicForm paymentInformationForm;
	private DynamicForm accountInformationForm;
	private DynamicForm paymentMethodForm;
	private ClientAccount selectedPayFromAccount;
	private List<ClientAccount> payFromAccounts;

	private Double initialEndingBalance = 0D;
	private Double endingBalance = 0D;
	private Double totalAmount = 0D;

	private ArrayList<DynamicForm> listforms;

	public VATPaymentView() {
		super(ClientTransaction.TYPE_PAY_TAX);
	}

	@Override
	protected void initTransactionViewData() {
		initTransactionNumber();
		getPayFromAccounts();
		if (transaction != null) {
			initAccounterClass();
		}
	}

	private void getPayFromAccounts() {
		payFromAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (account.getType() == ClientAccount.TYPE_CASH
					|| account.getType() == ClientAccount.TYPE_BANK
					|| account.getType() == ClientAccount.TYPE_CREDIT_CARD
					|| account.getType() == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
					|| account.getType() == ClientAccount.TYPE_LONG_TERM_LIABILITY) {

				payFromAccounts.add(account);
			}
		}
		payFromAccCombo.initCombo(payFromAccounts);
	}

	protected void createControls() {
		Label lab = new Label(messages.vatPayment());
		// lab.setAutoHeight();
		// lab.setWrap(false);

		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		// dateNoForm.setWidth("*");
		// dateNoForm.setLayoutAlign(Alignment.RIGHT);
		dateNoForm.setFields(transactionDateItem, transactionNumber);

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			dateNoForm.setFields(classListCombo);
		}

		vatAgency = new TextItem(messages.vatAgency());
		// vatAgency.setWidth("*");
		vatAgency.setDisabled(true);
		// vatAgency.setShowDisabled(false);

		addressForm = new AddressForm(null);
		vatAgencyForm = new DynamicForm();
		vatAgencyForm = UIUtils.form(messages.vatAgency());
		vatAgencyForm.setWidth("100%");
		// vatAgencyForm.setAutoHeight();
		vatAgencyForm.setFields(vatAgency);

		amount = new AmountField(messages.amount(), this, getBaseCurrency());
		// amount.setWidth("*");
		amount.setRequired(true);
		amount.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		// amount.setTextAlign(Alignment.RIGHT);

		vatBalance = new AmountField(messages.vatBalance(), this,
				getBaseCurrency());
		// vatBalance.setWidth("*");
		// vatBalance.setTextAlign(Alignment.RIGHT);
		vatBalance.setDisabled(true);
		// vatBalance.setShowDisabled(false);
		memo = new TextItem(messages.memo());
		// memo.setWidth("*");
		// memo.setTextAlign(Alignment.LEFT);

		referenceNo = new TextItem(messages.referenceNo());
		// referenceNo.setWidth("*");
		// referenceNo.setTextAlign(Alignment.RIGHT);

		paymentInformationForm = new DynamicForm();
		paymentInformationForm.setIsGroup(true);
		paymentInformationForm.setWidth("100%");
		// paymentInformationForm.setAutoHeight();
		paymentInformationForm.setGroupTitle(messages.paymentInformation());
		paymentInformationForm.setFields(vatBalance, amount, memo, referenceNo);

		endingBalanceText = new AmountField(messages.bankBalance(), this,
				getBaseCurrency());
		// endingBalanceText.setWidth("*");
		endingBalanceText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		// endingBalanceText.setAlign(Alignment.RIGHT);
		// endingBalanceText.setTextAlign(Alignment.RIGHT);
		endingBalanceText.setDisabled(true);
		// endingBalanceText.setShowDisabled(false);

		payFromAccCombo = new PayFromAccountsCombo(messages.payFrom());
		payFromAccCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		payFromAccCombo.setRequired(true);
		// payFromAccCombo.setWidth("*");

		payFromAccCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedPayFromAccount = selectItem;
						initialEndingBalance = !DecimalUtil.isEquals(
								selectedPayFromAccount.getTotalBalance(), 0) ? selectedPayFromAccount
								.getTotalBalance() : 0D;
						calculateEndingBalance();

						endingBalanceText.setValue(DataUtils
								.getAmountAsString(endingBalance));

					}

				});
		accountInformationForm = new DynamicForm();
		accountInformationForm.setIsGroup(true);
		accountInformationForm.setWidth("100%");
		// accountInformationForm.setAutoHeight();
		accountInformationForm.setGroupTitle(messages.payeeInformation(messages
				.Account()));
		accountInformationForm.setFields(payFromAccCombo, endingBalanceText);

		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setRequired(true);
		// paymentMethodCombo.setWidth("*");
		paymentMethodCombo.setDefaultValue(messages.check());

		toBePrinted = new CheckboxItem(messages.toBePrinted());
		// toBePrinted.setDefaultValue(true);
		toBePrinted.addChangedHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {

				boolean isChecked = (Boolean) toBePrinted.getValue();

				if (isChecked)
					// checkNoText.enable();
					checkNoText.setDisabled(false);
				else
					// checkNoText.disable();
					checkNoText.setDisabled(true);
			}

		});
		// printCheck.setShowDisabled(false);

		checkNoText = new TextItem(messages.checkNo());
		// checkNoText.setWidth("*");

		paymentMethodForm = new DynamicForm();
		paymentMethodForm.setIsGroup(true);
		paymentMethodForm.setWidth("100%");
		paymentMethodForm.setGroupTitle(messages.paymentMethod());
		paymentMethodForm.setFields(paymentMethodCombo, toBePrinted,
				checkNoText);

		VerticalPanel lLayout = new VerticalPanel();
		lLayout.setWidth("100%");
		// lLayout.setAutoHeight();
		lLayout.add(vatAgencyForm);
		lLayout.add(addressForm);
		lLayout.add(paymentInformationForm);
		VerticalPanel rLayout = new VerticalPanel();
		rLayout.setWidth("100%");
		// rLayout.setAutoHeight();
		rLayout.add(accountInformationForm);
		rLayout.add(paymentMethodForm);

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setWidth("100%");
		hPanel.add(lLayout);
		hPanel.add(rLayout);
		// HorizontalPanel.setMembersMargin(20);

		VerticalPanel mainLayout = new VerticalPanel();
		mainLayout.setWidth("100%");
		// mainLayout.setMembersMargin(10);
		// mainLayout.setMargin(10);
		mainLayout.add(lab);
		mainLayout.add(voidedPanel);
		mainLayout.add(dateNoForm);
		mainLayout.add(hPanel);
		this.add(mainLayout);

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vatAgencyForm);
		listforms.add(paymentInformationForm);
		listforms.add(accountInformationForm);
		listforms.add(paymentMethodForm);

	}

	private void calculateEndingBalance() {

		if (selectedPayFromAccount != null) {

			if (selectedPayFromAccount.isIncrease())
				endingBalance = initialEndingBalance + totalAmount;
			else
				endingBalance = initialEndingBalance - totalAmount;

		}
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();
		saveOrUpdate(transaction);
	}

	protected void updateTransaction() {
		super.updateTransaction();
		transaction.setType(ClientTransaction.TYPE_PAY_TAX);

		transaction.setNumber(transactionNumber.getValue().toString());

		transaction
				.setDate(((ClientFinanceDate) transactionDateItem.getValue())
						.getDate());

		transaction.setTotal(amount.getAmount());

		transaction.setMemo(memo.getValue().toString());

		transaction.setReference(referenceNo.getValue().toString());

		transaction.setPayFrom(selectedPayFromAccount.getID());

		transaction.setPaymentMethod(paymentMethod);
	}

	@Override
	protected void paymentMethodSelected(String paymentmethod) {
		super.paymentMethodSelected(paymentmethod);
		if (paymentmethod.equalsIgnoreCase(messages.check())) {
			toBePrinted.setDisabled(false);
			toBePrinted.setValue(true);
			checkNoText.setDisabled(false);
		} else {
			toBePrinted.setValue(false);
			toBePrinted.setDisabled(true);
			checkNoText.setDisabled(true);
		}
	}

	@Override
	public void updateNonEditableItems() {
		// currently not using

	}

	@Override
	public ValidationResult validate() {
		// NOTE:: not using it
		ValidationResult result = accountInformationForm.validate();
		result.add(paymentMethodForm.validate());
		result.add(super.validate());
		return result;
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
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	// its not using any where
	@Override
	public void print() {

	}

	// its not using any where
	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return UIUtils.title(messages.vatPayment());
	}

	@Override
	protected void refreshTransactionGrid() {

	}

	@Override
	public void updateAmountsFromGUI() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean canDelete() {
		return false;
	}

	@Override
	protected boolean canVoid() {
		return false;
	}
}
