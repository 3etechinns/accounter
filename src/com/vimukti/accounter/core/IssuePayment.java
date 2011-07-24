package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.ClientIssuePayment;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class IssuePayment extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3100092528926190914L;

	// long id;

	/**
	 * Account set for Issue Payment Transaction
	 * 
	 * @see Account
	 */
	Account account;

	/**
	 * Check Number
	 */
	String checkNumber;

	/**
	 * Contains List of Transaction IssuePayments
	 * 
	 * @see TransactionIssuePayment
	 */
	List<TransactionIssuePayment> transactionIssuePayment;

	// 

	public IssuePayment() {
		// TODO
	}

	public IssuePayment(Session session, ClientIssuePayment issuepayment) {
		this.type = TYPE_ISSUE_PAYMENT;

	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return the id
	 */
	@Override
	public long getID() {
		return id;
	}

	/**
	 * @return the checkNumber
	 */
	public String getCheckNumber() {
		return checkNumber;
	}

	@Override
	public boolean isDebitTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account getEffectingAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payee getPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TransactionIssuePayment> getTransactionIssuePayment() {
		return transactionIssuePayment;
	}

	public void setTransactionIssuePayment(
			List<TransactionIssuePayment> transactionIssuePayment) {
		this.transactionIssuePayment = transactionIssuePayment;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_VENDOR;
	}

	@Override
	public String toString() {
		return AccounterConstants.TYPE_ISSUE_PAYMENT;
	}


	@Override
	public Payee getInvolvedPayee() {

		return null;
	}

	public boolean equals(IssuePayment obj) {

		if ((this.paymentMethod != null && obj.paymentMethod != null) ? (this.paymentMethod == obj.paymentMethod)
				: true && (this.account != null && obj.account != null) ? (this.account
						.equals(obj.account))
						: true && (!DecimalUtil.isEquals(this.total, 0) && !DecimalUtil
								.isEquals(obj.total, 0)) ? DecimalUtil
								.isEquals(this.total, obj.total)
								: true && (this.checkNumber != null && obj.checkNumber != null) ? (this.checkNumber == obj.checkNumber)
										: true && this.transactionItems.size() == obj.transactionItems
												.size()) {
			for (int i = 0; i < this.transactionItems.size(); i++) {
				if (!this.transactionItems.get(i).equals(
						obj.transactionItems.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}

}
