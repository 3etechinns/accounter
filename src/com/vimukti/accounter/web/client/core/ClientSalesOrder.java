package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientSalesOrder extends ClientTransaction {

	long customer;

	ClientContact contact;

	ClientAddress billingAddress;

	ClientAddress shippingAdress;

	String phone;

	long salesPerson;

	long paymentTerm;

	long shippingTerm;

	long shippingMethod;

	long dueDate;

	double discountTotal;

	long priceLevel;

	double salesTaxAmount;

	long estimate;

	long salesTaxItem;

	/**
	 * To give the user the feature to maintain his own number to know about
	 * this SalesOrder
	 */
	String customerOrderNumber;

	public ClientSalesOrder() {
		super();
		setType(ClientTransaction.TYPE_SALES_ORDER);
	}

	public ClientSalesOrder(ClientEstimate estimate) {
		super();
		setType(ClientTransaction.TYPE_SALES_ORDER);

		this.customer = estimate.customer;
		this.contact = estimate.contact;
		this.phone = estimate.getPhone();
		this.billingAddress = estimate.address;

		this.paymentTerm = estimate.paymentTerm;
		this.salesPerson = estimate.salesPerson;
		this.priceLevel = estimate.priceLevel;
		this.salesTaxAmount = estimate.getSalesTax();
		this.total = estimate.getTotal();
		// this.deliverydate = estimate.getDeliveryDate();

		this.transactionDate = estimate.getDate().getTime();
		this.transactionItems = estimate.getTransactionItems();

		this.memo = estimate.getMemo();
		this.reference = estimate.getReference();
	}

	/**
	 * @return the customerOrderNumber
	 */
	public String getCustomerOrderNumber() {
		return customerOrderNumber;
	}

	/**
	 * @param customerOrderNumber
	 *            the customerOrderNumber to set
	 */
	public void setCustomerOrderNumber(String customerOrderNumber) {
		this.customerOrderNumber = customerOrderNumber;
	}

	/**
	 * @return the customer
	 */
	public long getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(long customer) {
		this.customer = customer;
	}

	/**
	 * @return the contact
	 */
	public ClientContact getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	/**
	 * @return the billingAddress
	 */
	public ClientAddress getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @param billingAddress
	 *            the billingAddress to set
	 */
	public void setBillingAddress(ClientAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @return the shippingAdress
	 */
	public ClientAddress getShippingAdress() {
		return shippingAdress;
	}

	/**
	 * @param shippingAdress
	 *            the shippingAdress to set
	 */
	public void setShippingAdress(ClientAddress shippingAdress) {
		this.shippingAdress = shippingAdress;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the salesPerson
	 */
	public long getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @param salesPerson
	 *            the salesPerson to set
	 */
	public void setSalesPerson(long salesPerson) {
		this.salesPerson = salesPerson;
	}

	/**
	 * @return the paymentTerm
	 */
	public long getPaymentTerm() {
		return paymentTerm;
	}

	/**
	 * @param paymentTerm
	 *            the paymentTerm to set
	 */
	public void setPaymentTerm(long paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @return the shippingTerm
	 */
	public long getShippingTerm() {
		return shippingTerm;
	}

	/**
	 * @param shippingTerm
	 *            the shippingTerm to set
	 */
	public void setShippingTerm(long shippingTerm) {
		this.shippingTerm = shippingTerm;
	}

	/**
	 * @return the shippingMethod
	 */
	public long getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * @param shippingMethod
	 *            the shippingMethod to set
	 */
	public void setShippingMethod(long shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * @return the priceLevel
	 */
	public long getPriceLevel() {
		return priceLevel;
	}

	/**
	 * @param priceLevel
	 *            the priceLevel to set
	 */
	public void setPriceLevel(long priceLevel) {
		this.priceLevel = priceLevel;
	}

	/**
	 * @return the estimate
	 */
	public long getEstimate() {
		return estimate;
	}

	/**
	 * @param estimate
	 *            the estimate to set
	 */
	public void setEstimate(long estimate) {
		this.estimate = estimate;
	}

	/**
	 * @return the salesTaxItem
	 */
	public long getSalesTaxItem() {
		return salesTaxItem;
	}

	/**
	 * @param salesTaxItem
	 *            the salesTaxItem to set
	 */
	public void setSalesTaxItem(long salesTaxItem) {
		this.salesTaxItem = salesTaxItem;
	}

	/**
	 * @return the dueDate
	 */
	public long getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(long dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the discountTotal
	 */
	public double getDiscountTotal() {
		return discountTotal;
	}

	/**
	 * @param discountTotal
	 *            the discountTotal to set
	 */
	public void setDiscountTotal(double discountTotal) {
		this.discountTotal = discountTotal;
	}

	public double getSalesTaxAmount() {
		return salesTaxAmount;
	}

	/**
	 * @param salesTaxAmount
	 *            the salesTaxAmount to set
	 */
	public void setSalesTaxAmount(double salesTaxAmount) {
		this.salesTaxAmount = salesTaxAmount;
	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientSalesOrder";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.SALESORDER;
	}

	@Override
	public long getID(){

		return this.id;
	}

	@Override
	public void setID(long id){

		this.id=id;
	}

}
