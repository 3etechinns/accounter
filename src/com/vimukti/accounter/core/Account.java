package com.vimukti.accounter.core;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * @author Suresh
 * @since 1.0.0
 * 
 *        Account is one of the Basic object in Finance with which we can keep
 *        track the money transfer with different type of Transactions with our
 *        Customers or Suppliers. It represents how the money is coming in or
 *        going out in our Business. We have several type of Accounts to manage
 *        our accounting software and we have some System Accounts (Opening
 *        Balance, Accounts Receivable, Accounts Payable, Un Deposited Funds).
 *        The existence of these System Accounts can not be avoidable. Each
 *        Account Object has its own values for opening balance, current balance
 *        and total balance properties. opening balance property hold the
 *        initial amount given for the account while creating. Current Balance
 *        can hold the amount which is used by any transaction. Total balance is
 *        the sum of it's current balance and it's all childs Total Balances.
 * 
 * 
 */
@SuppressWarnings( { "serial", "unchecked" })
public class Account extends CreatableObject implements IAccounterServerCore, Lifecycle {

	Logger log = Logger.getLogger(Account.class);

	int version;

	// public static final int BASETYPE_INCOME = 1;
	// public static final int BASETYPE_EXPENSE = 2;
	// public static final int BASETYPE_ASSET = 3;
	// public static final int BASETYPE_LIABILITY = 4;
	// public static final int BASETYPE_EQUITY = 5;
	/**
	 * Any account must belong from any of these type
	 */
	public static final int TYPE_CASH = 1;
	public static final int TYPE_BANK = 2;
	public static final int TYPE_ACCOUNT_RECEIVABLE = 3;// For UK Sales Ledger
	public static final int TYPE_OTHER_CURRENT_ASSET = 4;
	public static final int TYPE_INVENTORY_ASSET = 5;// For UK Stock Asset
	public static final int TYPE_FIXED_ASSET = 6;
	public static final int TYPE_OTHER_ASSET = 7;
	public static final int TYPE_ACCOUNT_PAYABLE = 8;// For UK Purchase Ledger
	public static final int TYPE_OTHER_CURRENT_LIABILITY = 9;
	public static final int TYPE_CREDIT_CARD = 10;
	public static final int TYPE_PAYROLL_LIABILITY = 11;
	public static final int TYPE_LONG_TERM_LIABILITY = 12;
	public static final int TYPE_EQUITY = 13;
	public static final int TYPE_INCOME = 14;
	public static final int TYPE_COST_OF_GOODS_SOLD = 15;
	public static final int TYPE_EXPENSE = 16;
	public static final int TYPE_OTHER_INCOME = 17;
	public static final int TYPE_OTHER_EXPENSE = 18;
	/**
	 * Types of cash flow
	 */
	public static final int CASH_FLOW_CATEGORY_FINANCING = 1;
	public static final int CASH_FLOW_CATEGORY_INVESTING = 2;
	public static final int CASH_FLOW_CATEGORY_OPERATING = 3;
	/**
	 * Types of bank
	 */
	public static final int BANK_ACCCOUNT_TYPE_NONE = 0;
	public static final int BANK_ACCCOUNT_TYPE_CHECKING = 1;
	public static final int BANK_ACCCOUNT_TYPE_SAVING = 2;
	public static final int BANK_ACCCOUNT_TYPE_MONEY_MARKET = 3;

	public static final int BASETYPE_ASSET = 1;
	public static final int BASETYPE_LIABILITY = 2;
	public static final int BASETYPE_EQUITY = 3;

	public static final int BASETYPE_ORDINARY_INCOME_OR_EXPENSE = 4;
	public static final int BASETYPE_OTHER_INCOME_OR_EXPENSE = 5;

	public static final int SUBBASETYPE_CURRENT_ASSET = 1;
	public static final int SUBBASETYPE_FIXED_ASSET = 2;
	public static final int SUBBASETYPE_OTHER_ASSET = 3;
	public static final int SUBBASETYPE_CURRENT_LIABILITY = 4;
	public static final int SUBBASETYPE_LONG_TERM_LIABILITY = 5;
	public static final int SUBBASETYPE_EQUITY = 6;
	public static final int SUBBASETYPE_INCOME = 7;
	public static final int SUBBASETYPE_COST_OF_GOODS_SOLD = 8;
	public static final int SUBBASETYPE_EXPENSE = 9;

