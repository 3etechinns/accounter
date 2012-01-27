package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientStatementRecord;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.MakeDepositView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class MakeDepositAction extends Action {
	protected MakeDepositView view;
	private double reconcileAmount;
	private ClientAccount reconcilationAccount;
	private ClientStatementRecord statementRecord;

	// private boolean isEdit;
	// private ClientMakeDeposit makeDeposit;

	public MakeDepositAction() {
		super();
		this.catagory = messages.banking();
	}

	public MakeDepositAction(ClientTransferFund makeDeposit,
			AccounterAsyncCallback<Object> callback) {
		super();
		this.catagory = messages.banking();

	}

	// For Reconciliation
	public MakeDepositAction(ClientAccount reconcilationAccount,
			double reconcileAmount, ClientStatementRecord statementRecord) {
		super();
		this.catagory = messages.banking();
		this.reconcilationAccount = reconcilationAccount;
		this.reconcileAmount = reconcileAmount;
		this.statementRecord = statementRecord;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				if (reconcilationAccount != null) {
					view = new MakeDepositView(reconcilationAccount,
							reconcileAmount, statementRecord);
				} else {
					view = MakeDepositView.getInstance();
				}

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, MakeDepositAction.this);
			}
		});
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().makeDeposit();
	}

	@Override
	public String getHistoryToken() {
		return "depositTransferFunds";
	}

	@Override
	public String getHelpToken() {
		return "transfer-funds";
	}

	@Override
	public String getText() {
		return messages.transferFund();
	}
}
