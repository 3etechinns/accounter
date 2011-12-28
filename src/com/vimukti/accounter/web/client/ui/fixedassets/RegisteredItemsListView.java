/**
 * 
 */
package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.RegisteredItemsListGrid;

/**
 * @author Murali.A
 * 
 */
public class RegisteredItemsListView extends BaseListView<ClientFixedAsset> {

	public RegisteredItemsListView() {
		this.isViewSelectRequired = false;
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getAddNewAction()
	 */
	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getNewFixedAssetAction();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getAddNewLabelString
	 */
	@Override
	protected String getAddNewLabelString() {
		return messages.addNewAsset();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getListViewHeading
	 */
	@Override
	protected String getListViewHeading() {
		return messages.registeredItemsList();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.BaseListView#initGrid()
	 */

	@Override
	protected void initGrid() {
		grid = new RegisteredItemsListGrid(false);
		grid.init();
		getRegistredFixedAssetsList();
		// grid.setRecords(getAssetsByType(ClientFixedAsset.STATUS_REGISTERED,
		// getCompany().getFixedAssets()));
		disableFilter();
	}

	private void getRegistredFixedAssetsList() {

		Accounter.createHomeService().getFixedAssetList(
				ClientFixedAsset.STATUS_REGISTERED,
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

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("isActive", isActiveAccounts);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(Map<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		// isActiveAccounts = (Boolean) viewDate.get("isActive");
		start = (Integer) viewDate.get("start");
		onPageChange(start, getPageSize());
		// if (isActiveAccounts) {
		// viewSelect.setComboItem(messages.active());
		// } else {
		// viewSelect.setComboItem(messages.inActive());
		// }

	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#initListCallback()
	 */
	@Override
	public void initListCallback() {

	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.IAccounterList#updateInGrid(
	 * java.lang.Object)
	 */
	@Override
	public void updateInGrid(ClientFixedAsset objectTobeModified) {

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
		return messages.registeredItemsList();
	}

}
