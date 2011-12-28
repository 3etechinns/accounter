/**
 * 
 */
package com.vimukti.accounter.web.client.ui.fixedassets;

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
public class PendingItemsListAction extends Action {

	private PendingItemsListView view;

	public PendingItemsListAction() {
		super();
		this.catagory = messages.fixedAssets();

	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().pendingItemsList();
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				view = new PendingItemsListView();
				MainFinanceWindow.getViewManager().showView(view, null, false,
						PendingItemsListAction.this);

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Vendor View..", t);
			}
		});
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/pending_items_list.png";
	// }

	@Override
	public String getHistoryToken() {
		return "pendingItems";
	}

	@Override
	public String getHelpToken() {
		return "pending-item-list";
	}

	@Override
	public String getText() {
		return messages.pendingItemsList();
	}

}
