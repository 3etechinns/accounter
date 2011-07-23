package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.SpecialReference;

public class TransactionCreditsAndPayments implements IAccounterServerCore,
		Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5200194290095371529L;

	long id;

	FinanceDate date;

	String memo;

	/**
	 * The amount to be used when using this object.
	 */
	double amountToUse;

	/**
	 * This TransactionCreditsAndPayments are used in TransactionReceivePayment
	 * while creating ReceivePayment. So its reference has to be maintained
	 */
	@ReffereredObject
	TransactionReceivePayment transactionReceivePayment;

	/**
	 * This TransactionCreditsAndPayments are used in TransactionPayBill while
	 * creating ReceivePayment. So its reference has to be maintained
	 */
	@ReffereredObject
	TransactionPayBill transactionPayBill;

	/**
	 * This is referred to CreditsAndPayments class which holds all the object
	 * of TransactionCreditsAndPayments.
	 */
	@SpecialReference
	@ReffereredObject
	CreditsAndPayments creditsAndPayments;

	// @ReffereredObject
	// CustomerPrePayment customerPrePayment;

	// public CustomerPrePayment getCustomerPrePayment() {
	// return customerPrePayment;
	// }
	//
	// public void setCustomerPrePayment(CustomerPrePayment customerPrePayment)
	// {
	// this.customerPrePayment = customerPrePayment;
	// }

	boolean isVoid = false;

	int version;

	transient boolean isImported;

	transient private boolean isOnSaveProccessed;

	public TransactionCreditsAndPayments() {
		// TODO
	}

	public long getId() {
		return id;
	}

	public int getVersion() {
		return version;
	}

	public void setID(long id) {
		this.id = id;
	}

	public FinanceDate getDate() {
		return date;
	}

	public String getMemo() {
		return memo;
	}

	public double getAmountToUse() {
		return amountToUse;
	}

	public void setAmountToUse(double amountToUse) {
		this.amountToUse = amountToUse;
	}

	public TransactionReceivePayment getTransactionReceivePayment() {
		return transactionReceivePayment;
	}

	public void setTransactionReceivePayment(
			TransactionReceivePayment transactionReceivePayment) {
		this.transactionReceivePayment = transactionReceivePayment;
	}

	public TransactionPayBill getTransactionPayBill() {
		return transactionPayBill;
	}

	public void setTransactionPayBill(TransactionPayBill transactionPayBill) {
		this.transactionPayBill = transactionPayBill;
	}

	/**
	 * @return the creditsAndPayments
	 */
	public CreditsAndPayments getCreditsAndPayments() {
		return creditsAndPayments;
	}

	/**
	 * @return the isVoid
	 */
	public boolean getIsVoid() {
		return isVoid;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setIsVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isImported) {
			return false;
		}
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		if (this.id == 0l && this.creditsAndPayments != null) {

			this.creditsAndPayments.updateBalance(this.getTransaction(),
					this.amountToUse);

			// this.creditsAndPayments.setBalance(this.creditsAndPayments
			// .getBalance()
			// - this.amountToUse);
			//

			int transactionType = this.creditsAndPayments.getTransaction()
					.getType();
			if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO
					|| transactionType == Transaction.TYPE_VENDOR_CREDIT_MEMO) {

				if (transactionType == Transaction.TYPE_VENDOR_CREDIT_MEMO)
					((VendorCreditMemo) (this.creditsAndPayments
							.getTransaction())).balanceDue -= this.amountToUse;

				if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO)
					((CustomerCreditMemo) (this.creditsAndPayments
							.getTransaction())).balanceDue -= this.amountToUse;

				if (DecimalUtil.isGreaterThan(
						this.creditsAndPayments.getBalance(), 0)
						&& DecimalUtil.isLessThan(
								this.creditsAndPayments.getBalance(),
								this.creditsAndPayments.getCreditAmount())) {

					this.creditsAndPayments
							.getTransaction()
							.setStatus(
									Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED);

				}
			} else if (DecimalUtil.isEquals(
					this.creditsAndPayments.getBalance(), 0.0)) {
				this.creditsAndPayments.getTransaction().setStatus(
						Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED);
			}

			session.update(this.creditsAndPayments);
			session.update(this.creditsAndPayments.getTransaction());
		}
		// ChangeTracker.put(this);
		return false;
	}

	/**
	 * Returns the involved transaction; whether it is Receive Payment or
	 * Paybill.
	 * 
	 * @return
	 */
	private Transaction getTransaction() {

		if (this.transactionReceivePayment != null) {
			return this.transactionReceivePayment.getReceivePayment();
		} else
		// if(this.transactionPayBill!=null)
		{
			return this.transactionPayBill.getPayBill();
		}
		// return null;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (this.transactionReceivePayment != null) {
			if (this.transactionReceivePayment.getIsVoid() && !this.isVoid) {

				this.setAmountToUse(0.0);
				this.setIsVoid(true);

			}
		} else if (this.transactionPayBill != null) {
			if (this.transactionPayBill.getIsVoid() && !this.isVoid) {

				this.setAmountToUse(0.0);
				this.setIsVoid(true);

			}
		}
		// ChangeTracker.put(this);
		return false;
	}

	/**
	 * It is called whenever the transaction is voided. It rolls back the
	 * balance of creditsAndPayments and the amount to use.
	 */
	public void onEditTransaction(double amtTouse) {
		if (creditsAndPayments != null) {
			this.creditsAndPayments.updateBalance(getTransaction(), amtTouse);
		}
	}

	/**
	 * Update the amount to use value of the credits involved in the
	 * transaction.
	 */
	public void updateAmountToUse() {
		if (this.transactionPayBill != null) {
			this.transactionPayBill.updateAppliedCredits(this.amountToUse);
			this.amountToUse = 0.0;
		} else if (this.transactionReceivePayment != null) {
			this.transactionReceivePayment
					.updateAppliedCredits(this.amountToUse);
			this.amountToUse = 0.0;
		}
		this.creditsAndPayments = null;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setCreditsAndPayments(CreditsAndPayments creditAndPayment) {
		this.creditsAndPayments = creditAndPayment;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public boolean equals(TransactionCreditsAndPayments obj) {
		if ((this.transactionReceivePayment != null && obj.transactionReceivePayment != null) ? (this.transactionReceivePayment
				.equals(obj.transactionReceivePayment))
				: true && (this.transactionPayBill != null && obj.transactionPayBill != null) ? (this.transactionPayBill
						.equals(obj.transactionPayBill))
						: true && (this.creditsAndPayments != null && obj.creditsAndPayments != null) ? (this.creditsAndPayments
								.equals(obj.creditsAndPayments))
								: true && (!DecimalUtil.isEquals(
										this.amountToUse, 0) && !DecimalUtil
										.isEquals(obj.amountToUse, 0)) ? DecimalUtil
										.isEquals(this.amountToUse,
												obj.amountToUse) : true) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}
}
