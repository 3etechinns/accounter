package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.CustomerSelectionListener;
import com.vimukti.accounter.web.client.ui.grids.CustomerTransactionsHistoryGrid;
import com.vimukti.accounter.web.client.ui.grids.CustomersListGrid;

public class CustomerCenterView<T> extends
		AbstractPayeeCenterView<ClientCustomer> {
	private static final int TYPE_ESTIMATE = 7;
	private static final int TYPE_INVOICE = 8;
	private static final int TYPE_CAHSSALE = 1;
	private static final int TYPE_RECEIVE_PAYMENT = 12;
	private static final int TYPE_CREDITNOTE = 4;
	private static final int TYPE_CUSTOMER_REFUND = 5;
	private static final int TYPE_ALL_TRANSACTION = 100;
	private ClientCustomer selectedCustomer;
	private List<PayeeList> listOfCustomers;
	private ArrayList<TransactionHistory> records;

	private CustomerDetailsPanel detailsPanel;
	private CustomersListGrid custGrid;
	private SelectCombo activeInActiveSelect, trasactionViewSelect,
			trasactionViewTypeSelect;
	private VerticalPanel transactionGridpanel;
	private CustomerTransactionsHistoryGrid custHistoryGrid;
	private Map<Integer, String> transactiontypebyStatusMap;
	private boolean isActiveAccounts = true;

	public CustomerCenterView() {

	}

	@Override
	public boolean canEdit() {
		return selectedCustomer == null ? false : true;
	}

	@Override
	public void onEdit() {
		NewCustomerAction newCustomerAction = ActionFactory
				.getNewCustomerAction();
		newCustomerAction.setisCustomerViewEditable(true);
		newCustomerAction.run(selectedCustomer, false);
	}

	@Override
	public void init() {
		super.init();
		creatControls();

	}

	private void creatControls() {
		HorizontalPanel mainPanel = new HorizontalPanel();
		VerticalPanel leftVpPanel = new VerticalPanel();
		viewTypeCombo();
		DynamicForm viewform = new DynamicForm();
		viewform.setFields(activeInActiveSelect);
		leftVpPanel.add(viewform);
		viewform.getElement().getParentElement()
				.setAttribute("margin-top", "-8px");
		leftVpPanel.setCellHorizontalAlignment(viewform, ALIGN_RIGHT);
		viewform.setNumCols(2);
		custGrid = new CustomersListGrid();
		custGrid.init();
		initCustomersListGrid();
		leftVpPanel.add(custGrid);
		leftVpPanel.setSpacing(5);

		custGrid.getElement().getParentElement().setAttribute("width", "15%");
		custGrid.setStyleName("cusotmerCentrGrid");
		VerticalPanel rightVpPanel = new VerticalPanel();
		detailsPanel = new CustomerDetailsPanel(selectedCustomer);
		rightVpPanel.add(detailsPanel);
		custGrid.setCustomerSelectionListener(new CustomerSelectionListener() {
			@Override
			public void cusotmerSelected() {
				OncusotmerSelected();
			}
		});
		transactionViewSelectCombo();
		transactionViewTypeSelectCombo();
		transactionDateRangeSelector();
		DynamicForm transactionViewform = new DynamicForm();

		transactionViewform.setNumCols(6);
		transactionViewform.setFields(trasactionViewSelect,
				trasactionViewTypeSelect, dateRangeSelector);

		transactionGridpanel = new VerticalPanel();
		transactionGridpanel.add(transactionViewform);
		custHistoryGrid = new CustomerTransactionsHistoryGrid();
		custHistoryGrid.init();
		custHistoryGrid.addEmptyMessage(messages.pleaseSelectAnyPayee(Global
				.get().Customer()));
		rightVpPanel.add(transactionGridpanel);
		rightVpPanel.add(custHistoryGrid);
		custHistoryGrid.setHeight("494px");
		mainPanel.add(leftVpPanel);
		mainPanel.add(rightVpPanel);
		add(mainPanel);

	}

	private void viewTypeCombo() {
		if (activeInActiveSelect == null) {
			activeInActiveSelect = new SelectCombo(messages.show());
			activeInActiveSelect.setHelpInformation(true);

			List<String> activetypeList = new ArrayList<String>();
			activetypeList.add(messages.active());
			activetypeList.add(messages.inActive());
			activeInActiveSelect.initCombo(activetypeList);
			activeInActiveSelect.setComboItem(messages.active());
			activeInActiveSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

						@Override
						public void selectedComboBoxItem(String selectItem) {
							if (activeInActiveSelect.getSelectedValue() != null) {
								if (activeInActiveSelect.getSelectedValue()
										.toString()
										.equalsIgnoreCase(messages.active())) {
									refreshActiveinActiveList(true);
								} else {
									refreshActiveinActiveList(false);

								}

							}
						}

					});
		}
	}

	private void refreshActiveinActiveList(boolean isActivelist) {
		custGrid.setSelectedCustomer(null);
		detailsPanel.custname.setText(messages.noPayeeSelected(Global.get()
				.Customer()));
		this.selectedCustomer = null;
		OncusotmerSelected();
		isActiveAccounts = isActivelist;
		initCustomersListGrid();
	}

	private void transactionViewSelectCombo() {
		if (trasactionViewSelect == null) {
			trasactionViewSelect = new SelectCombo(messages.currentView());
			trasactionViewSelect.setHelpInformation(true);

			List<String> transactionTypeList = new ArrayList<String>();
			transactionTypeList.add(messages.allTransactions());
			transactionTypeList.add(messages.invoices());
			transactionTypeList.add(messages.quotes());
			if (getCompany().getPreferences().isDelayedchargesEnabled()) {
				transactionTypeList.add(messages.Charges());
				transactionTypeList.add(messages.credits());
			}
			transactionTypeList.add(messages.allcashSales());
			transactionTypeList.add(messages.receivedPayments());
			transactionTypeList.add(messages.CustomerCreditNotes());
			transactionTypeList.add(messages.customerRefunds(Global.get()
					.Customer()));
			trasactionViewSelect.initCombo(transactionTypeList);
			trasactionViewSelect.setComboItem(messages.allTransactions());
			trasactionViewSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

						@Override
						public void selectedComboBoxItem(String selectItem) {
							if (trasactionViewSelect.getSelectedValue() != null) {
								getMessagesList();
								callRPC();
							}

						}

					});
		}

	}

	private void transactionViewTypeSelectCombo() {
		if (trasactionViewTypeSelect == null) {
			trasactionViewTypeSelect = new SelectCombo(messages.type());
			trasactionViewTypeSelect.setHelpInformation(true);
			getMessagesList();
			trasactionViewTypeSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

						@Override
						public void selectedComboBoxItem(String selectItem) {
							if (trasactionViewTypeSelect.getSelectedValue() != null) {
								callRPC();
							}

						}

					});
		}

	}

	private void getMessagesList() {
		transactiontypebyStatusMap = new HashMap<Integer, String>();
		String selectedValue = trasactionViewSelect.getSelectedValue();
		if (selectedValue.equalsIgnoreCase(messages.allTransactions())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_TRANSACTIONS,
					messages.allTransactions());
		} else if (selectedValue.equalsIgnoreCase(messages.invoices())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_INVOICES,
					messages.getallInvoices());
			transactiontypebyStatusMap.put(TransactionHistory.OPENED_INVOICES,
					messages.getOpendInvoices());
			transactiontypebyStatusMap.put(
					TransactionHistory.OVER_DUE_INVOICES,
					messages.overDueInvoices());
		} else if (selectedValue.equalsIgnoreCase(messages.allcashSales())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_CASHSALES,
					messages.all() + " " + messages.allcashSales());

		} else if (selectedValue.equalsIgnoreCase(messages.quotes())) {

			transactiontypebyStatusMap.put(TransactionHistory.ALL_QUOTES,
					messages.allQuotes());

		} else if (selectedValue.equalsIgnoreCase(messages.credits())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_CREDITS,
					messages.allCredits());
		} else if (selectedValue.equalsIgnoreCase(messages.Charges())) {

			transactiontypebyStatusMap.put(TransactionHistory.ALL_CHARGES,
					messages.allCahrges());

		} else if (selectedValue.equalsIgnoreCase(messages.receivedPayments())) {
			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_RECEIVEDPAYMENTS, messages.all()
							+ " " + messages.receivedPayments());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_CASH,
					messages.receivedPaymentsbyCash());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_CHEQUE,
					messages.receivedPaymentsbyCheque());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_CREDITCARD,
					messages.receivedPaymentsbyCreditCard());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_DIRECT_DEBIT,
					messages.receivedPaymentsbyDirectDebit());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_MASTERCARD,
					messages.receivedPaymentsbyMastercard());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_ONLINE,
					messages.receivedPaymentsbyOnlineBanking());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_STANDING_ORDER,
					messages.receivedPaymentsbyStandingOrder());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_MAESTRO,
					messages.receivedPaymentsbySwitchMaestro());

		} else if (selectedValue.equalsIgnoreCase(messages
				.CustomerCreditNotes())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_CREDITMEMOS,
					messages.allCreditMemos());
			// transactiontypebyStatusMap.put(
			// TransactionHistory.OPEND_CREDITMEMOS,
			// messages.openCreditMemos());

		} else if (selectedValue.equalsIgnoreCase(messages
				.customerRefunds(Global.get().Customer()))) {
			transactiontypebyStatusMap.put(
					TransactionHistory.REFUNDS_BY_CREDITCARD,
					messages.refundsByCreditCard());
			transactiontypebyStatusMap.put(TransactionHistory.REFUNDS_BYCASH,
					messages.refundsByCash());
			transactiontypebyStatusMap.put(TransactionHistory.REFUNDS_BYCHEQUE,
					messages.refundsByCheck());

			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_CUSTOMER_REFUNDS,
					messages.allCustomerRefunds());
		}
		List<String> typeList = new ArrayList<String>(
				transactiontypebyStatusMap.values());
		Collections.sort(typeList, new Comparator<String>() {

			@Override
			public int compare(String entry1, String entry2) {
				return entry1.compareTo(entry2);
			}

		});
		trasactionViewTypeSelect.initCombo(typeList);
		trasactionViewTypeSelect.setComboItem(typeList.get(0));
	}

	private void initCustomersListGrid() {
		Accounter.createHomeService().getPayeeList(ClientPayee.TYPE_CUSTOMER,
				isActiveAccounts, 0, -1,
				new AsyncCallback<PaginationList<PayeeList>>() {

					@Override
					public void onSuccess(PaginationList<PayeeList> result) {
						custGrid.removeAllRecords();
						if (result.size() == 0) {
							custGrid.addEmptyMessage(messages
									.youDontHaveAny(Global.get().Customers()));
						} else {
							custGrid.setRecords(result);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void OncusotmerSelected() {
		this.selectedCustomer = custGrid.getSelectedCustomer();
		detailsPanel.showCustomerDetails(selectedCustomer);
		custHistoryGrid.setSelectedCustomer(selectedCustomer);
		MainFinanceWindow.getViewManager().updateButtons();
		callRPC();
	}

	@Override
	public void onClose() {
		Accounter.showError("");
		super.onClose();

	}

	@Override
	protected String getViewTitle() {
		return messages.payees(Global.get().Customer());
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		Iterator<PayeeList> iterator = listOfCustomers.iterator();
		while (iterator.hasNext()) {
			PayeeList next = iterator.next();
			if (next.getID() == result.getID()) {
				iterator.remove();
			}
		}
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	public void setSelectedCustomer(ClientCustomer selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public ClientCustomer getSelectedCustomer() {
		return selectedCustomer;
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
	}

	@Override
	protected void callRPC() {
		custHistoryGrid.clear();
		if (selectedCustomer != null) {
			Accounter.createReportService().getCustomerTransactionsList(
					selectedCustomer.getID(), getTransactionType(),
					getTransactionStatusType(), getStartDate(), getEndDate(),
					new AsyncCallback<ArrayList<TransactionHistory>>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(
								ArrayList<TransactionHistory> result) {
							records = result;
							custHistoryGrid.clear();
							if (records != null) {
								custHistoryGrid.addRecords(records);
							}
							if (records.size() == 0) {
								custHistoryGrid.addEmptyMessage(messages
										.thereAreNo(messages.transactions()));
							}
						}
					});

		} else {
			custHistoryGrid.clear();
			custHistoryGrid.addEmptyMessage(messages.thereAreNo(messages
					.transactions()));
		}
	}

	private int getTransactionStatusType() {
		if (trasactionViewTypeSelect.getSelectedValue() != null) {
			Set<Integer> keySet = transactiontypebyStatusMap.keySet();
			for (Integer integerKey : keySet) {
				String entrystring = transactiontypebyStatusMap.get(integerKey);
				if (trasactionViewTypeSelect.getSelectedValue().equals(
						entrystring)) {
					return integerKey;
				}
			}
		}
		return TransactionHistory.ALL_INVOICES;

	}

	private int getTransactionType() {
		String selectedValue = trasactionViewSelect.getSelectedValue();
		if (selectedValue.equalsIgnoreCase(messages.invoices())) {

			return TYPE_INVOICE;
		} else if (selectedValue.equalsIgnoreCase(messages.allcashSales())) {
			return TYPE_CAHSSALE;
		} else if (selectedValue.equalsIgnoreCase(messages.receivedPayments())) {
			return TYPE_RECEIVE_PAYMENT;
		} else if (selectedValue.equalsIgnoreCase(messages
				.CustomerCreditNotes())) {
			return TYPE_CREDITNOTE;
		} else if (selectedValue.equalsIgnoreCase(messages.quotes())
				|| selectedValue.equalsIgnoreCase(messages.credits())
				|| selectedValue.equalsIgnoreCase(messages.Charges())) {
			return TYPE_ESTIMATE;
		} else if (selectedValue.equalsIgnoreCase(messages
				.customerRefunds(Global.get().Customer()))) {
			return TYPE_CUSTOMER_REFUND;
		}
		return TYPE_ALL_TRANSACTION;

	}

	@Override
	public void restoreView(ClientPayee customer) {
		this.selectedCustomer = (ClientCustomer) customer;
		if (this.selectedCustomer != null) {
			custGrid.setSelectedCustomer(selectedCustomer);
			OncusotmerSelected();
		}
	}

	@Override
	public ClientCustomer saveView() {
		return selectedCustomer;
	}
}
