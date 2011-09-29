package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;

public class ReceivedPaymentsListCommand extends AbstractTransactionCommand {

	private static final String VIEW_BY = "viewBy";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(VIEW_BY, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");
		Object selection = context.getSelection(VIEW_BY);
		ResultList list = new ResultList("invoicesList");
		Result result = null;
		result = viewTypeRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		String viewType = get(VIEW_BY).getValue();
		result = receivePaymentsList(context, viewType);
		return null;
	}

	@Override
	protected List<String> getViewTypes() {
		List<String> list = new ArrayList<String>();
		list.add("All");
		list.add("Open");
		list.add("Voided");
		list.add("OverDue");

		return list;
	}

	private Result receivePaymentsList(Context context, String viewType) {
		Result result = context.makeResult();
		result.add("Invoices  List");
		ResultList receivedPaymentsListData = new ResultList("invoicesList");
		int num = 0;
		List<ReceivePaymentsList> receivedPayments = getReceivePaymentsList(
				viewType, context.getCompany());

		for (ReceivePaymentsList receivedPayment : receivedPayments) {
			receivedPaymentsListData
					.add(createReceivePaymentRecord(receivedPayment));
			num++;
			if (num == PAYMENTS_TO_SHOW) {
				break;
			}
		}
		int size = receivedPaymentsListData.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Received Payment");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(receivedPaymentsListData);
		result.add(commandList);
		result.add("Type for Received Payment");

		return result;
	}

	private Record createReceivePaymentRecord(ReceivePaymentsList receivepayment) {

		Record record = new Record(receivepayment);

		record.add("Type", receivepayment.getType());
		record.add("PaymentDate", receivepayment.getPaymentDate());
		record.add("Number", receivepayment.getNumber());
		record.add("CustomerName", receivepayment.getCustomerName());
		record.add("PaymentMethod", receivepayment.getPaymentMethodName());
		record.add("AmountPaid", receivepayment.getAmountPaid());
		record.add("Voided", receivepayment.isVoided());
		return record;
	}
}
