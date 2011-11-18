package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class UsersListGrid extends BaseListGrid<ClientUserInfo> {
	private UsersView usersView;

	public UsersListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 5:
			return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return ListGrid.COLUMN_TYPE_TEXT;
		}
	}

	@Override
	public void addRecords(List<ClientUserInfo> list) {
		super.addRecords(list);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {

		case 0:
			return 150;
		case 1:
			return 150;
		case 2:
			return 150;
		case 4:
			return 100;
		case 5:
			return 25;
		default:
			return -1;
		}
	}

	@Override
	protected String[] getColumns() {

		return new String[] { Accounter.messages().firstName(),
				Accounter.messages().lastName(),
				Accounter.messages().userRole(),
				Accounter.messages().emailId(),
				Accounter.messages().status(), "" };
	}

	public void setUsersView(UsersView usersView) {
		this.usersView = usersView;
	}

	@Override
	protected Object getColumnValue(ClientUserInfo obj, int index) {
		switch (index) {
		case 0:
			return obj.getFirstName();
		case 1:
			return obj.getLastName();
		case 2:
			// if (obj.isCanDoUserManagement())
			// return obj.getUserRole() + " + Manage Users";
			// else
			return obj.getUserRole();
			// case 2:
			// // return obj.getStatus();
			// if (obj.isActive())
			// return "Active";
			// else
			// return "Pending";
		case 3:
			return obj.getEmail();
		case 4:
			return obj.isActive() ? Accounter.messages().active() : Accounter
					.messages().inActive();
		case 5:
			return Accounter.getFinanceImages().delete();
		default:
			return "";
		}
	}

	@Override
	protected String[] getSelectValues(ClientUserInfo obj, int index) {
		return null;
	}

	@Override
	protected boolean isEditable(ClientUserInfo obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(ClientUserInfo obj, int row, int index) {

		if (index == 5) {
			ClientUser user = Accounter.getUser();
			if (user.isCanDoUserManagement()) {
				if (user.getID() == obj.getID()) {
					Accounter.showInformation(Accounter.messages()
							.youCantDeleteYourSelf());
				} else {
					showWarnDialog(obj);
				}
			} else {
				Accounter.showInformation(Accounter.messages()
						.youdonthavepermissionstodeleteuser());
			}
		}
		/*
		 * if (!FinanceApplication.getUser().isCanDoUserManagement()) return; if
		 * (index == 4) { showWarnDialog(obj); }
		 */
	}

	@Override
	public void onDoubleClick(ClientUserInfo obj) {
		if (Accounter.getUser().isCanDoUserManagement()) {
			ActionFactory.getInviteUserAction().run(obj, false);
		}
	}

	@Override
	protected void onValueChange(ClientUserInfo obj, int index, Object value) {

	}

	@Override
	protected int sort(ClientUserInfo obj1, ClientUserInfo obj2, int index) {
		return 0;
	}

	@Override
	protected void executeDelete(ClientUserInfo object) {
		deleteUserObject(object);
	}

	@Override
	protected int[] setColTypes() {
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// if (caught instanceof InvalidOperationException)
		// Accounter.showError(((InvalidOperationException) caught)
		// .getMessage());
		// else
		// Accounter.showError(Accounter.constants().youcantDeleteThisUser());
		AccounterException accounterException = (AccounterException) caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
		caught.fillInStackTrace();
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// Accounter.showInformation("Deleted Successfully");
		deleteRecord(this.getSelection());
		usersView.deleteSuccess(result);
	}

}
