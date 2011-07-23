package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * A transaction which let us to write the checks to the payees. Payee involved
 * four types. {@link Customer},{@link Vendor},{@link TaxAgency} or
 * {@link TAXAgency}, {@link SalesPerson}. It also involves a payFrom account
 * which works a debit account for this transaction.
 * 
 * @author Chandan
 * 
 */
public class WriteCheck extends Transaction implements Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -672683575159440449L;

	public static final int TYPE_CUSTOMER = 1;
	public static final int TYPE_VENDOR = 2;
	public static final int TYPE_TAX_AGENCY = 4;
	public static final int TYPE_EMPLOYEE = 3;

	public static final String IS_TO_BE_PRINTED = null;

	/**
	 * The person to whom we are creating this write check
	 */
	int payToType;

	/**
	 * Through which account we are going to write Check
	 */
	@ReffereredObject
	Account bankAccount;

	double balance;

	/**
	 * The Customer to whom we are writing this check.
	 */
	@ReffereredObject
	Customer customer;

	/**
	 * The Vendor to whom we are writing this check.
	 */
	@ReffereredObject
	Vendor vendor;

	/**
	 * The TaxAgency to whom we are writing this check.
	 */
	@ReffereredObject
	TAXAgency taxAgency;

	/**
	 * Address of the Payee to whom we are writing this Check
	 */
	Address address;

	double amount;

	/**
	 * This will decide whether the Check is Issued or not
	 */
	boolean toBePrinted;

	SalesPerson salesPerson;

	/**
	 * String representation of the Check amount in words.
	 */
	String inWords;

	/**
	 * this is the Auto generated number to the check
	 */
	String checkNumber = WriteCheck.IS_TO_BE_PRINTED;

	// transient boolean isImported;

	/**
	 * @return the id
	 */
	public WriteCheck() {
		// TODO
	}

	@Override
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	@Override
	public void setID(long id){
		this.id = id;
	}

	/**
	 * @return the version
	 */
	@Override
	public int getVersion() {
		return version;
	}

	/**
	 * @return the payToType
	 */
	public int getPayToType() {
		return payToType;
	}

	/**
	 * @param payToType
	 *            the payToType to set
	 */
	public void setPayToType(int payToType) {
		this.payToType = payToType;
	}

	/**
	 * @return the bankAccount
	 */
	public Account getBankAccount() {
		return bankAccount;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
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
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the taxAgency
	 */
	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param taxAgency
	 *            the taxAgency to set
	 */
	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
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
	 * @return the toBePrinted
	 */
	public boolean isToBePrinted() {
		return toBePrinted;
	}

	/**
	 * @return the inWords
	 */
	public String getInWords() {
		return inWords;
	}

	/**
	 * @return the salesPerson
	 */
	public SalesPerson getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @return the checkNumber
	 */
	public String getCheckNumber() {
		return checkNumber;
	}

	@Override
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	@Override
	public Account getEffectingAccount() {
		return this.bankAccount;
	}

	@Override
	public Payee getPayee() {
		return null;
	}

	@Override
	public String toString() {
		return AccounterConstants.TYPE_WRITE_CHECK;
	}

	public void setBankAccount(Account bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;

	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	@Override
	public int getTransactionCategory() {
		if (this.getCustomer() != null)
			return Transaction.CATEGORY_CUSTOMER;
		else
			return Transaction.CATEGORY_VENDOR;
	}

	@Override
	public void setImported(boolean isImported) {
		this.isImported = isImported;
		for (TransactionItem ti : this.transactionItems) {
			ti.setImported(true);
		}
	}

	@Override
	public Payee getInvolvedPayee() {

		if (this.getPayee() instanceof Customer)
			return this.customer;
		else if (this.getPayee() instanceof Vendor)
			return this.vendor;
		else if (this.getPayee() instanceof SalesPerson)
			return this.salesPerson;
		else
			return this.taxAgency;

	}

	public boolean equals(WriteCheck obj) {
		if ((this.bankAccount != null && obj.bankAccount != null) ? (this.bankAccount
				.equals(obj.bankAccount))
				: true && (!DecimalUtil.isEquals(this.payToType, 0) && !DecimalUtil
						.isEquals(obj.payToType, 0)) ? DecimalUtil.isEquals(
						this.payToType, obj.payToType)
						: true && (!DecimalUtil.isEquals(this.amount, 0.0) && !DecimalUtil
								.isEquals(obj.amount, 0.0)) ? DecimalUtil
								.isEquals(this.amount, obj.amount)
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
	public boolean onSave(Session session) throws CallbackException {
		if (isImported) {
			return false;
		}
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		this.paymentMethod = AccounterConstants.PAYMENT_METHOD_CHECK;
		if (!isToBePrinted()) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		}
		setType(Transaction.TYPE_WRITE_CHECK);
		super.onSave(session);

		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		// super.onUpdate(session);
		// this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		// if (this.transactionItems != null) {
		// for (TransactionItem ti : this.transactionItems) {
		// if (ti instanceof Lifecycle) {
		// Lifecycle lifeCycle = (Lifecycle) ti;
		// lifeCycle.onUpdate(session);
		// }
		// }
		// }
		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) {

		WriteCheck writeCheck = (WriteCheck) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		if (!this.isVoid && !writeCheck.isVoid) {

			if (this.bankAccount.id != writeCheck.bankAccount.id) {

				Account account = (Account) session.get(Account.class,
						writeCheck.bankAccount.id);
				account.updateCurrentBalance(writeCheck, -writeCheck.total);
				session.update(account);
				account.onUpdate(session);

				writeCheck.total = 0.0;
			}

			this.bankAccount.updateCurrentBalance(this, this.total
					- writeCheck.total);
			this.bankAccount.onUpdate(session);
		}
		this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;

		if ((writeCheck.paymentMethod
				.equals(AccounterConstants.PAYMENT_METHOD_CHECK))
				&& (!this.paymentMethod
						.equals(AccounterConstants.PAYMENT_METHOD_CHECK))) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		}

		HibernateUtil.getCurrentSession().saveOrUpdate(this);
		super.onEdit(writeCheck);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {

		if ((this.bankAccount.equals("Un Deposited Funds"))
				&& this.transactionMakeDepositEntries != null) {
			throw new InvalidOperationException(
					"You can't void or edit because it has been deposited from Undeposited Funds");
		}

		return true;
	}

}
