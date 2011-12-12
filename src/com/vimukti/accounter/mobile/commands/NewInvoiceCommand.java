package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.EstimatesAndSalesOrderTableRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author SaiPrasad N
 * 
 */

public class NewInvoiceCommand extends NewAbstractTransactionCommand {

	private static final String ESTIMATEANDSALESORDER = "estimateAndSalesOrder";
	private static final String PAYMENT_TERMS = "paymentTerms";
	private static final String DUE_DATE = "duedate";
	private static final String ORDER_NO = "orderNo";

	private ClientInvoice invoice;

	@Override
	public String getId() {
		return null;
	}

	@Override
	public String getWelcomeMessage() {

		return invoice.getID() == 0 ? "Creating new invoice... "
				: "Updating invoice";
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CustomerRequirement(CUSTOMER, getMessages().pleaseSelect(
				Global.get().customer()), Global.get().Customer(), false, true,
				new ChangeListner<Customer>() {

					@Override
					public void onSelection(Customer value) {
						get(BILL_TO).setValue(null);
						Set<Address> addresses = value.getAddress();
						for (Address address : addresses) {
							if (address.getType() == Address.TYPE_BILL_TO) {
								try {
									ClientAddress addr = new ClientConvertUtil()
											.toClientObject(address,
													ClientAddress.class);
									get(BILL_TO).setValue(addr);
								} catch (AccounterException e) {
									e.printStackTrace();
								}
								break;
							}
						}

						NewInvoiceCommand.this.get(CONTACT).setValue(null);
						for (Contact contact : value.getContacts()) {
							if (contact.isPrimary()) {
								NewInvoiceCommand.this.get(CONTACT).setValue(
										contact);
								break;
							}
						}
						try {
							double mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							NewInvoiceCommand.this
									.get(CURRENCY_FACTOR)
									.setValue(
											mostRecentTransactionCurrencyFactor);
						} catch (AccounterException e) {
							e.printStackTrace();
						}

					}
				}) {

			@Override
			protected List<Customer> getLists(Context context) {
				return getCustomers();
			}
		});

