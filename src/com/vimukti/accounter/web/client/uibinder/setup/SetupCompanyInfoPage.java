/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Administrator
 * 
 */
public class SetupCompanyInfoPage extends AbstractSetupPage {

	private static SetupCompanyInfoPageUiBinder uiBinder = GWT
			.create(SetupCompanyInfoPageUiBinder.class);
	@UiField
	VerticalPanel viewPanel;
	@UiField
	Grid companyInfoField;
	@UiField
	TextBox companyName;
	@UiField
	TextBox legalName;
	@UiField
	TextBox taxId;
	@UiField
	TextBox streetAddress1;
	@UiField
	TextBox streetAdress2;
	@UiField
	ListBox country;
	@UiField
	TextBox phone;
	@UiField
	TextBox fax;
	@UiField
	TextBox emailAddress;
	@UiField
	TextBox webSite;
	@UiField
	TextBox zip;
	@UiField
	ListBox stateListBox;
	@UiField
	TextBox cityTextBox;
	@UiField
	Label displayNameLabel;
	@UiField
	Label legalNameLabel;
	@UiField
	Label taxIDLabel;
	@UiField
	Label streetAddress2Label;
	@UiField
	Label streetAdreess1Label;
	@UiField
	Label cityLabel;
	@UiField
	Label stateLabel;
	@UiField
	Label zipLabel;
	@UiField
	Label countryLabel;
	@UiField
	Label phoneLabel;
	@UiField
	Label faxLabel;
	@UiField
	Label emailAdressLabel;
	@UiField
	Label webSiteLabel;
	@UiField
	HTML useFormat;
	@UiField
	Label headerLabel;
	private ClientCompany company;
	private ClientAddress address;
	private List<String> countries, states;

