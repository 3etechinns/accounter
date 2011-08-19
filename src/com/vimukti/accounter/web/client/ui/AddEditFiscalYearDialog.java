package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class AddEditFiscalYearDialog extends BaseDialog {

	public AddEditFiscalYearDialog(String title, String desc,
			ClientFiscalYear fiscalYear) {
		super(title, desc);
		createControls(fiscalYear);
		center();
	}

	private void createControls(ClientFiscalYear fiscalYear) {

		// setHeight(250);
		// setWidth(400);

		DateItem startDate = new DateItem();
		startDate.setTitle(Accounter.constants().startOfFiscalYear());
		// startDate.setUseTextField(true);
		// int firstMonth =
		// FinanceApplication.getCompany().getFirstMonthOfFiscalYear();

		DateItem closeDate = new DateItem();
		closeDate.setTitle(Accounter.constants().closeOfFiscalYear());
		// closeDate.setUseTextField(true);
		VerticalPanel bodyLayout = new VerticalPanel();
		DynamicForm form = new DynamicForm();
		// form.setWidth(250);
		form.setFields(startDate, closeDate);
		bodyLayout.add(form);
		setBodyLayout(bodyLayout);

	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result){

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(AccounterException exception) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}

}