	public static final int SUBBASETYPE_OTHER_EXPENSE = 10;

	public static final int GROUPTYPE_CASH = 1;


	/**
	 * To decide about the type of the {@link Account}
	 */
	int type;

	/**
	 * This is to categorise again the existing basic Accounting types for the
	 * purpose of Reports.
	 */
	int baseType;

	/**
	 * This is to categorise again the existing Accounting Base Types for the
	 * purpose of Reports.
	 */
	int subBaseType;

	/**
	 * This is to categorise some of the Accounts to particular group for the
	 * purpose of Reports.
	 */
	int groupType;

	/**
	 * This is to store Account nominal code.
	 */
	String number;

	/**
	 * This is to store Account name.
	 */
	String name;

	/**
	 * This is to make an Account active or in active so that we can avoid all
	 * non interested Accounts in Finance when ever we want.
	 */
	boolean isActive;

	/**
	 * This is to hold the parent of the currently created Account. The value
	 * for this property is null if the currently created Account has no parent.
	 */
	Account parent;

	/**
	 * This property is to hold the cashFlowCategory of an Account. It will be
	 * useful for Cash Flow Category Report.
	 */
	int cashFlowCategory;

	/**
	 * This property is to hold the balance of the Account at the moment of
	 * creation.
	 */
	double openingBalance = 0.0D;

	/**
	 * This will hold the FinanceDate up to which the opening balance of this
	 * Account is.
	 */
	FinanceDate asOf;

	// OneZeroNineNineCategory oneZeroNineNineCategory;

	boolean isConsiderAsCashAccount;

	String comment;

	Bank bank;// for Bank name
	int bankAccountType;
	String bankAccountNumber;

	/**
	 * For Credit Card Account
	 */
	double creditLimit;
	String cardOrLoanNumber;

	/**
	 * this will decide the behaviour of the account whether the amount of this
	 * account should decrease or increase depends on the Transaction.
	 */
	boolean isIncrease;

	/**
	 * To keep track of the currently updated balance of an Account in any
	 * Transaction.
	 */
	double currentBalance = 0.0D;

	/**
	 * This will hold it's total balance and sum of all it's child Accounts.
	 */
	double totalBalance = 0.0D;

	/**
	 * This will decide whether an Account's opening balance can be editable or
	 * not
	 */
	boolean isOpeningBalanceEditable = Boolean.TRUE;

	/**
	 * This will represent the whole path of the Account (recursively it's name
	 * preceded with it's parent name)
	 */
	String hierarchy;

	/**
	 * This will hold a set of {@link Budget}
	 */
	Set<Budget> budget = new HashSet<Budget>();

	/**
	 * This is not Persistent variable, logically we have used in our code.
	 */
	transient AccountTransaction accountTransaction;

	/**
	 * This is not Persistent variable, logically we have used in our code.
	 */
	transient Account oldParent;


	/**
	 * This will index all Accounts.
	 */
	String flow;

	String createdBy;
	String lastModifier;
	FinanceDate createdDate;
	FinanceDate lastModifiedDate;

	Account linkedAccumulatedDepreciationAccount;

	boolean isDefault;

	Map<Integer, Double> monthViceAmounts = new HashMap();

	transient private boolean isOnSaveProccessed;

	/**
	 * Constructor of Account class
	 */
	public Account() {

	}

