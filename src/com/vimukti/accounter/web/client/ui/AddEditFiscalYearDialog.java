package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class AddEditFiscalYearDialog extends BaseDialog<ClientFiscalYear> {
	DateItem startDate;

	public AddEditFiscalYearDialog(String title, String desc,
			ClientFiscalYear fiscalYear) {
		super(title, desc);
		createControls(fiscalYear);
		center();
	}

	private void createControls(ClientFiscalYear fiscalYear) {

		// setHeight(250);
		// setWidth(400);

		startDate = new DateItem();
		startDate.setTitle(Accounter.messages().startOfFiscalYear());
		// startDate.setUseTextField(true);
		// int firstMonth =
		// FinanceApplication.getCompany().getFirstMonthOfFiscalYear();

		DateItem closeDate = new DateItem();
		closeDate.setTitle(Accounter.messages().closeOfFiscalYear());
		// closeDate.setUseTextField(true);
		VerticalPanel bodyLayout = new VerticalPanel();
		DynamicForm form = new DynamicForm();
		// form.setWidth(250);
		form.setFields(startDate, closeDate);
		bodyLayout.add(form);
		setBodyLayout(bodyLayout);

	}

	@Override
	public Object getGridColumnValue(ClientFiscalYear obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(AccounterException exception) {

	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setFocus() {
		startDate.setFocus();

	}

}
