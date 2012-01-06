package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.ChalanDetailsListGrid;

public class ChalanDetailsListView extends BaseListView<ClientTDSChalanDetail> {

	private ArrayList<ClientTDSChalanDetail> listOfCHalans;

	public ChalanDetailsListView() {

	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void updateInGrid(ClientTDSChalanDetail objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		super.deleteFailed(caught);
		AccounterException accounterException = caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	protected void initGrid() {
		grid = new ChalanDetailsListGrid();
		grid.init();

	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getTDSChalanDetailsList(this);
	}

	@Override
	protected String getListViewHeading() {
		return "Chalan List View";
	}

	@Override
	protected Action getAddNewAction() {
		ActionFactory.getTDSChalanDetailsView().run();
		return null;

	}

	@Override
	protected String getAddNewLabelString() {
		return "Add Chalan Details";
	}

	@Override
	protected String getViewTitle() {
		return "Chalan List View";
	}

	@Override
	public void onSuccess(PaginationList<ClientTDSChalanDetail> result) {
		super.onSuccess(result);
		grid.removeLoadingImage();
		listOfCHalans = result;
		filterList(true);
		grid.setViewType(viewSelect.getValue().toString());
		grid.sort(10, false);
	}

	@Override
	protected void filterList(boolean isActive) {
		super.filterList(isActive);
		grid.removeAllRecords();

		for (ClientTDSChalanDetail chalans : listOfCHalans) {
			grid.addData(chalans);
		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
	}

}
