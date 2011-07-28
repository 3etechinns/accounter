package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;


public class PayTypeListDialog extends GroupDialog {

	protected GroupDialogButtonsHandler groupDialogButtonHandler;

	public PayTypeListDialog(String title, String descript) {
		super(title, descript);

		initialise();
		center();
	}

	private void initialise() {

		setSize("30%", "50%");

		@SuppressWarnings("unused")
		final String title;
		@SuppressWarnings("unused")
		final String description;

		title = Accounter.constants().addOrEditPayType();
		description = Accounter.constants().toAddPayType();
		DialogGrid grid = getGrid();
		grid.addColumn(ListGrid.COLUMN_TYPE_TEXT, Accounter.constants()
				.active());
		grid.addColumn(ListGrid.COLUMN_TYPE_TEXT, Accounter.constants()
				.description());

		groupDialogButtonHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {

				// new AddOrEditPayTypeDialog(title, description).show();
			}

			public void onSecondButtonClick() {

				// new AddOrEditPayTypeDialog(title, description).show();
			}

			public void onThirdButtonClick() {

			}

		};
		addGroupButtonsHandler(groupDialogButtonHandler);
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {

		return null;
	}

	@Override
	public String[] setColumns() {

		return null;
	}

	@Override
	protected List getRecords() {

		return null;
	}

	public void deleteCallBack() {
	}

	public void addCallBack() {
	}

	public void editCallBack() {
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().addOrEditPayType();
	}

}
