package com.vimukti.accounter.web.client.ui;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author venki.p
 * 
 */

public class AddressDialog extends BaseDialog {

	ClientAddress addNew;
	TextAreaItem textAreaItem;
	private LinkedHashMap<Integer, ClientAddress> allAddresses;
	private TextItem address1;
	private TextItem address2;
	private TextItem city;
	private TextItem state;
	private TextItem country;
	private TextItem zip;
	private String addressType;

	public AddressDialog(String title, String description,
			final TextAreaItem textAreaItem, final String addressType,
			final LinkedHashMap<Integer, ClientAddress> allAddresses) {
		super(Accounter.constants().address(), "");
		this.addressType = addressType;
		this.allAddresses = allAddresses;
		createControls(textAreaItem, allAddresses);

	}

	protected void createControls(TextAreaItem textAreaItem,
			LinkedHashMap<Integer, ClientAddress> allAddresses) {

		this.textAreaItem = textAreaItem;
		ClientAddress add = null;
		if (allAddresses != null) {
			add = allAddresses.get(UIUtils.getAddressType(addressType));
		}
		// final TextItem street = new TextItem(Accounter
		// .constants().streetName());
		address1 = new TextItem(Accounter.constants().address1());
		address1.setHelpInformation(true);

		address2 = new TextItem(Accounter.constants().address2());
		address2.setHelpInformation(true);

		city = new TextItem(Accounter.constants().city());
		city.setHelpInformation(true);

		state = new TextItem(Accounter.constants().state());
		state.setHelpInformation(true);

		country = new TextItem(Accounter.constants().country());
		country.setHelpInformation(true);

		zip = new TextItem(Accounter.constants().postalCode());
		zip.setHelpInformation(true);
		// country.setWidth(100);

		// country.setValueMap(Accounter.constants().uk(),
		// Accounter.constants().india(),
		// Accounter.constants().us());
		// Iterator itr=countryList.iterator();
		// while(itr.hasNext())
		// // country.setValue(itr.next());
		// String[] countryList = (String[]) getCountryList().toArray();
		// country.setCountryList();

		// country.setValueMap(getCountryList());
		// country.setDefaultValue(Accounter.constants().UK());

		if (add != null) {
			if (add.getAddress1() != null)
				address1.setValue(add.getAddress1());
			if (add.getStreet() != null)
				address2.setValue(add.getStreet());
			if (add.getCity() != null)
				city.setValue(add.getCity());
			if (add.getStateOrProvinence() != null)
				state.setValue(add.getStateOrProvinence());
			if (add.getZipOrPostalCode() != null)
				zip.setValue(add.getZipOrPostalCode());
			if (add.getCountryOrRegion() != null)
				country.setValue(add.getCountryOrRegion());
		}

		DynamicForm AddressForm = new DynamicForm();
		AddressForm.setCellSpacing(10);
		AddressForm.setWidth("100%");
		AddressForm.setFields(address1, address2, city, state, zip, country);
		addNew = add;

		VerticalPanel v1 = new VerticalPanel();

		v1.add(AddressForm);
		v1.setHeight("100%");
		v1.setWidth("100%");

		setBodyLayout(v1);

		center();

	}

	public void close() {
		removeFromParent();
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

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

	public String[] getCountryList() {
		// List list = new ArrayList();
		String[] list = { Accounter.constants().australia(),
				Accounter.constants().belgium(),
				Accounter.constants().canada(), Accounter.constants().cyprus(),
				Accounter.constants().france(),
				Accounter.constants().germany(),
				Accounter.constants().greece(), Accounter.constants().india(),
				Accounter.constants().ireland(), Accounter.constants().italy(),
				Accounter.constants().kenya(), Accounter.constants().malta(),
				Accounter.constants().mauritius(),
				Accounter.constants().mozabique(),
				Accounter.constants().netherlands(),
				Accounter.constants().newZeland(),
				Accounter.constants().nigeria(),
				Accounter.constants().pakistan(),
				Accounter.constants().portugal(),
				Accounter.constants().southAfrica(),
				Accounter.constants().spain(),
				Accounter.constants().switzerland(),
				Accounter.constants().thailand(), Accounter.constants().uk(),
				Accounter.constants().usa(), Accounter.constants().others() };

		return list;
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		String toBeSet = "";
		int isEmptyCounter = 0;

		ClientAddress value = new ClientAddress();
		value.setType(UIUtils.getAddressType(addressType));
		if (address1.getValue().toString().trim() != null
				&& !address1.getValue().toString().trim().isEmpty()) {
			isEmptyCounter++;
			toBeSet = address1.getValue().toString().trim() + "\n";
			value.setAddress1(address1.getValue().toString().trim());
		}
		if (address2.getValue().toString().trim() != null
				&& !address2.getValue().toString().trim().isEmpty()) {
			isEmptyCounter++;
			toBeSet += address2.getValue().toString().trim() + "\n";
			value.setStreet(address2.getValue().toString().trim());
		}

		if (city.getValue() != null
				&& !city.getValue().toString().trim().isEmpty()) {
			isEmptyCounter++;
			toBeSet += city.getValue().toString().trim() + "\n";
			value.setCity(city.getValue().toString().trim());
		}

		if (state.getValue() != null
				&& !state.getValue().toString().trim().isEmpty()) {
			isEmptyCounter++;
			toBeSet += state.getValue().toString().trim() + "\n";
			value.setStateOrProvinence(state.getValue().toString().trim());
		}

		if (zip.getValue() != null && !zip.getValue().toString().isEmpty()) {
			isEmptyCounter++;
			toBeSet += zip.getValue().toString().trim() + "\n";
			value.setZipOrPostalCode(zip.getValue().toString().trim());
		}

		if (country.getValue() != null
				&& !country.getValue().toString().trim().isEmpty()) {
			isEmptyCounter++;
			toBeSet += country.getValue().toString().trim();
			value.setCountryOrRegion(country.getValue().toString().trim());
		}

		if (toBeSet != null && !toBeSet.isEmpty() && isEmptyCounter != 0) {
			textAreaItem.setValue(toBeSet);
			allAddresses.put(UIUtils.getAddressType(addressType), value);
		} else {
			result.addError(this, AccounterErrorType.SHOULD_NOT_EMPTY);
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		return true;
	}

}
