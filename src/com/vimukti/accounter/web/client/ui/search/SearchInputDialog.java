package com.vimukti.accounter.web.client.ui.search;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.SearchInput;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ItemCombo;
import com.vimukti.accounter.web.client.ui.combo.PayeeCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class SearchInputDialog extends BaseDialog {

	private SelectCombo transactionTypeCombo, searchByTypeCombo, findByCombo,
			matchIfCombo;
	private DynamicForm mainForm, searchTypeForm, findbyForm, matchIfForm;
	private VerticalPanel buttonPanel, mainPanel, allFormPanel, resultPanel;
	private HorizontalPanel panel, typePanel;
	private Button findButton, closeButton;
	private DateItem dateField;
	private CustomerCombo customerCombo;
	private VendorCombo vendorCombo;
	private AccountCombo accountCombo;
	private PayeeCombo payeeCombo;
	private ItemCombo itemCombo;
	private SearchInput input;
	private SearchTable grid;
	private Label labelItem;
	List<ClientAccount> accounts;
	private AmountField amountField;
	private SimplePager pager;
	protected AccounterMessages messages = Accounter.messages();

	private String[] transactionNames = {
			messages.all(),
			messages.bill(),
			messages.billPayment(),
			// messages.billableExpenseCharge(),
			messages.cashExpense(),
			messages.charge(),
			// messages.payeeCredit(Global.get().Customer()),
			messages.creditCardExpense(), messages.creditCardCharge(),
			messages.customerCreditNote(Global.get().Customer()),
			messages.customerRefund(Global.get().Customer()),
			messages.cashSale(), messages.deposit(), messages.estimate(),
			messages.invoice(),
			messages.journalEntry(),
			messages.paymentFromCustomer(),
			// messages.statement(),
			messages.payeeCredit(Global.get().Vendor()), messages.transfer(),
			messages.vatPayment(), messages.vatAdjustment(),
			messages.writeCheck() };

	private String[] searchByAll = { messages.amount(), messages.date(),
			messages.descOrMemo(), messages.payee() };// , messages.refNo() };

	private String[] searchByPayee = { messages.payee(), messages.Account(),
			messages.amount(), messages.date(), messages.descOrMemo() };

	// private String[] searchByCustomerStatement = { Global.get().Customer(),
	// messages.statementDate(), messages.statementNo() };

	private String[] searchByJournalOptions = { messages.Account(),
			messages.date(), messages.descOrMemo(), messages.journalEntryNo() };
	private String[] searchByCustomerTransactionOptions = {
			Global.get().Customer(), messages.amount(), messages.date(),
			messages.descOrMemo(), messages.productOrService() };

	private String[] searchBySupplierTransactionOptions = {
			Global.get().Vendor(), messages.amount(), messages.Account(),
			messages.date(), messages.descOrMemo(),// messages.refNo(),
			messages.productOrService() };
	private String[] searchByCustomerOptions = { Global.get().Customer(),
			messages.Account(), messages.amount(), messages.date(),
			messages.dueDate(), messages.invoiceDate(), messages.descOrMemo(),
			messages.productOrService() };

	private String[] searchByDepositOptions = { messages.amount(),
			messages.Account(), messages.date(), messages.descOrMemo() };

	private String[] matchIfoptions = { messages.contains(), messages.exact(),
			messages.greater(), messages.Less() };
	private String[] searchByVatOptions = { messages.Account(),
			messages.date(), messages.descOrMemo() };

	private TextItem findByItem;

	public SearchInputDialog(String string) {
		super(string);
		createControls();
	}

	public void createControls() {

		mainForm = new DynamicForm();
		typePanel = new HorizontalPanel();
		transactionTypeCombo = new SelectCombo(messages.transactionType());
		transactionTypeCombo.initCombo(getTransactionsList());
		transactionTypeCombo.setComboItem(getTransactionsList().get(0));
		transactionTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						// transactionType = getTransactionType(selectItem);
						setSearchOptionsComboByType(selectItem);
					}
				});

		dateField = new DateItem(messages.findBy());
		dateField.setValue(new ClientFinanceDate());
		findByItem = new TextItem(messages.findBy());
		dateField.addStyleName("search_combo_width");
		amountField = new AmountField(messages.findBy(), this);
		amountField.addStyleName("search_combo_width");
		searchTypeForm = new DynamicForm();
		searchByTypeCombo = new SelectCombo(messages.search());
		searchByTypeCombo.initCombo(allList());
		searchByTypeCombo.setComboItem(allList().get(0));
		searchByTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						setFindByComboOptions(selectItem);
						// searchByType = getSearchByType(selectItem);
					}
				});
		customerCombo = new CustomerCombo(messages.findBy(), false);
		vendorCombo = new VendorCombo(messages.findBy(), false);
		payeeCombo = new PayeeCombo(messages.findBy(), false);
		itemCombo = new ItemCombo(messages.findBy(), 1, false);
		itemCombo.initCombo(Accounter.getCompany().getAllItems());
		payeeCombo.initCombo(getCompany().getActivePayees());
		accountCombo = new AccountCombo(messages.findBy(), false) {

			@Override
			protected List<ClientAccount> getAccounts() {
				return getCompany().getActiveAccounts();
			}
		};
		findByCombo = new SelectCombo(messages.findBy());
		findByCombo.addStyleName("search_select_combo");
		matchIfCombo = new SelectCombo(messages.match(), false);
		matchIfCombo.initCombo(getMatchByNumber());
		matchIfCombo.setComboItem(getMatchByNumber().get(0));
		searchTypeForm.setFields(searchByTypeCombo);

		findbyForm = new DynamicForm();
		findbyForm.setFields(amountField);

		matchIfForm = new DynamicForm();
		matchIfForm.setFields(matchIfCombo);
		// matchIfCombo.addStyleName("search_match");

		allFormPanel = new VerticalPanel();
		allFormPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
		allFormPanel.add(searchTypeForm);
		allFormPanel.add(findbyForm);
		allFormPanel.add(matchIfForm);
		allFormPanel.addStyleName("search_textbox_labels");

		buttonPanel = new VerticalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
		findButton = new Button(messages.find());
		findButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				makeResult();
			}
		});
		closeButton = new Button(messages.close());
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				close();
			}
		});
		buttonPanel.add(findButton);
		buttonPanel.add(closeButton);
		buttonPanel.addStyleName("search_buttons");

		panel = new HorizontalPanel();
		panel.setStyleName("search-all-forms");
		panel.add(allFormPanel);
		panel.add(buttonPanel);

		labelItem = new Label();
		labelItem.setStyleName("label-status");
		labelItem.setText(messages.status() + ": " + messages.selectCreteria());

		mainForm.setFields(transactionTypeCombo);
		transactionTypeCombo.getMainWidget().getParent().getElement()
				.setAttribute("align", "center");
		transactionTypeCombo.getMainWidget().getParent().getElement()
				.addClassName("search_combo_width");
		typePanel.add(mainForm);
		typePanel.setStyleName("transaction_type_panel");

		mainPanel = new VerticalPanel();
		mainPanel.add(typePanel);
		mainPanel.add(panel);
		mainPanel.add(labelItem);
		footerLayout.setVisible(false);

		super.setWidth("500px");
		super.setHeight("200px");
		setBodyLayout(mainPanel);
		mainPanel.getElement().getParentElement().addClassName("search-width");
	}

	protected int getSearchByType(String value) {
		if (value.equals(messages.amount())) {
			return SearchInput.TYPE_AMOUNT;
		}
		if (value.equals(messages.descOrMemo())) {
			return SearchInput.TYPE_DESC_MEMO;
		}
		// if (value.equals(messages.refNo())) {
		// return SearchInput.TYPE_REF_NO;
		// }
		if (value.equals(messages.chequeNo())) {
			return SearchInput.TYPE_CHEQUE_NO;
		}
		if (value.equals(messages.receivedChequeNo())) {
			return SearchInput.TYPE_RECEIVED_NO;
		}
		if (value.equals(messages.creditNo())) {
			return SearchInput.TYPE_CREDIT_NO;
		}
		if (value.equals(messages.creditNoteNo())) {
			return SearchInput.TYPE_CREDIT_NOTE_NO;
		}
		if (value.equals(messages.chargeNo())) {
			return SearchInput.TYPE_CHARGE_NO;
		}
		if (value.equals(messages.invoiceNo())) {
			return SearchInput.TYPE_INOVICE_NO;
		}
		if (value.equals(messages.date())) {
			return SearchInput.TYPE_DATE;
		}
		if (value.equals(messages.dueDate())) {
			return SearchInput.TYPE_DUE_DATE;
		}
		if (value.equals(messages.invoiceDate())) {
			return SearchInput.TYPE_INVOICE_DATE;
		}
		if (value.equals(Global.get().Customer())) {
			return SearchInput.TYPE_CUSTOMER;
		}
		if (value.equals(Global.get().Vendor())) {
			return SearchInput.TYPE_VENDOR;
		}
		if (value.equals(messages.Account())) {
			return SearchInput.TYPE_ACCOUNT;
		}
		if (value.equals(messages.payee())) {
			return SearchInput.TYPE_PAYEE;
		}
		if (value.equals(messages.productOrService())) {
			return SearchInput.TYPE_PRODUCT_SERVICE;
		}
		if (value.equals(messages.estimateNo())) {
			return SearchInput.TYPE_ESTIMATE_NO;
		}
		if (value.equals(messages.saleNo())) {
			return SearchInput.TYPE_SALE_NO;
		}
		if (value.equals(messages.refundNo())) {
			return SearchInput.TYPE_REFUND_NO;
		}
		if (value.equals(messages.journalEntryNo())) {
			return SearchInput.TYPE_ENTRY_NO;
		}

		return 0;
	}

	protected void makeResult() {

		Object value = getFindByObjType().getValue();
		if (value == null || value.equals("")) {
			updateGrid();
			labelItem.setText(messages.status() + ": "
					+ messages.findByMustBeFilled());
			return;
		}

		input = new SearchInput();
		input.setTransactionType(getTransactionType(transactionTypeCombo
				.getSelectedValue()));
		input.setSearchbyType(getSearchByType(searchByTypeCombo
				.getSelectedValue()));

		if (getFindByObjType() instanceof AmountField) {
			String string = "" + ((AmountField) getFindByObjType()).getAmount();
			input.setFindBy(string.equals("") ? "0.00" : string);
		} else if (getFindByObjType() instanceof TextItem) {
			String string = getFindByObjType().getValue().toString();
			input.setFindBy(string);
		} else if (getFindByObjType() instanceof CustomerCombo) {
			input.setFindBy(((CustomerCombo) getFindByObjType())
					.getSelectedValue().getDisplayName());
		} else if (getFindByObjType() instanceof VendorCombo) {
			input.setFindBy(((VendorCombo) getFindByObjType())
					.getSelectedValue().getDisplayName());
		} else if (getFindByObjType() instanceof DateItem) {
			input.setValue(dateField.getDate().getDate());
		} else if (getFindByObjType() instanceof AccountCombo) {
			input.setFindBy(((AccountCombo) getFindByObjType())
					.getSelectedValue().getDisplayName());
		} else if (getFindByObjType() instanceof PayeeCombo) {
			input.setFindBy(((PayeeCombo) getFindByObjType())
					.getSelectedValue().getDisplayName());
		} else if (getFindByObjType() instanceof ItemCombo) {
			input.setFindBy(((ItemCombo) getFindByObjType()).getSelectedValue()
					.getDisplayName());
		}
		input.setMatchType(getMatachType(matchIfCombo.getSelectedValue()));
		if (resultPanel != null) {
			mainPanel.remove(resultPanel);
		}

		resultPanel = new VerticalPanel();
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);

		grid = new SearchTable(input) {
			@Override
			public void disableGrid() {
				updateGrid();
			}

			@Override
			public void enableGrid() {
				enableGridAndPager();
			}
		};

		pager.setDisplay(grid);
		pager.setStyleName("pager-alignment");

		// if (!grid.listDataProvider.getDataDisplays().isEmpty()) {
		// labelItem.setText(messages.status() + ": "
		// + messages.noRecordsToShow());
		// } else {
		resultPanel.add(grid);
		resultPanel.add(pager);
		labelItem.setVisible(false);
		// }
		mainPanel.add(resultPanel);
		resultPanel.setVisible(false);
	}

	protected void enableGridAndPager() {
		labelItem.setVisible(false);
		resultPanel.setVisible(true);
	}

	private void updateGrid() {
		labelItem.setVisible(true);
		labelItem
				.setText(messages.status() + ": " + messages.noRecordsToShow());
		if (resultPanel != null) {
			resultPanel.setVisible(false);
		}
	}

	private int getMatachType(String selectedValue) {
		if (selectedValue.equals(messages.contains())) {
			return 3;
		} else if (selectedValue.equals(messages.exact())) {
			return 0;
		} else if (selectedValue.equals(messages.greater())) {
			return 1;
		} else if (selectedValue.equals(messages.Less())) {
			return 2;
		}
		return 3;
	}

	protected int getTransactionType(String selectItem) {
		if (selectItem.equals(messages.all())) {
			return ClientTransaction.TYPE_ALL;
		}

		if (selectItem.equals(messages.bill())) {
			return ClientTransaction.TYPE_ENTER_BILL;
		}

		if (selectItem.equals(messages.billPayment())) {
			return ClientTransaction.TYPE_PAY_BILL;
		}

		// if (selectItem.equals(messages.billableExpenseCharge())) {
		// return ClientTransaction.TYPE_ESTIMATE;
		// }

		if (selectItem.equals(messages.cashExpense())) {
			return ClientTransaction.TYPE_CASH_EXPENSE;
		}

		if (selectItem.equals(messages.charge())) {
			return ClientTransaction.TYPE_ESTIMATE;
		}

		if (selectItem.equals(messages.writeCheck())) {
			return ClientTransaction.TYPE_WRITE_CHECK;
		}

		// if (selectItem.equals(messages.payeeCredit(Global.get().Customer())))
		// {
		// // return ClientTransaction.TYPE_
		// }

		if (selectItem.equals(messages.creditCardExpense())) {
			return ClientTransaction.TYPE_CREDIT_CARD_EXPENSE;
		}

		if (selectItem.equals(messages.creditCardCharge())) {
			return ClientTransaction.TYPE_CREDIT_CARD_CHARGE;
		}

		if (selectItem.equals(messages.customerCreditNote(Global.get()
				.Customer()))) {
			return ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO;
		}

		if (selectItem.equals(messages.invoice())) {
			return ClientTransaction.TYPE_INVOICE;
		}
		if (selectItem.equals(messages.paymentFromCustomer())) {
			return ClientTransaction.TYPE_RECEIVE_PAYMENT;
		}
		if (selectItem.equals(messages.estimate())) {
			return ClientTransaction.TYPE_ESTIMATE;
		}

		if (selectItem.equals(messages.customerRefund(Global.get().Customer()))) {
			return ClientTransaction.TYPE_CUSTOMER_REFUNDS;
		}
		if (selectItem.equals(messages.cashSale())) {
			return ClientTransaction.TYPE_CASH_SALES;
		}

		if (selectItem.equals(messages.deposit())) {
			return ClientTransaction.TYPE_MAKE_DEPOSIT;
		}

		if (selectItem.equals(messages.journalEntry())) {
			return ClientTransaction.TYPE_JOURNAL_ENTRY;
		}

		// if (selectItem.equals(messages.statement())) {
		// setOptiosByStatement();
		// }
		if (selectItem.equals(messages.transfer())) {
			return ClientTransaction.TYPE_MAKE_DEPOSIT;
		}
		if (selectItem.equals(messages.vatPayment())) {
			return ClientTransaction.TYPE_PAY_TAX;
		}
		if (selectItem.equals(messages.vatAdjustment())) {
			return ClientTransaction.TYPE_ADJUST_VAT_RETURN;
		}
		if (selectItem.equals(messages.payeeCredit(Global.get().Vendor()))) {
			return ClientTransaction.TYPE_VENDOR_CREDIT_MEMO;
		}
		return 0;
	}

	protected void close() {
		this.removeFromParent();
	}

	private List<String> getMatchIfComboList() {
		List<String> list = new ArrayList<String>();
		for (String string : matchIfoptions) {
			list.add(string);
		}
		list.remove(2);
		list.remove(2);
		return list;
	}

	private List<String> getMatchByNumber() {
		List<String> list = new ArrayList<String>();
		for (String string : matchIfoptions) {
			list.add(string);
		}
		list.remove(0);
		return list;
	}

	private List<String> getMatchByDate() {
		List<String> list = new ArrayList<String>();
		for (String string : matchIfoptions) {
			list.add(string);
		}
		list.remove(0);
		list.remove(1);
		list.remove(1);
		return list;
	}

	protected void setFindByComboOptions(String selectItem) {
		labelItem.setText(messages.status() + ": " + messages.selectCreteria());
		findByItem.setValue("");
		amountField.setAmount(null);
		findbyForm.clear();
		matchIfForm.clear();
		if (selectItem.equals(messages.descOrMemo())
				// || selectItem.equals(messages.refNo())
				|| selectItem.equals(messages.chequeNo())
				|| selectItem.equals(messages.receivedChequeNo())
				|| selectItem.equals(messages.creditNo())
				|| selectItem.equals(messages.creditNoteNo())
				|| selectItem.equals(messages.chargeNo())
				|| selectItem.equals(messages.invoiceNo())) {
			findbyForm.setFields(findByItem);
			findByItem.getMainWidget().addStyleName("search_textbox");
			matchIfForm.setFields(matchIfCombo);
			matchIfCombo.initCombo(getMatchIfComboList());
		} else if (selectItem.equals(messages.date())
				|| selectItem.equals(messages.dueDate())
				|| selectItem.equals(messages.invoiceDate())) {
			findbyForm.setFields(dateField);
			dateField.setValue(new ClientFinanceDate());
			dateField.getMainWidget().addStyleName("search_date");
			matchIfForm.setFields(matchIfCombo);
			matchIfCombo.initCombo(getMatchByDate());
		} else if (selectItem.equals(Global.get().Customer())) {
			findbyForm.setFields(customerCombo);
			matchIfForm.setFields(matchIfCombo);
			matchIfCombo.initCombo(getMatchIfComboList());
		} else if (selectItem.equals(Global.get().Vendor())) {
			findbyForm.setFields(vendorCombo);
			matchIfForm.setFields(matchIfCombo);
			matchIfCombo.initCombo(getMatchIfComboList());
		} else if (selectItem.equals(messages.Account())) {
			initAccountsCombo();
			findbyForm.setFields(accountCombo);
			accountCombo.getMainWidget().addStyleName("search_account_combo");
			matchIfForm.setFields(matchIfCombo);
			matchIfCombo.initCombo(getMatchIfComboList());
		} else if (selectItem.equals(messages.payee())) {
			findbyForm.setFields(payeeCombo);
			payeeCombo.getMainWidget().addStyleName("search_account_combo");
			matchIfForm.setFields(matchIfCombo);
			matchIfCombo.initCombo(getMatchIfComboList());
		} else if (selectItem.equals(messages.productOrService())) {
			findbyForm.setFields(itemCombo);
			itemCombo.getMainWidget().addStyleName("search_account_combo");
			matchIfForm.setFields(matchIfCombo);
			matchIfCombo.initCombo(getMatchIfComboList());
		} else if (selectItem.equals(messages.amount())) {
			findbyForm.setFields(amountField);
			amountField.getMainWidget().addStyleName("search_textbox");
			matchIfForm.setFields(matchIfCombo);
			matchIfCombo.initCombo(getMatchByNumber());
		} else if (selectItem.equals(messages.journalEntryNo())
				|| selectItem.equals(messages.refundNo())
				|| selectItem.equals(messages.saleNo())
				|| selectItem.equals(messages.estimateNo())) {
			findbyForm.setFields(findByItem);
			findByItem.getMainWidget().addStyleName("search_textbox");
			matchIfForm.setFields(matchIfCombo);
			matchIfCombo.initCombo(getMatchByNumber());
		}
		matchIfCombo.setSelectedItem(0);
		// matchIfCombo.getMainWidget().addStyleName("search_account_combo");

	}

	private void initAccountsCombo() {
		accounts = new ArrayList<ClientAccount>();
		accounts.clear();
		if (transactionTypeCombo.getSelectedValue().equals(messages.bill())
				|| transactionTypeCombo.getSelectedValue().equals(
						messages.creditCardCharge())
				|| transactionTypeCombo.getSelectedValue().equals(
						messages.payeeCredit(Global.get().Vendor()))) {
			accounts.addAll(getCompany()
					.getAccounts(ClientAccount.TYPE_EXPENSE));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_COST_OF_GOODS_SOLD));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_OTHER_EXPENSE));
			accountCombo.initCombo(accounts);
		} else if (transactionTypeCombo.getSelectedValue().equals(
				messages.writeCheck())) {
			accounts.addAll(getCompany()
					.getAccounts(ClientAccount.TYPE_EXPENSE));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_COST_OF_GOODS_SOLD));
			accountCombo.initCombo(accounts);
		} else if (transactionTypeCombo.getSelectedValue().equals(
				messages.billPayment())
				|| transactionTypeCombo.getSelectedValue().equals(
						messages.cashExpense())
				|| transactionTypeCombo.getSelectedValue().equals(
						messages.vatPayment())) {
			accounts.addAll(getCompany().getAccounts(ClientAccount.TYPE_BANK));
			accounts.addAll(getCompany().getAccounts(ClientAccount.TYPE_CASH));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_OTHER_CURRENT_ASSET));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_CREDIT_CARD));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_INVENTORY_ASSET));
			accountCombo.initCombo(accounts);
		} else if (transactionTypeCombo.getSelectedValue().equals(
				messages.vatAdjustment())) {
			accounts.addAll(getCompany().getAccounts());
			accountCombo.initCombo(accounts);
		} else if (transactionTypeCombo.getSelectedValue().equals(
				messages.creditCardExpense())) {
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_CREDIT_CARD));
			accountCombo.initCombo(accounts);
		} else if (transactionTypeCombo.getSelectedValue().equals(
				messages.customerCreditNote(Global.get().Customer()))) {
			accounts.addAll(getCompany().getAccounts(ClientAccount.TYPE_INCOME));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_CREDIT_CARD));
			accountCombo.initCombo(accounts);
		} else if (transactionTypeCombo.getSelectedValue().equals(
				messages.deposit())) {
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_OTHER_ASSET));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_CREDIT_CARD));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_OTHER_CURRENT_LIABILITY));
			accounts.addAll(getCompany().getAccounts(ClientAccount.TYPE_BANK));
			accounts.addAll(getCompany().getAccounts(ClientAccount.TYPE_EQUITY));
			accountCombo.initCombo(accounts);
		} else if (transactionTypeCombo.getSelectedValue().equals(
				messages.paymentFromCustomer())) {
			accounts.addAll(getCompany().getAccounts(ClientAccount.TYPE_BANK));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_CREDIT_CARD));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_OTHER_CURRENT_ASSET));
			accountCombo.initCombo(accounts);
		}
	}

	public FormItem<?> getFindByObjType() {
		for (FormItem<?> formItem : findbyForm.getFormItems()) {
			return formItem;
		}
		return null;
	}

	protected void setSearchOptionsComboByType(String selectItem) {
		updateGrid();
		labelItem.setText(messages.status() + ": " + messages.selectCreteria());
		if (selectItem.equals(messages.all())) {
			setOptionsForAll(selectItem);
		} else if (selectItem.equals(messages.bill())) {
			setOptionsForPayeeList(selectItem);
		} else if (selectItem.equals(messages.billPayment())) {
			setOptionsForSupplierTransactions(selectItem);
			// }
			// else if (selectItem.equals(messages.billableExpenseCharge())) {
			// setOptionsByCustomer(selectItem);
		} else if (selectItem.equals(messages.cashExpense())) {
			setOptionsForSupplierTransactions(selectItem);
		} else if (selectItem.equals(messages.charge())) {
			setOptionsByCustomer(selectItem);
		} else if (selectItem.equals(messages.writeCheck())) {
			setOptionsForPayeeList(selectItem);
			// }
			// else if (selectItem.equals(messages.payeeCredit(Global.get()
			// .Customer()))) {
			// setOptionsByCustomer(selectItem);
		} else if (selectItem.equals(messages.creditCardExpense())) {
			setOptionsForSupplierTransactions(selectItem);
		} else if (selectItem.equals(messages.creditCardCharge())) {
			setOptionsForPayeeList(selectItem);
		} else if (selectItem.equals(messages.customerCreditNote(Global.get()
				.Customer()))) {
			setOptionsByCustomerTransactions(selectItem);
		} else if (selectItem.equals(messages.invoice())) {
			setOptionsByCustomerTransactions(selectItem);
		} else if (selectItem.equals(messages.paymentFromCustomer())) {
			setOptionsByCustomerTransactions(selectItem);
		} else if (selectItem.equals(messages.estimate())) {
			setOptionsByCustomerTransactions(selectItem);
		} else if (selectItem.equals(messages.customerRefund(Global.get()
				.Customer()))) {
			setOptionsByCustomerTransactions(selectItem);
		} else if (selectItem.equals(messages.cashSale())) {
			setOptionsByCustomerTransactions(selectItem);
		} else if (selectItem.equals(messages.deposit())) {
			setOptionsForDepositList(selectItem);
		} else if (selectItem.equals(messages.journalEntry())) {
			setOptionsByJournal();
			// } else if (selectItem.equals(messages.statement())) {
			// setOptiosByStatement();
		} else if (selectItem.equals(messages.transfer())) {
			setOptionsByVatOrTransfer(selectItem);
		} else if (selectItem.equals(messages.vatPayment())) {
			setOptionsByVatOrTransfer(selectItem);
		} else if (selectItem.equals(messages.vatAdjustment())) {
			setOptionsByVatOrTransfer(selectItem);
		} else if (selectItem.equals(messages
				.payeeCredit(Global.get().Vendor()))) {
			setOptionsForSupplierTransactions(selectItem);
		}
	}

	private void setOptionsForDepositList(String selectItem) {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> list = new ArrayList<String>();
		for (String string : searchByDepositOptions) {
			list.add(string);
		}
		searchByTypeCombo.initCombo(list);
		searchByTypeCombo.setComboItem(list.get(0));
		searchTypeForm.setFields(searchByTypeCombo);
		findbyForm.setFields(amountField);
		setFindByComboOptions(list.get(0));
	}

	private void setOptionsForPayeeList(String selectItem) {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> list = new ArrayList<String>();
		for (String string : searchByPayee) {
			list.add(string);
		}
		if (selectItem.equals(messages.bill())) {
			list.add(messages.productOrService());
			// list.add(messages.refNo());
			list.add(messages.dueDate());
			list.remove(0);
			list.add(Global.get().Vendor());
		}
		if (selectItem.equals(messages.deposit())) {
			list.add(messages.receivedChequeNo());
		}
		if (selectItem.equals(messages.creditCardCharge())) {
			list.remove(0);
			list.add(Global.get().Vendor());
			list.add(messages.productOrService());
			// list.add(messages.refNo());
		}
		if (selectItem.equals(messages.writeCheck())) {
			list.remove(0);
			list.add(Global.get().Customer());
			list.add(Global.get().Vendor());
			list.add(messages.chequeNo());
		}
		searchByTypeCombo.initCombo(list);
		searchByTypeCombo.setComboItem(list.get(1));
		searchTypeForm.setFields(searchByTypeCombo);
		findByItem.addStyleName("search_combo_width");
		findbyForm.setFields(findByItem);
		setFindByComboOptions(list.get(1));
	}

	private void setOptionsForAll(String selectItem) {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> searchAll = new ArrayList<String>();
		for (String string : searchByAll) {
			searchAll.add(string);
		}
		searchByTypeCombo.initCombo(searchAll);
		searchByTypeCombo.setComboItem(searchAll.get(0));
		searchTypeForm.setFields(searchByTypeCombo);
		findbyForm.setFields(findByItem);
		findByItem.addStyleName("search_combo_width");
		setFindByComboOptions(searchAll.get(0));
	}

	public void setOptionsForSupplierTransactions(String selectItem) {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> list = new ArrayList<String>();
		for (String string : searchBySupplierTransactionOptions) {
			list.add(string);
		}
		if (selectItem.equals(messages.billPayment())) {
			list.remove(4);
			list.remove(4);
			list.add(messages.chequeNo());
		}
		searchByTypeCombo.initCombo(list);
		searchByTypeCombo.setComboItem(list.get(1));
		searchTypeForm.setFields(searchByTypeCombo);
		findbyForm.setFields(findByItem);
		findByItem.addStyleName("search_combo_width");
		setFindByComboOptions(list.get(1));
	}

	public void setOptionsByCustomerTransactions(String selectItem) {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> list = new ArrayList<String>();
		for (String string : searchByCustomerTransactionOptions) {
			list.add(string);
		}
		if (selectItem.equals(messages.customerRefund(Global.get().Customer()))) {
			list.remove(4);
			list.add(messages.refundNo());
		}
		if (selectItem.equals(messages.cashSale())) {
			list.add(messages.saleNo());
		}
		if (selectItem.equals(messages.customerCreditNote(Global.get()
				.Customer()))) {
			list.add(messages.creditNoteNo());
		}
		if (selectItem.equals(messages.invoice())) {
			list.add(messages.invoiceNo());
			list.add(messages.dueDate());
		}
		if (selectItem.equals(messages.estimate())) {
			list.add(messages.estimateNo());
			list.add(messages.invoiceDate());
		}
		if (selectItem.equals(messages.paymentFromCustomer())) {
			list.remove(4);
			list.add(messages.receivedChequeNo());
			list.add(messages.Account());
		}
		searchByTypeCombo.initCombo(list);
		searchByTypeCombo.setComboItem(list.get(0));
		searchTypeForm.setFields(searchByTypeCombo);
		customerCombo.addStyleName("search_combo_width");
		findbyForm.setFields(customerCombo);
		setFindByComboOptions(list.get(0));
	}

	private void setOptionsByJournal() {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> list = new ArrayList<String>();
		for (String string : searchByJournalOptions) {
			list.add(string);
		}
		searchByTypeCombo.initCombo(list);
		searchByTypeCombo.setComboItem(list.get(1));
		searchTypeForm.setFields(searchByTypeCombo);
		findbyForm.setFields(dateField);
		setFindByComboOptions(list.get(1));
	}

	// private void setOptiosByStatement() {
	// searchTypeForm.clear();
	// findbyForm.clear();
	// List<String> list = new ArrayList<String>();
	// for (String string : searchByCustomerStatement) {
	// list.add(string);
	// }
	// searchByTypeCombo.initCombo(list);
	// searchTypeForm.setFields(searchByTypeCombo);
	// }

	private void setOptionsByVatOrTransfer(String selectItem) {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> list = new ArrayList<String>();
		for (String string : searchByVatOptions) {
			list.add(string);
		}
		if (selectItem.equals(messages.transfer())) {
			list.add(messages.amount());

		}
		searchByTypeCombo.initCombo(list);
		searchByTypeCombo.setComboItem(list.get(0));
		searchTypeForm.setFields(searchByTypeCombo);
		findbyForm.setFields(accountCombo);
		setFindByComboOptions(list.get(0));
	}

	public void setOptionsByCustomer(String slectItem) {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> list = new ArrayList<String>();
		for (String string : searchByCustomerOptions) {
			list.add(string);
		}
		if (slectItem.equals(messages.charge())) {
			list.remove(1);
			list.remove(3);
			list.add(messages.chargeNo());
		}
		searchByTypeCombo.initCombo(list);
		searchByTypeCombo.setComboItem(list.get(0));
		searchTypeForm.setFields(searchByTypeCombo);
		findbyForm.setFields(customerCombo);
		customerCombo.addStyleName("search_combo_width");
		setFindByComboOptions(list.get(0));
	}

	private List<String> allList() {
		List<String> searchAll = new ArrayList<String>();
		for (String string : searchByAll) {
			searchAll.add(string);
		}
		return searchAll;
	}

	private List<String> getTransactionsList() {
		List<String> transactionTypes = new ArrayList<String>();
		for (String string : transactionNames) {
			transactionTypes.add(string);
		}
		return transactionTypes;
	}

	@Override
	protected boolean onOK() {
		return false;
	}

	@Override
	public void setFocus() {
		transactionTypeCombo.setFocus();
	}

}
