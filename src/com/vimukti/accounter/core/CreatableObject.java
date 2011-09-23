package com.vimukti.accounter.core;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

public abstract class CreatableObject implements Lifecycle {

	protected long id;

	protected User createdBy;
	protected User lastModifier;
	protected Timestamp createdDate;
	protected Timestamp lastModifiedDate;
	protected int version;
	private Company company;

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getCreatedBy() {
		return this.createdBy;
	}

	public void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}

	public User getLastModifier() {
		return this.lastModifier;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getID() {
		return id;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (id == 0) {
			this.createdBy = AccounterThreadLocal.get();
			this.createdDate = AccounterThreadLocal.currentTime();
		}
		this.lastModifier = AccounterThreadLocal.get();
		this.lastModifiedDate = AccounterThreadLocal.currentTime();
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		this.lastModifier = AccounterThreadLocal.get();
		this.lastModifiedDate = AccounterThreadLocal.currentTime();
		return false;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// TODO Auto-generated method stub

	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
