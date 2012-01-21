package com.vimukti.accounter.core;

import java.util.List;
import java.util.Map;

import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * @author Suresh Garikapati <br>
 *         <b>Receive Payment: </b><br>
 * <br>
 *         Receive Payment is also known as Customer Payment. By using this
 *         Customer Payment the Customer can pay his Invoices(Opening Balance of
 *         the Customer, Invoice, Customer Refunds).<br>
 * 
 * 
 * <br>
 *         <b><i>Effect on Transaction ReceivePayment: </i></b><br>
 * 
 *         If the finance account selected for Cash Discount and Write off
 *         isIncrease is true then the current and total balances of that
 *         account decrease by the amount given othewise Increase.<br>
 * <br>
 *         (amount = cashdiscount+writeoff+appliedcredits+payment).
 *         Corresponding Invoice or CustomerRefund payment should increase by
 *         the amount and BalanceDue should decrease by the amount. If any cash
 *         discount or writeoff applied for this Invoice/CustomerRefund then set
 *         CanVoidorEdit field for this Invoice/CustomerRefund as false.<br>
 * <br>
 *         <b><i>Effect on Deposite In Account: </i></b><br>
 * 
 *         If Specified Deposit In Account(cash|Bank|Credit Card) isIncrease
 *         true then Decrease the current and total balance by ReceivePayment
 *         total otherwise Increase<br>
 * <br>
 *         <b><i>Other Effect: </i></b><br>
 * 
 *         Accounts Receivable account current and total balance will Decrease
 *         by the ReceivePayment total. It's also Decrease by the total
 *         CashDiscount and total Writeoff.<br>
 * <br>
 *         Customer balance should Decrease by the ReceivePayment total. It's
 *         also Decrease by the total CashDiscount and total Writeoff.<br>
 * 
 * <br>
 * 
 *         <b><i>Status Update: </i></b><br>
 * 
 *         Status of the Receive payment must update according to the amount and
 *         the Unused amount of the ReceivePayment.<br>
 * <br>
 *         If the BalanceDue of the paid Invoices(only) is equal to zero then
 *         update that particular Invoice status as PAID other wise if
 *         BalanceDue >0 then udpate status as PARTIALLY_PAID.<br>
 * <br>
 *         <b><i>Effect on CreditsAndPayments: </i></b><br>
 * 
 *         <i>Updation: -----------</i> The balance of the credits and payments
 *         used in this receivepayment must be reduced receptively by the
 *         AmountToUse given for each CreditAndPayment in that receivepayment.<br>
 * <br>
 *         <i>Creation: ------------</i> If any UnUsedPayment is there then New
 *         Credit and Payment is created for this customer with Credit Amount as
 *         Receive Payment total and Balance as UnUsedPayment and Memo as
 *         transaction number followed by CustomerPayment.<br>
 * 
 * 
 */

