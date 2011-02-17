package com.vimukti.accounter.web.client.core;

import java.util.Map;

/**
 * @author vimukti21
 * 
 */
@SuppressWarnings("serial")
public class ClientAccount implements IAccounterCore {

	// public static final int BASETYPE_INCOME = 1;
	// public static final int BASETYPE_EXPENSE = 2;
	// public static final int BASETYPE_ASSET = 3;
	// public static final int BASETYPE_LIABILITY = 4;
	// public static final int BASETYPE_EQUITY = 5;

	public static final int TYPE_CASH = 1;
	public static final int TYPE_BANK = 2;
	public static final int TYPE_ACCOUNT_RECEIVABLE = 3;
	public static final int TYPE_OTHER_CURRENT_ASSET = 4;
	public static final int TYPE_INVENTORY_ASSET = 5;
	public static final int TYPE_FIXED_ASSET = 6;
	public static final int TYPE_OTHER_ASSET = 7;
	public static final int TYPE_ACCOUNT_PAYABLE = 8;
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

	public static final int CASH_FLOW_CATEGORY_FINANCING = 1;
	public static final int CASH_FLOW_CATEGORY_INVESTING = 2;
	public static final int CASH_FLOW_CATEGORY_OPERATING = 3;

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

	// String stringID;

	public String stringID;

	int type;

	String number;

	String name;

	boolean isActive;

	String parent = "";

	int cashFlowCategory;

	double openingBalance = 0.0D;

	long asOf; // Date

	boolean isConsiderAsCashAccount;

	String comment;

	String bank;

	int bankAccountType;

	String bankAccountNumber;

	double creditLimit;

	String cardOrLoanNumber;

	boolean isIncrease;

	double currentBalance = 0.0D;

	double totalBalance = 0.0D;

	int baseType;

	int subBaseType;
	int groupType;

	String linkedAccumulatedDepreciationAccount;;

	boolean isDefault;

	boolean isOpeningBalanceEditable = Boolean.TRUE;

	Map<Integer, Double> monthViceAmounts;

	String hierarchy;
	String flow;

	// ClientTaxCode VATcode;

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	//
	// public ClientTaxCode getVATcode() {
	// return VATcode;
	// }
	//
	// public void setVATcode(ClientTaxCode tcode) {
	// VATcode = tcode;
	// }

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public ClientAccount() {

	}

	public String getLinkedAccumulatedDepreciationAccount() {
		return linkedAccumulatedDepreciationAccount;
	}

	public void setLinkedAccumulatedDepreciationAccount(
			String linkedAccumulatedDepreciationAccount) {
		this.linkedAccumulatedDepreciationAccount = linkedAccumulatedDepreciationAccount;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
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
	public void setNumber(String number) {
		this.number = number;
	}

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
	public void setName(String name) {
		this.name = name;
	}

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
	 * @return the cashFlowCategory
	 */
	public int getCashFlowCategory() {
		return cashFlowCategory;
	}

	/**
	 * @param cashFlowCategory
	 *            the cashFlowCategory to set
	 */
	public void setCashFlowCategory(int cashFlowCategory) {
		this.cashFlowCategory = cashFlowCategory;
	}

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
	 * @return the asOf
	 */
	public long getAsOf() {
		return asOf;
	}

	/**
	 * @param asOf
	 *            the asOf to set
	 */
	public void setAsOf(long asOf) {
		this.asOf = asOf;
	}

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
	public void setConsiderAsCashAccount(boolean isConsiderAsCashAccount) {
		this.isConsiderAsCashAccount = isConsiderAsCashAccount;
	}

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
	public void setComment(String comment) {
		this.comment = comment;
	}

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
	public void setBankAccountType(int bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

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
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

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
	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}

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
	public void setCardOrLoanNumber(String cardOrLoanNumber) {
		this.cardOrLoanNumber = cardOrLoanNumber;
	}

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
	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}

	@Override
	public String toString() {

		return this.name + "  " + this.totalBalance;
	}

	@Override
	public String getDisplayName() {

		// StringBuffer buffer = new StringBuffer();
		//
		// buffer.append(String.valueOf(name));
		//
		// buffer.append(" [" + Utility.getAccountTypeString(type) + "]");
		//
		// buffer.append(" " + String.valueOf(getNumber()) + "");

		return name;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBank() {
		return bank;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ACCOUNT;
	}

	@Override
	public String getStringID() {

		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientAccount";
	}

}
