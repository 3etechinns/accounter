/**
 * 
 */
package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * @author Murali.A
 * 
 */
public class ManageTAXCodesListAction extends Action {

	private ManageTAXCodesListView view;

	/**
	 * @param text
	 */
	public ManageTAXCodesListAction(String text) {
		super(text);
		this.catagory = Accounter.messages().tax();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.Action#getBigImage()
	 */
	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.Action#getSmallImage()
	 */
	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().manageSalesTaxGroup();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.Action#getView()
	 */

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.Action#run(java.lang.Object,
	 * java.lang.Boolean)
	 */
	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				view = new ManageTAXCodesListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ManageTAXCodesListAction.this);

			}

		});
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Manage_Sales_Tax_Group.png";
	// }

	@Override
	public String getHistoryToken() {
		return "vatCodes";
	}

	@Override
	public String getHelpToken() {
		return "manage-tax-code";
	}
}
