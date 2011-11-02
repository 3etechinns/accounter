package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class CompanyEmailOption extends AbstractPreferenceOption {

	private static CompanyEmailOptionUiBinder uiBinder = GWT
			.create(CompanyEmailOptionUiBinder.class);
	@UiField
	Label companyEmailHeaderLabel;
	@UiField
	TextBox companyEmailTextBox;
	@UiField
	Label emailIDDescriptionLabel;

	// @UiField
	// CheckBox customersEmailAddressCheckBox;
	// @UiField
	// VerticalPanel companyLegalAddressPanel;
	// @UiField
	// Label customerEmailHeaderLabel;
	// @UiField
	// TextBox customerEmailTextBox;

	interface CompanyEmailOptionUiBinder extends
			UiBinder<Widget, CompanyEmailOption> {
	}

	public CompanyEmailOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public void initData() {
		companyEmailTextBox.setText(getCompanyPreferences().getCompanyEmail());
	}

	@Override
	public String getTitle() {
		return Accounter.constants().email();
	}

	@Override
	public void onSave() {
		
		if (UIUtils.isValidEmail(companyEmailTextBox.getValue()))
			getCompanyPreferences().setCompanyEmail(companyEmailTextBox.getValue());
		else {
			Accounter.showError(Accounter.constants().invalidEmail());
		}
		
	}

	@Override
	public String getAnchor() {
		return Accounter.constants().email();
	}

	@Override
	public void createControls() {
		emailIDDescriptionLabel.setText(constants.emailIdDescription());
		emailIDDescriptionLabel.setStyleName("organisation_comment");

		companyEmailHeaderLabel.setText(Accounter.constants().emailId());
		// customersEmailAddressCheckBox.setText(Accounter.constants()
		// .getCustomersEmailId());
		// customersEmailAddressCheckBox.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// companyLegalAddressPanel
		// .setVisible(customersEmailAddressCheckBox.getValue());
		//
		// }
		// });
		// customerEmailHeaderLabel.setText(Accounter.constants().customerID());

	}

}
