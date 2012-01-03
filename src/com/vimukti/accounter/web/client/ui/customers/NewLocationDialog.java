package com.vimukti.accounter.web.client.ui.customers;

import java.util.LinkedHashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressDialog;
import com.vimukti.accounter.web.client.ui.LocationGroupListDialog;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * Add new Location.
 * 
 * @author Lingarao.R
 * 
 */

public class NewLocationDialog extends BaseDialog<ClientLocation> {

	private ValueCallBack<ClientLocation> successCallback;
	private DynamicForm form;
	private TextItem locationName;
	ClientLocation clientLocation;
	LocationGroupListDialog locationGroupListDialog;
	private TextAreaItem companyAdressTextArea;
	private LinkedHashMap<Integer, ClientAddress> allAddresses;
	TextItem titleTextBox, companyNameTextBox, email, phone;
	ClientCompanyPreferences preferences;

	public NewLocationDialog(String title) {
		super(title, "");
		setWidth("400px");
		initilize();
		createControls();
		initData();
		center();
	}

	private void initData() {
		if (clientLocation != null) {
			if (clientLocation.getTitle() == null) {
				titleTextBox.setValue(preferences.getDisplayName());
			} else {
				titleTextBox.setValue(clientLocation.getTitle());
			}
			if (clientLocation.getCompanyName() == null) {
				companyNameTextBox.setValue(preferences.getDisplayName());
			} else {
				companyNameTextBox.setValue(clientLocation.getCompanyName());
			}
			if (clientLocation.getAddress() == null) {
				allAddresses.put(ClientAddress.TYPE_BILL_TO,
						preferences.getTradingAddress());
				companyAdressTextArea.setValue(getValidAddress(preferences
						.getTradingAddress()));
			} else {
				allAddresses.put(ClientAddress.TYPE_BILL_TO,
						clientLocation.getAddress());
				companyAdressTextArea.setValue(getValidAddress(clientLocation
						.getAddress()));
			}
			if (clientLocation.getEmail() == null) {
				email.setValue(preferences.getCompanyEmail());
			} else {
				email.setValue(clientLocation.getEmail());
			}
			if (clientLocation.getPhone() == null) {
				phone.setValue(preferences.getPhone());
			} else {
				phone.setValue(clientLocation.getPhone());
			}
		} else {
			titleTextBox.setValue(preferences.getDisplayName());
			companyNameTextBox.setValue(preferences.getDisplayName());
			allAddresses.put(ClientAddress.TYPE_BILL_TO,
					preferences.getTradingAddress());
			companyAdressTextArea.setValue(getValidAddress(preferences
					.getTradingAddress()));
			email.setValue(preferences.getCompanyEmail());
			phone.setValue(preferences.getPhone());
		}

	}

	public NewLocationDialog(LocationGroupListDialog locationGroupListDialog,
			String title, String string, ClientLocation clientLocation2) {
		super(title, "");
		this.clientLocation = clientLocation2;
		this.locationGroupListDialog = locationGroupListDialog;
		// setWidth("600px");
		initilize();
		createControls();
		initData();
		center();
	}

	private void initilize() {
		companyAdressTextArea = new TextAreaItem();
		allAddresses = new LinkedHashMap<Integer, ClientAddress>();
		preferences = Accounter.getCompany().getPreferences();
		titleTextBox = new TextItem(Global.get().messages().title());
		companyNameTextBox = new TextItem(Global.get().messages().companyName());
		email = new TextItem(Global.get().messages().emailId());
		phone = new TextItem(Global.get().messages().phone());
	}

