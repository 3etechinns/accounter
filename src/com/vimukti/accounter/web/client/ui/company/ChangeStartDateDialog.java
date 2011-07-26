package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

@SuppressWarnings("unchecked")
public class ChangeStartDateDialog extends BaseDialog {

	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);

	public ChangeStartDateDialog(String title, String desc) {
		super(title, desc);
		createControls();
		center();

	}

	private void createControls() {

		// setHeight(150);

		VerticalPanel bodyLayout = new VerticalPanel();
		DynamicForm form = new DynamicForm();
		// form.setWidth(250);
		DateItem dateItem = new DateItem();
		dateItem.setTitle(companyConstants.startDate());
		dateItem.setRequired(true);
		// dateItem.setUseTextField(true);
		form.setFields(dateItem);
		bodyLayout.add(form);
		setBodyLayout(bodyLayout);

	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		return null;
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return Accounter.getCompanyMessages().changeStartDate();
	}

}
