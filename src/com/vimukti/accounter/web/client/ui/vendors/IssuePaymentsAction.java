package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.IssuePaymentDialog;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author kumar kasimala
 * @modified by Ravi kiran.G
 */

public class IssuePaymentsAction extends Action {

	public IssuePaymentsAction(String text) {
		super(text);
	}

	public IssuePaymentsAction(String text, String iconString) {
		super(text, iconString);
	}

	
	@Override
	public ParentCanvas getView() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		new IssuePaymentDialog(Accounter.constants()
				.selectPaymentsToIssue(), Accounter.constants()
				.selectPaymentMethod()).show();
		// MainFinanceWindow.getViewManager().showView(view, data, isDependent,
		// IssuePaymentsAction.this);
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().issuePayment();
	}

	@Override
	public String getImageUrl() {
		return "/images/issue_payment.png";
	}

	@Override
	public String getHistoryToken() {
		return "issuePayments";
	}

}