	/**
	 * Constructor of Account class
	 * 
	 * @param type
	 *            Type of the account
	 * @param number
	 * @param name
	 *            Name of the account
	 * @param isActive
	 *            status of the account
	 * @param parent
	 *            The parent of the account
	 * @param cashFlowCategory
	 *            Cash Flow Category
	 * @param openingBalance
	 *            Opening balance
	 * @param isConsiderAsCashAccount
	 *            cash account status
	 * @param comment
	 *            Comment on the account
	 * @param bank
	 *            Bank of the account
	 * @param bankAccountType
	 *            Type of the bank
	 * @param bankAccountNumber
	 *            Bank account number
	 * @param creditLimit
	 *            Credit limit on the bank account
	 * @param cardOrLoanNumber
	 *            Credit card or loan account number
	 * @param isIncrease
	 *            Behaviour of cash (true Increase else decrease )
	 * @param isOpeningBalanceEditable
	 *            Edit status of opening balance
	 * @param openingBalanceAccount
	 *            Not being used
	 * @param flow
	 *            The parent and child flow of account
	 */
	public Account(int type, String number, String name, boolean isActive,
			Account parent, int cashFlowCategory, double openingBalance,
			boolean isConsiderAsCashAccount, String comment, Bank bank,
			int bankAccountType, String bankAccountNumber, double creditLimit,
			String cardOrLoanNumber, boolean isIncrease,
			boolean isOpeningBalanceEditable, Account openingBalanceAccount,
			String flow, boolean isDefaultAccount, FinanceDate asOf) {

		this.type = type;
		this.number = number;
		this.name = name;
		this.isActive = isActive;
		this.parent = parent;
		this.cashFlowCategory = cashFlowCategory;
		this.openingBalance = openingBalance;
		this.isConsiderAsCashAccount = isConsiderAsCashAccount;
		this.comment = comment;
		this.bank = bank;
		this.bankAccountType = bankAccountType;
		this.bankAccountNumber = bankAccountNumber;
		this.creditLimit = creditLimit;
		this.cardOrLoanNumber = cardOrLoanNumber;
		this.isIncrease = isIncrease;
		this.isOpeningBalanceEditable = isOpeningBalanceEditable;
		this.flow = flow;
		this.isDefault = isDefaultAccount;
		this.asOf = asOf;
		this.createdDate = new FinanceDate();
	}

	/**
	 * Constructor of Account class
	 * 
	 * @param session
	 * @param clientaccount
	 */
	public Account(Session session, ClientAccount clientaccount) {
		int type = clientaccount.getType();
		if (type == Account.TYPE_INCOME || type == Account.TYPE_OTHER_INCOME
				|| type == Account.TYPE_CREDIT_CARD
				|| type == Account.TYPE_PAYROLL_LIABILITY
				|| type == Account.TYPE_OTHER_CURRENT_LIABILITY
				|| type == Account.TYPE_LONG_TERM_LIABILITY
				|| type == Account.TYPE_EQUITY
				|| type == Account.TYPE_ACCOUNT_PAYABLE) {
			clientaccount.setIncrease(Boolean.TRUE);
		} else {
			clientaccount.setIncrease(Boolean.FALSE);
		}

	}

	public Map<Integer, Double> getMonthViceAmounts() {
		return monthViceAmounts;
	}

	public void setMonthViceAmounts(Map<Integer, Double> monthViceAmounts) {
		this.monthViceAmounts = monthViceAmounts;
	}

	/**
	 * @return the budget
	 */
	public Set<Budget> getBudget() {
		return budget;
	}

