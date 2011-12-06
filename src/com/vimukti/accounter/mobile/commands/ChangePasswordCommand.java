package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.Security;

public class ChangePasswordCommand extends NewAbstractCommand {
	private static final String OLDPASSWORD = "oldpass";
	private static final String NEWPASSWORD = "newpass";
	private static final String CONFORMPASSWORD = "conformpass";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().passwordSuccessfullyChanged();
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringRequirement(OLDPASSWORD, getMessages().pleaseEnter(
				getMessages().oldPassword()), getMessages().oldPassword(),
				false, true) {
			@Override
			public void setValue(Object value) {
				Client client = getContext().getIOSession().getClient();
				String passwordWithHash = HexUtil.bytesToHex(Security
						.makeHash(client.getEmailId() + value));
				if (!client.getPassword().equals(passwordWithHash)) {
					addFirstMessage(getMessages().youHaveEnteredWrongPassword());
					return;
				}
				super.setValue(value);
			}
		});

		list.add(new StringRequirement(NEWPASSWORD, getMessages().pleaseEnter(
				getMessages().newPassword()), getMessages().newPassword(),
				false, true) {
			@Override
			public void setValue(Object value) {
				Client client = getContext().getIOSession().getClient();
				String passwordWithHash = HexUtil.bytesToHex(Security
						.makeHash(client.getEmailId() + value));
				if (client.getPassword().equals(passwordWithHash)) {
					addFirstMessage(getMessages()
							.newpasswordAndPreviouspasswordAreSame());
					return;
				}
				super.setValue(value);
			}

		});

		list.add(new StringRequirement(CONFORMPASSWORD, getMessages()
				.pleaseEnter(getMessages().confirmNewPassword()), getMessages()
				.confirmNewPassword(), false, true) {
			@Override
			public void setValue(Object value) {
				String newpass = get(NEWPASSWORD).getValue();
				if (!newpass.equals(value)) {
					addFirstMessage(getMessages().passwordsnotmatched());
					return;
				}
				super.setValue(value);
			}
		});

	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		String pass1 = get(NEWPASSWORD).getValue().toString();
		Client client = context.getIOSession().getClient();
		Session hibernateSession = context.getHibernateSession();
		Transaction beginTransaction = hibernateSession.beginTransaction();
		String passwordWithHash = HexUtil.bytesToHex(Security.makeHash(client
				.getEmailId() + pass1));
		client.setPassword(passwordWithHash);
		hibernateSession.saveOrUpdate(client);
		beginTransaction.commit();
		markDone();
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		return null;
	}
}
