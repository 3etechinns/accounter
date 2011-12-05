package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * Payee is the object which represents a real-time entity of either
 * {@link Customer}, {@link Vendor}, {@link SalesPerson} or {@link TAXAgency}
 * 
 * @author Chandan
 * 
 */
public abstract class Payee extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3882317049281524783L;

	public static final int TYPE_NONE = 0;

	public static final int TYPE_CUSTOMER = 1;

	public static final int TYPE_VENDOR = 2;

	public static final int TYPE_TAX_AGENCY = 4;

	public static final int TYPE_EMPLOYEE = 3;

	FinanceDate payeeSince;
	int type;

	// FinanceDate date;

	double balance;

	double currencyFactor = 1;

	/**
	 * The till date up to which the Specified Opening balance of the Customer
	 * is for.
	 */
	FinanceDate balanceAsOf;

	String name;
	String fileAs;

	Set<Address> address = new HashSet<Address>();
	Set<Phone> phoneNumbers = new HashSet<Phone>();
	Set<Fax> faxNumbers = new HashSet<Fax>();
	Set<Contact> contacts = new HashSet<Contact>();
	String webPageAddress;
	boolean isActive = Boolean.TRUE;
	String memo;
	String paymentMethod;
	boolean isOpeningBalanceEditable = Boolean.TRUE;
	private String phoneNo;
	private String faxNo;

	transient private double previousOpeningBal;
	// UKvariables
	// boolean isEUVATExemptPayee;
	String VATRegistrationNumber;
	TAXCode TAXCode;
	TAXItem TAXItem;

	public TAXItem getTAXItem() {
		return TAXItem;
	}

	public void setTAXItem(TAXItem tAXItem) {
		TAXItem = tAXItem;
	}

	private String email;
	boolean isDefault;

	String bankAccountNo;
	String bankName;
	String bankBranch;
	String panNumber;
	String cstNumber;
	String serviceTaxRegistrationNumber;
	String tinNumber;
	protected Currency currency;

	public transient boolean isOnSaveProccessed;

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isOpeningBalanceEditable() {
		return isOpeningBalanceEditable;
	}

	public void setOpeningBalanceEditable(boolean isOpeningBalanceEditable) {
		this.isOpeningBalanceEditable = isOpeningBalanceEditable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileAs() {
		return fileAs;
	}

	public void setFileAs(String fileAs) {
		this.fileAs = fileAs;
	}

	/**
	 * @return the vATRegistrationNumber
	 */
	public String getVATRegistrationNumber() {
		return VATRegistrationNumber;
	}

	/**
	 * @param registrationNumber
	 *            the vATRegistrationNumber to set
	 */
	public void setVATRegistrationNumber(String registrationNumber) {
		VATRegistrationNumber = registrationNumber;
	}

	/**
	 * @return the vATCode
	 */
	public TAXCode getTAXCode() {
		return TAXCode;
	}

	/**
	 * @param code
	 *            the vATCode to set
	 */
	public void setTAXCode(TAXCode code) {
		TAXCode = code;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	protected double openingBalance = 0D;

	private transient double previousCurrencyFactor = 1;

	/**
	 * @return the openingBalance
	 */
	public double getOpeningBalance() {
		return openingBalance;
	}

	/**
	 * @param openingBalance
	 *            the openingBalance to set
	 */
	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	// /**
	// * @return the date
	// */
	// public FinanceDate getDate() {
	// return date;
	// }
	//
	// /**
	// * @param date
	// * the date to set
	// */
	// public void setDate(FinanceDate date) {
	// this.date = date;
	// }

	public Set<Address> getAddress() {
		return address;
	}

	public void setAddress(Set<Address> address) {
		this.address = address;
	}

	public Set<Phone> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(Set<Phone> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public Set<Fax> getFaxNumbers() {
		return faxNumbers;
	}

	public void setFaxNumbers(Set<Fax> faxNumbers) {
		this.faxNumbers = faxNumbers;
	}

	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	public String getWebPageAddress() {
		return webPageAddress;
	}

	public void setWebPageAddress(String webPageAddress) {
		this.webPageAddress = webPageAddress;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public boolean isCustomer() {
		return this != null && this instanceof Customer;
	}

	public boolean isVendor() {
		return this != null && this instanceof Vendor;
	}

	public boolean isTaxAgency() {
		return this != null && this instanceof TAXAgency;
	}

	public FinanceDate getPayeeSince() {
		return payeeSince;
	}

	public void setPayeeSince(FinanceDate payeeSince) {
		this.payeeSince = payeeSince;
	}

	/**
	 * 
	 * @param session
	 * @param transaction
	 * @param amount
	 * @param object
	 * 
	 *            method for reverse back effect on the Account related to this
	 *            Payee
	 */

	public void updateBalance(Session session, Transaction transaction,
			double amount) {
		updateBalance(session, transaction, amount, true);
	}

	/**
	 * 
	 * @param session
	 * @param transaction
	 * @param amount
	 * @param object
	 * 
	 *            method for reverse back effect on the Account related to this
	 *            Payee
	 */

	public void updateBalance(Session session, Transaction transaction,
			double amount, boolean updateBalanceInPayeeCurrency) {

		/**
		 * To check whether this payee is Customer, Vendor or Tax Agency.
		 * Depending on the type we will either decrease or increase the balance
		 * of the corresponding Payee.
		 */
		String tempStr = this.getName() + " Balance has been updated from "
				+ this.balance;

		if (this.type == TYPE_CUSTOMER) {
			if (updateBalanceInPayeeCurrency) {
				this.balance -= amount;
			}
		} else if (this.type == TYPE_VENDOR || this.type == TYPE_TAX_AGENCY) {
			if (updateBalanceInPayeeCurrency) {
				this.balance += amount;
			}
		}

		/**
		 * Getting the Account related to this Payee.
		 */
		Account account = this.getAccount();

		// /**
		// * In case of ItemReceipt instead of Updating Accounts Payable balance
		// * we should update Pending Item Receipt Account Balance.
		// */
		// if (transaction instanceof ItemReceipt) {
		// List<Account> accounts = (List<Account>) session
		// .createQuery(
		// "from com.vimukti.accounter.core.Account a where a.name = ?")
		// .setParameter(0, AccounterConstants.PENDING_ITEM_RECEIPTS)
		// .list();
		// if (accounts != null && !accounts.isEmpty()
		// && accounts.get(0) != null) {
		// account = accounts.get(0);
		// }
		// }

		/**
		 * Update the account balance by the given amount if the account is not
		 * null
		 */
		if (account != null) {
			account.updateCurrentBalance(transaction, amount,
					transaction.getCurrencyFactor());
			session.update(account);
			account.onUpdate(session);
		}

		/**
		 * once if any Payee got created with opening balance then that Payee
		 * balance should not be editable. So we will make it as un editable.
		 */
		// isOpeningBalanceEditable = Boolean.FALSE;
		ChangeTracker.put(this);
	}

	public abstract Account getAccount();

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getPANno() {
		return panNumber;
	}

	public void setPANno(String pANno) {
		panNumber = pANno;
	}

	public String getCSTno() {
		return cstNumber;
	}

	public void setCSTno(String cSTno) {
		cstNumber = cSTno;
	}

	public String getServiceTaxRegistrationNo() {
		return serviceTaxRegistrationNumber;
	}

	public void setServiceTaxRegistrationNo(String serviceTaxRegistrationNo) {
		this.serviceTaxRegistrationNumber = serviceTaxRegistrationNo;
	}

	public String getTINNumber() {
		return tinNumber;
	}

	public void setTINNumber(String tINNumber) {
		tinNumber = tINNumber;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	protected void modifyJournalEntry(JournalEntry existEntry) {
		Session session = HibernateUtil.getCurrentSession();
		session.delete(existEntry);
		if (!DecimalUtil.isEquals(this.openingBalance, 0)) {
			JournalEntry journalEntry = createJournalEntry();
			session.save(journalEntry);
		}
	}

	protected JournalEntry createJournalEntry() {
		String number = NumberUtils.getNextTransactionNumber(
				Transaction.TYPE_JOURNAL_ENTRY, getCompany());

		JournalEntry journalEntry = new JournalEntry();
		journalEntry.setInvolvedPayee(this);
		journalEntry.setCompany(getCompany());
		journalEntry.number = number;
		journalEntry.transactionDate = balanceAsOf;
		journalEntry.memo = "Opening Balance";
		journalEntry.balanceDue = getOpeningBalance();

		List<TransactionItem> items = new ArrayList<TransactionItem>();
		// Line 1
		TransactionItem item1 = new TransactionItem();
		item1.setAccount(getCompany().getOpeningBalancesAccount());
		item1.setType(TransactionItem.TYPE_ACCOUNT);
		item1.setDescription(getName());
		if (this instanceof Customer) {
			item1.setLineTotal(-1 * getOpeningBalance());
		} else {
			item1.setLineTotal(getOpeningBalance());
		}
		items.add(item1);

		TransactionItem item2 = new TransactionItem();
		item2.setAccount(getAccount());
		item2.setType(TransactionItem.TYPE_ACCOUNT);
		item2.setDescription(AccounterServerConstants.MEMO_OPENING_BALANCE);
		if (this instanceof Customer) {
			item2.setLineTotal(getOpeningBalance());
		} else {
			item2.setLineTotal(-1 * getOpeningBalance());
		}
		items.add(item2);

		journalEntry.setDebitTotal(items.get(1).getLineTotal());
		journalEntry.setCreditTotal(items.get(0).getLineTotal());

		journalEntry.setTransactionItems(items);

		journalEntry.setCurrency(currency);
		journalEntry.setCurrencyFactor(currencyFactor);

		return journalEntry;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	/**
	 * @return the balanceAsOf
	 */
	public FinanceDate getBalanceAsOf() {
		return balanceAsOf;
	}

	public void setBalanceAsOf(FinanceDate balanceAsOf) {
		this.balanceAsOf = balanceAsOf;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (currency == null) {
			this.currency = getCompany().getPrimaryCurrency();
		}
		return super.onSave(session);
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		super.onUpdate(session);
		if (!DecimalUtil.isEquals(this.openingBalance, this.previousOpeningBal)
				|| !DecimalUtil.isEquals(this.currencyFactor,
						this.previousCurrencyFactor)) {

			this.balance -= previousOpeningBal;
			this.balance += openingBalance;

			JournalEntry existEntry = (JournalEntry) session
					.getNamedQuery("getJournalEntryForCustomer")
					.setLong("id", this.id).uniqueResult();
			if (existEntry == null) {
				JournalEntry journalEntry = createJournalEntry();
				session.save(journalEntry);
			} else {
				modifyJournalEntry(existEntry);
			}
		}

		ChangeTracker.put(this);
		return false;

	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		this.previousOpeningBal = openingBalance;
		this.previousCurrencyFactor = currencyFactor;
	}

	public void clearOpeningBalance() {
		balance -= openingBalance;
		openingBalance = 0.00D;
	}
}
