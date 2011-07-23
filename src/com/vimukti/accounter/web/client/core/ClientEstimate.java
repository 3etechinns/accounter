package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientEstimate extends ClientTransaction {

	public static final int STATUS_OPEN = 0;

	public static final int STATUS_REJECTED = 1;

	public static final int STATUS_ACCECPTED = 2;

	long customer;

	ClientContact contact;

	ClientAddress address;

	String phone;

	long salesPerson;

	long paymentTerm;

	long expirationDate;

	long deliveryDate;

	long priceLevel;

	double salesTax;

	boolean isTurnedToInvoice = false;

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
	 * @return the expirationDate
	 */
	public long getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate
	 *            the expirationDate to set
	 */
	public void setExpirationDate(long expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the deliveryDate
	 */
	public long getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate
	 *            the deliveryDate to set
	 */
	public void setDeliveryDate(long deliveryDate) {
		this.deliveryDate = deliveryDate;
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
	 * @param contact2
	 *            the contact to set
	 */
	public void setContact(ClientContact contact2) {
		this.contact = contact2;
	}

	/**
	 * @return the address
	 */
	public ClientAddress getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(ClientAddress address) {
		this.address = address;
	}

	/**
	 * @param salesPerson
	 *            the salesPerson to set
	 */
	public void setSalesPerson(long salesPerson) {
		this.salesPerson = salesPerson;
	}

	/**
	 * @param paymentTerm
	 *            the paymentTerm to set
	 */
	public void setPaymentTerm(long paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @param priceLevel
	 *            the priceLevel to set
	 */
	public void setPriceLevel(long priceLevel) {
		this.priceLevel = priceLevel;
	}

	/**
	 * @param salesTax
	 *            the salesTax to set
	 */
	public void setSalesTax(double salesTax) {
		this.salesTax = salesTax;
	}

	/**
	 * @param isTurnedToInvoice
	 *            the isTurnedToInvoice to set
	 */
	public void setTurnedToInvoice(boolean isTurnedToInvoice) {
		this.isTurnedToInvoice = isTurnedToInvoice;
	}

	/**
	 * @return the salesTax
	 */
	public double getSalesTax() {
		return salesTax;
	}

	/**
	 * @return the isTurnedToInvoice
	 */
	public Boolean getIsTurnedToInvoice() {
		return isTurnedToInvoice;
	}

	/**
	 * @param isTurnedToInvoice
	 *            the isTurnedToInvoice to set
	 */
	public void setIsTurnedToInvoice(Boolean isTurnedToInvoice) {
		this.isTurnedToInvoice = isTurnedToInvoice;
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	public void setCustomer(ClientCustomer customer) {
		this.customer = customer.getID();

	}

	public void setSalesPerson(ClientSalesPerson salesPerson) {
		this.salesPerson = salesPerson.getID();

	}

	public void setPriceLevel(ClientPriceLevel priceLevel) {
		this.priceLevel = priceLevel.getID();
	}

	public long getPaymentTerm() {

		return this.paymentTerm;
	}

	public long getPriceLevel() {

		return this.priceLevel;
	}

	public long getSalesPerson() {

		return this.salesPerson;
	}

	public ClientContact getContact() {

		return this.contact;
	}

	@Override
	public String getName() {

		return null;
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

		return "ClientEstimate";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ESTIMATE;
	}

}