	interface SetupCompanyInfoPageUiBinder extends
			UiBinder<Widget, SetupCompanyInfoPage> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public SetupCompanyInfoPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(accounterConstants.enterYourCompanyInfo());

		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			taxIDLabel.setText(accounterConstants.taxId());
		} else if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			taxIDLabel.setText(accounterConstants.vatNo());
		} else if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_INDIA) {
			taxIDLabel.setText(Accounter.messages().panNumber(
					Global.get().Account()));
		}

		displayNameLabel.setText(accounterConstants.companyName());
		legalNameLabel.setText(accounterConstants.legalName());
		streetAddress2Label.setText(accounterConstants.streetAddress2());
		streetAdreess1Label.setText(accounterConstants.streetAddress1());
		cityLabel.setText(accounterConstants.city());
		stateLabel.setText(accounterConstants.state());
		zipLabel.setText(accounterConstants.zipCode());
		countryLabel.setText(accounterConstants.country());
		phoneLabel.setText(accounterConstants.phone());
		faxLabel.setText(accounterConstants.fax());
		emailAdressLabel.setText(accounterConstants.emailId());
		webSiteLabel.setText(accounterConstants.webSite());
		useFormat.setHTML("");
		Accounter.createGETService().getCountries(
				new AsyncCallback<List<String>>() {

					@Override
					public void onSuccess(List<String> result) {
						countries = result;
						for (int i = 0; i < result.size(); i++) {
							country.addItem(result.get(i));
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						Accounter.showError("Unable to get countries list");
					}
				});

		country.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (stateListBox.getItemCount() != 0) {
					for (int i = 0; i < stateListBox.getItemCount(); i++) {
						stateListBox.removeItem(i);
					}
				}

				Accounter.createGETService().getStates(
						country.getItemText(country.getSelectedIndex()),
						new AsyncCallback<List<String>>() {

							@Override
							public void onFailure(Throwable caught) {

							}

							@Override
							public void onSuccess(List<String> result) {
								states = result;
								for (int i = 0; i < result.size(); i++) {
									stateListBox.addItem(result.get(i));
								}
							}
						});
			}
		});
	}

	public void onLoad() {

		this.company = Accounter.getCompany();
		if (this.company != null) {
			companyName.setValue(company.getName());
			legalName.setValue(company.getTradingName());
			this.taxId.setValue(company.getTaxId());
			this.fax.setValue(company.getFax());
			this.phone.setValue(company.getPhone());
			this.webSite.setValue(company.getWebSite());
			this.emailAddress.setValue(company.getCompanyEmail());
			address = company.getTradingAddress();
			if (address != null) {
				this.streetAddress1.setValue(address.getAddress1());
				this.streetAdress2.setValue(address.getStreet());
				this.cityTextBox.setValue(address.getCity());
				if (address.getStateOrProvinence() != ""
						&& address.getStateOrProvinence() != null
						&& address.getStateOrProvinence().length() != 0) {
					this.stateListBox.setSelectedIndex(states.indexOf(address
							.getStateOrProvinence()));
				}
				if (address.getCountryOrRegion() != ""
						&& address.getCountryOrRegion() != null
						&& address.getStateOrProvinence().length() != 0) {
					this.country.setSelectedIndex(countries.indexOf(address
							.getCountryOrRegion()));
				}
			}
		}
	}

	@Override
	public void onSave() {

		ClientCompany clientCompany = new ClientCompany();
		address = new ClientAddress();
		clientCompany.id = company.id;
		clientCompany.setName(companyName.getValue().toString());
		clientCompany.setTradingName(legalName.getValue().toString());
		clientCompany.setPhone(phone.getValue().toString());
		clientCompany.setCompanyEmail(emailAddress.getValue().toString());
		clientCompany.setTaxId(taxId.getValue().toString());
		clientCompany.setFax(fax.getValue().toString());
		clientCompany.setWebSite(webSite.getValue().toString());
		address.setAddress1(streetAddress1.getValue());
		address.setStreet(streetAdress2.getValue());
		address.setCity(cityTextBox.getValue());
		if (stateListBox.getSelectedIndex() != -1) {
			address.setStateOrProvinence(states.get(stateListBox
					.getSelectedIndex()));
		}
		if (country.getSelectedIndex() != 1)
			address.setCountryOrRegion(countries.get(country.getSelectedIndex()));
		company.setTradingAddress(address);
		Accounter.setCompany(clientCompany);
	}

	@Override
	public boolean canShow() {
		return true;
	}

	public static boolean checkIfNotNumber(String in) {
		try {
			Integer.parseInt(in);

		} catch (NumberFormatException ex) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean validate() {
		if (companyName.getText().trim() != null
				|| companyName.getText().trim() != ""
				|| companyName.getText().trim().length() != 0) {
			if (companyName.getText().trim() != null
					|| companyName.getText().trim() != ""
					|| companyName.getText().trim().length() != 0) {
				if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
					if (taxId.getText().length() == 10) {
						if (taxId.getText().indexOf(2) == '-'
								&& checkIfNotNumber(taxId.getText().substring(
										0, 1))
								&& checkIfNotNumber(taxId.getText().substring(
										3, 9))) {
							return true;
						} else {
							Accounter.showError(accounterMessages
									.vatIDValidationDesc());
							return false;
						}
					} else {
						Accounter.showError(accounterMessages
								.vatIDValidationDesc());
						return false;
					}
				} else if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
					if (taxId.getText().length() == 10) {
						if (taxId.getText().indexOf(2) == '-'
								&& checkIfNotNumber(taxId.getText().substring(
										0, 1))
								&& checkIfNotNumber(taxId.getText().substring(
										3, 9))) {
							return true;
						} else {
							Accounter.showError(accounterMessages
									.vatIDValidationDesc());
							return false;
						}
					} else {
						Accounter.showError(accounterMessages
								.vatIDValidationDesc());
						return false;
					}
				} else if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_INDIA) {
					taxIDLabel.setText(Accounter.messages().panNumber(
							Global.get().account()));
					return true;
				} else {
					return true;
				}
			} else {
				return true;
			}

		} else {
			return false;
		}

	}

}
