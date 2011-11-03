package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemAccountsRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemItemsRequirement;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewCustomerCreditMemoCommand extends NewAbstractTransactionCommand {

	@Override
	protected String getWelcomeMessage() {
		return getMessages().create(getConstants().CustomerCreditNote());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().CustomerCreditNote());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(MEMO).setDefaultValue("");
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO,
						context.getCompany()));
		get(CURRENCY).setDefaultValue(null);
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		
		get(CURRENCY_FACTOR).setDefaultValue(1.0);

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(
				getConstants().CustomerCreditNote());
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new CustomerRequirement(CUSTOMER,
				"Please Enter Customer name or number ", "Customer", false,
				true, new ChangeListner<ClientCustomer>() {

					@Override
					public void onSelection(ClientCustomer value) {
						NewCustomerCreditMemoCommand.this.get(CONTACT)
								.setValue(null);
					}
				}) {

			@Override
			protected List<ClientCustomer> getLists(Context context) {
				return getClientCompany().getCustomers();
			}
		});

		
		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getClientCompany().getPreferences().isEnableMultiCurrency()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected List<ClientCurrency> getLists(Context context) {
				return context.getClientCompany().getCurrencies();
			}
		});

		list.add(new AmountRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseSelect(getConstants().currency()), getConstants()
				.currency(), false, true) {
			@Override
			protected String getDisplayValue(Double value) {
				String primaryCurrency = getClientCompany().getPreferences()
						.getPrimaryCurrency();
				ClientCurrency selc = get(CURRENCY).getValue();
				return "1 " + selc.getFormalName() + " = " + value + " " + primaryCurrency;
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (get(CURRENCY).getValue() != null) {
					if (getClientCompany().getPreferences()
							.isEnableMultiCurrency()
							&& !((ClientCurrency)get(CURRENCY).getValue()).equals(
									getClientCompany().getPreferences()
											.getPrimaryCurrency())) {
						return super.run(context, makeResult, list, actions);
					}
				} 
					return null;
				
				
			}
		});

		
		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().creditNoteNo()), getConstants().creditNoteNo(),
				true, true));
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().transactionDate()), getConstants()
				.transactionDate(), true, true));
		list.add(new TransactionItemAccountsRequirement(ACCOUNTS,
				"please select accountItems", getConstants().Account(), true,
				true) {

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						if (e.getType() == ClientAccount.TYPE_INCOME
								|| e.getType() == ClientAccount.TYPE_FIXED_ASSET) {
							return true;
						}
						return false;
					}
				}, getClientCompany().getAccounts());
			}
		});
		list.add(new TransactionItemItemsRequirement(ITEMS,
				"Please Enter Item Name or number", getConstants().items(),
				true, true, true) {

			@Override
			protected List<ClientItem> getLists(Context context) {
				return getClientCompany().getItems();
			}
		});

		list.add(new ContactRequirement(CONTACT, "Enter contact name",
				"Contact", true, true, null) {

			@Override
			protected List<ClientContact> getLists(Context context) {
				return new ArrayList<ClientContact>(
						((ClientCustomer) NewCustomerCreditMemoCommand.this
								.get(CUSTOMER).getValue()).getContacts());
			}

			@Override
			protected String getContactHolderName() {
				return ((ClientCustomer) get(CUSTOMER).getValue())
						.getDisplayName();
			}
		});
		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getConstants().taxCode()), getConstants().taxCode(), false,
				true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getClientCompany().getPreferences().isTrackTax()
						&& !getClientCompany().getPreferences()
								.isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<ClientTAXCode> getLists(Context context) {
				return getClientCompany().getTaxCodes();
			}

			@Override
			protected boolean filter(ClientTAXCode e, String name) {
				return e.getName().contains(name);
			}
		});

		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context
						.getClientCompany().getPreferences();
				if (preferences.isTrackTax()
						&& !preferences.isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return "Include VAT with Amount enabled";
			}

			@Override
			protected String getFalseString() {
				return "Include VAT with Amount disabled";
			}
		});

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getConstants().billTo()), getConstants().billTo(), true, true));

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().reasonForIssue()), getConstants()
				.reasonForIssue(), true, true));
		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, new ChangeListner<ClientCurrency>() {

					@Override
					public void onSelection(ClientCurrency value) {

					}
				}) {

			@Override
			protected List<ClientCurrency> getLists(Context context) {
				return context.getClientCompany().getCurrencies();
			}
		});
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}

		ClientCustomerCreditMemo creditMemo = new ClientCustomerCreditMemo();

		ClientFinanceDate date = get(DATE).getValue();
		creditMemo.setDate(date.getDate());

		creditMemo.setType(Transaction.TYPE_CUSTOMER_CREDIT_MEMO);

		String number = get(NUMBER).getValue();
		creditMemo.setNumber(number);

		ClientContact contact = (ClientContact) get(CONTACT).getValue();

		creditMemo.setContact(contact);
		ClientAddress billingAddress = get(BILL_TO).getValue();
		creditMemo.setBillingAddress(billingAddress);

		accounts.addAll(items);

		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		ClientCompanyPreferences preferences = context.getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			creditMemo.setAmountsIncludeVAT(isVatInclusive);
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : accounts) {
				item.setTaxCode(taxCode.getID());
			}
		}
		creditMemo.setTransactionItems(accounts);
		ClientCustomer customer = get(CUSTOMER).getValue();
		creditMemo.setCustomer(customer.getID());

		if (context.getClientCompany().getPreferences().isEnableMultiCurrency()) {
			ClientCurrency currency = get(CURRENCY).getValue();
			if (currency != null) {
				creditMemo.setCurrency(currency.getID());
			}

			double factor = get(CURRENCY_FACTOR).getValue();
			creditMemo.setCurrencyFactor(factor);
		}
		
		String memo = get(MEMO).getValue();
		creditMemo.setMemo(memo);
		double taxTotal = updateTotals(context, creditMemo, false);
		creditMemo.setTaxTotal(taxTotal);
		create(creditMemo, context);
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			addFirstMessage(context,
					"Transaction total can not zero or less than zero.So you can't finish this command");
		}
	}
}