package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.bizantra.server.storage.HibernateUtil;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * A sales order (SO) is an internal document of the company, meaning it is
 * generated by the company itself. A sales order should record the customer's
 * originating {@linkplain PurchaseOrder} which is an external document. Rather
 * than using the customer's purchase order document, an internal sales order
 * form allows the internal audit control of completeness to be monitored as a
 * sequential sales order number can be used by the company for its sales order
 * documents.
 */

public class SalesOrder extends Transaction implements Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6865355000270674797L;

	/**
	 * The {@linkplain Customer} involved in a SalesOrder Transaction
	 */
	@ReffereredObject
	Customer customer;

	/**
	 * Contact Selected for the Selected Customer
	 */
	Contact contact;

	/**
	 * Billing {@link Address} of the Selected {@link Customer}
	 */
	Address billingAddress;

	/**
	 * Shipping {@link Address} of the Selected {@link Customer}
	 */
	Address shippingAdress;

	/**
	 * Phone Detail as Entered for the Customer Selected
	 */
	String phone;

	/**
	 * SalesPerson, Default will be the Selected {@link Customer} default
	 * {@link SalesPerson}
	 */
	SalesPerson salesPerson;

	/**
	 * {@link PaymentTerms} for this SalesOrder
	 */
	PaymentTerms paymentTerm;

	/**
	 * {@link ShippingTerms} for this SalesOrder
	 */
	ShippingTerms shippingTerm;

	/**
	 * {@link ShippingMethod} for this SalesOrder
	 */
	ShippingMethod shippingMethod;

	FinanceDate dueDate;

	double discountTotal;

	/**
	 * {@link PriceLevel} Selected for this SalesOrder
	 */
	PriceLevel priceLevel;

	/**
	 * The Sales Tax Amount, for this SalesOrder
	 */
	double salesTaxAmount;

	/**
	 * {@link Estimate} from Which this SAlesOrder was Produced
	 */
	@ReffereredObject
	Estimate estimate;

	/**
	 * Sales Tax {@link Item} selected for this SalesOrder
	 */
	Item salesTaxItem;

	/**
	 * To give the user the feature to maintain his own number to know about
	 * this SalesOrder
	 */
	String customerOrderNumber;

	// transient boolean isImported;

	// List<Invoice> invoices;

	public SalesOrder() {

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
	 * @return the version
	 */
	@Override
	public int getVersion() {
		return version;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the salesTaxItem
	 */
	public Item getSalesTaxItem() {
		return salesTaxItem;
	}

	/**
	 * @param salesTaxItem
	 *            the salesTaxItem to set
	 */
	public void setSalesTaxItem(Item salesTaxItem) {
		this.salesTaxItem = salesTaxItem;
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
	 * @return the dueDate
	 */
	public FinanceDate getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(FinanceDate dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the billingAddress
	 */
	public Address getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @param billingAddress
	 *            the billingAddress to set
	 */
	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @return the shippingAdress
	 */
	public Address getShippingAdress() {
		return shippingAdress;
	}

	/**
	 * @param shippingAdress
	 *            the shippingAdress to set
	 */
	public void setShippingAdress(Address shippingAdress) {
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
	public SalesPerson getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @param salesPerson
	 *            the salesPerson to set
	 */
	public void setSalesPerson(SalesPerson salesPerson) {
		this.salesPerson = salesPerson;
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
	 * @return the shippingTerm
	 */
	public ShippingTerms getShippingTerm() {
		return shippingTerm;
	}

	/**
	 * @param shippingTerm
	 *            the shippingTerm to set
	 */
	public void setShippingTerm(ShippingTerms shippingTerm) {
		this.shippingTerm = shippingTerm;
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

	/**
	 * @return the priceLevel
	 */
	public PriceLevel getPriceLevel() {
		return priceLevel;
	}

	/**
	 * @param priceLevel
	 *            the priceLevel to set
	 */
	public void setPriceLevel(PriceLevel priceLevel) {
		this.priceLevel = priceLevel;
	}

	/**
	 * @return the salesTaxAmount
	 */
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

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the isVoid
	 */
	public boolean getIsVoid() {
		return isVoid;
	}

	/**
	 * @return the estimate
	 */
	public Estimate getEstimate() {
		return estimate;
	}

	/**
	 * @param estimate
	 *            the estimate to set
	 */
	public void setEstimate(Estimate estimate) {
		this.estimate = estimate;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isImported) {
			return false;
		}
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		/**
		 * 
		 * To update the Status of the Estimate (if any) Involved in this Sales
		 * Order. If any Estimate is involved then we should mark it as PAID
		 */
		modifyEstimate(this, true);
		// if (this.estimate != null) {
		// // To Mark the Estimate as Invoiced.
		// this.estimate.setTurnedToInvoiceOrSalesOrder(Boolean.TRUE);
		//
		// // To update the status of the Estimate as Accepted.
		// this.estimate.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		// }
		return false;
	}

	private void modifyEstimate(SalesOrder salesorder, boolean isAddition) {
		if (salesorder.estimate == null)
			return;
		Session session = HibernateUtil.getCurrentSession();
		Estimate estimate = (Estimate) session.get(Estimate.class,
				salesorder.estimate.id);
		if (estimate != null) {
			boolean isPartiallyInvoiced = false;
			boolean flag = true;
			if (salesorder.transactionItems != null
					&& salesorder.transactionItems.size() > 0) {

				FinanceLogger
						.log(
								"update the Status of the Estimate with Number {0}  (if any) Involved in this Invoice ",
								String.valueOf(this.estimate.number));

				for (TransactionItem transactionItem : salesorder.transactionItems) {
					/**
					 * This is to know whether this transaction item is of new
					 * one or it's came from any Sales Order.
					 */

					if (transactionItem.referringTransactionItem != null) {
						TransactionItem referringTransactionItem = (TransactionItem) session
								.get(
										TransactionItem.class,
										transactionItem.referringTransactionItem
												.getId());
						double amount = 0d;
						if (!isAddition)
							if (transactionItem.type == TransactionItem.TYPE_ITEM
									|| transactionItem.type == TransactionItem.TYPE_SERVICE) {
								if (DecimalUtil
										.isLessThan(
												transactionItem.lineTotal,
												transactionItem.quantity
														* referringTransactionItem.unitPrice))
									referringTransactionItem.usedamt -= transactionItem.lineTotal;
								else
									referringTransactionItem.usedamt -= transactionItem.quantity
											* referringTransactionItem.unitPrice;
							} else
								referringTransactionItem.usedamt -= transactionItem.lineTotal;

						else {
							if (transactionItem.type == TransactionItem.TYPE_ITEM
									|| transactionItem.type == TransactionItem.TYPE_SERVICE) {
								if (DecimalUtil
										.isLessThan(
												transactionItem.lineTotal,
												transactionItem.quantity
														* referringTransactionItem.unitPrice))
									referringTransactionItem.usedamt += transactionItem.lineTotal;
								else
									referringTransactionItem.usedamt += transactionItem.quantity
											* referringTransactionItem.unitPrice;
							} else
								referringTransactionItem.usedamt += transactionItem.lineTotal;
						}

						amount = referringTransactionItem.usedamt;
						/**
						 * This is to save changes to the invoiced amount of the
						 * referring transaction item to this transaction item.
						 */
						session.update(referringTransactionItem);

						if (flag
								&& ((transactionItem.type == TransactionItem.TYPE_ACCOUNT
										|| transactionItem.type == TransactionItem.TYPE_SALESTAX || ((transactionItem.type == TransactionItem.TYPE_ITEM || transactionItem.type == TransactionItem.TYPE_SERVICE) && DecimalUtil
										.isLessThan(
												transactionItem.quantity,
												referringTransactionItem.quantity))))) {
							if (isAddition ? DecimalUtil.isLessThan(amount,
									referringTransactionItem.lineTotal)
									: DecimalUtil.isGreaterThan(amount, 0)) {
								isPartiallyInvoiced = true;
								flag = false;
							}
						}
						// if (id != 0l && !invoice.isVoid())
						// referringTransactionItem.usedamt +=
						// transactionItem.lineTotal;

					}

				}
			}
			/**
			 * Updating the Status of the Sales Order involved in this Invoice
			 * depending on the above Analysis.
			 */
			if (!isPartiallyInvoiced) {
				double usdAmount = 0;
				for (TransactionItem orderTransactionItem : estimate.transactionItems)
					// if (orderTransactionItem.getType() != 6)
					usdAmount += orderTransactionItem.usedamt;
				// else
				// usdAmount += orderTransactionItem.lineTotal;
				if (DecimalUtil.isLessThan(usdAmount, estimate.netAmount))
					isPartiallyInvoiced = true;
			}
			if (isPartiallyInvoiced) {
				estimate.status = Estimate.STATUS_OPEN;
			} else {
				estimate.status = isAddition ? Estimate.STATUS_ACCECPTED
						: Estimate.STATUS_OPEN;
			}
			session.saveOrUpdate(estimate);

		}

		// if (invoice.estimate != null) {
		// FinanceLogger
		// .log(
		// "update the Status of the Estimate with Number {0}  (if any) Involved in this Invoice ",
		// String.valueOf(this.estimate.number));
		//
		// // To Mark the Estimate as Invoiced.
		// invoice.estimate.setTurnedToInvoiceOrSalesOrder(isCreated);
		//
		// // To update the status of the Estimate as Accepted.
		// invoice.estimate.status = isCreated ? Estimate.STATUS_ACCECPTED
		// : Estimate.STATUS_OPEN;
		// }
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		super.onUpdate(session);
		if (this.transactionItems != null) {
			for (TransactionItem ti : this.transactionItems) {
				if (ti instanceof Lifecycle) {
					Lifecycle lifeCycle = (Lifecycle) ti;
					lifeCycle.onUpdate(session);
				}
			}
		}
		return false;
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

		return this.customer;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_CUSTOMER;
	}

	@Override
	public String toString() {
		return AccounterConstants.TYPE_SALES_ORDER;
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

		return this.customer;
	}

	// @Override
	// public boolean equals(SalesOrder so) {
	// // TODO Auto-generated method stub
	// if (this.id == so.id
	// && (this.transactionDate != null && so.transactionDate != null) ?
	// (this.transactionDate
	// .equals(so.transactionDate))
	// : true && (this.customer != null && so.customer != null) ? (this.customer
	// .equals(so.customer))
	// : true && (this.estimate != null && so.estimate != null) ? (this.estimate
	// .equals(so.estimate))
	// : true && (this.shippingAdress != null && so.shippingAdress != null) ?
	// (this.shippingAdress
	// .equals(so.shippingAdress))
	// : true && (this.shippingMethod != null && so.shippingMethod != null) ?
	// (this.shippingMethod
	// .equals(so.shippingMethod))
	// : true && (this.shippingTerm != null && so.shippingTerm != null) ?
	// (this.shippingTerm
	// .equals(so.shippingTerm))
	// : true && (this.contact != null && so.contact != null) ? (this.contact
	// .equals(so.contact))
	// : true && (this.billingAddress
	// .equals(so.billingAddress)) ? (this.billingAddress
	// .equals(so.billingAddress))
	// : true
	// && (this.customerOrderNumber == so.customerOrderNumber)
	// && (this.dueDate != null && so.dueDate != null) ? (this.dueDate
	// .equals(so.dueDate))
	// : true && (this.transactionItems
	// .size() == so.transactionItems
	// .size())) {
	// for (int i = 0; i < this.transactionItems.size(); i++) {
	// if (!this.transactionItems.get(i).equals(
	// so.transactionItems.get(i)))
	// return false;
	//	
	// }
	// return true;
	// }
	//	
	// return false;
	// }

	public boolean equals(SalesOrder salesOrder) {
		if (this.id == salesOrder.id) {
			return true;
		}

		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) {
		// Session session = HibernateUtil.getCurrentSession();
		// SalesOrder salesOrder = (SalesOrder) clonedObject;
		// String string =
		// "from com.vimukti.core.Invoice I where I.salesOrder=?";
		// Query query = session.createQuery(string).setParameter(1, this);
		//
		// List<Invoice> list = query.list();
		// if (list != null && list.size() > 0) {
		// for (Invoice invoice : list) {
		// for (TransactionItem transactionItem : this.transactionItems) {
		// for (TransactionItem item : invoice.transactionItems) {
		// if (transactionItem == item.referringTransactionItem) {
		//                         
		// }
		// }
		// }
		//
		// }

		// }

		SalesOrder salesOrder = (SalesOrder) clonedObject;
		// if (this.status != STATUS_DELETED
		// && (this.status != STATUS_CANCELLED || this.status !=
		// STATUS_COMPLETED)) {
		if (this.status != STATUS_DELETED && this.status == STATUS_OPEN) {
			for (TransactionItem item : salesOrder.transactionItems) {
				if (DecimalUtil.isLessThan(item.lineTotal, item.usedamt)
						|| DecimalUtil.isEquals(item.lineTotal, item.usedamt)) {
					this.status = STATUS_COMPLETED;
				} else {
					this.status = STATUS_OPEN;
				}
			}
			if (this.estimate != null || salesOrder.estimate != null)
				if (this.estimate == null && salesOrder.estimate != null) {
					modifyEstimate(salesOrder, false);
				} else if (this.estimate != null && salesOrder.estimate == null) {
					modifyEstimate(this, true);
				} else if (!this.estimate.equals(salesOrder.estimate)) {
					modifyEstimate(salesOrder, false);
					modifyEstimate(this, true);
				} else {
					for (TransactionItem transactionItem : salesOrder.transactionItems) {
						if (transactionItem.referringTransactionItem != null
								&& DecimalUtil
										.isGreaterThan(
												transactionItem.referringTransactionItem.usedamt,
												0)) {
							transactionItem.referringTransactionItem.usedamt -= transactionItem.lineTotal;
						}
					}
					modifyEstimate(this, true);

				}
		}

		super.onEdit(clonedObject);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {

		// if (this.status == STATUS_COMPLETED || this.status ==
		// STATUS_CANCELLED
		// || this.status == STATUS_PAID_OR_APPLIED_OR_ISSUED) {
		//			
		// throw new InvalidOperationException(
		// "This SalesOrder can't be edited, becuase it is Completed or Canceled "
		// + this.getNumber());
		// }
		return true;
	}

}
