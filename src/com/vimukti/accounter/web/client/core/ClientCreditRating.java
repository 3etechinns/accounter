package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientCreditRating implements IAccounterCore {

	long id;

	String name;
	boolean isDefault;

	public ClientCreditRating() {
	}

	/**
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.CREDIT_RATING;
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
		return "ClientCreditRating";
	}

	public ClientCreditRating clone() {
		ClientCreditRating creditRating = (ClientCreditRating) this.clone();
		return creditRating;

	}

}
