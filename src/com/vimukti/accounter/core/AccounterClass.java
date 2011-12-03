package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class AccounterClass extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	private String className;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	public String getclassName() {
		return className;
	}

	public void setclassName(String trackingClassName) {
		this.className = trackingClassName;
	}

	@Override
	public String getName() {
		return className;
	}

	@Override
	public void setName(String name) {
		this.className = name;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.ACCOUNTER_CLASS;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();
		
		w.put(messages.name(), this.className);
	}
}
