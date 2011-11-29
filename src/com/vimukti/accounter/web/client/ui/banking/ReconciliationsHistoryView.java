/**
 * 
 */
package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.BankAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ReconciliationsHistoryView extends BaseView<ClientReconciliation> {

	private BankAccountCombo bankAccountsCombo;
	private ReconciliationsTable grid;
	private ClientBankAccount selectedBankAccount;

	private void createControls() {

		this.bankAccountsCombo = new BankAccountCombo(
				messages.selectBankAccount());
		this.bankAccountsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						bankAccountChanged((ClientBankAccount) bankAccountsCombo
								.getSelectedValue());
					}
				});
		this.grid = new ReconciliationsTable();
		grid.setWidth("100%");

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		DynamicForm form = new DynamicForm();
		form.setItems(bankAccountsCombo);
		Label label = new Label("Reconciliation List");
		label.setStyleName("bold");

		mainPanel.add(form);
		mainPanel.add(label);
		mainPanel.add(grid);
		// mainPanel.setCellHeight(grid, "200px");
		grid.getElement().getParentElement()
				.addClassName("recounciliation_grid");
		this.add(mainPanel);
		saveAndCloseButton.setVisible(false);
		saveAndNewButton.setVisible(false);
	}

	/**
	 * @param selectItem
	 */
	protected void bankAccountChanged(ClientBankAccount clientAccount) {
		this.selectedBankAccount = clientAccount;
		rpcGetService.getReconciliationsByBankAccountID(
				selectedBankAccount.getID(),
				new AccounterAsyncCallback<List<ClientReconciliation>>() {

					@Override
					public void onException(AccounterException exception) {

					}

					@Override
					public void onResultSuccess(
							List<ClientReconciliation> result) {
						grid.setData(result);
					}
				});
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	@Override
	public void initData() {
		ArrayList<ClientAccount> bankAccounts = Accounter.getCompany()
				.getAccounts(ClientAccount.TYPE_BANK);
		if (bankAccounts == null || bankAccounts.isEmpty()) {
			grid.setData(new ArrayList<ClientReconciliation>());
			return;
		}
		for (ClientAccount account : bankAccounts) {
			bankAccountsCombo.addItem(account);
		}
		bankAccountsCombo.setSelectedItem(0);
		bankAccountChanged((ClientBankAccount) bankAccountsCombo
				.getSelectedValue());
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {
		this.bankAccountsCombo.setFocus();

	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean canDelete() {
		return false;
	}
}
