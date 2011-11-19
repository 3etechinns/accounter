package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.vat.TAXAgencyView;

/**
 * 
 * @author Raj Vimal
 * 
 */
public class NewTAXAgencyAction extends Action<ClientTAXAgency> {

	protected TAXAgencyView view;

	public NewTAXAgencyAction(String text) {
		super(text);
		String flag;
		// if (Accounter.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US)
		// flag = messages.company();
		// else
		flag = Accounter.messages().tax();
		this.catagory = flag;
	}

	@Override
	public void run() {

		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = TAXAgencyView.getInstance();

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewTAXAgencyAction.this);

			}

		});
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newTaxAgency();
	}

	@Override
	public String getHistoryToken() {
		return "newTaxAgency";
	}

	@Override
	public String getHelpToken() {
		return "new_tax-agency";
	}
}
