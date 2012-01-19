package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class CustomField extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String name;

	boolean showCustomer;
	boolean showVendor;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isShowCustomer() {
		return showCustomer;
	}

	public void setShowCustomer(boolean showCustomer) {
		this.showCustomer = showCustomer;
	}

	public boolean isShowVendor() {
		return showVendor;
	}

	public void setShowVendor(boolean showVendor) {
		this.showVendor = showVendor;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		super.onDelete(session);

		session.getNamedQuery("delete.customFiledValuesByCustomField")
				.setParameter("Id", this.getID()).executeUpdate();

		return false;
	}

	@Override
	public int getObjType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.CustomField()).gap();

		if (name != null) {
			w.put(messages.name(), this.name).gap();
		}
		w.put(messages.customer(), this.showCustomer).gap();
		w.put(messages.vendor(), this.showVendor).gap();
	}

}