	/**
	 * @param budget
	 *            the budget to set
	 */
	public void setBudget(Set<Budget> budget) {
		this.budget = budget;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	// /**
	// * @param version
	// * the version to set
	// */
	// public void setVersion(int version) {
	// this.version = version;
	// }

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	// public void setID(long id){
	// this.id = id;
	// }
	//
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	public Account getLinkedAccumulatedDepreciationAccount() {
		return linkedAccumulatedDepreciationAccount;
	}

	public void setLinkedAccumulatedDepreciationAccount(
			Account linkedAccumulatedDepreciationAccount) {
		this.linkedAccumulatedDepreciationAccount = linkedAccumulatedDepreciationAccount;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public int getBaseType() {
		return baseType;
	}

	public void setBaseType(int baseType) {
		this.baseType = baseType;
	}

	public int getSubBaseType() {
		return subBaseType;
	}

	public void setSubBaseType(int subBaseType) {
		this.subBaseType = subBaseType;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	// public void setType(int type) {
	// this.type = type;
	// }
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	// public void setNumber(String number) {
	// this.number = number;
	// }
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	// public void setName(String name) {
	// this.name = name;
	// }
	/**
	 * @return the isActive
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the parent
	 */
	public Account getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Account parent) {
		this.parent = parent;
	}

	/**
	 * @return the cashFlowCategory
	 */
	public int getCashFlowCategory() {
		return cashFlowCategory;
	}

	/**
	 * @param cashFlowCategory
	 *            the cashFlowCategory to set
	 */
	// public void setCashFlowCategory(int cashFlowCategory) {
	// this.cashFlowCategory = cashFlowCategory;
	// }
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
	// public void setOpeningBalance(double openingBalance) {
	// this.openingBalance = openingBalance;
	//
	// }
	/**
	 * @return the asOf
	 */
	public FinanceDate getAsOf() {
		return asOf;
	}

	/**
	 * @param asOf
	 *            the asOf to set
	 */
	// public void setAsOf(Date asOf) {
	// this.asOf = asOf;
	// }
	/**
	 * @return the isConsiderAsCashAccount
	 */
	public boolean isConsiderAsCashAccount() {
		return isConsiderAsCashAccount;
	}

	/**
	 * @param isConsiderAsCashAccount
	 *            the isConsiderAsCashAccount to set
	 */
	// public void setConsiderAsCashAccount(boolean isConsiderAsCashAccount) {
	// this.isConsiderAsCashAccount = isConsiderAsCashAccount;
	// }
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	// public void setComment(String comment) {
	// this.comment = comment;
	// }
	/**
	 * @return the bankName
	 */
	public Bank getBank() {
		return bank;
	}

	/**
	 * @param bankName
	 *            the bankName to set
	 */
	// public void setBank(Bank bank) {
	// this.bank = bank;
	// }
	/**
	 * @return the bankAccountType
	 */
	public int getBankAccountType() {
		return bankAccountType;
	}

	/**
	 * @param bankAccountType
	 *            the bankAccountType to set
	 */
	// public void setBankAccountType(int bankAccountType) {
	// this.bankAccountType = bankAccountType;
	// }
	/**
	 * @return the bankAccountNumber
	 */
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	/**
	 * @param bankAccountNumber
	 *            the bankAccountNumber to set
	 */
	// public void setBankAccountNumber(String bankAccountNumber) {
	// this.bankAccountNumber = bankAccountNumber;
	// }
	/**
	 * @return the creditLimit
	 */
	public double getCreditLimit() {
		return creditLimit;
	}

	/**
	 * @param creditLimit
	 *            the creditLimit to set
	 */
	// public void setCreditLimit(double creditLimit) {
	// this.creditLimit = creditLimit;
	// }
	/**
	 * @return the cardOrLoanNumber
	 */
	public String getCardOrLoanNumber() {
		return cardOrLoanNumber;
	}

	/**
	 * @param cardOrLoanNumber
	 *            the cardOrLoanNumber to set
	 */
	// public void setCardOrLoanNumber(String cardOrLoanNumber) {
	// this.cardOrLoanNumber = cardOrLoanNumber;
	// }
	/**
	 * @return the isIncrease
	 */
	public boolean isIncrease() {
		return isIncrease;
	}

	/**
	 * @param isIncrease
	 *            the isIncrease to set
	 */
	public void setIncrease(boolean isIncrease) {
		this.isIncrease = isIncrease;
	}

	/**
	 * @return the currentBalance
	 */
	public double getCurrentBalance() {
		return currentBalance;
	}

	/**
	 * @return the totalBalance
	 */
	public double getTotalBalance() {
		return totalBalance;
	}

	// /**
	// * @param currentBalance
	// * the currentBalance to set
	// */
	// public void setCurrentBalance(double currentBalance) {
	// this.currentBalance = currentBalance;
	// }

	// /**
	// * @param totalBalance
	// * the totalBalance to set
	// */
	// public void setTotalBalance(double totalBalance) {
	// this.totalBalance = totalBalance;
	// }

	/**
	 * @return the isOpeningBalanceEditable
	 */
	public boolean isOpeningBalanceEditable() {
		return isOpeningBalanceEditable;
	}

	/**
	 * @param isOpeningBalanceEditable
	 *            the isOpeningBalanceEditable to set
	 */
	public void setOpeningBalanceEditable(boolean isOpeningBalanceEditable) {
		this.isOpeningBalanceEditable = isOpeningBalanceEditable;
	}

	/**
	 * @return the hierarchy
	 */
	public String getHierarchy() {
		return hierarchy;
	}

	/**
	 * @param hierarchy
	 *            the hierarchy to set
	 */
	// public void setHierarchy(String hierarchy) {
	// this.hierarchy = hierarchy;
	// }
	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		FinanceLogger.log("Account {0} has been deleted", this.getName());
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(id);
		accounterCore.setObjectType(AccounterCoreType.ACCOUNT);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		this.oldParent = parent;

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;

		FinanceLogger.log("{0} OnSave method execution...........", this
				.getName());

		try {
			if (this.type == Account.TYPE_INCOME
					|| this.type == Account.TYPE_OTHER_INCOME
					|| this.type == Account.TYPE_CREDIT_CARD
					|| this.type == Account.TYPE_PAYROLL_LIABILITY
					|| this.type == Account.TYPE_OTHER_CURRENT_LIABILITY
					|| this.type == Account.TYPE_LONG_TERM_LIABILITY
					|| this.type == Account.TYPE_EQUITY
					|| this.type == Account.TYPE_ACCOUNT_PAYABLE) {
				this.isIncrease = true;
			} else {
				this.isIncrease = false;
			}
			if (this.type == Account.TYPE_INVENTORY_ASSET) {
				this.isOpeningBalanceEditable = false;
			}

			FinanceLogger.log("Account  {0}: {1} for the type: {2}", this.name,
					(isIncrease ? " IsIncrease becomes true" : ""), String
							.valueOf(this.type));

			switch (type) {

			case Account.TYPE_CASH:
				this.setBaseType(Account.BASETYPE_ASSET);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
				this.setGroupType(Account.GROUPTYPE_CASH);

				break;
			case Account.TYPE_BANK:
				this.setBaseType(Account.BASETYPE_ASSET);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
				this.setGroupType(Account.GROUPTYPE_CASH);

				break;
			case Account.TYPE_ACCOUNT_RECEIVABLE:
				this.setBaseType(Account.BASETYPE_ASSET);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
				break;
			case Account.TYPE_OTHER_CURRENT_ASSET:
				this.setBaseType(Account.BASETYPE_ASSET);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
				break;
			case Account.TYPE_INVENTORY_ASSET:
				this.setBaseType(Account.BASETYPE_ASSET);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
				break;
			case Account.TYPE_FIXED_ASSET:
				this.setBaseType(Account.BASETYPE_ASSET);
				this.setSubBaseType(Account.SUBBASETYPE_FIXED_ASSET);
				break;
			case Account.TYPE_OTHER_ASSET:
				this.setBaseType(Account.BASETYPE_ASSET);
				this.setSubBaseType(Account.SUBBASETYPE_OTHER_ASSET);
				break;
			case Account.TYPE_ACCOUNT_PAYABLE:
				this.setBaseType(Account.BASETYPE_LIABILITY);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_LIABILITY);
				break;
			case Account.TYPE_CREDIT_CARD:
				this.setBaseType(Account.BASETYPE_LIABILITY);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_LIABILITY);
				break;
			case Account.TYPE_OTHER_CURRENT_LIABILITY:
				this.setBaseType(Account.BASETYPE_LIABILITY);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_LIABILITY);
				break;
			case Account.TYPE_PAYROLL_LIABILITY:
				this.setBaseType(Account.BASETYPE_LIABILITY);
				this.setSubBaseType(Account.SUBBASETYPE_CURRENT_LIABILITY);
				break;
			case Account.TYPE_LONG_TERM_LIABILITY:
				this.setBaseType(Account.BASETYPE_LIABILITY);
				this.setSubBaseType(Account.SUBBASETYPE_LONG_TERM_LIABILITY);
				break;
			case Account.TYPE_EQUITY:
				this.setBaseType(Account.BASETYPE_EQUITY);
				this.setSubBaseType(Account.SUBBASETYPE_EQUITY);
				break;

			case Account.TYPE_INCOME:
				this.setBaseType(Account.BASETYPE_ORDINARY_INCOME_OR_EXPENSE);
				this.setSubBaseType(Account.SUBBASETYPE_INCOME);
				break;
			case Account.TYPE_COST_OF_GOODS_SOLD:
				this.setBaseType(Account.BASETYPE_ORDINARY_INCOME_OR_EXPENSE);
				this.setSubBaseType(Account.SUBBASETYPE_COST_OF_GOODS_SOLD);
				break;
			case Account.TYPE_EXPENSE:
				this.setBaseType(Account.BASETYPE_ORDINARY_INCOME_OR_EXPENSE);
				this.setSubBaseType(Account.SUBBASETYPE_EXPENSE);
				break;

			case Account.TYPE_OTHER_INCOME:
				this.setBaseType(Account.BASETYPE_OTHER_INCOME_OR_EXPENSE);
				this.setSubBaseType(Account.SUBBASETYPE_INCOME);
				break;

			case Account.TYPE_OTHER_EXPENSE:
				this.setBaseType(Account.BASETYPE_OTHER_INCOME_OR_EXPENSE);
				this.setSubBaseType(Account.SUBBASETYPE_OTHER_EXPENSE);
				break;

			}

