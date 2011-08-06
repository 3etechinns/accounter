package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AddItemTaxDialog extends BaseDialog {

	public TextItem taxText;
	public RadioGroupItem taxableRadio;
	public DynamicForm form;

	public AddItemTaxDialog(String title, String desc) {
		super(title, desc);
		initiliase();
		center();
	}

	private void initiliase() {
		taxText = new TextItem();
		taxText.setTitle(Accounter.constants().itemTax());
		taxText.setRequired(true);
		taxText.setColSpan(3);

		taxableRadio = new RadioGroupItem();
		taxableRadio.setColSpan(1);
		taxableRadio.setShowTitle(false);
		taxableRadio.setValueMap(Accounter.constants().taxable(), Accounter
				.constants().nonTaxable());

		form = new DynamicForm();
		form.setNumCols(4);
		form.setFields(taxText, taxableRadio);
		form.setSize("100%", "100%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "50%");
		// mainVLay.setTop(10);
		mainVLay.add(form);
		setSize("350", "200");
		setBodyLayout(mainVLay);

	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		// TODO Auto-generated method stub
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

}
