package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.Global;

public class ClientTransferFund extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long transferFrom;
	long transferTo;

	/**
	 * @return the version
	 */
	@Override
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the transferFrom
	 */
	public long getTransferFrom() {
		return transferFrom;
	}

	/**
	 * @param transferFrom
	 *            the transferFrom to set
	 */
	public void setTransferFrom(long transferFrom) {
		this.transferFrom = transferFrom;
	}

	/**
	 * @return the transferTo
	 */
	public long getTransferTo() {
		return transferTo;
	}

	/**
	 * @param transferTo
	 *            the transferTo to set
	 */
	public void setTransferTo(long transferTo) {
		this.transferTo = transferTo;
	}

	@Override
	public String toString() {
		return Global.get().messages().transferFund();
	}

	@Override
	public String getDisplayName() {

		return getName();
	}

	@Override
	public String getName() {

		return Utility.getTransactionName(getType());
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientTransferFund";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TRANSFERFUND;
	}

	public ClientTAXAdjustment clone() {
		ClientTAXAdjustment taxAdjustment = (ClientTAXAdjustment) this.clone();

		// transactionItems list
		List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem clientTransactionItem : this.transactionItems) {
			transactionItems.add(clientTransactionItem.clone());
		}
		taxAdjustment.transactionItems = transactionItems;

		// transactionMakeDeposit list
		List<ClientTransactionMakeDeposit> transactionMakeDeposit = new ArrayList<ClientTransactionMakeDeposit>();
		for (ClientTransactionMakeDeposit clientTransactionMakeDeposit : this.transactionMakeDeposit) {
			transactionMakeDeposit.add(clientTransactionMakeDeposit.clone());
		}
		taxAdjustment.transactionMakeDeposit = transactionMakeDeposit;

		// transactionPayBill list
		List<ClientTransactionPayBill> transactionPayBillList = new ArrayList<ClientTransactionPayBill>();
		for (ClientTransactionPayBill clientTransactionPayBill : this.transactionPayBill) {
			transactionPayBillList.add(clientTransactionPayBill.clone());
		}
		taxAdjustment.transactionPayBill = transactionPayBillList;

		// transactionReceivePayment list
		List<ClientTransactionReceivePayment> transactionReceivePaymentList = new ArrayList<ClientTransactionReceivePayment>();
		for (ClientTransactionReceivePayment clientTransactionReceivePayment : this.transactionReceivePayment) {
			transactionReceivePaymentList.add(clientTransactionReceivePayment
					.clone());
		}
		taxAdjustment.transactionReceivePayment = transactionReceivePaymentList;

		// transactionIssuePayment list
		List<ClientTransactionIssuePayment> transactionIssuePayment = new ArrayList<ClientTransactionIssuePayment>();
		for (ClientTransactionIssuePayment clientTransactionIssuePayment : this.transactionIssuePayment) {
			transactionIssuePayment.add(clientTransactionIssuePayment.clone());
		}
		taxAdjustment.transactionIssuePayment = transactionIssuePayment;

		// transactionPaySalestax list
		List<ClientTransactionPayTAX> transactionPaySalesTax = new ArrayList<ClientTransactionPayTAX>();
		for (ClientTransactionPayTAX clientTransactionPaySalesTax : this.transactionPaySalesTax) {
			transactionPaySalesTax.add(clientTransactionPaySalesTax.clone());
		}
		taxAdjustment.transactionPaySalesTax = transactionPaySalesTax;

		// taxAdjustment.creditsAndPayments = this.creditsAndPayments.clone();

		return taxAdjustment;
	}
}
