package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * This transactionItem Object is used for
 * CashSales,CustomerCreditMemo,Estimate,Invoice
 * CashPurchase,CreditCardCharge,EnterBill,VendorCreditMemo
 * 
 */

public class TransactionItem implements IAccounterServerCore, Lifecycle {

	/**
  * 
  */
	private static final long serialVersionUID = 8031314980652784129L;
	public static final int TYPE_ITEM = 1;
	public static final int TYPE_COMMENT = 2;
	// public static final int TYPE_SALESTAX = 3;
	public static final int TYPE_ACCOUNT = 4;
	// public static final int TYPE_SERVICE = 6;

	int version;

	/**
	 * @return the vatItem
	 */
	// public TAXItem getTaxItem() {
	// return taxItem;
	// }

	private long id;
	/**
	 * @param vatItem
	 *            the vatItem to set
	 */
	// public void setVatItem(TAXItem vatItem) {
	// this.vatItem = vatItem;
	// }

	/**
	 * The type field is used to differ the Item, comment, salestax, account
	 * types of the TransactionItem.
	 */
	int type;

	/**
	 * This is the reference to the Item which is a type of TransactionItem.
	 */
	@ReffereredObject
	Item item;

	/**
	 * This is the reference to the TaxCode which is a type of TransactionItem.
	 */
	// @ReffereredObject
	// TAXItem taxItem;

	/**
	 * This is the reference to the Account which is a type of TransactionItem.
	 */
	@ReffereredObject
	Account account;

	/**
	 * This is common to all types of TransactionItems. It is used to write any
	 * description of the TransactionItem which is being used.
	 */
	private String description;

	/**
	 * The quantity of TransactionItem to be used.
	 */
	// int quantity;
	private Quantity quantity;

	/**
	 * The unit price which is given to be multiplied with quantity and reduced
	 * by discount which results in Line total
	 */
	double unitPrice;

	/**
	 * The amount of discount by which we want to decrease the price of
	 * TransactionItem
	 */
	double discount;

	/**
	 * This is resulted by unit price which is multiplied by quantity and
	 * reduced by discount amount.
	 */
	double lineTotal;

	/**
	 * To indicate whether this particular TransactionItem is taxable or not.
	 */
	boolean isTaxable;

	/**
	 * This reference is maintained to apply the tax for all the
	 * transactionItems equally.
	 */
	// @ReffereredObject
	// TAXGroup taxGroup;

	/**
	 * This reference to Transaction is maintained to indicate the source of
	 * this TransactionItem
	 */
	@ReffereredObject
	Transaction transaction;

	/**
	 * This is used for the logical functionality internally
	 */
	// private boolean flag;
	/**
	 * This is for {@link SalesOrder} to indicate how much amount is invoiced
	 */
	double usedamt;

	/**
	 * This is for {@link SalesOrder} to indicate how much has to be order back
	 */
	double backOrder;

	/**
	 * The fraction of amount which was calculated by the VAT rates in the VAT
	 * codes that were selected for this TransactionItem in UK accounting
	 */
	double VATfraction;

	/**
	 * Every TransactionItem consists of a set of {@link TaxRateCalculation} for
	 * the purpose
	 */

	@ReffereredObject
	ItemBackUp itemBackUp;
	/**
	 * Every TransactionItem in UK consists of a set of
	 * {@link TAXRateCalculation} for the purpose
	 */
	@ReffereredObject
	private Set<TAXRateCalculation> taxRateCalculationEntriesList = new HashSet<TAXRateCalculation>();

	/**
	 * For the purpose of SalesOrder. To know how much amount of this
	 * transactionItem in SalesOrder is invoiced
	 */
	@ReffereredObject
	TransactionItem referringTransactionItem;

	/**
	 * This is to specify whether this Transaction Item has become void or not.
	 * 
	 */
	boolean isVoid;

	/**
	 * This field is used internally for the logical functionality to know that
	 * the transaction is voided before or not.
	 */
	boolean isVoidBefore;

	/**
	 * This amount is used for storing the amount that by which amount the
	 * effecting account is updated.
	 */
	private double updateAmount;

	TAXCode taxCode;
	// TAXItem vatItem;
	private boolean isOnSaveProccessed;

