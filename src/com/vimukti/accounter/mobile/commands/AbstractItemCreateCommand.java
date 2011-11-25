package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ItemGroupRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

public abstract class AbstractItemCreateCommand extends NewAbstractCommand {

	private static final String NAME = "name";
	private static final String I_SELL_THIS = "iSellthis";
	private static final String SALES_DESCRIPTION = "salesDescription";
	private static final String SALES_PRICE = "salesPrice";
	private static final String INCOME_ACCOUNT = "incomeAccount";
	private static final String IS_TAXABLE = "isTaxable";
	private static final String IS_COMMISION_ITEM = "isCommisionItem";
	private static final String STANDARD_COST = "standardCost";
	private static final String ITEM_GROUP = "itemGroup";
	private static final String IS_ACTIVE = "isActive";
	private static final String I_BUY_THIS = "iBuyService";
	private static final String PURCHASE_DESCRIPTION = "purchaseDescription";
	private static final String PURCHASE_PRICE = "purchasePrice";
	private static final String EXPENSE_ACCOUNT = "expenseAccount";
	private static final String PREFERRED_SUPPLIER = "preferredSupplier";
	private static final String SERVICE_NO = "supplierServiceNo";
	protected static final String WEIGHT = "weight";
	private static final String TAXCODE = "taxCode";
	private int itemType;
	private ClientItem item;

	public AbstractItemCreateCommand(int itemType) {
		this.itemType = itemType;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(NAME, getMessages().pleaseEnter(
				getMessages().itemName()), getMessages().name(), false, true));

		list.add(new BooleanRequirement(I_SELL_THIS, true) {

			@Override
			protected String getTrueString() {
				return getMessages().isellthisservice();
			}

			@Override
			protected String getFalseString() {
				return getMessages().idontSellThisService();
			}
		});

		list.add(new BooleanRequirement(I_BUY_THIS, true) {

			@Override
			protected String getTrueString() {
				return getMessages().iBuyThisItem();
			}

			@Override
			protected String getFalseString() {
				return getMessages().idontBuyThisService();
			}
		});

		list.add(new NameRequirement(SALES_DESCRIPTION, getMessages()
				.pleaseEnter(getMessages().salesDescription()), getMessages()
				.salesDescription(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new AmountRequirement(SALES_PRICE, getMessages().pleaseEnter(
				getMessages().salesPrice()), getMessages().salesPrice(), true,
				true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new AccountRequirement(INCOME_ACCOUNT, getMessages()
				.pleaseEnter(getMessages().incomeAccount()), getMessages()
				.incomeAccount(), false, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<Account> getLists(Context context) {
				return Utility.filteredList(new ListFilter<Account>() {

					@Override
					public boolean filter(Account account) {
						if (account.getType() != Account.TYPE_CASH
								&& account.getType() != Account.TYPE_BANK
								&& account.getType() != Account.TYPE_INVENTORY_ASSET
								&& account.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != Account.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != Account.TYPE_EXPENSE
								&& account.getType() != Account.TYPE_OTHER_EXPENSE
								&& account.getType() != Account.TYPE_COST_OF_GOODS_SOLD
								&& account.getType() != Account.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != Account.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != Account.TYPE_LONG_TERM_LIABILITY
								&& account.getType() != Account.TYPE_OTHER_ASSET
								&& account.getType() != Account.TYPE_EQUITY) {
							return true;
						} else {
							return false;
						}
					}
				}, new ArrayList<Account>(context.getCompany().getAccounts()));
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Account());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().equals(name);
			}

			@Override
			protected String getSetMessage() {
				return "Income Account has been selected";
			}
		});

		list.add(new BooleanRequirement(IS_TAXABLE, true) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().taxable();
			}

			@Override
			protected String getFalseString() {
				return getMessages().taxExempt();
			}
		});

		list.add(new BooleanRequirement(IS_COMMISION_ITEM, true) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().thisIsCommisionItem();
			}