public class ReceivePayment extends Transaction implements Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5928413655354497201L;

	/**
	 * {@link Customer} from Which Payment is being Received
	 */
	@ReffereredObject
	Customer customer;

	double amount = 0D;

	double customerBalance = 0D;

	/**
	 * The DepositIn {@link Account}, for this RecievePayment
	 */
	@ReffereredObject
	Account depositIn;

	/**
	 * Un-Used Credits
	 */
	double unUsedCredits = 0D;

	/**
	 * UnUsed Payments
	 */
	double unUsedPayments = 0D;

	/**
	 * Total of Cash Discounts for this Receive Payment Transaction against
	 * this, {@link Customer}
	 */
	double totalCashDiscount = 0D;

	/**
	 * Total WriteOff Amount
	 */
	double totalWriteOff = 0D;

	/**
	 * Total of Applied Credit Payments
	 */
	double totalAppliedCredits = 0D;

	private double tdsTotal;

	// TaxCode VATCode;
	//
	// double VATFraction;

	/**
	 * List of {@link TransactionReceivePayment}'s, for this Receive Payment
	 * Transaction
	 */
	List<TransactionReceivePayment> transactionReceivePayment;

	private String checkNumber;

	//

	public ReceivePayment() {
		setType(Transaction.TYPE_RECEIVE_PAYMENT);
	}

	/**
	 * @return the transactionReceivePayment
	 */
	public List<TransactionReceivePayment> getTransactionReceivePayment() {
		return transactionReceivePayment;
	}

	/**
	 * @param transactionReceivePayment
	 *            the transactionReceivePayment to set
	 */
	public void setTransactionReceivePayment(
			List<TransactionReceivePayment> transactionReceivePayment) {
		this.transactionReceivePayment = transactionReceivePayment;

		if (transactionReceivePayment == null)
			return;

		for (TransactionReceivePayment transactionReceivePaymentItem : transactionReceivePayment) {

			transactionReceivePaymentItem.setReceivePayment(this);
		}
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the customerBalance
	 */
	public double getCustomerBalance() {
		return customerBalance;
	}

	/**
	 * @return the depositIn
	 */
	public Account getDepositIn() {
		return depositIn;
	}

	/**
	 * @return the unUsedCredits
	 */
	public double getUnUsedCredits() {
		return unUsedCredits;
	}

	/**
	 * @return the unUsedPayments
	 */
	public double getUnUsedPayments() {
		return unUsedPayments;
	}

	public double getTotalCashDiscount() {
		return totalCashDiscount;
	}

	public double getTotalWriteOff() {
		return totalWriteOff;
	}

	/**
	 * @return the totalAppliedCredits
	 */
	public double getTotalAppliedCredits() {
		return totalAppliedCredits;
	}

	@Override
	public Account getEffectingAccount() {
		return null;
	}

	@Override
	public Payee getPayee() {
		return this.customer;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		super.onUpdate(session);
		// if (isBecameVoid()) {
		// this.depositIn.updateCurrentBalance(this, this.total);
		// this.depositIn.onUpdate(session);
		// if (this.creditsAndPayments != null) {
		// // this.creditsAndPayments.setTransaction(null);
		// // session.delete(this.creditsAndPayments);
		// this.creditsAndPayments = null;
		// }
		//
		// this.totalCashDiscount = 0.0;
		// this.totalWriteOff = 0.0;
		// this.totalAppliedCredits = 0.0;
		// for (TransactionReceivePayment transactionReceivePayment :
		// this.transactionReceivePayment) {
		// transactionReceivePayment.setIsVoid(Boolean.TRUE);
		// session.update(transactionReceivePayment);
		// if (transactionReceivePayment instanceof Lifecycle) {
		// Lifecycle lifeCycle = (Lifecycle) transactionReceivePayment;
		// lifeCycle.onUpdate(session);
		// }
		// }
		// }
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.core.Transaction#onSave(org.hibernate
	 * .Session)
	 */
	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		this.subTotal = this.amount + tdsTotal;

		super.onSave(session);

		if (this.getTransactionReceivePayment() != null) {
			for (TransactionReceivePayment payment : this
					.getTransactionReceivePayment()) {
				payment.setReceivePayment(this);
			}
		}

		if (isDraftOrTemplate()) {
			return false;
		}
		// this.total = this.amount;
		//
		// this.subTotal = this.total - this.unUsedPayments;

		if (this.depositIn != null) {
			// Deposit in account should be updated by the amount received, not
			// by transaction total.
			this.depositIn.updateCurrentBalance(this, -this.amount,
					currencyFactor);
			// -(this.amount != 0 ? this.amount : this.total));
			this.depositIn.onUpdate(session);
		}

		// the following condition checking is for UK
		// if (Company.getCompany().accountingType == Company.ACCOUNTING_TYPE_UK
		// && !this.customer.isEUVATExemptPayee
		// && this.showPricesWithVATOrVATInclusive
		// ) {
		// this.unUsedPayments += this.total;
		// }

		if (!DecimalUtil.isEquals(this.unUsedPayments, 0D)) {
			if (creditsAndPayments != null
					&& DecimalUtil.isEquals(creditsAndPayments.creditAmount,
							0.0d)) {
				creditsAndPayments.update(this);

			} else {
				creditsAndPayments = new CreditsAndPayments(this);
			}
			this.setCreditsAndPayments(creditsAndPayments);
			session.save(creditsAndPayments);
		}

		// To check whether Unused payment is equal to amount or not and any
		// Cash discount or Write off or Applied Credit are applied or not
		if (DecimalUtil.isEquals(this.unUsedPayments, this.amount)
				&& DecimalUtil.isGreaterThan(this.totalCashDiscount, 0D)
				|| DecimalUtil.isGreaterThan(this.totalWriteOff, 0D)
				|| DecimalUtil.isGreaterThan(this.totalAppliedCredits, 0D)) {
			this.status = Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
		}
		// To check whether Unused payment is equal to amount or not
		else if (DecimalUtil.isEquals(this.unUsedPayments, this.amount)) {
			this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		}
		// To check whether Unused payment is greater than zero and not equal to
		// amount or not
		else if (DecimalUtil.isGreaterThan(this.unUsedPayments, 0D)
				&& !DecimalUtil.isEquals(this.unUsedPayments, this.amount)) {
			this.status = Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
		}
		// To check whether Unused payment is equal to zero or not
		else if (DecimalUtil.isEquals(this.unUsedPayments, 0D)) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		}

		if (DecimalUtil.isGreaterThan(tdsTotal, 0)
				&& getCompany().getPreferences().isTDSEnabled()
				&& this.getCustomer().isWillDeductTDS()) {
			Account tdsAccount = getTDSAccount();
			if (tdsAccount != null) {
				tdsAccount
						.updateCurrentBalance(this, -tdsTotal, currencyFactor);
			}
		}
		return false;
	}

	private Account getTDSAccount() {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);

		try {
			List list = (List) session.getNamedQuery("getTDSAgencyOfCompany")
					.setEntity("company", getCompany()).list();
			if (list != null && !list.isEmpty()) {
				return ((TAXAgency) list.get(0)).getSalesLiabilityAccount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.setFlushMode(flushMode);
		}
		return null;
	}

	@Override
	protected void checkNullValues() throws AccounterException {
		checkingCustomerNull(customer);
		checkPaymentMethodNull();
		checkAccountNull(depositIn);
		checkPaymentItems();
		checkingTotal0();
	}

	private void checkPaymentItems() throws AccounterException {
		if (transactionReceivePayment.isEmpty()) {
			throw new AccounterException(
					AccounterException.ERROR_THERE_IS_NO_TRANSACTION_ITEMS);
		}
	}

	public void updateUnUsedAmount(Session session, double payment) {
		if (DecimalUtil.isEquals(this.unUsedPayments, 0.0)) {
			this.unUsedPayments += payment;
			if (creditsAndPayments != null
					&& DecimalUtil.isEquals(creditsAndPayments.creditAmount,
							0.0d)) {
				creditsAndPayments.update(this);
				this.setCreditsAndPayments(creditsAndPayments);
			} else {
				creditsAndPayments = new CreditsAndPayments(this);
			}
			this.setCreditsAndPayments(creditsAndPayments);
			session.save(creditsAndPayments);
		} else {
			this.getCreditsAndPayments().updateBalance(this, -payment);
			this.setUnUsedPayments(this.getUnUsedPayments() + payment);
		}
	}

	public void setTotalCashDiscount(double totalCashDiscount) {
		this.totalCashDiscount = totalCashDiscount;
	}

	public void setUnUsedPayments(double unUsedPayments) {
		this.unUsedPayments = unUsedPayments;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setTotalWriteOff(double totalWriteOff) {
		this.totalWriteOff = totalWriteOff;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setUnUsedCredits(double unUsedCredits) {
		this.unUsedCredits = unUsedCredits;
	}

	public void setDepositIn(Account depositIn) {
		this.depositIn = depositIn;
	}

	public void setTotalAppliedCredits(double totalAppliedCredits) {
		this.totalAppliedCredits = totalAppliedCredits;
	}

	// public TaxCode getVATCode() {
	// return VATCode;
	// }
	//
	// public void setVATCode(TaxCode code) {
	// VATCode = code;
	// }
	//
	// public double getVATFraction() {
	// return VATFraction;
	// }
	//
	// public void setVATFraction(double fraction) {
	// VATFraction = fraction;
	// }

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_CUSTOMER;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_RECEIVE_PAYMENT;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.customer;
	}

	@Override
	public void onEdit(Transaction clonedObject) {
		ReceivePayment receivePayment = (ReceivePayment) clonedObject;

		super.onEdit(receivePayment);

		if (isDraftOrTemplate()) {
			return;
		}

		/**
		 * If present transaction is deleted or voided & the previous
		 * transaction is not voided then it is entered into the loop
		 */
		Session session = HibernateUtil.getCurrentSession();
		if (this.isVoid() && !receivePayment.isVoid()) {

			this.depositIn.updateCurrentBalance(this, this.amount,
					currencyFactor);
			this.depositIn.onUpdate(session);

			if (this.creditsAndPayments != null) {
				// this.creditsAndPayments.setTransaction(null);
				// session.delete(this.creditsAndPayments);
				this.creditsAndPayments = null;
			}

			this.totalCashDiscount = 0.0;
			this.totalWriteOff = 0.0;
			this.totalAppliedCredits = 0.0;
			for (TransactionReceivePayment trp : this.transactionReceivePayment) {
				trp.setIsVoid(Boolean.TRUE);
				session.update(trp);
				trp.onUpdate(session);
			}

			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;

			Account tdsAccount = getTDSAccount();
			if (tdsAccount != null) {
				tdsAccount.updateCurrentBalance(this, tdsTotal, currencyFactor);
				session.save(tdsAccount);
			}
		} else {

			if (this.depositIn.getID() != receivePayment.getDepositIn().getID()
					|| isCurrencyFactorChanged()
					|| !DecimalUtil.isEquals(this.amount,
							receivePayment.getAmount())) {
				Account old = receivePayment.getDepositIn();
				old.updateCurrentBalance(this, receivePayment.getAmount(),
						receivePayment.getCurrencyFactor());
				session.update(old);
				depositIn.updateCurrentBalance(this, -this.amount,
						currencyFactor);
				session.update(depositIn);
			}

			if (!DecimalUtil.isEquals(this.total, receivePayment.getTotal())
					|| isCurrencyFactorChanged()) {
				customer.updateBalance(session, this,
						-receivePayment.getTotal(),
						receivePayment.getCurrencyFactor());

				customer.updateBalance(session, this, this.total);
			}

			if (!DecimalUtil.isEquals(this.tdsTotal,
					receivePayment.getTdsTotal())) {
				Account tdsAccount = getTDSAccount();
				if (tdsAccount != null) {
					tdsAccount.updateCurrentBalance(this,
							receivePayment.getTdsTotal(),
							receivePayment.getCurrencyFactor());
					tdsAccount.updateCurrentBalance(this, -tdsTotal,
							currencyFactor);
					session.save(tdsAccount);
				}
			}

			if (receivePayment.getCreditsAndPayments() != null) {
				voidCreditsAndPayments(receivePayment);
				receivePayment.creditsAndPayments = null;
			}

			if (!DecimalUtil.isEquals(this.unUsedPayments, 0D)) {
				if (creditsAndPayments != null
						&& DecimalUtil.isEquals(
								creditsAndPayments.creditAmount, 0.0d)) {
					creditsAndPayments.update(this);

				} else {
					creditsAndPayments = new CreditsAndPayments(this);
				}
				this.setCreditsAndPayments(creditsAndPayments);
				session.save(creditsAndPayments);
			}
			// for (TransactionReceivePayment tReceivePaymentOld :
			// receivePayment
			// .getTransactionReceivePayment()) {
			// for (TransactionCreditsAndPayments tcp : tReceivePaymentOld
			// .getTransactionCreditsAndPayments()) {
			// boolean isExists = false;
			// for (TransactionReceivePayment tReceivePayment : this
			// .getTransactionReceivePayment()) {
			// List<TransactionCreditsAndPayments> transactionCreditsAndPayments
			// = tReceivePayment
			// .getTransactionCreditsAndPayments();
			// if (transactionCreditsAndPayments != null)
			// for (TransactionCreditsAndPayments tcp2 :
			// transactionCreditsAndPayments) {
			// if (tcp.getCreditsAndPayments().getID() == tcp2
			// .getCreditsAndPayments().getID()) {
			// isExists = true;
			// break;
			// }
			// }
			//
			// }
			// if (!isExists) {
			// tcp.onEditTransaction(-tcp.amountToUse);
			// tcp.amountToUse = 0.0;
			// session.saveOrUpdate(tcp.getCreditsAndPayments());
			// }
			// }
			//
			// tReceivePaymentOld.doReverseEffect(true);
			// }

			cleanTransactionitems(receivePayment);
		}
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			this.depositIn.updateCurrentBalance(this, this.amount,
					currencyFactor);
			this.depositIn.onUpdate(session);

			// if (this.creditsAndPayments != null) {
			// this.creditsAndPayments.setTransaction(null);
			// session.delete(this.creditsAndPayments);
			// this.creditsAndPayments = null;
			// }

			this.totalCashDiscount = 0.0;
			this.totalWriteOff = 0.0;
			this.totalAppliedCredits = 0.0;
			for (TransactionReceivePayment trp : this.transactionReceivePayment) {
				trp.setIsVoid(Boolean.TRUE);
				session.update(trp);
				trp.onUpdate(session);
			}

			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		}
		return super.onDelete(session);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// Kumar said that no need to check the condition to void ReceivePayment
		// eventhough the DepostitIn account is MakeDeposited.

		/**
		 * If Receive Payment is already voided or deleted, we can't edit it
		 */
		// ReceivePayment receivePayment = (ReceivePayment) clientObject;
		// if ((this.isVoid && !receivePayment.isVoid)
		// || (this.isDeleted() && !receivePayment.isDeleted())) {
		// throw new InvalidOperationException(
		// "You can't edit ReceivePayment, since it is Voided or Deleted");
		// }

		if (!UserUtils.canDoThis(ReceivePayment.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		return super.canEdit(clientObject);
	}

	protected void checkForReconciliation(Transaction transaction)
			throws AccounterException {

		if (getReconciliationItems() == null
				|| getReconciliationItems().isEmpty()) {
			return;
		}
		ReceivePayment receivePayment = (ReceivePayment) transaction;
		for (ReconciliationItem item : getReconciliationItems()) {
			if (item.getReconciliation().getAccount().getID() == getDepositIn()
					.getID() && this.amount != receivePayment.amount) {
				throw new AccounterException(
						AccounterException.ERROR_TRANSACTION_RECONCILIED);
			}
		}
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Override
	public Map<Account, Double> getEffectingAccountsWithAmounts() {
		Map<Account, Double> map = super.getEffectingAccountsWithAmounts();
		map.put(depositIn, amount);
		map.put(creditsAndPayments.getPayee().getAccount(),
				creditsAndPayments.getEffectingAmount());
		return map;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.receivePayment()).gap();
		w.put(messages.paymentNo(), this.number);

		w.put(messages.date(), this.transactionDate.toString()).gap();

		w.put(messages.name(), this.customer.name);
		w.put(messages.currency(), this.currencyFactor).gap();

		w.put(messages.amount(), this.total).gap();

		w.put(messages.paymentMethod(), this.paymentMethod).gap();

		w.put(messages.memo(), this.memo).gap();

		w.put(messages.details(), this.transactionReceivePayment);

	}

	/**
	 * @return the tdsTotal
	 */
	public double getTdsTotal() {
		return tdsTotal;
	}

	/**
	 * @param tdsTotal
	 *            the tdsTotal to set
	 */
	public void setTdsTotal(double tdsTotal) {
		this.tdsTotal = tdsTotal;
	}

	@Override
	protected void updatePayee(boolean onCreate) {
		double amount = onCreate ? total : -total;
		customer.updateBalance(HibernateUtil.getCurrentSession(), this, amount);
	}
}
