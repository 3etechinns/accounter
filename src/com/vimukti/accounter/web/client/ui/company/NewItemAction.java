package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ItemView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.SelectItemTypeDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal modified by Rajesh.A
 */

public class NewItemAction extends Action<ClientItem> {

	int type;
	private boolean forCustomer;
	public static final int TYPE_SERVICE = 1;
	public static final int NON_INVENTORY_PART = 3;

	public NewItemAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	public NewItemAction(String text, boolean forCustomer) {
		super(text);
		this.catagory = Accounter.constants().company();
		this.forCustomer = forCustomer;
	}

	public NewItemAction(String text, ClientItem item,
			AccounterAsyncCallback<Object> callback, boolean forCustomer) {
		super(text);
		this.catagory = Accounter.constants().company();
		this.forCustomer = forCustomer;
		// this.baseView = baseView;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final ClientItem data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				if (type == 0) {
					SelectItemTypeDialog dialog = new SelectItemTypeDialog(
							forCustomer);
					dialog.setDependent(isDependent);
					dialog.show();

				} else {

					ItemView view = new ItemView(data, type, forCustomer);
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewItemAction.this);
				}

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newItem();
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String getHistoryToken() {
		return "newItem";
	}
}
