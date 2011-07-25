package com.vimukti.accounter.web.client.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class ClientTransactionPayBill implements IAccounterCore {

	long id;
	int version;
	long dueDate;
	long enterBill;
	double originalAmount = 0D;
	double amountDue = 0D;
	long discountDate;
	long discountAccount;
	double cashDiscount = 0D;
	double appliedCredits = 0D;
	double payment = 0D;
	ClientPayBill payBill;
	private double dummyDue;

	List<ClientTransactionCreditsAndPayments> transactionCreditsAndPayments;

	long transactionMakeDeposit;

	boolean isVoid = false;

	long vendorId;

	String billNumber;

	long journalEntry;

	/* The following fields are just for saving credits temporarly */
	Map<Integer, Object> tempCredits = new HashMap<Integer, Object>();
	double remainingCreditBalance;
	boolean isCreditsApplied;
	
	

	public boolean isCreditsApplied() {
		return isCreditsApplied;
	}

	public void setCreditsApplied(boolean isCreditsApplied) {
		this.isCreditsApplied = isCreditsApplied;
	}

	public Map<Integer, Object> getTempCredits() {
		return tempCredits;
	}

	public void setTempCredits(Map<Integer, Object> tempCredits) {
		this.tempCredits = tempCredits;
	}

	public double getRemainingCreditBalance() {
		return remainingCreditBalance;
	}

	public void setRemainingCreditBalance(double remainingCreditBalance) {
		this.remainingCreditBalance = remainingCreditBalance;
	}

	/**
	 * @return the id
	 */

	public long getJournalEntry() {
		return journalEntry;
	}

	public void setJournalEntry(long journalEntry) {
		this.journalEntry = journalEntry;
	}

	/**
	 * @return the dueDate
	 */
	public long getDueDate() {
		return dueDate;
	}

	/**
	 * @return the vendor
	 */
	public long getVendor() {
		return vendorId;
	}

	/**
	 * @return the enterBill
	 */
	public long getEnterBill() {
		return enterBill;
	}

	/**
	 * @return the originalAmount
	 */
	public double getOriginalAmount() {
		return originalAmount;
	}

	/**
	 * @return the amountDue
	 */
	public double getAmountDue() {
		return amountDue;
	}

	/**
	 * @return the discountDate
	 */
	public long getDiscountDate() {
		return discountDate;
	}

	/**
	 * @return the discountAccount
	 */
	public long getDiscountAccount() {
		return discountAccount;
	}

	/**
	 * @param discountAccountId
	 *            the discountAccount to set
	 */
	public void setDiscountAccount(long discountAccountId) {
		this.discountAccount = discountAccountId;
	}

	/**
	 * @return the cashDiscount
	 */
	public double getCashDiscount() {
		return cashDiscount;
	}

	/**
	 * @return the appliedCredits
	 */
	public double getAppliedCredits() {
		return appliedCredits;
	}

	/**
	 * @return the payment
	 */
	public double getPayment() {
		return payment;
	}

	/**
	 * @param id
	 *            the id to set
	 */

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(long dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @param vendorId
	 *            the vendor to set
	 */
	public void setVendor(long vendorId) {
		this.vendorId = vendorId;
	}

	/**
	 * @param enterBillId
	 *            the enterBill to set
	 */
	public void setEnterBill(long enterBillId) {
		this.enterBill = enterBillId;
	}

	/**
	 * @param originalAmount
	 *            the originalAmount to set
	 */
	public void setOriginalAmount(double originalAmount) {
		this.originalAmount = originalAmount;
	}

	/**
	 * @param amountDue
	 *            the amountDue to set
	 */
	public void setAmountDue(double amountDue) {
		this.amountDue = amountDue;
	}

	/**
	 * @param discountDate
	 *            the discountDate to set
	 */
	public void setDiscountDate(long discountDate) {
		this.discountDate = discountDate;
	}

	/**
	 * @param cashDiscount
	 *            the cashDiscount to set
	 */
	public void setCashDiscount(double cashDiscount) {
		this.cashDiscount = cashDiscount;
	}

	/**
	 * @param appliedCredits
	 *            the appliedCredits to set
	 */
	public void setAppliedCredits(double appliedCredits) {
		this.appliedCredits = appliedCredits;
	}

	/**
	 * @param payment
	 *            the payment to set
	 */
	public void setPayment(double payment) {
		this.payment = payment;
	}

	/**
	 * @return the paybill
	 */
	public ClientPayBill getPayBill() {
		return payBill;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setPayBill(ClientPayBill payBill) {
		this.payBill = payBill;
	}

	public List<ClientTransactionCreditsAndPayments> getTransactionCreditsAndPayments() {
		return transactionCreditsAndPayments;
	}

	public void setTransactionCreditsAndPayments(
			List<ClientTransactionCreditsAndPayments> transactionCreditsAndPayments) {
		this.transactionCreditsAndPayments = transactionCreditsAndPayments;
	}

	/**
	 * @return the isVoid
	 */
	public Boolean getIsVoid() {
		return isVoid;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setIsVoid(Boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * @return the transactionMakeDeposit
	 */
	public long getTransactionMakeDeposit() {
		return transactionMakeDeposit;
	}

	/**
	 * @param transactionMakeDepositId
	 *            the transactionMakeDeposit to set
	 */
	public void setTransactionMakeDeposit(long transactionMakeDeposit) {
		this.transactionMakeDeposit = transactionMakeDeposit;
	}

	/**
	 * @return the billNumber
	 */
	public String getBillNumber() {
		return billNumber;
	}

	/**
	 * @param billNumber
	 *            the billNumber to set
	 */
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public void setAccountsPayable(Object accountsPayableAccount) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getID(){
		return this.id;
	}

	@Override
	public void setID(long id){
		this.id=id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientTransactionPayBill";
	}

	public void setDummyDue(double amountDue){
		this.dummyDue=amountDue;
	}

	public double getDummyDue() {
		return dummyDue;
	}

}
