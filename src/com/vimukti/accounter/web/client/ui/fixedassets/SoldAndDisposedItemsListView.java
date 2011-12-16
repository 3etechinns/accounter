/**
 * 
 */
package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.SoldAndDisposedItemsListGrid;

/**
 * @author Murali.A
 * 
 */
public class SoldAndDisposedItemsListView extends
		BaseListView<ClientFixedAsset> {

	public SoldAndDisposedItemsListView() {
		this.isViewSelectRequired = false;
	}

	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getNewFixedAssetAction();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getAddNewLabelString
	 * ()
	 */
	@Override
	protected String getAddNewLabelString() {
		return messages().addNewAsset();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getListViewHeading
	 * ()
	 */
	@Override
	protected String getListViewHeading() {
		return messages().soldAndDisposedItems();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.BaseListView#initGrid()
	 */

	@Override
	protected void initGrid() {
		grid = new SoldAndDisposedItemsListGrid(false);
		grid.init();
		getSoldOrDisposedFixedAssetsList();
		// grid.setRecords(getAssetsByType(
		// ClientFixedAsset.STATUS_SOLD_OR_DISPOSED, Accounter
		// .getCompany().getFixedAssets()));
		disableFilter();
	}

	private void getSoldOrDisposedFixedAssetsList() {

		Accounter.createHomeService().getFixedAssetList(
				ClientFixedAsset.STATUS_SOLD_OR_DISPOSED,
				new AsyncCallback<List<ClientFixedAsset>>() {

					@Override
					public void onSuccess(List<ClientFixedAsset> list) {
						grid.setRecords(list);
						grid.sort(10, false);
					}

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}
				});

	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#initListCallback()
	 */
	@Override
	public void initListCallback() {
		// TODO Auto-generated method stub

	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.IAccounterList#updateInGrid(
	 * java.lang.Object)
	 */

	@Override
	public void updateInGrid(ClientFixedAsset objectTobeModified) {
		grid.setRecords(getAssetsByType(ClientFixedAsset.STATUS_PENDING,
				getCompany().getFixedAssets()));
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return messages().soldDisposedItems();
	}

}
