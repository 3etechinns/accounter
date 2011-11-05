package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.IssuePaymentView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author kumar kasimala
 * @modified by Ravi kiran.G
 */

public class IssuePaymentsAction extends Action {
	private IssuePaymentView view;

	public IssuePaymentsAction(String text) {
		super(text);
		this.catagory = Global.get().Vendor();
	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new IssuePaymentView(Accounter.constants()
						.selectPaymentsToIssue(), Accounter.messages()
						.selectPaymentMethod(Global.get().Account()));
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, IssuePaymentsAction.this);
			}
		});
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().issuePayment();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/issue_payment.png";
	// }

	@Override
	public String getHistoryToken() {
		return "issuePayments";
	}

	@Override
	public String getHelpToken() {
		return "issue-payment";
	}

}
