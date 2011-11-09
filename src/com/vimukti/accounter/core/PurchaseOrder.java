package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * A purchase order (PO) is a commercial document issued by a buyer to a seller,
 * indicating types, quantities, and agreed prices for products or services the
 * seller will provide to the buyer. Sending a PO to a supplier constitutes a
 * legal offer to buy products or services. Acceptance of a PO by a seller
 * usually forms a one-off contract between the buyer and seller, so no contract
 * exists until the PO is accepted.
 */

public class PurchaseOrder extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1005959266516210357L;

	/**
	 * {@link Vendor} associated for this PurchaseOrder
	 */
	@ReffereredObject
	Vendor vendor;

	/**
	 * ShipTo {@link Account}, Associated
	 */
	@ReffereredObject
	Account shipTo;

	/**
	 * FinanceDate of Purchase Order
	 */
	FinanceDate purchaseOrderDate;

	/**
	 * this field , signifies whether this SalesOrder needs to be printed
	 */
	boolean toBePrinted;

	boolean toBeEmailed;

	/**
	 * {@link Contact} of the {@link Vendor}, selected for this PurchaseOrder
	 */
	Contact contact;

	/**
	 * {@link Address} of the {@link Vendor}, selected for this Purchase Order.
	 */
	Address vendorAddress;

	/**
	 * Phone Number as Entered for the Vendor, selected
	 */
	String phone;

	/**
	 * {@link PaymentTerms} for this Purchase Order
	 */
	PaymentTerms paymentTerm;

	/**
	 * Due FinanceDate, for this Purchase Order
	 */
	FinanceDate dueDate;

	/**
	 * Despatch Date, for this Purchase Order
	 */

	private FinanceDate dispatchDate;
	/**
	 * Delivery Date, for this Purchase Order.
	 */
	FinanceDate deliveryDate;

	/**
	 * Shipping {@link Address} for this Vendor Selected
	 */
	Address shippingAddress;

	/**
	 * {@link ShippingTerms} for the PurchaseOrder
	 */
	ShippingTerms shippingTerms;

	/**
	 * {@link ShippingMethod} for the Purchase Order
	 */
	ShippingMethod shippingMethod;

	/**
	 * To maintain the user defined auto incrementing number.
	 */
	String purchaseOrderNumber;

	EnterBill usedBill;

	//

	// List<ItemReceipt> itemReceipts;
	//
	// List<EnterBill> enterBills;

	public PurchaseOrder() {
		setType(TYPE_PURCHASE_ORDER);
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
	 * @return the shipTo
	 */
	public Account getShipTo() {
		return shipTo;
	}

	/**
	 * @param shipTo
	 *            the shipTo to set
	 */
	public void setShipTo(Account shipTo) {
		this.shipTo = shipTo;
	}

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
	 * @return the purchaseOrderFinanceDate
	 */
	public FinanceDate getPurchaseOrderDate() {
		return purchaseOrderDate;
	}

	/**
	 * @param purchaseOrderDate
	 *            the purchaseOrderDate to set
	 */
	public void setPurchaseOrderDate(FinanceDate purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
	}

	/**
	 * @return the toBePrinted
	 */
	public boolean isToBePrinted() {
		return toBePrinted;
	}

	/**
	 * @param toBePrinted
	 *            the toBePrinted to set
	 */
	public void setToBePrinted(boolean toBePrinted) {
		this.toBePrinted = toBePrinted;
	}

	/**
	 * @return the toBeEmailed
	 */
	public boolean isToBeEmailed() {
		return toBeEmailed;
	}

	/**
	 * @param toBeEmailed
	 *            the toBeEmailed to set
	 */
	public void setToBeEmailed(boolean toBeEmailed) {
		this.toBeEmailed = toBeEmailed;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * @return the vendorAddress
	 */
	public Address getVendorAddress() {
		return vendorAddress;
	}

	/**
	 * @param vendorAddress
	 *            the vendorAddress to set
	 */
	public void setVendorAddress(Address vendorAddress) {
		this.vendorAddress = vendorAddress;
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
	 * @return the paymentTerm
	 */
	public PaymentTerms getPaymentTerm() {
		return paymentTerm;
	}

	/**
	 * @param paymentTerm
	 *            the paymentTerm to set
	 */
	public void setPaymentTerm(PaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @return the dueDate
	 */
	public FinanceDate getDueDate() {
		return dueDate;
	}

	public void setDespatchDate(FinanceDate despatchDate) {
		this.dispatchDate = despatchDate;
	}

	public FinanceDate getDespatchDate() {
		return dispatchDate;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(FinanceDate dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the deliveryDate
	 */
	public FinanceDate getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate
	 *            the deliveryDate to set
	 */
	public void setDeliveryDate(FinanceDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @return the shippingAddress
	 */
	public Address getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * @param shippingAddress
	 *            the shippingAddress to set
	 */
	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	/**
	 * @return the shippingTerms
	 */
	public ShippingTerms getShippingTerms() {
		return shippingTerms;
	}

	/**
	 * @param shippingTerms
	 *            the shippingTerms to set
	 */
	public void setShippingTerms(ShippingTerms shippingTerms) {
		this.shippingTerms = shippingTerms;
	}

	/**
	 * @return the shippingMethod
	 */
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * @param shippingMethod
	 *            the shippingMethod to set
	 */
	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	@Override
	public boolean isDebitTransaction() {

		return false;
	}

	@Override
	public boolean isPositiveTransaction() {

		return false;
	}

	@Override
	public Account getEffectingAccount() {

		return null;
	}

	@Override
	public Payee getPayee() {

		return null;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_VENDOR;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_PURCHASE_ORDER;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		super.onUpdate(session);
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
	public Payee getInvolvedPayee() {

		return this.vendor;
	}

	public boolean equals(PurchaseOrder purchaseOrder) {
		if (vendor.id == purchaseOrder.vendor.id
				&& this.transactionItems.size() == purchaseOrder.transactionItems
						.size()) {
			for (int i = 0; i < this.transactionItems.size(); i++) {
				if (!this.transactionItems.get(i).equals(
						purchaseOrder.transactionItems.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) {
		// Session session = HibernateUtil.getCurrentSession();
		// PurchaseOrder purchaseOrder = (PurchaseOrder) clonedObject;
		// if (this.status != STATUS_DELETED
		// && this.status == STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED) {
		// for (TransactionItem transactionItem :
		// purchaseOrder.transactionItems) {
		//
		// if (transactionItem.lineTotal < transactionItem.usedamt
		// || transactionItem.lineTotal == transactionItem.usedamt) {
		// this.status = STATUS_PAID_OR_APPLIED_OR_ISSUED;
		// } else {
		// this.status = STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
		// }
		// }
		// }
		// Session session = HibernateUtil.getCurrentSession();
		PurchaseOrder purchaseOrder = (PurchaseOrder) clonedObject;
		if (this.status != STATUS_DELETED && this.status == STATUS_OPEN) {
			for (TransactionItem transactionItem : purchaseOrder.transactionItems) {

				if (DecimalUtil.isLessThan(transactionItem.lineTotal,
						transactionItem.usedamt)
						|| DecimalUtil.isEquals(transactionItem.lineTotal,
								transactionItem.usedamt)) {
					this.status = STATUS_COMPLETED;
				} else {
					this.status = STATUS_OPEN;
				}
			}
		}
		super.onEdit(clonedObject);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// if (this.status == STATUS_CANCELLED || this.status ==
		// STATUS_COMPLETED || this.status == STATUS_PAID_OR_APPLIED_OR_ISSUED)
		// {
		// throw new InvalidOperationException(
		// "This PurchaseOrder can't be edited, becuase it is Completed or Canceled.  PurchaseOrderNo:"
		// + this.number);
		// }
		return true;
	}

	/**
	 * @return the usedBill
	 */
	public EnterBill getUsedBill() {
		return usedBill;
	}

	/**
	 * @param usedBill
	 *            the usedBill to set
	 */
	public void setUsedBill(EnterBill usedBill, Session session) {
		this.usedBill = usedBill;
		// for (TransactionItem item : transactionItems) {
		// item.doCreateEffect(session);
		// }
	}

}
