package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerContactRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.ShippingMethodRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.VendorGroupRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.util.CountryPreferenceFactory;
import com.vimukti.accounter.web.client.util.ICountryPreferences;

public class NewVendorCommand extends NewAbstractCommand {

	protected static final String ACCOUNT = "Account";
	protected static final String CREDIT_LIMIT = "Credit Limit";
	private static final String CST_NUM = "CST number";
	private static final String SERVICE_TAX_NUM = "Service tax registration no";
	private static final String TIN_NUM = "Taxpayer identification number";
	private static final String PAYMENT_METHOD = "paymentMethod";
	private static final String PAYMENT_TERMS = "paymentTerms";
	private static final String ACCOUNT_NO = "Account No";
	private static final String BANK_NAME = "Bank Name";
	private static final String BANK_BRANCH = "Bank Branch";
	private static final String VENDOR_GROUP = "Vendor Group";
	private static final String VAT_REGISTRATION_NUMBER = "Vat Registration Number";
	private static final String VENDOR_VAT_CODE = "Vendor Vat Code";
	private static final String VENDOR_NAME = "Vendor Name";
	private static final String VENDOR_NUMBER = "Vendor Number";
	private static final String VENDOR_SINCE = "Vendor Since";
	private static final String ACTIVE = "Active";
	private static final String TRACK_PAYMENTS_FOR_1099 = "Track payments for 1099";
	private static final String BALANCE = "Balance";
	private static final String BALANCE_AS_OF = "Balance As Of";
	private static final String PHONE = "Phone";
	private static final String FAX = "Fax";
	private static final String EMAIL = "E-mail";
	private static final String WEB_PAGE_ADDRESS = "Web Page Address";
	private static final String CONTACTS = "contact";

	protected static final String PRIMARY = "Primary";

	private static final String BILL_TO = "billTo";

