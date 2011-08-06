package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class AccountRegisterAction extends Action {

	private ClientAccount account;

	public AccountRegisterAction(String text, ClientAccount account) {
		super(text);
		this.account = account;
		this.catagory = Accounter.constants().banking();
	}

	public AccountRegisterAction(String text) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	public void run() {

		runAsync(null, false);

	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isEditable) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				try {
					account = (ClientAccount) data;
					// if (account.getType() == ClientAccount.TYPE_CREDIT_CARD
					// || account.getType() == ClientAccount.TYPE_BANK) {
					// MainFinanceWindow.getViewManager().showView(
					// new AccountRegisterView(account), data,
					// isEditable, AccountRegisterAction.this);
					// } else {
					MainFinanceWindow.getViewManager().showView(
							new AccountRegisterOthersView(account), data,
							isEditable, AccountRegisterAction.this);
					// }

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Account Register....", t);
			}
		});
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */
	public void run(AccounterAsyncCallback<Object> AccounterAsyncCallback) {
	}

	@Override
	// public ParentCanvas getView() {
	// return null;
	// }
	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "accountRegister";
	}

}
