package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.ActionRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.server.FinanceTool;

public class CustomerRefundsListCommand extends NewAbstractCommand {

	private static final int NO_OF_RECORD_TO_SHOW = 20;
	private static final int STATUS_NOT_ISSUED = 0;
	private static final int STATUS_PARTIALLY_PAID = 1;
	private static final int STATUS_ISSUED = 2;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {

		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(VIEW_BY).setDefaultValue(getMessages().issued());

	}

	@Override
	public String getSuccessMessage() {

		return "Success";

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ActionRequirement(VIEW_BY, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().notIssued());
				list.add(getMessages().issued());
				list.add(getMessages().voided());
				list.add(getMessages().all());
				return list;
			}
		});
		list.add(new ShowListRequirement<CustomerRefundsList>(getMessages()
				.customerRefund(Global.get().Customer()), getMessages()
				.pleaseSelect(
						getMessages().customerRefund(Global.get().Customer())),
				NO_OF_RECORD_TO_SHOW) {

			@Override
			protected String onSelection(CustomerRefundsList value) {
				return "Edit Transaction " + value.getTransactionId();
			}

			@Override
			protected String getShowMessage() {

				return getMessages().customerRefunds(Global.get().Customer());
			}

			@Override
			protected String getEmptyString() {
				return "No Customer Refunds are available";

			}

			@Override
			protected Record createRecord(CustomerRefundsList value) {
				Record record = new Record(value);
				record.add("", value.getPaymentNumber());
				record.add("", value.getPaymentDate());
				record.add("", value.getIssueDate());
				record.add("", value.getName());
				record.add("", Utility.getTransactionName((value.getType())));
				record.add("", value.getPaymentMethod());
				record.add("", value.getAmountPaid());
				record.add("", Utility.getStatus(
						ClientTransaction.TYPE_CUSTOMER_REFUNDS,
						value.getStatus()));

				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("Create Customer Refund");

			}

			@Override
			protected boolean filter(CustomerRefundsList e, String name) {
				return e.getName().startsWith(name)
						|| e.getPaymentNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<CustomerRefundsList> getLists(Context context) {

				return getList(context);
			}
		});

	}

	protected List<CustomerRefundsList> getList(Context context) {

		String viewType = get(VIEW_BY).getValue();

		List<CustomerRefundsList> customerRefundsList = null;
		try {

			customerRefundsList = new FinanceTool().getCustomerManager()
					.getCustomerRefundsList(context.getCompany().getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<CustomerRefundsList> newList = new ArrayList<CustomerRefundsList>();
		for (CustomerRefundsList customerRefund : customerRefundsList) {
			if (viewType.equals(getMessages().notIssued())) {
				if ((customerRefund.getStatus() == STATUS_NOT_ISSUED || customerRefund
						.getStatus() == STATUS_PARTIALLY_PAID)
						&& (!customerRefund.isVoided()))
					newList.add(customerRefund);
			}
			if (viewType.equals(getMessages().issued())) {
				if (customerRefund.getStatus() == STATUS_ISSUED
						&& (!customerRefund.isVoided()))
					newList.add(customerRefund);
			}
			if (viewType.equals(getMessages().voided())) {
				if (customerRefund.isVoided() && !customerRefund.isDeleted())
					newList.add(customerRefund);
			}
			if (viewType.equals(getMessages().all())) {

				newList.add(customerRefund);
			}
		}
		return newList;

	}
}
