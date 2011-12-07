package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCompany;
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
	private String itemName;

	public NewItemAction(String text) {
		super(text);
		this.catagory = Accounter.messages().company();
	}

	public NewItemAction(String text, boolean forCustomer) {
		super(text);
		this.catagory = Accounter.messages().company();
		this.forCustomer = forCustomer;
	}

	public NewItemAction(String text, ClientItem item,
			AccounterAsyncCallback<Object> callback, boolean forCustomer) {
		super(text);
		this.catagory = Accounter.messages().company();
		this.forCustomer = forCustomer;
		// this.baseView = baseView;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final ClientItem data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				ClientCompany company = Accounter.getCompany();
				boolean sellServices = company.getPreferences()
						.isSellServices();
				boolean sellProducts = company.getPreferences()
						.isSellProducts();
				if (sellProducts && sellServices) {
					if (type == 0 && data == null) {
						SelectItemTypeDialog dialog = new SelectItemTypeDialog(
								forCustomer);
						dialog.setDependent(isDependent);
						dialog.setCallback(getCallback());
						dialog.setItemname(itemName);
						dialog.show();
					} else {
						if (data != null) {
							type = data.getType();
						}
						ItemView view = new ItemView(type, forCustomer);
						view.setItemName(itemName);
						MainFinanceWindow.getViewManager().showView(view, data,
								isDependent, NewItemAction.this);

					}
				} else if (sellServices) {
					ItemView view = new ItemView(ClientItem.TYPE_SERVICE,
							forCustomer);
					view.setItemName(itemName);
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewItemAction.this);
				} else if (sellProducts) {
					if (Accounter.getCompany().getPreferences()
							.isInventoryEnabled()) {
						if (type == 0 && data == null) {
							SelectItemTypeDialog dialog = new SelectItemTypeDialog(
									forCustomer);
							dialog.setDependent(isDependent);
							dialog.setCallback(getCallback());
							dialog.setItemname(itemName);
							dialog.show();
						} else {
							if (data != null) {
								type = data.getType();
							}
							ItemView view = new ItemView(type, forCustomer);
							view.setItemName(itemName);
							MainFinanceWindow.getViewManager().showView(view,
									data, isDependent, NewItemAction.this);
						}

					} else {
						ItemView view = new ItemView(
								ClientItem.TYPE_NON_INVENTORY_PART, forCustomer);
						view.setItemName(itemName);
						MainFinanceWindow.getViewManager().showView(view, data,
								isDependent, NewItemAction.this);
					}

				}

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
		if (forCustomer) {
			return "newItemCustomer";
		}
		return "newItemSupplier";

	}

	@Override
	public String getHelpToken() {
		return "customer-item";
	}

	public void setItemText(String text) {
		this.itemName = text;

	}
}