	private static final String SHIPPING_METHODS = "shippingMethod";
	private static final String SHIP_TO = "shipTo";
	private ClientVendor vendor;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new NameRequirement(VENDOR_NAME, getMessages().pleaseEnter(
				getMessages().payeeName(Global.get().Vendor())), getMessages()
				.payeeName(Global.get().Vendor()), false, true) {
			@Override
			public void setValue(Object value) {
				if (vendor.getID() == 0
						&& NewVendorCommand.this.isVendorExists((String) value)) {
					addFirstMessage(getMessages().alreadyExist());
					addFirstMessage(getMessages().pleaseEnter(
							getMessages().payeeName(Global.get().Vendor())));
					return;
				}
				super.setValue(value);
			}
		});

		list.add(new NumberRequirement(VENDOR_NUMBER, getMessages()
				.pleaseEnter(getMessages().payeeNumber(Global.get().Vendor())),
				getMessages().payeeNumber(Global.get().Vendor()), false, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().getUseCustomerId()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new BooleanRequirement(ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return getMessages().active();
			}

			@Override
			protected String getFalseString() {
				return getMessages().inActive();
			}
		});

		list.add(new DateRequirement(VENDOR_SINCE, getMessages().pleaseEnter(
				getMessages().payeeSince(Global.get().Vendor())), getMessages()
				.payeeSince(Global.get().Vendor()), true, true));

		list.add(new AmountRequirement(BALANCE, getMessages().pleaseEnter(
				getMessages().payeeBalance(Global.get().Vendor())),
				getMessages().payeeBalance(Global.get().Vendor()), true, true));

		list.add(new DateRequirement(BALANCE_AS_OF, getMessages().pleaseEnter(
				getMessages().balanceAsOfDate()), getMessages()
				.balanceAsOfDate(), true, true));

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getMessages().billTo()), getMessages().billTo(), true, true));

		list.add(new AddressRequirement(SHIP_TO, getMessages().pleaseEnter(
				getMessages().shipTo()), getMessages().shipTo(), true, true));

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				true, true));

		list.add(new NumberRequirement(FAX, getMessages().pleaseEnter(
				getMessages().fax()), getMessages().fax(), true, true));

		list.add(new NameRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), true, true));

		list.add(new NameRequirement(WEB_PAGE_ADDRESS, getMessages()
				.pleaseEnter(getMessages().webPageAddress()), getMessages()
				.webPageAddress(), true, true));

		list.add(new BooleanRequirement(TRACK_PAYMENTS_FOR_1099, true) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getCompany().getCountry()
						.equals(CountryPreferenceFactory.UNITED_STATES)) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().trackPaymentsFor1099();
			}

			@Override
			protected String getFalseString() {
				return getMessages().dontTrackPaymentsFor1099();
			}
		});

		list.add(new CustomerContactRequirement(CONTACTS, getMessages()
				.pleaseSelect(getMessages().contact()), CONTACTS, true, true) {

			@Override
			protected List<ClientContact> getList() {
				List<ClientContact> contacts = getVendorContacts();
				return new ArrayList<ClientContact>(contacts);
			}
		});

		list.add(new AccountRequirement(ACCOUNT, getMessages().pleaseSelect(
				getMessages().Account()), getMessages().Account(), true, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().Account());
			}

			@Override
			protected List<Account> getLists(Context context) {
				return new ArrayList<Account>(context.getCompany()
						.getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Accounts());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().equals(name);
			}
		});

		list.add(new AmountRequirement(CREDIT_LIMIT, getMessages().pleaseEnter(
				getMessages().creditLimit()), getMessages().creditLimit(),
				true, true));

		list.add(new ShippingMethodRequirement(SHIPPING_METHODS, getMessages()
				.pleaseEnter(getMessages().shippingMethod()), getMessages()
				.shippingMethod(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getMessages().shippingMethod());
			}

			@Override
			protected List<ShippingMethod> getLists(Context context) {
				return new ArrayList<ShippingMethod>(context.getCompany()
						.getShippingMethods());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().shippingMethod());
			}
		});

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getMessages().paymentMethod()), getMessages()
				.paymentMethod(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages()
						.pleaseSelect(getMessages().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPaymentMethods();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().paymentMethod());
			}
		});

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseSelect(getMessages().paymentTerm()), getMessages()
				.paymentTerm(), true, true, null) {

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
			}
		});

		list.add(new NumberRequirement(ACCOUNT_NO, getMessages().pleaseEnter(
				getMessages().accountNumber()), getMessages().accountNumber(),
				true, true));

		list.add(new NameRequirement(BANK_NAME, getMessages().pleaseEnter(
				getMessages().bankName()), getMessages().bankName(), true, true));

		list.add(new NameRequirement(BANK_BRANCH, getMessages().pleaseEnter(
				getMessages().bankBranch()), getMessages().bankBranch(), true,
				true));

		list.add(new VendorGroupRequirement(VENDOR_GROUP, getMessages()
				.pleaseEnterName(
						getMessages().payeeGroup(Global.get().Vendor())),
				getMessages().payeeGroup(Global.get().Vendor()), true, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().payeeGroup(Global.get().Vendor()));
			}

			@Override
			protected List<VendorGroup> getLists(Context context) {
				return new ArrayList<VendorGroup>(context.getCompany()
						.getVendorGroups());
			}

			@Override
			protected boolean filter(VendorGroup e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new NumberRequirement(VAT_REGISTRATION_NUMBER, getMessages()
				.pleaseEnter(getMessages().vatRegistrationNumber()),
				getMessages().vatRegistrationNumber(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& context.getCompany().getCountryPreferences()
								.isVatAvailable()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new TaxCodeRequirement(VENDOR_VAT_CODE,
				"Please enter the tax code name", getMessages().taxCode(),
				true, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				return new ArrayList<TAXCode>(context.getCompany()
						.getTaxCodes());
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new NumberRequirement(CST_NUM, getMessages().pleaseEnter(
				getMessages().payeeNumber(Global.get().Customer())),
				getMessages().payeeNumber(Global.get().Customer()), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()
						&& context.getCompany().getCountryPreferences()
								.isSalesTaxAvailable()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new NumberRequirement(SERVICE_TAX_NUM, getMessages()
				.pleaseEnter(getMessages().serviceTax()), getMessages()
				.serviceTax(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()
						&& context.getCompany().getCountryPreferences()
								.isServiceTaxAvailable()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new NumberRequirement(TIN_NUM, getMessages().pleaseEnter(
				getMessages().tinNumber()), getMessages().tinNumber(), true,
				true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()
						&& context.getCompany().getCountryPreferences()
								.isTDSAvailable()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

	}

	protected boolean isVendorExists(String value) {
		return CommandUtils.isVendorExistsWithSameName(getCompany()
				.getVendors(), value);
	}

	protected List<ClientContact> getVendorContacts() {
		if (vendor.getID() != 0) {
			return new ArrayList<ClientContact>(vendor.getContacts());
		}
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {

		CompanyPreferences preferences = context.getCompany().getPreferences();
		ICountryPreferences countryPreferences = context.getCompany()
				.getCountryPreferences();

		String name = get(VENDOR_NAME).getValue();
		String number = null;
		if (preferences.getUseVendorId()) {
			number = get(VENDOR_NUMBER).getValue().toString();
		}
		List<ClientContact> contact = get(CONTACTS).getValue();
		boolean isActive = (Boolean) get(ACTIVE).getValue();
		ClientFinanceDate balancedate = get(BALANCE_AS_OF).getValue();
		double balance = get(BALANCE).getValue();
		ClientAddress billTo = get(BILL_TO).getValue();
		ClientAddress shipTo = get(SHIP_TO).getValue();
		Account account = get(ACCOUNT).getValue();
		String phoneNum = get(PHONE).getValue();
		String faxNum = get(FAX).getValue();
		String emailId = get(EMAIL).getValue();
		String webaddress = get(WEB_PAGE_ADDRESS).getValue();
		double creditLimit = Double
				.parseDouble((get(CREDIT_LIMIT).getValue() == null ? 0.0 : get(
						CREDIT_LIMIT).getValue().toString()).toString());
		String bankName = get(BANK_NAME).getValue();
		String bankAccountNum = get(ACCOUNT_NO).getValue();
		String bankBranch = get(BANK_BRANCH).getValue();
		ShippingMethod shippingMethod = get(SHIPPING_METHODS).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		PaymentTerms paymentTerms = get(PAYMENT_TERMS).getValue();

		String vatRegistredNum = get(VAT_REGISTRATION_NUMBER).getValue();

		String cstNum = get(CST_NUM).getValue();
		String serviceTaxNum = get(SERVICE_TAX_NUM).getValue();
		String tinNum = get(TIN_NUM).getValue();

		HashSet<ClientAddress> addresses = new HashSet<ClientAddress>();
		if (billTo != null && billTo.getAddress1() != "") {
			billTo.setType(ClientAddress.TYPE_BILL_TO);
			addresses.add(billTo);
		}
		if (shipTo != null && shipTo.getAddress1() != "") {
			shipTo.setType(ClientAddress.TYPE_SHIP_TO);
			addresses.add(shipTo);

		}
		vendor.setName(name);
		if (preferences.getUseVendorId())
			vendor.setVendorNumber(number);

		if (balancedate != null) {
			vendor.setBalanceAsOf(balancedate.getDate());
		}
		// vendor.set
		vendor.setContacts(new HashSet<ClientContact>(contact));
		vendor.setBalance(balance);
		if (!addresses.isEmpty())
			vendor.setAddress(addresses);
		vendor.setPhoneNo(phoneNum);
		vendor.setFaxNo(faxNum);
		vendor.setWebPageAddress(webaddress);
		vendor.setBankAccountNo(bankAccountNum);
		vendor.setBankBranch(bankBranch);
		vendor.setBankName(bankName);
		vendor.setEmail(emailId);

		if (context.getCompany().getCountry()
				.equals(CountryPreferenceFactory.UNITED_STATES)) {
			boolean isTrackPaymentsFor1099 = get(TRACK_PAYMENTS_FOR_1099)
					.getValue();
			vendor.setTrackPaymentsFor1099(isTrackPaymentsFor1099);
		}
		if (account != null)
			vendor.setExpenseAccount(account.getID());
		vendor.setCreditLimit(creditLimit);
		if (preferences.isDoProductShipMents() && shippingMethod != null)
			vendor.setShippingMethod(shippingMethod.getID());
		vendor.setPaymentMethod(paymentMethod);
		if (paymentTerms != null)
			vendor.setPaymentTerms(paymentTerms.getID());

		VendorGroup value = get(VENDOR_GROUP).getValue();
		if (value != null)
			vendor.setVendorGroup(value.getID());
		vendor.setActive(isActive);
		TAXCode code = get(VENDOR_VAT_CODE).getValue();
		if (code != null)
			vendor.setTAXCode(code.getID());
		vendor.setVATRegistrationNumber(vatRegistredNum);

		if (preferences.isTrackTax() && code != null) {
			if (countryPreferences.isVatAvailable()) {
				vendor.setTAXCode(code.getID());
				vendor.setVATRegistrationNumber(vatRegistredNum);
			}
			if (countryPreferences.isSalesTaxAvailable()) {
				vendor.setCstNumber(cstNum);
			}
			if (countryPreferences.isServiceTaxAvailable()) {
				vendor.setServiceTaxRegistrationNumber(serviceTaxNum);
			}
			if (countryPreferences.isTDSAvailable()) {
				vendor.setTinNumber(tinNum);
			}
			// customer.setPANno(panNum);
		}
		create(vendor, context);

		return null;
	}

	@Override
	protected String getDeleteCommand(Context context) {
		return vendor != null ? "deleteVendor " + vendor.getID() : null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (isUpdate) {
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Vendor to update.");
				return "vendors";
			}
			ClientPayee vendorByName = CommandUtils.getPayeeByName(
					context.getCompany(), string.toLowerCase());
			if (vendorByName == null) {
				long numberFromString = getNumberFromString(string);
				if (numberFromString != 0) {
					string = String.valueOf(numberFromString);
				}
				vendorByName = CommandUtils.getVendorByNumber(
						context.getCompany(), string);
				if (vendorByName == null) {
					addFirstMessage(context, "Select a Vendor to update.");
					return "vendors " + string.trim();
				}
			}
			vendor = (ClientVendor) vendorByName;
			setValues();
		} else {
			vendor = new ClientVendor();
			if (!string.isEmpty()) {
				get(VENDOR_NAME).setValue(string);
			}
		}
		return null;
	}

	private void setValues() {
		get(VENDOR_NAME).setValue(vendor.getName());
		get(VENDOR_NUMBER).setValue(vendor.getVendorNumber());
		get(VENDOR_GROUP).setValue(
				CommandUtils.getServerObjectById(vendor.getVendorGroup(),
						AccounterCoreType.VENDOR_GROUP));
		get(ACTIVE).setValue(vendor.isActive());
		get(VENDOR_SINCE).setValue(
				new ClientFinanceDate(vendor.getPayeeSince()));
		get(BALANCE).setValue(vendor.getBalance());
		// get(BALANCE).setEditable(false);
		get(BALANCE_AS_OF).setValue(
				new ClientFinanceDate(vendor.getBalanceAsOf()));
		Set<ClientAddress> address = vendor.getAddress();
		for (ClientAddress clientAddress : address) {
			if (clientAddress.getType() == ClientAddress.TYPE_BILL_TO) {
				get(BILL_TO).setValue(clientAddress);
			} else if (clientAddress.getType() == ClientAddress.TYPE_SHIP_TO) {
				get(SHIP_TO).setValue(clientAddress);
			}
		}
		get(PAYMENT_METHOD).setDefaultValue(getMessages().cash());
		get(VENDOR_VAT_CODE).setValue(
				CommandUtils.getServerObjectById(vendor.getTAXCode(),
						AccounterCoreType.TAX_CODE));

		get(CONTACTS).setValue(
				new ArrayList<ClientContact>(vendor.getContacts()));
		get(ACCOUNT).setValue(
				CommandUtils.getServerObjectById(vendor.getExpenseAccount(),
						AccounterCoreType.ACCOUNT));
		get(PHONE).setValue(vendor.getPhoneNo());
		get(FAX).setValue(vendor.getFaxNo());
		get(EMAIL).setValue(vendor.getEmail());
		get(WEB_PAGE_ADDRESS).setValue(vendor.getWebPageAddress());
		get(CREDIT_LIMIT).setValue(vendor.getCreditLimit());
		get(BANK_NAME).setValue(vendor.getBankName());
		get(ACCOUNT_NO).setValue(vendor.getBankAccountNo());
		get(BANK_BRANCH).setValue(vendor.getBankBranch());
		get(SHIPPING_METHODS).setValue(
				CommandUtils.getServerObjectById(vendor.getShippingMethod(),
						AccounterCoreType.SHIPPING_METHOD));
		get(PAYMENT_METHOD).setValue(vendor.getPaymentMethod());
		get(PAYMENT_TERMS).setValue(
				CommandUtils.getServerObjectById(vendor.getPaymentTerms(),
						AccounterCoreType.PAYMENT_TERM));
		get(VAT_REGISTRATION_NUMBER)
				.setValue(vendor.getVATRegistrationNumber());
		get(CST_NUM).setValue(vendor.getCstNumber());
		get(SERVICE_TAX_NUM).setValue(vendor.getServiceTaxRegistrationNumber());
		get(TIN_NUM).setValue(vendor.getTinNumber());
		get(TRACK_PAYMENTS_FOR_1099).setValue(vendor.isTrackPaymentsFor1099());
	}

	@Override
	protected String getWelcomeMessage() {
		if (vendor.getID() == 0) {
			return getMessages().creating(Global.get().Vendor());
		} else {
			return "Updating '" + vendor.getDisplayName()
					+ Global.get().Vendor();
		}
	}

	@Override
	protected String getDetailsMessage() {
		if (vendor.getID() == 0) {
			return getMessages().readyToCreate(Global.get().Vendor());
		} else {
			return getMessages().readyToUpdate(Global.get().Vendor());
		}
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(ACTIVE).setDefaultValue(true);
		get(VENDOR_SINCE).setDefaultValue(new ClientFinanceDate());
		get(BALANCE).setDefaultValue(Double.valueOf(0.0D));
		get(BALANCE_AS_OF).setDefaultValue(new ClientFinanceDate());
		get(BILL_TO).setDefaultValue(new ClientAddress());
		get(PAYMENT_METHOD).setDefaultValue(getMessages().cash());
	}

	@Override
	public String getSuccessMessage() {
		if (vendor.getID() == 0) {
			return getMessages().createSuccessfully(Global.get().Vendor());
		} else {
			return getMessages().updateSuccessfully(Global.get().Vendor());
		}
	}

}
