package com.vimukti.accounter.web.client.core;

public class ClientFinanceLogger implements IAccounterCore {

	public ClientFinanceLogger() {

	}

	long id;
	long createdDate;
	String createdBy;
	String description;
	String logMessge;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogMessge() {
		return logMessge;
	}

	public void setLogMessge(String logMessge) {
		this.logMessge = logMessge;
	}

	public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String getClientClassSimpleName() {
		// its not using any where
		return null;
	}

	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.FINANCELOG;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public ClientFinanceLogger clone() {
		return null;

	}

}
