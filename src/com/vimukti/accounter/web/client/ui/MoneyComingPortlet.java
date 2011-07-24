package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.ColumnChart;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.BankingActionFactory;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.customers.InvoiceListView;

public class MoneyComingPortlet extends DashBoardPortlet {

	public double draftInvoiceAmount = 0.00;
	public double overDueInvoiceAmount = 0.00;
	public ClientAccount debitors;

	public Label draftLabel;
	public Label overDueLabel;
	public Label draftAmtLabel;
	public Label overDueAmtLabel;

	public MoneyComingPortlet(String title) {
		super(title);
	}

	@Override
	public String getGoToText() {
		return Accounter.getCompanyMessages().goToAccountReceivable();
	}

	@Override
	public void helpClicked() {

	}

	@Override
	public void goToClicked() {
		HistoryTokenUtils.setPresentToken(BankingActionFactory
				.getAccountRegisterAction(), debitors);
		BankingActionFactory.getAccountRegisterAction().run(debitors, true);
	}

	@Override
	public Cursor getTitleCursor() {
		return Cursor.POINTER;
	}

	@Override
	public TextDecoration getTitleDecoration() {
		return TextDecoration.UNDERLINE;
	}

	@Override
	public void createBody() {
		updateDebitorsAccount();

		HorizontalPanel hPanel = new HorizontalPanel();
		FlexTable fTable = new FlexTable();

		AccounterButton addReceivableInvoiceBtn = new AccounterButton(
				Accounter.getCompanyMessages().addReceivableInvoice());
		addReceivableInvoiceBtn.addStyleName("addButtonPortlet");
		addReceivableInvoiceBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				HistoryTokenUtils.setPresentToken(CustomersActionFactory
						.getNewInvoiceAction(), null);
				CustomersActionFactory.getNewInvoiceAction().run(null, true);
			}
		});

		draftLabel = getLabel(Accounter.getCompanyMessages()
				.draftInvoices());
		overDueLabel = getLabel(Accounter.getCompanyMessages()
				.overDueInvoices());
		overDueLabel.getElement().getStyle().setPaddingLeft(10, Unit.PX);

		updateAmounts();

		draftAmtLabel = getAmountLabel(String.valueOf(draftInvoiceAmount));
		overDueAmtLabel = getAmountLabel(String.valueOf(overDueInvoiceAmount));
		overDueAmtLabel.getElement().getStyle().setPaddingLeft(10, Unit.PX);

		fTable.setWidget(0, 0, draftLabel);
		fTable.setWidget(0, 1, overDueLabel);
		fTable.setWidget(1, 0, draftAmtLabel);
		fTable.setWidget(1, 1, overDueAmtLabel);
		fTable.addStyleName("fTablePortlet");

		if (Accounter.getUser().canDoInvoiceTransactions()) {
			hPanel.add(addReceivableInvoiceBtn);

			addReceivableInvoiceBtn.enabledButton();
		}

		hPanel.add(fTable);

		body.add(hPanel);

		AsyncCallback<List<Double>> callBack = new AsyncCallback<List<Double>>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter
							.showError("Failed to get Account Receivable chart values");
				}
			}

			@Override
			public void onSuccess(final List<Double> result) {
				if (result != null && result.size() > 0) {
					overDueInvoiceAmount = result.get(result.size() - 1);
					result.remove(result.size() - 1);
					overDueAmtLabel.setText(DataUtils
							.getAmountAsString(overDueInvoiceAmount));
				}
				if (result != null && result.size() > 0) {
					draftInvoiceAmount = result.get(result.size() - 1);
					result.remove(result.size() - 1);
					draftAmtLabel.setText(DataUtils
							.getAmountAsString(draftInvoiceAmount));
				}

				Runnable runnable = new Runnable() {

					@Override
					public void run() {
						GraphChart chart = new GraphChart();
						body.add(chart.createAccountReceivableChart(result));
					}
				};
				VisualizationUtils.loadVisualizationApi(runnable,
						ColumnChart.PACKAGE);

				// GraphChart chart = new GraphChart(
				// GraphChart.ACCOUNTS_RECEIVABLE_CHART_TYPE, UIUtils
				// .getMaxValue(result), 400, 150, result);
				// body.add(chart);
				// chart.update();
			}
		};
		Accounter.createHomeService().getGraphPointsforAccount(
				GraphChart.ACCOUNTS_RECEIVABLE_CHART_TYPE, 0, callBack);

	}

	private void updateDebitorsAccount() {
		List<ClientAccount> accounts = new ArrayList<ClientAccount>();
		if (getCompany() != null) {
			accounts = getCompany().getAccounts(
					ClientAccount.TYPE_OTHER_CURRENT_ASSET);
		}
		for (ClientAccount account : accounts) {
			if (account.getName().equals("Debtors")) {
				debitors = account;
				break;
			}
		}
	}

	Label getLabel(final String title) {
		final Label label = new Label(title);
		label.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				label.getElement().getStyle().setCursor(Cursor.POINTER);
				label.getElement().getStyle().setTextDecoration(
						TextDecoration.UNDERLINE);
			}
		});
		label.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				label.getElement().getStyle().setTextDecoration(
						TextDecoration.NONE);
			}
		});
		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				label.getElement().getStyle().setTextDecoration(
						TextDecoration.NONE);
				if (title.equals(Accounter.getCompanyMessages()
						.draftInvoices())) {
					HistoryTokenUtils.setPresentToken(CustomersActionFactory
							.getInvoicesAction(null), null);
					CustomersActionFactory.getInvoicesAction(null).run(null,
							true);
				} else {
					HistoryTokenUtils.setPresentToken(CustomersActionFactory
							.getInvoicesAction(InvoiceListView.OVER_DUE), null);
					CustomersActionFactory.getInvoicesAction(
							InvoiceListView.OVER_DUE).run(null, true);
				}

			}
		});
		return label;
	}

	Label getAmountLabel(String title) {
		Label label = new Label(title);
		label.addStyleName("amountLabelPortlet");
		return label;
	}

	private void updateAmounts() {

	}

	@Override
	public void titleClicked() {
		HistoryTokenUtils.setPresentToken(BankingActionFactory
				.getAccountRegisterAction(), debitors);
		BankingActionFactory.getAccountRegisterAction().run(debitors, true);
	}

	@Override
	public void refreshWidget() {
		this.body.clear();
		createBody();
	}
}
