package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;

/**
 * 
 * @author Venki.p
 * 
 */

public class AddressForm extends DynamicForm {
	LinkedHashMap<Integer, ClientAddress> allAddresses;
	ClientAddress toBeShown = null;
	public SelectCombo businessSelect;
	private TextAreaItem addrArea;

	public AddressForm(Set<ClientAddress> addresses) {

		@SuppressWarnings("unused")
		Label l1 = new Label(Accounter.constants()
				.enterAddress());
		allAddresses = new LinkedHashMap<Integer, ClientAddress>();

		setAddresses(addresses);

		businessSelect = new SelectCombo(Accounter
				.constants().address());
		businessSelect.setHelpInformation(true);
		// businessSelect.setWidth(85);
		businessSelect.getMainWidget().removeStyleName(
				Accounter.constants().gwtListBox());
		businessSelect.initCombo(new ClientAddress().getAddressTypes());

		businessSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						new AddressDialog("", "", addrArea, businessSelect
								.getSelectedValue(), allAddresses);
					}
				});

		addrArea = new TextAreaItem();
		addrArea.setHelpInformation(true);
		addrArea.setWidth(100);
		addrArea.setShowTitle(false);
		addrArea.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new AddressDialog("", "", addrArea, businessSelect
						.getSelectedValue(), allAddresses);

			}
		});

		addrArea.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				new AddressDialog("", "", addrArea, businessSelect
						.getSelectedValue(), allAddresses);

			}
		});

		if (toBeShown != null) {
			// businessSelect.setSelected(toBeShown.getAddressTypes().get(
			// toBeShown.getType()));
			businessSelect.setComboItem(UIUtils.getAddressesTypes(toBeShown
					.getType()));
			String toToSet = new String();
			if (toBeShown.getAddress1() != null
					&& !toBeShown.getAddress1().isEmpty()) {
				toToSet = toBeShown.getAddress1().toString() + "\n";
			}

			if (toBeShown.getStreet() != null
					&& !toBeShown.getStreet().isEmpty()) {
				toToSet += toBeShown.getStreet().toString() + "\n";
			}

			if (toBeShown.getCity() != null && !toBeShown.getCity().isEmpty()) {
				toToSet += toBeShown.getCity().toString() + "\n";
			}

			if (toBeShown.getStateOrProvinence() != null
					&& !toBeShown.getStateOrProvinence().isEmpty()) {
				toToSet += toBeShown.getStateOrProvinence() + "\n";
			}
			if (toBeShown.getZipOrPostalCode() != null
					&& !toBeShown.getZipOrPostalCode().isEmpty()) {
				toToSet += toBeShown.getZipOrPostalCode() + "\n";
			}
			if (toBeShown.getCountryOrRegion() != null
					&& !toBeShown.getCountryOrRegion().isEmpty()) {
				toToSet += toBeShown.getCountryOrRegion();
			}
			addrArea.setValue(toToSet);
		} else
			businessSelect.setDefaultToFirstOption(Boolean.TRUE);
		setGroupTitle(Accounter.constants().addresses());
		setNumCols(3);
		setFields(businessSelect, addrArea);
	}

	
	private void setAddresses(Set<ClientAddress> addresses) {
		if (addresses != null) {
			Iterator it = addresses.iterator();
			while (it.hasNext()) {
				ClientAddress add = (ClientAddress) it.next();
				if (add.getIsSelected()) {
					toBeShown = add;
				}
				allAddresses.put(add.getType(), add);
				// System.out.println("Existing Address  Type " + add.getType()
				// + " Street is " + add.getStreet() + " Is Selected"
				// + add.getIsSelected());
			}
		}
	}

	
	public Set<ClientAddress> getAddresss() {
		ClientAddress selectedAddress = allAddresses.get(UIUtils
				.getAddressType(businessSelect.getSelectedValue()));
		if (selectedAddress != null) {
			selectedAddress.setIsSelected(true);
			allAddresses.put(UIUtils.getAddressType(businessSelect
					.getSelectedValue()), selectedAddress);
		}
		Collection add = allAddresses.values();
		Set<ClientAddress> toBeSet = new HashSet<ClientAddress>();
		Iterator it = add.iterator();
		while (it.hasNext()) {
			ClientAddress a = (ClientAddress) it.next();
			toBeSet.add(a);
			// System.out.println("Sending Address  Type " + a.getType()
			// + " Street is " + a.getStreet() + " Is Selected"
			// + a.getIsSelected());
		}
		return toBeSet;
	}

	
	public List<ClientAddress> getAddresssList() {
		ClientAddress selectedAddress = allAddresses.get(businessSelect
				.getSelectedValue());
		if (selectedAddress != null) {
			selectedAddress.setIsSelected(true);
			allAddresses.put(UIUtils.getAddressType(businessSelect
					.getSelectedValue()), selectedAddress);
		}
		Collection add = allAddresses.values();
		List<ClientAddress> toBeSet = new ArrayList<ClientAddress>();
		Iterator it = add.iterator();
		while (it.hasNext()) {
			ClientAddress a = (ClientAddress) it.next();
			toBeSet.add(a);
			// System.out.println("Sending Address  Type " + a.getType()
			// + " Street is " + a.getStreet() + " Is Selected"
			// + a.getIsSelected());
		}
		return toBeSet;
	}

	public void setSelectValueMap(List<String> linkedHashMap) {
		this.businessSelect.initCombo(linkedHashMap);
	}

	public void setAddress(List<ClientAddress> addresses) {
		for (ClientAddress address : addresses) {
			this.allAddresses.put(address.getType(), address);
			addrArea.setValue(address.getAddress1() + "\n"
					+ address.getStreet() + "\n" + address.getCity() + "\n"
					+ address.getStateOrProvinence() + "\n"
					+ address.getZipOrPostalCode());

		}
	}

}
