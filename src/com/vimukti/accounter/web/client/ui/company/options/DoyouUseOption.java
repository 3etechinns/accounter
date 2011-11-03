package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;

public class DoyouUseOption extends AbstractPreferenceOption {

	private static DoyouUseOptionUiBinder uiBinder = GWT
			.create(DoyouUseOptionUiBinder.class);
	@UiField
	Label doYouLabelItem;
	@UiField
	CheckBox useCustomerNo;
	@UiField
	CheckBox useVendorNo;
	@UiField
	CheckBox useAccountNo;

	interface DoyouUseOptionUiBinder extends UiBinder<Widget, DoyouUseOption> {
	}

	public DoyouUseOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public void createControls() {
		doYouLabelItem.setText("Numbers");
		useCustomerNo.setText(messages.usePayeeId(Global.get().customer()));
		useVendorNo.setText(messages.usePayeeId(Global.get().vendor()));
		useAccountNo.setText(messages.useAccountNos(Global.get().account()));
	}

	@Override
	public String getTitle() {
		return "Numbers";
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setUseCustomerId(useCustomerNo.getValue());
		getCompanyPreferences().setUseVendorId(useVendorNo.getValue());
		getCompanyPreferences().setUseAccountNumbers(useAccountNo.getValue());
	}

	@Override
	public String getAnchor() {
		return "Numbers";
	}

	@Override
	public void initData() {
		useCustomerNo.setValue(getCompanyPreferences().getUseCustomerId());
		useVendorNo.setValue(getCompanyPreferences().getUseVendorId());
		useAccountNo.setValue(getCompanyPreferences().getUseAccountNumbers());

	}

}
