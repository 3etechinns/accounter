package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class ChangeStartDateDialog extends CustomDialog {

	public ChangeStartDateDialog(String title, String desc) {
		super(false, true);
		createControls();
		center();

	}

	private void createControls() {

		// setHeight(150);

		VerticalPanel bodyLayout = new VerticalPanel();
		DynamicForm form = new DynamicForm();
		// form.setWidth(250);
		DateItem dateItem = new DateItem();
		dateItem.setTitle(messages.startDate());
		dateItem.setRequired(true);
		// dateItem.setUseTextField(true);
		form.setFields(dateItem);
		bodyLayout.add(form);
		add(bodyLayout);

	}

	// @Override
	// public Object getGridColumnValue(IsSerializable obj, int index) {
	// return null;
	// }
	//
	// @Override
	// public void deleteFailed(AccounterException caught) {
	//
	// }
	//
	// @Override
	// public void deleteSuccess(IAccounterCore result){
	//
	// }
	//
	// @Override
	// public void saveSuccess(IAccounterCore object) {
	// }
	//
	// @Override
	// public void saveFailed(AccounterException exception) {
	//
	// }
	//
	//
	// @Override
	// protected boolean onOK() {
	// // TODO Auto-generated method stub
	// return false;
	// }

}