	private void createControls() {
		form = new DynamicForm();
		form.setWidth("100%");

		locationName = new TextItem(messages.locationName(Global.get()
				.Location()));
		locationName.setHelpInformation(true);
		locationName.setRequired(true);
		VerticalPanel layout = new VerticalPanel();
		layout.add(form);
		form.setItems(locationName);
		if (clientLocation != null) {
			allAddresses = new LinkedHashMap<Integer, ClientAddress>();
			companyAdressTextArea.setHelpInformation(true);
			companyAdressTextArea.setWidth(100);
			companyAdressTextArea.setTitle(messages.address());
			companyAdressTextArea.setHelpInformation(true);
			companyAdressTextArea.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					new AddressDialog("", "", companyAdressTextArea, messages
							.billTo(), allAddresses);
				}
			});

			final DynamicForm addDynamicForm = new DynamicForm();
			final DynamicForm addComapnyNameForm = new DynamicForm();
			final DynamicForm addAddressForm = new DynamicForm();
			final DynamicForm addEmailForm = new DynamicForm();
			final DynamicForm addPhoneForm = new DynamicForm();
			if (this.clientLocation != null) {
				locationName.setValue(clientLocation.getName());
			}
			final CheckBox titleCheckBox = new CheckBox(
					messages.useDifferentTitle());
			final CheckBox companyNameCheckBox = new CheckBox(
					messages.useDifferentComapanyName());
			final CheckBox addressCheckBox = new CheckBox(
					messages.useDifferentAddrerss());
			final CheckBox emailCheckBox = new CheckBox(
					messages.useDifferentEmail());
			final CheckBox phoneCheckBox = new CheckBox(
					messages.useDifferentPhoneNumber());
			addDynamicForm.setItems(titleTextBox);
			addComapnyNameForm.setItems(companyNameTextBox);
			addAddressForm.setItems(companyAdressTextArea);
			addEmailForm.setItems(email);
			addPhoneForm.setItems(phone);
			titleCheckBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					addDynamicForm.setVisible(titleCheckBox.getValue());
				}
			});
			companyNameCheckBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					addComapnyNameForm.setVisible(companyNameCheckBox
							.getValue());
				}
			});
			addressCheckBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					addAddressForm.setVisible(addressCheckBox.getValue());
				}
			});
			emailCheckBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					addEmailForm.setVisible(emailCheckBox.getValue());
				}
			});
			phoneCheckBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					addPhoneForm.setVisible(phoneCheckBox.getValue());
				}
			});
			addDynamicForm.setVisible(titleCheckBox.getValue());
			addComapnyNameForm.setVisible(companyNameCheckBox.getValue());
			addAddressForm.setVisible(addressCheckBox.getValue());
			addEmailForm.setVisible(emailCheckBox.getValue());
			addPhoneForm.setVisible(phoneCheckBox.getValue());
			layout.add(titleCheckBox);
			layout.add(addDynamicForm);
			layout.add(companyNameCheckBox);
			layout.add(addComapnyNameForm);
			layout.add(addressCheckBox);
			layout.add(addAddressForm);
			layout.add(emailCheckBox);
			layout.add(addEmailForm);
			layout.add(phoneCheckBox);
			layout.add(addPhoneForm);
		}
		setBodyLayout(layout);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		if (locationGroupListDialog != null && !result.haveErrors())
			result.add(locationGroupListDialog.validate());
		return result;
	}

	@Override
	protected boolean onOK() {
		return locationGroupListDialog.onOK();
	}

	public void addSuccessCallback(
			ValueCallBack<ClientLocation> newContactHandler) {
		this.successCallback = newContactHandler;
	}

	public String getLocationGroupName() {
		return this.locationName.getValue().toString();
	}

	public ClientLocation createOrEditLocation() {
		if (clientLocation == null) {
			clientLocation = new ClientLocation();
			this.clientLocation.setLocationName(locationName.getValue()
					.toString());
			this.clientLocation.setTitle(titleTextBox.getValue());
			this.clientLocation.setCompanyName(companyNameTextBox.getValue());
			this.clientLocation.setAddress(allAddresses
					.get(ClientAddress.TYPE_BILL_TO));
			this.clientLocation.setEmail(email.getValue());
			this.clientLocation.setPhone(phone.getValue());
		} else {
			this.clientLocation.setLocationName(locationName.getValue()
					.toString());
			this.clientLocation.setTitle(titleTextBox.getValue());
			this.clientLocation.setCompanyName(companyNameTextBox.getValue());
			this.clientLocation.setAddress(allAddresses
					.get(ClientAddress.TYPE_BILL_TO));
			this.clientLocation.setEmail(email.getValue());
			this.clientLocation.setPhone(phone.getValue());
		}

		return this.clientLocation;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	protected String getValidAddress(ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion();
		}
		return toToSet;
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

}
