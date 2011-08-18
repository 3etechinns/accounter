package com.vimukti.accounter.web.client.core;

public class ClientTAXCode implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	long id;
	String name;
	String description;
	boolean isTaxable;
	boolean isActive;
	long TAXItemGrpForPurchases;
	long TAXItemGrpForSales;

	boolean isECSalesEntry;
	boolean isDefault;
	private long taxAgency;

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isTaxable() {
		return isTaxable;
	}

	public void setTaxable(boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public long getTAXItemGrpForPurchases() {
		return TAXItemGrpForPurchases;
	}

	public void setTAXItemGrpForPurchases(long vATItemGrpForPurchases) {
		TAXItemGrpForPurchases = vATItemGrpForPurchases;
	}

	public long getTAXItemGrpForSales() {
		return TAXItemGrpForSales;
	}

	public void setTAXItemGrpForSales(long taxItemGroup) {
		TAXItemGrpForSales = taxItemGroup;
	}

	/**
	 * @return the isECSalesEntry
	 */
	public boolean isECSalesEntry() {
		return isECSalesEntry;
	}

	/**
	 * @param isECSalesEntry
	 *            the isECSalesEntry to set
	 */
	public void setECSalesEntry(boolean isECSalesEntry) {
		this.isECSalesEntry = isECSalesEntry;
	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientTAXCode";
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TAX_CODE;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public void setTaxAgency(long taxAgency) {
		this.taxAgency = taxAgency;
	}

	public long getTaxAgency() {
		return taxAgency;
	}

	public ClientTAXCode clone() {
		ClientTAXCode taxCode = (ClientTAXCode) this.clone();
		return taxCode;
	}

}
