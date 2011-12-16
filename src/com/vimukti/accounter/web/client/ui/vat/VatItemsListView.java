package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.VATItemsListGrid;

public class VatItemsListView extends BaseListView<ClientTAXItem> {

	private List<ClientTAXItem> listOfVatItems;

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			return ActionFactory.getNewVatItemAction();
		} else {
			return null;
		}
	}

	@Override
	public void onSuccess(ArrayList<ClientTAXItem> result) {
		super.onSuccess(result);
		grid.sort(10, false);
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			return messages().addaNewTaxItem();
		} else {
			return "";
		}
	}

	@Override
	protected String getListViewHeading() {
		return messages().vatItemsList();
	}

	@Override
	protected void initGrid() {
		grid = new VATItemsListGrid(false);
		grid.addStyleName("listgrid-tl");
		grid.init();
		listOfVatItems = getCompany().getTaxItems();
		filterList(true);
	}

	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		for (ClientTAXItem item : listOfVatItems) {
			if (isActive) {
				if (item.isActive() == true)
					grid.addData(item);
			} else if (item.isActive() == false) {
				grid.addData(item);
			}

		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages().noRecordsToShow());
		}
	}

	@Override
	public void initListCallback() {

	}

	@Override
	public void updateInGrid(ClientTAXItem objectTobeModified) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {
		// currently not using

	}

	@Override
	public void print() {
		// currently not using

	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void printPreview() {
	}

	@Override
	protected String getViewTitle() {
		return messages().vatItemList();
	}

}
