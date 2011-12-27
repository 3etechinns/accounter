package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.core.RecentTransactionsList;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class RecentTransactionsPortlet extends Portlet {
	private RecentTransactionHistoryGrid grid;
	private SelectCombo limitCombo;
	private List<String> limitList;

	public RecentTransactionsPortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.recentTransactions(), "", "100%");
	}

	@Override
	public void createBody() {
		limitCombo = new SelectCombo(messages.view());
		limitList = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			limitList.add(messages.lastOf((i + 1) * 5));
		}
		limitCombo.initCombo(limitList);
		if (getConfiguration().getPortletMap().get(LIMIT) != null) {
			limitCombo.setSelected(getConfiguration().getPortletMap()
					.get(LIMIT));
		} else {
			limitCombo.setSelectedItem(0);
		}
		DynamicForm dynamicForm = new DynamicForm();
		dynamicForm.setFields(limitCombo);
		dynamicForm.addStyleName("list_combo_form");
		this.body.add(dynamicForm);
		limitCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						Map<String, String> portletMap = new HashMap<String, String>();
						portletMap.put(LIMIT, selectItem);
						getConfiguration().setPortletMap(portletMap);
						updateData();
					}
				});
		updateData();
	}

	private void updateData() {
		Accounter.createHomeService().getRecentTransactions(
				getLimit(limitCombo.getSelectedIndex()),
				new AsyncCallback<List<RecentTransactionsList>>() {

					@Override
					public void onSuccess(List<RecentTransactionsList> result) {
						if (grid != null) {
							body.remove(grid);
						}
						grid = new RecentTransactionHistoryGrid();
						grid.init();
						if (result != null && !(result.isEmpty())) {
							grid.setRecords(result);
						} else {
							grid.addEmptyMessage(messages
									.noTransactionsToshow());
						}
						body.add(grid);
						completeInitialization();
					}

					@Override
					public void onFailure(Throwable caught) {
						completeInitialization();
					}
				});
	}

	private int getLimit(int selectedIndex) {
		return (selectedIndex + 1) * 5;
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