			String className = this.getClass().getName();
			if (this.flow == null) {
				if (this.parent == null) {
					List l = session.createQuery(
							"select a.flow from " + className
									+ " a where a.id= (select max(a1.id) from "
									+ className
									+ " a1 where a1.parent is null)").list();
					if (l != null && l.size() > 0) {
						int count = Integer.parseInt((String) l.get(0));
						count++;
						this.flow = count + "";

					} else {
						this.flow = "1";
					}
				} else {
					List l = session.createQuery(
							"select count(*) from " + className
									+ " a where a.parent=:parent")
							.setParameter("parent", this.parent).list();
					if (l != null) {
						long count = (Long) l.get(0);
						count++;
						this.flow = this.parent.flow + "." + count;
					}
				}
			}

			FinanceLogger.log("Account {0}: Flow with {1} has been added", this
					.getName(), this.flow);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return onUpdate(session);

	}

	private void updateTotalBalance(double amount) {
		System.out.println(this.getName());

		String tempStr = " Total Balance of " + this.getName()
				+ " has been updated from " + this.totalBalance;

		this.totalBalance += amount;

		FinanceLogger.log("{0} To {1}", tempStr, String
				.valueOf(this.totalBalance));

		if (this.parent != null) {
			this.parent.updateTotalBalance(amount);
		}
		ChangeTracker.put(this);
	}

	public void updateCurrentBalance(Transaction transaction, double amount) {

//		if (!this.getName().equals(AccounterConstants.SALES_TAX_VAT_UNFILED))
		amount = (isIncrease ? 1 : -1) * amount;

		String tempStr = "Current Balance of  " + this.getName()
				+ " has been updated from " + this.currentBalance;

		this.currentBalance += amount;

		FinanceLogger.log("{0} To {1}", tempStr, String
				.valueOf(this.currentBalance));

		if (!DecimalUtil.isEquals(this.currentBalance, 0.0)
				&& isOpeningBalanceEditable) {
			isOpeningBalanceEditable = Boolean.FALSE;
		}
		this.updateTotalBalance(amount);
		// log.info(accountTransaction);

		// if (!transaction.isBecameVoid()) {

		// This condition checking is for the purpose of not to consider
		// this entry in Profit and Loss Report if the entry is for the
		// closing Fiscal Year.
		if (transaction.type == Transaction.TYPE_JOURNAL_ENTRY
				&& ((JournalEntry) transaction).journalEntryType == JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY
				&& transaction.memo != null
				&& transaction.memo.equals("Closing Fiscal Year")) {
			FinanceLogger
					.log("Create AccountTransaction when  Fiscal Year closed ");
			accountTransaction = new AccountTransaction(this, transaction,
					amount, true, false);

		}
		// The below condition never satisfy because while creating
		// Cash-Basis Journal Entry Account Balances should not effect.
		else if (transaction.type == Transaction.TYPE_JOURNAL_ENTRY
				&& ((JournalEntry) transaction).journalEntryType == JournalEntry.TYPE_CASH_BASIS_JOURNAL_ENTRY
				&& transaction.memo != null
				&& transaction.memo.equals("Closing Fiscal Year")) {
			accountTransaction = new AccountTransaction(this, transaction,
					amount, true, true);
		} else {
			accountTransaction = new AccountTransaction(this, transaction,
					amount, false, false);
		}

		if (this.name.equalsIgnoreCase("Un Deposited Funds")) {
			TransactionMakeDepositEntries transactionMakeDepositEntries = this.accountTransaction
					.getTransaction().getTransactionMakeDepositEntries();

			if (transactionMakeDepositEntries != null) {
				transactionMakeDepositEntries.updateAmount(amount);
			} else {
				transactionMakeDepositEntries = new TransactionMakeDepositEntries(
						this, transaction, amount);
				this.accountTransaction.getTransaction()
						.setTransactionMakeDepositEntries(
								transactionMakeDepositEntries);
			}

		}
		ChangeTracker.put(this);
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);

		log.info("Uupdate Account: " + this.getName() + "Balace:"
				+ this.totalBalance);

		if (this.id != 0
				&& this.parent != null
				&& ((this.oldParent != null && this.oldParent != this.parent) || this.oldParent == null)) {

			parent.updateTotalBalance(this.totalBalance);
			// updating the flow

			String oldFlow = this.flow;

			Query query = session.getNamedQuery("getCountOfParentAccount")
					.setParameter("parentId", this.parent.getId());
			List l = query.list();
			// List l = session.createQuery(
			// "select count(*) from Account a where a.parent=:parent")
			// .setParameter("parent", this.parent).list();
			if (l != null) {
				long count = ((BigInteger) l.get(0)).longValue();
				count++;
				this.flow = this.parent.flow + "." + count;
			}

			if (this.oldParent != null && this.oldParent != this.parent) {

				FinanceLogger.log("{0} parent changed from {1} to {2} ", this
						.getName(), this.oldParent.getName(), this.parent
						.getName());

				FinanceLogger.log("{0} flow changed from {1} to {2}", this
						.getName(), oldFlow, this.flow);

				this.oldParent.updateTotalBalance(-1 * this.totalBalance);
				session.update(this.oldParent);

				int i = Integer.parseInt(oldFlow
						.substring(oldFlow.length() - 1));

				// Query query1 =
				// session.getNamedQuery("getAccountDetails").setParameter("parentId",
				// this.oldParent.getId()).setParameter("flow", oldFlow);

				Query query1 = session
						.createQuery(
								"from com.vimukti.accounter.core.Account a where a.parent=:parent and a.flow !=:flow order by a.id")
						.setParameter("parent", this.oldParent).setParameter(
								"flow", oldFlow);
				List<Account> l2 = query1.list();

				// List<Account> l2 = session
				// .createQuery(
				// "from com.vimukti.accounter.core.Account a where a.parent=:parent and a.flow =:flow order by a.id")
				// .setParameter("parent", this.oldParent).setParameter(
				// "flow", oldFlow).list();

				if (l2 != null) {
					for (Account account : l2) {

						if (Integer.parseInt(account.flow.substring(oldFlow
								.length() - 1)) > i) {

							account.flow = account.flow.substring(0,
									account.flow.length() - 1).concat("" + i);
							session.update(account);
							i++;
						}

					}
				}

			}
		}

		if (!DecimalUtil.isEquals(this.openingBalance, 0.0)
				&& isOpeningBalanceEditable) {

			FinanceLogger
					.log("Create Journal Entry if Opening Balance is not 0 to this account");

			this.isOpeningBalanceEditable = Boolean.FALSE;
			// Query query = session.getNamedQuery("getNextTransactionNumber");
			// query.setLong("type", Transaction.TYPE_JOURNAL_ENTRY);
			// List list = query.list();
			String nextVoucherNumber = NumberUtils
					.getNextTransactionNumber(Transaction.TYPE_JOURNAL_ENTRY);
			JournalEntry journalEntry = new JournalEntry(this,
					nextVoucherNumber, JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY);
			session.save(journalEntry);
		}

		if (this.accountTransaction != null) {
			session.saveOrUpdate(this.accountTransaction);
		}

		// Object object =
		// session.createQuery(
		// "from com.vimukti.accounter.core.TransactionMakeDepositEntries tme where tme.transaction.id=?"
		// ).setParameter(0,
		// accountTransaction.getTransaction().getId()).uniqueResult();
		// transactionMakeDepositEntries = object !=
		// null?(TransactionMakeDepositEntries) object:null;
		// if(transactionMakeDepositEntries != null){
		// transactionMakeDepositEntries.updateAmount(accountTransaction.getAmount
		// ());
		// }else{
		// transactionMakeDepositEntries=new
		// TransactionMakeDepositEntries(this.accountTransaction.transaction,this
		// .accountTransaction.getAmount());
		// }
		this.updateEntryMemo(session);
		ChangeTracker.put(this);

		session.setFlushMode(flushMode);
		return false;
	}

	@Override
	public String toString() {
		return "Account Name: " + this.name + "Balance: " + this.totalBalance;
	}

	public boolean equals(Account account) {

		if (this.version == account.version
				&& this.id == account.id
				&& this.type == account.type
				&& this.isActive == account.isActive
				&& DecimalUtil.isEquals(this.openingBalance,
						account.openingBalance)
				&& this.isConsiderAsCashAccount == account.isConsiderAsCashAccount
				&& this.bankAccountType == account.bankAccountType
				&& this.cashFlowCategory == account.cashFlowCategory
				&& DecimalUtil.isEquals(this.creditLimit, account.creditLimit)
				&& this.isIncrease == account.isIncrease
				&& DecimalUtil.isEquals(this.currentBalance,
						account.currentBalance)
				&& DecimalUtil
						.isEquals(this.totalBalance, account.totalBalance)
				&& this.baseType == account.baseType
				&& this.isOpeningBalanceEditable == account.isOpeningBalanceEditable
				&& (this.number != null && account.number != null) ? (this.number
				.equals(account.number))
				: true && (this.name != null && account.name != null) ? (this.name
						.equals(account.name))
						: true && (this.parent != null && account.parent != null) ? (this.parent
								.equals(account.parent))
								: true && (this.asOf != null && account.asOf != null) ? (this.asOf
										.equals(account.asOf))
										: true && (this.comment != null && account.comment != null) ? (this.comment
												.equals(account.comment))
												: true && (this.bank != null && account.bank != null) ? (this.bank
														.equals(account.bank))
														: true && (this.bankAccountNumber != null && account.bankAccountNumber != null) ? (this.bankAccountNumber
																.equals(account.bankAccountNumber))
																: true && (this.cardOrLoanNumber != null && account.cardOrLoanNumber != null) ? (this.cardOrLoanNumber
																		.equals(account.cardOrLoanNumber))
																		: true && (this.hierarchy != null && account.hierarchy != null) ? (this.hierarchy
																				.equals(account.hierarchy))
																				: true)
			return true;

		return false;
	}

	public void setNumber(String number) {
		this.number = number;

	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;

	}

	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;

	}

	public void setAsOf(FinanceDate asOf) {
		this.asOf = asOf;

	}


	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {

		Session session = HibernateUtil.getCurrentSession();
		Account account = (Account) clientObject;
		// Query query = session.createQuery(
		// "from com.vimukti.accounter.core.Account A where A.name=?")
		// .setParameter(0, account.name);
		// List list = query.list();
		//
		// if (list != null && list.size() > 0) {
		// Account newAccount = (Account) list.get(0);
		// if (account.id != newAccount.id) {
		// throw new InvalidOperationException(
		// "Account Name already existed Enter Unique name for Account");

		Query query = session.getNamedQuery("getAccounts").setParameter("name",
				this.name).setParameter("number", this.number).setParameter(
				"id", this.id);

		List list = query.list();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] object = (Object[]) it.next();

			if (this.name.equals((String) object[0])) {
				Iterator it2 = list.iterator();
				while (it2.hasNext()) {
					Object[] object2 = (Object[]) it2.next();
					if (this.number.equals((String) object2[1])) {
						throw new InvalidOperationException(
								"An Account already exists with this name and number");
					}
				}
				throw new InvalidOperationException(
						"An Account already exists with this name");
			} else if (this.number.equals((String) object[1])) {
				Iterator it2 = list.iterator();
				while (it2.hasNext()) {
					Object[] object2 = (Object[]) it2.next();
					if (this.name.equals((String) object2[0])) {
						throw new InvalidOperationException(
								"An Account already exists with this name and number");
					}
				}
				throw new InvalidOperationException(
						"An Account already exists with this number");
			}
		}
		return true;

	}

	/*
	 * Is to update Memo in Entry if and only if account Name was altered
	 */
	protected void updateEntryMemo(Session session) {

		Query query = session
				.createQuery(
						"select a.name from com.vimukti.accounter.core.Account a where a.id=:id")
				.setParameter("id", this.getID());
		String accountName = (String) query.uniqueResult();

		if (accountName != null && !this.getName().equals(accountName))
			Entry.updateEntryMemo(session, accountName, this.getName());
	}

}