			@Override
			protected String getFalseString() {
				return getMessages().thisIsNoCommisionItem();
			}
		});

		list.add(new AmountRequirement(STANDARD_COST, getMessages()
				.pleaseEnter(getMessages().standardCost()), getMessages()
				.standardCost(), true, true));

		list.add(new ItemGroupRequirement(ITEM_GROUP,
				"Enter the item group name", getMessages().itemGroup(), true,
				true, null) {

			@Override
			protected String getSetMessage() {
				return "Item group has been selected";
			}

			@Override
			protected List<ItemGroup> getLists(Context context) {
				return new ArrayList<ItemGroup>(context.getCompany()
						.getItemGroups());
			}

			@Override
			protected boolean filter(ItemGroup e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new TaxCodeRequirement(TAXCODE, "Enter the Tax Code name",
				getMessages().taxCode(), false, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getCompany().getPreferences()
						.isClassOnePerTransaction()) {
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

		list.add(new BooleanRequirement(IS_ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return getMessages().active();
			}

			@Override
			protected String getFalseString() {
				return getMessages().inActive();
			}
		});

		list.add(new NameRequirement(PURCHASE_DESCRIPTION, getMessages()
				.pleaseEnter(getMessages().purchaseDescription()),
				getMessages().purchaseDescription(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new AmountRequirement(PURCHASE_PRICE, getMessages()
				.pleaseEnter(getMessages().purchasePrice()), getMessages()
				.purchasePrice(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new AccountRequirement(EXPENSE_ACCOUNT, getMessages()
				.pleaseEnter(getMessages().expenseAccount()), getMessages()
				.expenseAccount(), false, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<Account> getLists(Context context) {
				return Utility.filteredList(new ListFilter<Account>() {

					@Override
					public boolean filter(Account e) {
						return Arrays.asList(Account.TYPE_EXPENSE,
								Account.TYPE_COST_OF_GOODS_SOLD,
								Account.TYPE_OTHER_EXPENSE).contains(
								e.getType());
					}
				}, new ArrayList<Account>(context.getCompany().getAccounts()));
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Account());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().equals(name);
			}

			@Override
			protected String getSetMessage() {
				return "Expense Account has been selected";
			}
		});

		list.add(new VendorRequirement(PREFERRED_SUPPLIER,
				"enter the supplier name or number", getMessages()
						.preferredVendor(Global.get().Vendor()), true, true,
				null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getSetMessage() {
				return "Preferred Supplier has been selected";
			}

			@Override
			protected List<Vendor> getLists(Context context) {
				return getVendors();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Vendor());
			}

			@Override
			protected boolean filter(Vendor e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new NumberRequirement(SERVICE_NO, getMessages().pleaseEnter(
				getMessages().vendorServiceNo(Global.get().Vendor())),
				getMessages().vendorServiceNo(Global.get().Vendor()), true,
				true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

	}

	@Override
	protected void setDefaultValues(Context context) {
		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String name = (String) get(NAME).getValue();

		// TODO:check weather it is product or service item
		Integer weight = 0;
		Requirement weightReq = get(WEIGHT);
		if (weightReq != null) {
			String s = weightReq.getValue();
			weight = s != null ? Integer.parseInt(s) : 0;
		}
		Boolean iSellthis = (Boolean) get(I_SELL_THIS).getValue();
		String description = (String) get(SALES_DESCRIPTION).getValue();
		double price = (Double) get(SALES_PRICE).getValue();
		Account incomeAccount = get(INCOME_ACCOUNT).getValue();
		Boolean isTaxable = (Boolean) get(IS_TAXABLE).getValue();
		Boolean isCommisionItem = (Boolean) get(IS_COMMISION_ITEM).getValue();
		double cost = (Double) get(STANDARD_COST).getValue();
		ItemGroup itemGroup = (ItemGroup) get(ITEM_GROUP).getValue();
		TAXCode vatcode = (TAXCode) get(TAXCODE).getValue();
		Boolean isActive = (Boolean) get(IS_ACTIVE).getValue();
		Boolean isBuyservice = (Boolean) get(I_BUY_THIS).getValue();
		String purchaseDescription = (String) get(PURCHASE_DESCRIPTION)
				.getValue();
		double purchasePrice = (Double) get(PURCHASE_PRICE).getValue();
		Account expenseAccount = (Account) get(EXPENSE_ACCOUNT).getValue();
		Vendor preferedSupplier = (Vendor) get(PREFERRED_SUPPLIER).getValue();
		String supplierServiceNo = (String) get(SERVICE_NO).getValue();

		getItem().setName(name);
		getItem().setWeight(weight);
		getItem().setISellThisItem(iSellthis);
		if (iSellthis) {
			getItem().setSalesDescription(description);
			getItem().setSalesPrice(price);
			getItem().setIncomeAccount(incomeAccount.getID());
			getItem().setTaxable(isTaxable);
			getItem().setCommissionItem(isCommisionItem);
		}
		getItem().setStandardCost(cost);
		if (context.getCompany().getPreferences().isClassOnePerTransaction()) {
			getItem().setTaxCode(vatcode.getID());
		}
		getItem().setActive(isActive);
		getItem().setIBuyThisItem(isBuyservice);
		if (itemGroup != null)
			getItem().setItemGroup(itemGroup.getID());
		if (isBuyservice) {
			getItem().setPurchaseDescription(purchaseDescription);
			getItem().setPurchasePrice(purchasePrice);
			getItem().setExpenseAccount(expenseAccount.getID());
			if (preferedSupplier != null)
				getItem().setPreferredVendor(preferedSupplier.getID());
			getItem().setVendorItemNumber(supplierServiceNo);
		}
		getItem().setType(itemType);
		create(getItem(), context);

		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return getItem().getID() == 0 ? getMessages().readyToCreate(
				getMessages().item())
				: "Item is ready to update with following details";

	}

	@Override
	public String getSuccessMessage() {
		return getItem().getID() == 0 ? getMessages().createSuccessfully(
				getMessages().item()) : getMessages().updateSuccessfully(
				getMessages().item());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (!isUpdate) {
			if (!string.isEmpty()) {
				if (string.equals("sell")) {
					get(I_SELL_THIS).setValue(true);
				} else {
					get(I_BUY_THIS).setValue(true);
				}
			} else {
				get(I_SELL_THIS).setValue(true);
			}
			setItem(new ClientItem());
		} else {
			if (string.isEmpty()) {
				addFirstMessage(context, "Select an Item to update.");
				return "Items List";
			}
			Item customerByName = CommandUtils.getItemByName(
					context.getCompany(), string);
			if (customerByName == null) {
				addFirstMessage(context, "Select an Item to update.");
				return "Items List " + string;
			}
			setItem((ClientItem) CommandUtils.getClientObjectById(
					customerByName.getID(), AccounterCoreType.ITEM, context
							.getCompany().getId()));
			setValues();
		}
		return null;
	}

	private void setValues() {
		get(NAME).setValue(item.getName());

		get(I_SELL_THIS).setValue(item.isISellThisItem());
		get(SALES_DESCRIPTION).setValue(item.getSalesDescription());
		get(SALES_PRICE).setValue(item.getSalesPrice());
		get(INCOME_ACCOUNT).setValue(
				CommandUtils.getServerObjectById(item.getIncomeAccount(),
						AccounterCoreType.ACCOUNT));
		get(IS_TAXABLE).setValue(item.isTaxable());
		get(IS_COMMISION_ITEM).setValue(item.isCommissionItem());
		get(STANDARD_COST).setValue(item.getStandardCost());
		long itemGroup = item.getItemGroup();
		get(ITEM_GROUP).setValue(
				CommandUtils.getServerObjectById(itemGroup,
						AccounterCoreType.ITEM_GROUP));
		get(TAXCODE).setValue(
				CommandUtils.getServerObjectById(item.getTaxCode(),
						AccounterCoreType.TAX_CODE));
		get(IS_ACTIVE).setValue(item.isActive());
		get(I_BUY_THIS).setValue(item.isIBuyThisItem);
		get(PURCHASE_DESCRIPTION).setValue(item.getPurchaseDescription());
		get(PURCHASE_PRICE).setValue(item.getPurchasePrice());
		get(EXPENSE_ACCOUNT).setValue(
				CommandUtils.getServerObjectById(item.getExpenseAccount(),
						AccounterCoreType.ACCOUNT));
		get(PREFERRED_SUPPLIER).setValue(
				CommandUtils.getServerObjectById(item.getPreferredVendor(),
						AccounterCoreType.VENDOR));
	}

	public ClientItem getItem() {
		return item;
	}

	public void setItem(ClientItem item) {
		this.item = item;
	}
}
