package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Transaction;

import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.IMUser;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.EMailJob;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.mobile.AccounterChatServer;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.PhoneRequirement;
import com.vimukti.accounter.mobile.requirements.TermsAndCunditionsRequirement;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.utils.Security;
import com.vimukti.accounter.web.client.Global;

public class SignupCommand extends NewAbstractCommand {
	private static final String FIRST_NAME = "firstname";
	private static final String LAST_NAME = "lastname";
	private static final String SUBSCRIBED_NEWSLETTER = "subscribed";
	private static final String EMAIL = "email";
	private static final String PHONE = "phone";
	private static final String COUNTRY = "country";
	private static final String PASSOWRD = "password";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(FIRST_NAME, getMessages().pleaseEnter(
				getMessages().firstName()), getMessages().firstName(), false,
				true) {
			@Override
			public void setValue(Object value) {
				String v = (String) value;
				v.replaceFirst(String.valueOf(v.charAt(0)),
						String.valueOf(v.charAt(0)).toUpperCase());
				if (v.trim().length() > 0) {
					super.setValue(v);
				}
			}
		});

		list.add(new NameRequirement(LAST_NAME, getMessages().pleaseEnter(
				getMessages().lastName()), getMessages().lastName(), false,
				true) {
			@Override
			public void setValue(Object value) {
				String v = (String) value;
				v.replaceFirst(String.valueOf(v.charAt(0)),
						String.valueOf(v.charAt(0)).toUpperCase());
				if (v.trim().length() > 0) {
					super.setValue(v);
				}
			}
		});

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), false, true) {
			@Override
			public void setValue(Object val) {
				String value = (String) val;
				if (value == null) {
					return;
				} else if (getClient(value) != null) {
					setEnterString(getMessages().enteredEmailAlreadyRegistred());
					return;
				}
				setEnterString("Enter Email");
				super.setValue(value.toLowerCase().trim());
			}
		});

		list.add(new NameRequirement(PASSOWRD, getMessages().pleaseEnter(
				getMessages().password()), getMessages().password(), false,
				true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getNetworkType() == AccounterChatServer.NETWORK_TYPE_MOBILE) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			public InputType getInputType() {
				return new InputType(INPUT_TYPE_PASSWORD);
			}
		});

		list.add(new PhoneRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				false, true));

		list.add(new CountryRequirement(COUNTRY, getMessages().pleaseEnter(
				getMessages().country()), getMessages().country(), false, true,
				null));

		list.add(new BooleanRequirement(SUBSCRIBED_NEWSLETTER, true) {

			@Override
			protected String getFalseString() {
				return getMessages().notsubscribed();
			}

			@Override
			protected String getTrueString() {
				return getMessages().subscribed();
			}
		});

		list.add(new TermsAndCunditionsRequirement());
	}

	private void sendPasswordMail(String token, String emailId) {
		System.out.println("Password : " + token);
		Client client = getClient(emailId);
		UsersMailSendar.sendActivationMail(token, client);
	}

	protected String createActivation(String emailID, Context context) {
		String token = SecureUtils.createID(16).toLowerCase().trim();
		Activation activation = new Activation();
		activation.setEmailId(emailID);
		activation.setToken(token);
		activation.setSignUpDate(new Date());
		saveEntry(activation, context);
		return token;
	}

	private void saveEntry(Object object, Context context) {
		Transaction beginTransaction = context.getHibernateSession()
				.beginTransaction();
		context.getHibernateSession().save(object);
		beginTransaction.commit();
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().account());
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		Result result = new Result();
		Boolean boolean1 = get("TermsAndConditions").getValue();
		if (!boolean1) {
			result.add(getMessages().acceptThetermsAndCondition());
			return result;
		}
		Client client = new Client();
		client.setActive(false);
		client.setUsers(new HashSet<User>());
		String emailId = get(EMAIL).getValue();
		client.setEmailId(emailId.toLowerCase());

		String firstName = get(FIRST_NAME).getValue();
		client.setFirstName(firstName);

		String lastName = get(LAST_NAME).getValue();
		client.setLastName(lastName);

		client.setFullName(Global.get().messages()
				.fullName(firstName, lastName));

		String password = "***REMOVED***";
		if (context.getNetworkType() == AccounterChatServer.NETWORK_TYPE_MOBILE) {
			password = get(PASSOWRD).getValue();
			context.setLast(RequirementType.STRING, emailId);
		} else {
			password = SecureUtils.createID(16);
			sendPasswordMail(password, emailId);
		}
		String passwordWithHash = HexUtil.bytesToHex(Security.makeHash(emailId
				+ password));
		client.setPassword(passwordWithHash);

		String phoneNumber = get(PHONE).getValue();
		client.setPhoneNo(phoneNumber);

		String country = get(COUNTRY).getValue();
		client.setCountry(country);

		Boolean isSubscribedToNewsLetter = get(SUBSCRIBED_NEWSLETTER)
				.getValue();
		client.setSubscribedToNewsLetters(isSubscribedToNewsLetter);

		saveEntry(client, context);

		UsersMailSendar.sendActivationMail(createActivation(emailId, context),
				client);

		IMUser imUser = new IMUser();
		imUser.setClient(client);
		imUser.setNetworkId(context.getNetworkId());
		imUser.setNetworkType(context.getNetworkType());
		saveEntry(imUser, context);
		return null;
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createdSucessFullysentAnActivationCodetoYourMail();
	}

	@Override
	protected String getWelcomeMessage() {
		return "Sign Up Command is Activated";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		context.setString(null);
		if (string == null || string.isEmpty()) {
			return null;
		}
		if (string.contains("@")) {
			get(EMAIL).setValue(string);
		} else {
			get(FIRST_NAME).setValue(string);
		}
		return null;
	}
}
