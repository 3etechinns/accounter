package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class TransactionItemItemsRequirement extends
		AbstractTransactionItemsRequirement<ClientItem> {
	private static final String ITEM_PROPERTY_ATTR = "itemPropertyAttr";
	private static final Object QUANTITY = "quantity";
	private static final Object UNIT_PRICE = "unitPrice";
	private static final Object DISCOUNT = "discount";
	private static final String TAXCODE = "taxcode";
	private static final Object DESCRIPTION = "description";
	private static final String ITEM_DETAILS = "itemDetails";
	private static final Object TAX = "tax";
	private boolean isSales;

	public TransactionItemItemsRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext, boolean isSales) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext);
		this.isSales = isSales;
	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getConstants().item());
	}

	@Override
	protected Result checkItemToEdit(Context context,
			ClientTransactionItem transactionItem) {
		if (transactionItem.getUnitPrice() == 0) {
			context.putSelection(ITEM_DETAILS, UNIT_PRICE);
			Result transactionItemResult = transactionItem(context,
					transactionItem);
			if (transactionItemResult != null) {
				return transactionItemResult;
			}
		}
		if (context.getCompany().getPreferences().isTrackTax()
				&& context.getCompany().getPreferences().isTaxPerDetailLine()
				&& transactionItem.getTaxCode() == 0) {
			context.putSelection(ITEM_DETAILS, TAXCODE);
			Result transactionItemResult = transactionItem(context,
					transactionItem);
			if (transactionItemResult != null) {
				return transactionItemResult;
			}
		}
		return null;
	}

	@Override
	protected Result transactionItem(Context context,
			ClientTransactionItem transactionItem) {
		context.setAttribute(PROCESS_ATTR, getName());
		context.setAttribute(OLD_TRANSACTION_ITEM_ATTR, transactionItem);

		String lineAttr = (String) context.getAttribute(ITEM_PROPERTY_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(ITEM_PROPERTY_ATTR);
			if (lineAttr.equals(QUANTITY)) {
				if (context.getDouble() != null) {
					transactionItem.getQuantity().setValue(context.getDouble());
				} else {
					transactionItem.getQuantity().setValue(
							context.getInteger().doubleValue());
				}
			} else if (lineAttr.equals(UNIT_PRICE)) {
				if (context.getDouble() != null) {
					transactionItem.setUnitPrice(context.getDouble());
				} else {
					transactionItem.setUnitPrice(context.getInteger()
							.doubleValue());
				}
			} else if (lineAttr.equals(DISCOUNT)) {
				if (context.getDouble() != null) {
					transactionItem.setDiscount(context.getDouble());
				} else {
					transactionItem.setDiscount(context.getInteger()
							.doubleValue());
				}
			} else if (lineAttr.equals(TAXCODE)) {
				ClientTAXCode taxCode = context.getSelection(TAXCODES);
				if (taxCode != null) {
					transactionItem.setTaxCode(taxCode.getID());
				} else {
					context.putSelection(ITEM_DETAILS, TAXCODE);
				}
			} else if (lineAttr.equals(DESCRIPTION)) {
				transactionItem.setDescription(context.getString());
			}
		}

		Object selection = context.getSelection(ITEM_DETAILS);
		if (selection != null) {
			if (selection.equals(QUANTITY)) {
				context.setAttribute(ITEM_PROPERTY_ATTR, QUANTITY);
				return amount(
						context,
						getMessages().pleaseEnterThe(
								getItemName(transactionItem),
								getConstants().quantity()), transactionItem
								.getQuantity().getValue());
			} else if (selection.equals(UNIT_PRICE)) {
				context.setAttribute(ITEM_PROPERTY_ATTR, UNIT_PRICE);
				return amount(
						context,
						getMessages().pleaseEnterThe(
								getItemName(transactionItem),
								getConstants().unitPrice()),
						transactionItem.getUnitPrice());
			} else if (selection.equals(DISCOUNT)) {
				context.setAttribute(ITEM_PROPERTY_ATTR, DISCOUNT);
				return amount(
						context,
						getMessages().pleaseEnterThe(
								getItemName(transactionItem),
								getConstants().discount()),
						transactionItem.getDiscount());
			} else if (selection.equals(TAXCODE)) {
				context.setAttribute(ITEM_PROPERTY_ATTR, TAXCODE);
				return taxCode(
						context,
						getMessages().pleaseEnterThe(
								getItemName(transactionItem),
								getConstants().taxCode()), null,
						getItemDisplayValue(transactionItem));
			} else if (selection.equals(TAX)) {
				transactionItem.setTaxable(!transactionItem.isTaxable());
			} else if (selection.equals(DESCRIPTION)) {
				context.setAttribute(ITEM_PROPERTY_ATTR, DESCRIPTION);
				return number(
						context,
						getMessages().pleaseEnterThe(
								getItemName(transactionItem),
								getConstants().description()),
						transactionItem.getDescription());
			}
		} else {
			selection = context.getSelection(ACTIONS);
			if (selection == ActionNames.FINISH_ITEM) {
				if (transactionItem.getUnitPrice() == 0) {
					context.setAttribute(ITEM_PROPERTY_ATTR, UNIT_PRICE);
					return amount(context, "", transactionItem.getUnitPrice());
				} else if (context.getCompany().getPreferences().isTrackTax()
						&& context.getCompany().getPreferences()
								.isTaxPerDetailLine()
						&& transactionItem.getTaxCode() == 0) {
					context.setAttribute(ITEM_PROPERTY_ATTR, TAXCODE);
					return taxCode(context, "", null,
							getItemDisplayValue(transactionItem));
				}
				context.removeAttribute(PROCESS_ATTR);
				context.removeAttribute(OLD_TRANSACTION_ITEM_ATTR);
				return null;
			} else if (selection == ActionNames.DELETE_ITEM) {
				context.removeAttribute(PROCESS_ATTR);
				return null;
			}
		}

		Result chekResult = checkItemToEdit(context, transactionItem);
		if (chekResult != null) {
			return chekResult;
		}
		ResultList list = new ResultList(ITEM_DETAILS);
		Record record = new Record(QUANTITY);
		record.add("", getConstants().quantity());
		record.add("", transactionItem.getQuantity());
		list.add(record);

		record = new Record(UNIT_PRICE);
		record.add("", getConstants().unitPrice());
		record.add("", transactionItem.getUnitPrice());
		list.add(record);

		record = new Record(DISCOUNT);
		record.add("", getConstants().discount());
		record.add("", transactionItem.getDiscount());
		list.add(record);

		if (getClientCompany().getPreferences().isTrackTax()
				&& getClientCompany().getPreferences().isTaxPerDetailLine()) {
			record = new Record(TAXCODE);
			record.add("", getConstants().taxCode());
			if (transactionItem.getTaxCode() != 0) {
				record.add(
						"",
						getClientCompany().getTAXCode(
								transactionItem.getTaxCode()).getName());
			} else {
				record.add("", "");
			}
			list.add(record);
		} else {
			record = new Record(TAX);
			record.add("", getConstants().isTaxable());
			if (transactionItem.isTaxable()) {
				record.add("", getConstants().taxable());
			} else {
				record.add("", getConstants().taxExempt());
			}
			list.add(record);
		}

		record = new Record(DESCRIPTION);
		record.add("", getConstants().description());
		record.add("", transactionItem.getDescription());
		list.add(record);

		Result result = context.makeResult();
		result.add(getConstants().itemDetails());
		result.add(getConstants().name() + " : "
				+ getItemDisplayValue(transactionItem));
		result.add(list);

		double lt = transactionItem.getQuantity().getValue()
				* transactionItem.getUnitPrice();
		double disc = transactionItem.getDiscount();
		transactionItem
				.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
						* disc / 100)) : lt);
		if (context.getCompany().getPreferences().isTrackTax()
				&& getClientCompany().getPreferences().isTaxPerDetailLine()) {
			double salesTaxRate = getClientCompany().getTAXCode(
					transactionItem.getTaxCode()).getSalesTaxRate();
			transactionItem
					.setVATfraction((transactionItem.getLineTotal() / 100)
							* salesTaxRate);
			result.add(getConstants().vat() + " "
					+ transactionItem.getVATfraction());
		}
		result.add(getConstants().total() + " : "
				+ transactionItem.getLineTotal());

		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.DELETE_ITEM);
		record.add("", getConstants().delete());
		actions.add(record);
		record = new Record(ActionNames.FINISH_ITEM);
		record.add("", getConstants().finish());
		actions.add(record);
		result.add(actions);
		return result;
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getConstants().items());
	}

	@Override
	protected String getEmptyString() {
		return getMessages().thereAreNo(getConstants().item());
	}

	@Override
	protected void setPrice(ClientTransactionItem transactionItem,
			ClientItem item) {
		transactionItem.setItem(item.getID());
	}

	@Override
	protected String getItemDisplayValue(ClientTransactionItem item) {
		if (item.getItem() > 0) {
			return getClientCompany().getItem(item.getItem()).getDisplayName();
		}
		return null;
	}

	@Override
	protected void setItem(ClientTransactionItem transactionItem,
			ClientItem item) {
		if (isSales) {
			transactionItem.setUnitPrice(item.getSalesPrice());
		} else {
			transactionItem.setUnitPrice(item.getPurchasePrice());
		}
	}

	@Override
	protected Record createRecord(ClientItem value) {
		Record record = new Record(value);
		record.add("", value.getName());
		ClientTAXCode taxCode = getClientCompany().getTAXCode(
				value.getTaxCode());
		if (taxCode != null) {
			record.add("", taxCode.getName());
		}
		return record;
	}

	@Override
	protected String getDisplayValue(ClientItem value) {
		return value.getDisplayName();
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().serviceItem()));
		list.add("Create Non Inventory Item");
		list.add("Create Inventory Item");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().item());
	}

	@Override
	protected ClientItem getTransactionItem(ClientTransactionItem item) {
		return getClientCompany().getItem(item.getItem());
	}

	@Override
	protected boolean filter(ClientItem e, String name) {
		return e.getName().contains(name);
	}
}