		list.add(new EstimatesAndSalesOrderTableRequirement(
				ESTIMATEANDSALESORDER, getMessages().selectTypeOfThis(
						getMessages().quote()), getMessages()
						.quoteAndSalesOrderList()) {
			@Override
			protected Customer getCustomer() {
				return (Customer) NewInvoiceCommand.this.get(CUSTOMER)
						.getValue();
			}

		});

		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), getMessages()
				.currencyFactor()) {
			@Override
			protected ClientCurrency getSelectedCurrency() {
				Customer customer = (Customer) NewInvoiceCommand.this.get(
						CUSTOMER).getValue();
				return getCurrency(customer.getCurrency().getID());
			}

		});

		list.add(new TransactionItemTableRequirement(ITEMS, getMessages()
				.pleaseEnter(getMessages().itemName()), getMessages().items(),
				true, true) {
			@Override
			protected double getCurrencyFactor() {
				return NewInvoiceCommand.this.getCurrencyFactor();
			}

			@Override
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				ArrayList<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.isISellThisItem()) {
						if (item.isActive())
							items.add(item);
					}
				}
				return items;
			}

			@Override
			public boolean isSales() {
				return true;
			}

			@Override
			protected Payee getPayee() {
				return (Customer) NewInvoiceCommand.this.get(CUSTOMER)
						.getValue();
			}
		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, true));

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseSelect(getMessages().paymentTerm()), getMessages()
				.paymentTerm(), true, true, null) {

			@Override
			public List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
			}
		});

		list.add(new ContactRequirement(CONTACT, getMessages().pleaseEnter(
				getMessages().contactName()), getMessages().contact(), true,
				true, null) {

			@Override
			protected Payee getPayee() {
				return get(CUSTOMER).getValue();
			}

		});

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getMessages().billTo()), getMessages().billTo(), true, true));

		list.add(new DateRequirement(DUE_DATE, getMessages().pleaseEnter(
				getMessages().dueDate()), getMessages().dueDate(), true, true));

		list.add(new NumberRequirement(ORDER_NO, getMessages().pleaseEnter(
				getMessages().orderNo()), getMessages().orderNo(), true, true));

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()
						&& !context.getPreferences().isTaxPerDetailLine()) {
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
				return e.getName().contains(name);
			}
		});

		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context.getPreferences();
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
	}

	private ClientEstimate getEstimate(long transactionId, Context context) {
		ClientEstimate cEstimate = null;
		try {
			cEstimate = new FinanceTool().getManager().getObjectById(
					AccounterCoreType.ESTIMATE, transactionId,
					context.getCompany().getID());
		} catch (DAOException e1) {
			e1.printStackTrace();
		} catch (AccounterException e1) {
			e1.printStackTrace();
		}
		return cEstimate;
	}

	private ClientSalesOrder getSalesOrder(long transactionId, Context context) {
		ClientSalesOrder cSalesOrder = null;
		try {
			cSalesOrder = new FinanceTool().getManager().getObjectById(
					AccounterCoreType.SALESORDER, transactionId,
					context.getCompany().getID());
		} catch (DAOException e1) {
			e1.printStackTrace();
		} catch (AccounterException e1) {
			e1.printStackTrace();
		}
		return cSalesOrder;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientFinanceDate date = get(DATE).getValue();
		invoice.setDate(date.getDate());

		invoice.setType(Transaction.TYPE_INVOICE);

		String number = get(NUMBER).getValue();
		invoice.setNumber(number);

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<EstimatesAndSalesOrdersList> e = get(ESTIMATEANDSALESORDER)
				.getValue();
		if (items.isEmpty() && e.isEmpty()) {
			return new Result();
		}
		Customer customer = get(CUSTOMER).getValue();
		invoice.setCustomer(customer.getID());

		ClientFinanceDate dueDate = get(DUE_DATE).getValue();
		invoice.setDueDate(dueDate.getDate());

		Contact contact = get(CONTACT).getValue();
		invoice.setContact(toClientContact(contact));

		ClientAddress billTo = get(BILL_TO).getValue();
		invoice.setBillingAddress(billTo);

		PaymentTerms paymentTerm = get(PAYMENT_TERMS).getValue();
		invoice.setPaymentTerm(paymentTerm.getID());

		String orderNo = get(ORDER_NO).getValue();
		invoice.setOrderNum(orderNo);

		String memo = get(MEMO).getValue();
		invoice.setMemo(memo);
		invoice.setStatus(Invoice.STATUS_OPEN);
		// Adding selecting estimate or salesOrder to Invoice
		invoice.setCurrencyFactor(1);
		ClientCompanyPreferences preferences = context.getPreferences();

		List<ClientEstimate> estimates = new ArrayList<ClientEstimate>();
		List<ClientSalesOrder> salesOrders = new ArrayList<ClientSalesOrder>();
		for (EstimatesAndSalesOrdersList estimatesAndSalesOrdersList : e) {
			if (e != null) {
				if (estimatesAndSalesOrdersList.getType() == ClientTransaction.TYPE_ESTIMATE) {
					ClientEstimate cct = getEstimate(
							estimatesAndSalesOrdersList.getTransactionId(),
							context);
					estimates.add(cct);
				} else {
					ClientSalesOrder cSalesOrder = getSalesOrder(
							estimatesAndSalesOrdersList.getTransactionId(),
							context);
					salesOrders.add(cSalesOrder);
				}
			}
		}
		invoice.setEstimates(estimates);
		invoice.setSalesOrders(salesOrders);
		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			invoice.setAmountsIncludeVAT(isVatInclusive);
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		invoice.setTransactionItems(items);
		double taxTotal = updateTotals(context, invoice, true);
		double totalAmount = 0.0;
		double totalNetAmount = 0.0;
		for (ClientSalesOrder clientSalesOrder : salesOrders) {
			totalAmount += clientSalesOrder.getTotal();
			totalNetAmount += clientSalesOrder.getNetAmount();
			taxTotal += clientSalesOrder.getTaxTotal();
		}
		for (ClientEstimate clientEstimate : estimates) {
			if (clientEstimate.getEstimateType() == ClientEstimate.CREDITS) {
				totalAmount -= clientEstimate.getTotal();
				totalNetAmount -= clientEstimate.getNetAmount();
				taxTotal -= clientEstimate.getTaxTotal();
			} else {
				totalAmount += clientEstimate.getTotal();
				totalNetAmount += clientEstimate.getNetAmount();
				taxTotal += clientEstimate.getTaxTotal();
			}
		}
		invoice.setNetAmount(invoice.getNetAmount() + totalNetAmount);
		invoice.setTotal(invoice.getTotal() + totalAmount);
		invoice.setTaxTotal(taxTotal);
		invoice.setCurrency(customer.getCurrency().getID());
		invoice.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());
		create(invoice, context);
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return invoice.getID() == 0 ? getMessages().readyToCreate(
				getMessages().invoice()) : getMessages().readyToUpdate(
				getMessages().invoice());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_INVOICE, context.getCompany()));
		get(CONTACT).setDefaultValue(null);
		Set<PaymentTerms> paymentTerms = context.getCompany().getPaymentTerms();
		for (PaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}
		get(DUE_DATE).setDefaultValue(new ClientFinanceDate());
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);

	}

	@Override
	public String getSuccessMessage() {
		return invoice.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().invoice()) : getMessages().updateSuccessfully(
				getMessages().invoice());
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		// TODO
		List<ClientTransactionItem> allrecords = get(ITEMS).getValue();
		List<EstimatesAndSalesOrdersList> e = get(ESTIMATEANDSALESORDER)
				.getValue();
		if (allrecords.isEmpty() && e.isEmpty()) {
			addFirstMessage(context,
					getMessages().pleaseSelect(getMessages().transactionItem()));
		}
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : allrecords) {
				if (taxCode != null) {
					item.setTaxCode(taxCode.getID());
				}
			}
		}

		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		double[] result = getTransactionTotal(isVatInclusive, allrecords, true);

		for (EstimatesAndSalesOrdersList estimatesAndSalesOrdersList : e) {
			if (e != null) {
				if (estimatesAndSalesOrdersList.getType() == ClientTransaction.TYPE_ESTIMATE) {
					ClientEstimate clientEstimate = getEstimate(
							estimatesAndSalesOrdersList.getTransactionId(),
							context);
					if (clientEstimate.getEstimateType() == ClientEstimate.CREDITS) {
						result[0] -= clientEstimate.getNetAmount();
						result[1] -= clientEstimate.getTaxTotal();
					} else {
						result[0] += clientEstimate.getNetAmount();
						result[1] += clientEstimate.getTaxTotal();
					}
				} else {
					ClientSalesOrder cSalesOrder = getSalesOrder(
							estimatesAndSalesOrdersList.getTransactionId(),
							context);
					result[0] += cSalesOrder.getNetAmount();
					result[1] += cSalesOrder.getTaxTotal();
				}
			}
		}
		if (context.getPreferences().isTrackTax()) {
			makeResult.add("Total Tax: " + result[1]);
		}
		Customer customer = get(CUSTOMER).getValue();
		Currency currency = customer.getCurrency();
		String formalName = getPreferences().getPrimaryCurrency()
				.getFormalName();
		if (!currency.getFormalName().equalsIgnoreCase(formalName))
			makeResult.add("Total"
					+ "("
					+ formalName
					+ ")"
					+ ": "
					+ (result[0] * getCurrencyFactor() + result[1]
							* getCurrencyFactor()));
		makeResult.add("Total" + "(" + currency.getFormalName() + ")" + ": "
				+ (result[0] + result[1]));
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().invoice()));
				return "invoices";
			}
			invoice = getTransaction(string, AccounterCoreType.INVOICE, context);

			if (invoice == null) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().invoice()));
				return "invoices" + string;
			}
			setValues(context);
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			invoice = new ClientInvoice();
		}
		setTransaction(invoice);
		return null;
	}

	/**
	 * update the requirements
	 * 
	 * @param context
	 */

	private void setValues(Context context) {
		get(DATE).setValue(invoice.getDate());
		get(NUMBER).setValue(invoice.getNumber());
		ArrayList<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		if (invoice.getTransactionItems() != null
				&& !invoice.getTransactionItems().isEmpty()) {
			for (ClientTransactionItem item : invoice.getTransactionItems()) {
				// We should exclude those which come from quote/charge/credit
				if (item.getReferringTransactionItem() == 0) {
					list.add(item);
				}
			}
		}
		get(CURRENCY_FACTOR).setValue(invoice.getCurrencyFactor());
		get(ITEMS).setValue(list);
		get(CUSTOMER).setValue(
				CommandUtils.getServerObjectById(invoice.getCustomer(),
						AccounterCoreType.CUSTOMER));
		get(DUE_DATE).setValue(new ClientFinanceDate(invoice.getDueDate()));
		get(CONTACT).setValue(toServerContact(invoice.getContact()));
		get(BILL_TO).setValue(invoice.getBillingAddress());
		get(PAYMENT_TERMS).setValue(
				CommandUtils.getServerObjectById(invoice.getPaymentTerm(),
						AccounterCoreType.PAYMENT_TERM));
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			get(TAXCODE).setValue(
					getTaxCodeForTransactionItems(
							invoice.getTransactionItems(), context));
		}
		get(ORDER_NO).setValue(invoice.getOrderNum());
		get(MEMO).setValue(invoice.getMemo());
		/* get(CURRENCY_FACTOR).setValue(invoice.getCurrencyFactor()); */

		List<EstimatesAndSalesOrdersList> e = getEstimatesSalesOrderList();
		get(ESTIMATEANDSALESORDER).setValue(e);
		get(IS_VAT_INCLUSIVE).setValue(invoice.isAmountsIncludeVAT());
	}

	/**
	 * 
	 * @return
	 */

	private List<EstimatesAndSalesOrdersList> getEstimatesSalesOrderList() {
		List<EstimatesAndSalesOrdersList> list = new ArrayList<EstimatesAndSalesOrdersList>();
		List<ClientEstimate> estimates = invoice.getEstimates();
		if (estimates == null) {
			return list;
		}
		for (ClientEstimate clientEstimate : estimates) {
			EstimatesAndSalesOrdersList el = new EstimatesAndSalesOrdersList();
			el.setTransactionId(clientEstimate.getID());
			el.setType(clientEstimate.getType());
			el.setTransactionNumber(clientEstimate.getNumber());
			el.setTotal(clientEstimate.getTotal());
			el.setDate(clientEstimate.getDate());
			ClientCustomer clientObjectById = (ClientCustomer) CommandUtils
					.getClientObjectById(clientEstimate.getCustomer(),
							AccounterCoreType.CUSTOMER, getCompanyId());
			el.setCustomerName(clientObjectById != null ? clientObjectById
					.getName() : "");
			el.setEstimateType(clientEstimate.getEstimateType());
			list.add(el);
		}

		return list;
	}

	@Override
	protected Payee getPayee() {
		return (Customer) NewInvoiceCommand.this.get(CUSTOMER).getValue();
	}
}