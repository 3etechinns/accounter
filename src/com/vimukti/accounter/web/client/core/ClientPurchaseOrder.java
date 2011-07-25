package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientPurchaseOrder extends ClientTransaction {

	long vendor;

	ClientAddress shipTo;

	long purchaseOrderDate;

	boolean toBePrinted;

	boolean toBeEmailed;

	ClientContact contact;

	ClientAddress vendorAddress;

	String phone;

	long paymentTerm;

	long dueDate;

	long deliveryDate;

	private long dispatchDate;

	ClientAddress shippingAddress;

	long shippingTerms;

	long shippingMethod;

	/**
	 * To maintain the user defined auto incrementing number.
	 */
	String purchaseOrderNumber;

	/**
	 * @return the purchaseOrderNumber
	 */
	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}

	/**
	 * @param purchaseOrderNumber
	 *            the purchaseOrderNumber to set
	 */
	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	/**
	 * @return the vendor
	 */
	public long getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(long vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the shipTo
	 */
	public ClientAddress getShipTo() {
		return shipTo;
	}

	/**
	 * @param shipTo
	 *            the shipTo to set
	 */
	public void setShipTo(ClientAddress shipTo) {
		this.shipTo = shipTo;
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
	 * @return the vendorAddress
	 */
	public ClientAddress getVendorAddress() {
		return vendorAddress;
	}

	/**
	 * @param vendorAddress
	 *            the vendorAddress to set
	 */
	public void setVendorAddress(ClientAddress vendorAddress) {
		this.vendorAddress = vendorAddress;
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
	 * @return the shippingAddress
	 */
	public ClientAddress getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * @param shippingAddress
	 *            the shippingAddress to set
	 */
	public void setShippingAddress(ClientAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	/**
	 * @return the shippingTerms
	 */
	public long getShippingTerms() {
		return shippingTerms;
	}

	/**
	 * @param shippingTerms
	 *            the shippingTerms to set
	 */
	public void setShippingTerms(long shippingTerms) {
		this.shippingTerms = shippingTerms;
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

	@Override
	public String getDisplayName() {
		return this.getName();
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
	 * @return the purchaseOrderDate
	 */
	public long getPurchaseOrderDate() {
		return purchaseOrderDate;
	}

	/**
	 * @param purchaseOrderDate
	 *            the purchaseOrderDate to set
	 */
	public void setPurchaseOrderDate(long purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
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

	public void setDespatchDate(long despatchDate) {
		this.dispatchDate = despatchDate;
	}

	public long getDespatchDate() {
		return dispatchDate;
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

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	public void setToBePrinted(boolean toBePrinted) {
		this.toBePrinted = toBePrinted;
	}

	public boolean isToBePrinted() {
		return toBePrinted;
	}

	public void setToBeEmailed(boolean toBeEmailed) {
		this.toBeEmailed = toBeEmailed;
	}

	public boolean isToBeEmailed() {
		return toBeEmailed;
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

		return "ClientPurchaseOrder";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PURCHASEORDER;
	}
}
