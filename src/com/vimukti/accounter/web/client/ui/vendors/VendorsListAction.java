package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal
 */

public class VendorsListAction extends Action {

	public VendorsListAction(String text) {
		super(text);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return null;
	// }

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				try {
					// UIUtils.setCanvas(new VendorListView(),
					// getViewConfiguration());
					MainFinanceWindow.getViewManager().showView(
							new VendorListView(), null, false,
							VendorsListAction.this);

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Vendor list", t);
			}
		});

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vendors();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/vendors.png";
	// }

	@Override
	public String getHistoryToken() {

		return UIUtils.getVendorString("supplierList", "VendorList");
	}

}
