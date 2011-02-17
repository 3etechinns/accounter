package com.vimukti.accounter.web.client.core;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * This transactionItem Object is used for
 * CashSales,CustomerCreditMemo,Estimate,Invoice
 * CashPurchase,CreditCardCharge,EnterBill,VendorCreditMemo
 * 
 */

public class ClientTransactionItem implements IAccounterCore {

	private static final long serialVersionUID = 1L;
	public static final int TYPE_ITEM = 1;
	public static final int TYPE_COMMENT = 2;
	public static final int TYPE_SALESTAX = 3;
	public static final int TYPE_ACCOUNT = 4;
	public static final int TYPE_SERVICE = 6;

	int version;

	String stringID;

	int type;

	String item;

	ClientTAXItem taxItem;

	private String taxitem;

	String taxCode;

	// String taxCode;
	String vatItem;

	String account;

	ClientTAXGroup taxGroup;

	String taxItemGroup;

	String description;

	double quantity = 1;

	double unitPrice;

	double discount;

	double lineTotal;

	boolean isTaxable;

	private ClientTransaction transaction;

	double usedamt;

	double backOrder;

	double VATfraction;

	boolean isVoid;

	/**
	 * Every TransactionItem consists of a set of for the purpose
	 */

	public Set<ClientItemBackUp> itemBackUpList = new HashSet<ClientItemBackUp>();

	/**
	 * Every TransactionItem in UK consists of a set of for the purpose
	 */

	public Set<ClientTAXRateCalculation> taxRateCalculationEntriesList = new HashSet<ClientTAXRateCalculation>();

	/**
	 * For the purpose of SalesOrder. To know how much amount of this
	 * transactionItem in SalesOrder is invoiced
	 */
	String referringTransactionItem;

	public double getVATfraction() {
		return VATfraction;
	}

	public void setVATfraction(double tfraction) {
		VATfraction = tfraction;
	}

	/**
	 * @return the vatItem
	 */
	public String getVatItem() {
		return vatItem;
	}

	/**
	 * @param vatItem
	 *            the vatItem to set
	 */
	public void setVatItem(String vatItem) {
		this.vatItem = vatItem;
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
	public void setInvoiced(double usedamt) {
		// this.usedamt = usedamt;
	}

	/**
	 * @return the referringTransactionItem
	 */
	public String getReferringTransactionItem() {
		return referringTransactionItem;
	}

	/**
	 * @param referringTransactionItem
	 *            the referringTransactionItem to set
	 */
	public void setReferringTransactionItem(String referringTransactionItem) {
		this.referringTransactionItem = referringTransactionItem;
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

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the item
	 */
	public String getItem() {
		return item;
	}

	/**
	 * @return the taxCode
	 */
	public ClientTAXItem getTaxItem() {
		return taxItem;
	}

	// public String getTaxCode() {
	// return this.taxCode;
	// }

	/**
	 * @return the account
	 */
	public String getAccount() {
		return this.account;
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
	public double getQuantity() {
		return quantity;
	}

	/**
	 * @return the unitPrice
	 */
	public double getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @return the vatCode
	 */
	public String getTaxCode() {
		return taxCode;
	}

	/**
	 * @param taxCode
	 *            the vatCode to set
	 */
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
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
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @param itemId
	 *            the item to set
	 */
	public void setItem(String item) {
		this.item = item;
	}

	/**
	 * @param taxCodeId
	 *            the taxCode to set
	 */
	public void setTaxItem(ClientTAXItem taxItem) {
		this.taxItem = taxItem;
	}

	// public void setTaxCode(String taxCodeId) {
	// this.taxCode = taxCodeId;
	// }

	/**
	 * @param accountId
	 *            the account to set
	 */
	public void setAccount(String accountId) {
		this.account = accountId;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	/**
	 * @param unitPrice
	 *            the unitPrice to set
	 */
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
	}

	/**
	 * @param lineTotal
	 *            the lineTotal to set
	 */
	public void setLineTotal(double lineTotal) {
		this.lineTotal = lineTotal;
	}

	public boolean isTaxable() {
		return isTaxable;
	}

	public void setTaxable(boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	/**
	 * @param transactionId
	 *            the transaction to set
	 */
	public void setTransaction(ClientTransaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * @param transactionId
	 *            the transaction to get
	 */
	public ClientTransaction getTransaction() {
		return this.transaction;
	}

	public String getTaxItemGroup() {
		return taxItemGroup;
	}

	public void setTaxItemGroup(String taxItemGroup) {
		this.taxItemGroup = taxItemGroup;
	}

	public ClientTAXGroup getTaxGroup() {
		return taxGroup;
	}

	public void setTaxGroup(ClientTAXGroup taxGroup) {
		this.taxGroup = taxGroup;
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
			case ClientTransactionItem.TYPE_ACCOUNT:
				buffer.append(" Account:" + String.valueOf(account));
				break;

			case ClientTransactionItem.TYPE_ITEM:
				buffer.append(" Item:" + String.valueOf(item));

			case ClientTransactionItem.TYPE_SALESTAX:
				buffer.append(" SalesTax:" + String.valueOf(taxItem));

			default:
				buffer.append(" Comment" + String.valueOf(description));
				break;
			}

			return buffer.toString();
		} catch (Exception e) {
			return super.toString();
		}
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
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

		return "ClientTransactionItem";
	}

	public void setTaxitem(String taxitem) {
		this.taxitem = taxitem;
	}

	public String getTaxitem() {
		return taxitem;
	}

}