	private AccounterClass accounterClass;
	@ReffereredObject
	private Customer customer;

	private Warehouse wareHouse;

	private boolean isBillable;

	public TransactionItem() {

	}

	public double getVATfraction() {
		return VATfraction;
	}

	public void setVATfraction(double tfraction) {
		VATfraction = tfraction;
	}

	/**
	 * @return the vatCode
	 */
	public TAXCode getTaxCode() {
		return taxCode;
	}

	/**
	 * @param vatCode
	 *            the vatCode to set
	 */
	public void setTaxCode(TAXCode vatCode) {
		this.taxCode = vatCode;
	}

	/**
	 * @return the invoiced
	 */
	public double getInvoiced() {
		return usedamt;
	}

	/**
	 * @param invoiced
	 *            the invoiced to set
	 */
	public void setInvoiced(double invoiced) {
		this.usedamt = invoiced;
	}

	/**
	 * @return the backOrder
	 */
	public double getBackOrder() {
		return backOrder;
	}

	/**
	 * @param backOrder
	 *            the backOrder to set
	 */
	public void setBackOrder(double backOrder) {
		this.backOrder = backOrder;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
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
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @return the taxCode
	 */
	// public TAXItem getTaxItem() {
	// return taxItem;
	// }

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the quantity
	 */
	public Quantity getQuantity() {
		return quantity;
	}

	/**
	 * @return the unitPrice
	 */
	public double getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @return the discount
	 */
	public double getDiscount() {
		return discount;
	}

	/**
	 * @return the lineTotal
	 */
	public double getLineTotal() {
		return lineTotal;
	}

	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public boolean isVoid() {
		return isVoid;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	@Override
	public String toString() {
		try {
			StringBuffer buffer = new StringBuffer();

			switch (type) {
			case TransactionItem.TYPE_ACCOUNT:
				buffer.append(" Account:" + String.valueOf(account));
				break;

			case TransactionItem.TYPE_ITEM:
				buffer.append(" Item:" + String.valueOf(item));

			default:
				buffer.append("Service :" + String.valueOf(item));
				break;
			}

			return buffer.toString();
		} catch (Exception e) {
			return super.toString();
		}
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {

		if (this.transaction.type == Transaction.TYPE_EMPLOYEE_EXPENSE)
			return false;

		if (shouldUpdateAccounts(true)) {
			this.transaction.clonedTransactionDate = this.transaction.oldTransaction.transactionDate;

			// double amount = (isPositiveTransaction() ? -1d : 1d)
			// * (this.transaction.isAmountsIncludeVAT() ? this.lineTotal
			// - this.VATfraction : this.lineTotal);
			if (this.type == TYPE_ACCOUNT || this.type == TYPE_ITEM) {
				Account effectingAccount = this.getEffectingAccount();
				if (effectingAccount != null) {
					effectingAccount.updateCurrentBalance(this.transaction, -1
							* this.updateAmount, transaction.currencyFactor);

					session.saveOrUpdate(effectingAccount);
					effectingAccount.onUpdate(session);
				}

				if (this.type == TYPE_ITEM
						&& this.item.getType() == Item.TYPE_INVENTORY_PART) {
					this.updateWareHouse(true);
				}
			}
		}

		deleteCreatedEntries(session);
		//
		// if (this.isTaxable
		// && taxGroup != null
		// && Company.getCompany().getAccountingType() ==
		// Company.ACCOUNTING_TYPE_US)
		// Company.setTaxRateCalculation(this, session, amount);
		// else if (Company.getCompany().getAccountingType() ==
		// Company.ACCOUNTING_TYPE_UK)
		// Company.setVATRateCalculation(this, session);
		// } else if (this.type == TYPE_SALESTAX) {
		// if (Company.getCompany().getAccountingType() ==
		// Company.ACCOUNTING_TYPE_US) {
		//
		// Company.setTaxRateCalculation(this, session, amount);
		// } else if (Company.getCompany().getAccountingType() ==
		// Company.ACCOUNTING_TYPE_UK) {
		// Company.setVATRateCalculation(this, session);
		// }
		// }
		// doReverseEffect(session);
		return false;
	}

	@Override
	public void onLoad(Session session, Serializable arg1) {

		this.isVoidBefore = isVoid;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed())
			return true;

		this.setOnSaveProccessed(true);

		initVRCids();

		if (this.transaction.type == Transaction.TYPE_EMPLOYEE_EXPENSE
				&& ((CashPurchase) this.transaction).expenseStatus != CashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED)
			return false;

		doCreateEffect(session);
		return false;
	}

	private void initVRCids() {
		if (this.getTaxRateCalculationEntriesList() == null
				|| this.getTaxRateCalculationEntriesList().isEmpty())
			return;

		TAXRateCalculation vrc1 = null, vrc2 = null;

		/*
		 * In synchronization, we will get vatRateCalculationEntriesList from
		 * desktop client which the transactionItem is null. Here we have to
		 * take the id and remove that entry from vatRateCalculationEntriesList,
		 * if and only if the transaction is going to void otherwise we have to
		 * remove all entries from the list.
		 */
		for (TAXRateCalculation vrc : this.getTaxRateCalculationEntriesList()) {
			if (this.isVoid && vrc.transactionItem == null) {
				// vrcids.add(vrc.getID());

				if (vrc1 == null)
					vrc1 = vrc;
				else
					vrc2 = vrc;

			}
			// else if (!this.isVoid)
			// vrcids.add(vrc.getID());
		}

		if (vrc1 != null)
			this.getTaxRateCalculationEntriesList().remove(vrc1);
		if (vrc2 != null)
			this.getTaxRateCalculationEntriesList().remove(vrc2);

		if (!this.isVoid())
			this.getTaxRateCalculationEntriesList().clear();

	}

	public void doCreateEffect(Session session) {

		/**
		 * First take the Back up of the TransactionItem information
		 */
		// ItemBackUp itemBackUp = null;
		// if (this.type == TYPE_ITEM) {
		// this.itemBackUp = new ItemBackUp(this);
		// // this.itemBackUpList.add(itemBackUp);
		// }
		if (shouldUpdateAccounts(false)) {
			double amount = (isPositiveTransaction() ? 1d : -1d)
					* (this.transaction.isAmountsIncludeVAT() ? this.lineTotal
							- this.VATfraction : this.lineTotal);
			this.setUpdateAmount(amount);
			if (this.type == TYPE_ACCOUNT || this.type == TYPE_ITEM) {
				Account effectingAccount = getEffectingAccount();
				if (effectingAccount != null) {
					effectingAccount.updateCurrentBalance(this.transaction,
							amount, transaction.currencyFactor);
					session.saveOrUpdate(effectingAccount);
					effectingAccount.onUpdate(session);
				}

				if ((getCompany().getPreferences().isTrackTax())
						&& this.isTaxable)
					Company.setTAXRateCalculation(this, session);

				if (this.type == TYPE_ITEM
						&& this.item.getType() == Item.TYPE_INVENTORY_PART) {
					this.updateWareHouse(false);
				}
			}
		}
		// else if (this.type == TYPE_SALESTAX) {
		// if (Company.getCompany().getAccountingType() ==
		// Company.ACCOUNTING_TYPE_US) {
		// // Company.setTaxRateCalculation(this, session, amount);
		// } else if (Company.getCompany().getAccountingType() ==
		// Company.ACCOUNTING_TYPE_UK) {
		// Company.setTAXRateCalculation(this, session);
		// }
		// }
		// ChangeTracker.put(this);
	}

	private boolean shouldUpdateAccounts(boolean whenDeleting) {
		switch (transaction.getType()) {
		case Transaction.TYPE_ESTIMATE:
			Estimate est = (Estimate) transaction;
			if (whenDeleting) {
				return est.getUsedInvoice() != est.getOldUsedInvoice();
			} else {
				return est.getUsedInvoice() != null;
			}
		case Transaction.TYPE_SALES_ORDER:
			SalesOrder order = ((SalesOrder) transaction);
			if (whenDeleting) {
				return order.getUsedInvoice() != order.getOldUsedInvoice();
			} else {
				return order.getUsedInvoice() != null;
			}
		case Transaction.TYPE_PURCHASE_ORDER:
			return ((PurchaseOrder) transaction).getUsedBill() != null;
		default:
			return true;
		}
	}

	public void updateWareHouse(boolean isVoidOrDelete) {
		if (wareHouse == null) {
			return;
		}
		Session session = HibernateUtil.getCurrentSession();
		Unit selectedUnit = this.quantity.getUnit();
		Measurement defaultMeasurement = this.getItem().getMeasurement();
		Unit defaultUnit = defaultMeasurement.getDefaultUnit();
		double value = (this.quantity.getValue() * selectedUnit.getFactor())
				/ defaultUnit.getFactor();
		if (transaction.isDebitTransaction()) {
			wareHouse.updateItemStatus(item, value, isVoidOrDelete);
		} else {
			wareHouse.updateItemStatus(item, value, !isVoidOrDelete);
		}
		wareHouse.onUpdate(session);
		session.saveOrUpdate(wareHouse);
	}

	private Company getCompany() {
		return transaction.getCompany();
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		initVRCids();

		if (this.transaction.type == Transaction.TYPE_EMPLOYEE_EXPENSE
				&& ((CashPurchase) this.transaction).expenseStatus != CashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED)
			return false;

		if (this.isBecameVoid()) {

			doReverseEffect(session);
		}
		// ChangeTracker.put(this);
		return false;
	}

	/**
	 * It rolls back all the effect that was done at the time of creation of the
	 * object. This rolling back involves updating the effectingAccount's
	 * balances, reversing the effect of the Tax amount in the corresponding
	 * tables.
	 * 
	 * @param session
	 */
	public void doReverseEffect(Session session) {

		// double amount = (isPositiveTransaction() ? -1d : 1d)
		// * (this.transaction.isAmountsIncludeVAT() ? this.lineTotal
		// - this.VATfraction : this.lineTotal);
		if (this.type == TYPE_ACCOUNT || this.type == TYPE_ITEM) {
			Account effectingAccount = this.getEffectingAccount();
			if (effectingAccount != null) {
				effectingAccount.updateCurrentBalance(this.transaction, -1
						* this.getUpdateAmount(), transaction.currencyFactor);
				effectingAccount.onUpdate(session);
				session.saveOrUpdate(effectingAccount);

			}

			if (this.isTaxable)
				Company.setTAXRateCalculation(this, session);

			if (this.type == TYPE_ITEM
					&& this.item.getType() == Item.TYPE_INVENTORY_PART) {
				this.updateWareHouse(true);
			}

		}
		// else if (this.type == TYPE_SALESTAX) {
		// if (Company.getCompany().getAccountingType() ==
		// Company.ACCOUNTING_TYPE_US) {
		// // Company.setTaxRateCalculation(this, session, amount);
		// } else if (Company.getCompany().getAccountingType() ==
		// Company.ACCOUNTING_TYPE_UK) {
		// Company.setTAXRateCalculation(this, session);
		// }
		// }

	}

	// private TaxRateCalculation getTaxRateCalculation(
	// Set<TaxRateCalculation> trcList, TaxCode code) {
	//
	// TaxRateCalculation result = null;
	//
	// for (TaxRateCalculation trc : trcList) {
	//
	// if (trc.transactionItem == this && trc.taxCode == code) {
	// result = trc;
	// break;
	// }
	//
	// }
	//
	// return result;
	// }

	public boolean isPositiveTransaction() {
		return this.transaction.isPositiveTransaction();
	}

	protected boolean isBecameVoid() {
		return isVoid && !this.isVoidBefore;
	}

	/**
	 * This is called in the Transaction class so as to effect the account
	 * involved which plays the vital role in the current transaction. For
	 * different transactions it also varies differently.
	 * 
	 * @return {@link Account}
	 */
	public Account getEffectingAccount() {

		switch (this.type) {

		case TYPE_ACCOUNT:

			return this.account;

		case TYPE_ITEM:
			// ItemBackUp itemBackUp = getItemBackUp(itemBackUpList, this.item);

			if (this.isVoid) {
				if (this.transaction.isDebitTransaction()) {
					if (this.transaction.getType() == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
						return this.itemBackUp.getIncomeAccount();
					}
					// else if (this.transaction.getType() ==
					// Transaction.TYPE_WRITE_CHECK
					// && ((WriteCheck) this.transaction).getCustomer() != null)
					// {
					//
					// return this.itemBackUp.getIncomeAccount();
					//
					// }
					else {

						return this.itemBackUp.getExpenseAccount();
					}

				} else {
					if (this.transaction.getType() == Transaction.TYPE_VENDOR_CREDIT_MEMO) {
						return this.itemBackUp.getExpenseAccount();
					} else {
						return this.itemBackUp.getIncomeAccount();
					}

				}
			} else {
				if (this.transaction.isDebitTransaction()) {
					if (this.transaction.getType() == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
						return this.item.getIncomeAccount();
					}
					// else if (this.transaction.getType() ==
					// Transaction.TYPE_WRITE_CHECK
					// && ((WriteCheck) this.transaction).getCustomer() != null)
					// {
					//
					// return this.item.getIncomeAccount();
					//
					// }
					else {

						return this.item.getExpenseAccount();
					}

				} else {
					if (this.transaction.getType() == Transaction.TYPE_VENDOR_CREDIT_MEMO) {
						return this.item.getExpenseAccount();
					} else {
						return this.item.getIncomeAccount();
					}

				}
			}
		}
		return null;
	}

	public double getEffectiveAmount() {
		double amount = (isPositiveTransaction() ? -1d : 1d)
				* (this.transaction.isAmountsIncludeVAT() ? this.lineTotal
						- this.VATfraction : this.lineTotal);
		return amount;
	}

	public void setType(int type) {
		this.type = type;

	}

	public void setQuantity(Quantity quality) {
		this.quantity = quality;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setLineTotal(double lineTotal) {
		this.lineTotal = lineTotal;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	// public void setTaxItem(TAXItem taxItem) {
	// this.taxItem = taxItem;
	// }

	public boolean isTaxable() {
		return isTaxable;
	}

	public void setTaxable(boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void deleteCreatedEntries(Session session) {

		// if (this.itemBackUpList != null) {
		// this.itemBackUpList.clear();
		// }

		if (this.getTaxRateCalculationEntriesList() != null) {
			this.getTaxRateCalculationEntriesList().clear();
		}
	}

	/**
	 * Checks all the values and references of Transaction Item and gives the
	 * boolean result whether they are equal or not.
	 * 
	 * @param obj
	 * @return
	 */

	public boolean equals(TransactionItem obj) {

		if ((item != null && obj.item != null) ? (item.equals(obj.item))
				: true && (account != null && obj.account != null) ? (account
						.equals(obj.account))
						: true && (transaction != null && obj.transaction != null) ? (transaction
								.equals(obj.transaction)) : true) {

			return true;
		}
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		return true;
	}

	@Override
	protected TransactionItem clone() throws CloneNotSupportedException {
		TransactionItem item = (TransactionItem) super.clone();
		item.setId(0);
		item.taxRateCalculationEntriesList = new HashSet<TAXRateCalculation>();
		return item;
	}

	public void resetId() {
		setId(0);
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	public AccounterClass getAccounterClass() {
		return accounterClass;
	}

	public void setAccounterClass(AccounterClass accounterClass) {
		this.accounterClass = accounterClass;
	}

	public double getUpdateAmount() {
		return updateAmount;
	}

	public void setUpdateAmount(double updateAmount) {
		this.updateAmount = updateAmount;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean isBillable() {
		return isBillable;
	}

	public void setBillable(boolean isBillable) {
		this.isBillable = isBillable;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isOnSaveProccessed() {
		return isOnSaveProccessed;
	}

	public void setOnSaveProccessed(boolean isOnSaveProccessed) {
		this.isOnSaveProccessed = isOnSaveProccessed;
	}

	public Set<TAXRateCalculation> getTaxRateCalculationEntriesList() {
		return taxRateCalculationEntriesList;
	}

	public void setTaxRateCalculationEntriesList(
			Set<TAXRateCalculation> taxRateCalculationEntriesList) {
		this.taxRateCalculationEntriesList = taxRateCalculationEntriesList;
	}

	public Warehouse getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(Warehouse wareHouse) {
		this.wareHouse = wareHouse;
	}

}