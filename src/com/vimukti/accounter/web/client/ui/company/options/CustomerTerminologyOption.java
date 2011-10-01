/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;

/**
 * @author vimukti36
 * 
 */
public class CustomerTerminologyOption extends AbstractPreferenceOption {

	private static CustomerTerminologyOptionUiBinder uiBinder = GWT
			.create(CustomerTerminologyOptionUiBinder.class);
	@UiField
	Label terminologyforCustomerLabel;
	@UiField
	RadioButton tenantsLabelRadioButton;
	@UiField
	RadioButton clientsRadioButton;
	@UiField
	RadioButton custimersRadioBuitton;
	@UiField
	RadioButton DonorsRadioButton;
	@UiField
	RadioButton guestardioButton;
	@UiField
	RadioButton membersRadioButton;
	@UiField
	RadioButton PatientRadioButton;
	@UiField
	Label customerDescriptionLabel;

	interface CustomerTerminologyOptionUiBinder extends
			UiBinder<Widget, CustomerTerminologyOption> {
	}

	public CustomerTerminologyOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public CustomerTerminologyOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void createControls() {
		terminologyforCustomerLabel.setText(messages.useTerminologyFor(Global
				.get().Customer()));
		customerDescriptionLabel.setText(constants.customerDescription());
		customerDescriptionLabel.setStyleName("organisation_comment");
		tenantsLabelRadioButton.setName(constants.group());
		tenantsLabelRadioButton.setHTML(constants.tenant());
		custimersRadioBuitton.setName(constants.group());
		custimersRadioBuitton.setHTML(constants.Customer());
		guestardioButton.setName(constants.group());
		guestardioButton.setHTML(constants.Guest());
		membersRadioButton.setName(constants.group());
		membersRadioButton.setHTML(constants.Member());
		PatientRadioButton.setName(constants.group());
		PatientRadioButton.setHTML(constants.Patient());
		clientsRadioButton.setName(constants.group());
		clientsRadioButton.setHTML(constants.Client());
		DonorsRadioButton.setName(constants.group());
		DonorsRadioButton.setHTML(constants.Donar());
	}

	@Override
	public String getTitle() {
		return "Customer Terminology";
	}

	@Override
	public void onSave() {

		if (tenantsLabelRadioButton.getValue()) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.TENANT);
		} else if (custimersRadioBuitton.getValue()) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.CUSTOMER);
		} else if (guestardioButton.getValue()) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.GUEST);
		} else if (membersRadioButton.getValue()) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.MEMBER);
		} else if (PatientRadioButton.getValue()) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.PATITEINT);
		} else if (clientsRadioButton.getValue()) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.CLIENT);
		} else {
			getCompanyPreferences().setReferCustomers(ClientCustomer.DONAR);
		}

	}

	@Override
	public String getAnchor() {
		return constants.company();
	}

	@Override
	public void initData() {

		int referCustomers = getCompanyPreferences().getReferCustomers();
		terminologyforCustomerLabel.setText(messages.useTerminologyFor(Global
				.get().Customer()));
		switch (referCustomers) {
		case ClientCustomer.TENANT:
			tenantsLabelRadioButton.setValue(true);
			break;
		case ClientCustomer.CUSTOMER:
			custimersRadioBuitton.setValue(true);
			break;
		case ClientCustomer.GUEST:
			guestardioButton.setValue(true);
			break;
		case ClientCustomer.MEMBER:
			tenantsLabelRadioButton.setValue(true);
			break;
		case ClientCustomer.PATITEINT:
			PatientRadioButton.setValue(true);
			break;
		case ClientCustomer.CLIENT:
			clientsRadioButton.setValue(true);
			break;
		case ClientCustomer.DONAR:
			DonorsRadioButton.setValue(true);
			break;
		}

	}

}
