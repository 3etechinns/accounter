package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class Contact implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This will specify which Contact is Primary among all the Contacts of a
	 * {@link Payee}
	 */
	boolean isPrimary = false;
	/**
	 * This is Contact Name
	 */
	String name = "";
	/**
	 * This is Contact Title
	 */
	String title = "";
	/**
	 * This is Contact Business Phone Number.
	 */
	String businessPhone = "";
	/**
	 * This is Contact Email Address
	 */
	String email = "";

	transient private boolean isOnSaveProccessed;

	private int version;

	public Contact() {

	}

	/**
	 * @return the isPrimary
	 */
	public boolean isPrimary() {
		return isPrimary;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the businessPhone
	 */
	public String getBusinessPhone() {
		return businessPhone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {
		return false;
	}

	@Override
	public void onLoad(Session s, Serializable id) {

	}

	@Override
	public boolean onSave(Session s) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		return false;
	}

	@Override
	public boolean onUpdate(Session s) throws CallbackException {
		return false;
	}

	@Override
	public long getID() {

		return 0;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public String toString() {
		return "Contact Name:" + this.name + " Email:" + this.email;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Contact) {
			return ((Contact) obj).getID() == this.getID();
		}
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

		AccounterMessages messages = Global.get().messages();
		w.put(messages.type(), this.title).gap().gap();
		w.put(messages.name(), this.name).gap().gap();
		w.put(messages.email(), this.email);
	}
}
