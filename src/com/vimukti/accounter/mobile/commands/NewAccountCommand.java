package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewAccountCommand extends NewAbstractCommand {

	private static final String ACCOUNT_NAME = "Account Name";
	private static final String ACCOUNT_NUMBER = "Account Number";
	private static final String OPENINGBALANCE = "Opening Balance";
	private static final String ACCOUNT_TYPE = "Account Type";
	private static final String ACTIVE = "Active";

	private static final String ASOF = "AsOf";
	private static final String COMMENTS = "Comments";
	private static final String CONSIDER_AS_CASH_ACCOUNT = "Consider As Cash Account";

	private ClientAccount account;

	@Override
	public String getId() {
		return null;
	}

	@Override
	public String getWelcomeMessage() {
		if (account.getID() == 0) {
			return "Create Account Command is activated.";
		}
		return "Update Account(" + account.getName()
				+ ") Command is activated.";
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringListRequirement(ACCOUNT_TYPE,
				"Please Enter Account Type", "Account Type", true, true, null) {

			@Override
			protected String getSetMessage() {
				return "Account type has been selected";
			}

			@Override
			protected String getSelectString() {
				return "Select Account type";
			}

			@Override
			protected List<String> getLists(Context context) {
				return getAccountTypes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new NameRequirement(ACCOUNT_NAME, "Please Enter Account Name",
				"Name", false, true));

		list.add(new NumberRequirement(ACCOUNT_NUMBER,
				"Please Enter Account number", "Account Number", false, true));

		list.add(new AmountRequirement(OPENINGBALANCE,
				"Please Enter Opening balece", "Opening balence", true, true));

		list.add(new BooleanRequirement(ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return "This account is Active";
			}

			@Override
			protected String getFalseString() {
				return "This account is InActive";
			}
		});

		list.add(new DateRequirement(ASOF, "Please Enter As Of Date",
				"As Of Date", true, true));

		list.add(new StringRequirement(COMMENTS, "Please Enter Comment",
				"Comment", true, true));

		list.add(new BooleanRequirement(CONSIDER_AS_CASH_ACCOUNT, true) {

			@Override
			protected String getTrueString() {
				return "This account is cash account";
			}

			@Override
			protected String getFalseString() {
				return "This account is not a cash account";
			}
		});

	}

	protected List<String> getAccountTypes() {
		List<String> list = new ArrayList<String>();
		list.add("Cash");
		list.add("Bank");
		list.add("Account Receivable");
		list.add("Other Current Asset");
		list.add("Inventory Asset");
		list.add("Fixed Assets");
		list.add("OtherAssets");
		list.add("Account Payable");
		list.add("Other Current Liability");
		list.add("CreditCard");
		list.add("Payroll Liability");
		list.add("Long Term Liability");
		list.add("Equity");
		list.add("Income");
		list.add("CostOfGoodSold");
		list.add("Expense");
		list.add("OtherIncome");
		list.add("OtherExpense");
		list.add("Liability");
		list.add("Asset");

		return list;
	}

	@Override
	protected String getDetailsMessage() {
		if (account.getID() == 0) {
			return "Account is ready to created with following details.";
		} else {
			return "Account is ready to updated with following details.";
		}
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(ACCOUNT_NUMBER).setDefaultValue("1");
		get(ACCOUNT_TYPE).setDefaultValue("Income");
		get(ACTIVE).setDefaultValue(Boolean.TRUE);
		get(CONSIDER_AS_CASH_ACCOUNT).setDefaultValue(Boolean.TRUE);
	}

	@Override
	public String getSuccessMessage() {
		if (account.getID() == 0) {
			return "Account is created succesfully.";
		} else {
			return "Account is updated successfully.";
		}
	}

	@Override
	public Result onCompleteProcess(Context context) {
		String accType = get(ACCOUNT_TYPE).getValue();
		String accname = get(ACCOUNT_NAME).getValue();
		String accountNum = get(ACCOUNT_NUMBER).getValue();
		double openingBal = get(OPENINGBALANCE).getValue();
		boolean isActive = get(ACTIVE).getValue();
		boolean isCashAcount = get(CONSIDER_AS_CASH_ACCOUNT).getValue();
		ClientFinanceDate asOf = get(ASOF).getValue();
		String comment = get(COMMENTS).getValue();

		account.setDefault(true);
		account.setType(getAccountTypes().indexOf(accType) + 1);
		account.setName(accname);
		account.setNumber(accountNum);
		account.setOpeningBalance(openingBal);
		account.setIsActive(isActive);
		account.setAsOf(asOf.getDate());
		account.setConsiderAsCashAccount(isCashAcount);
		account.setComment(comment);
		create(account, context);
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string.isEmpty()) {
			if (!isUpdate) {
				account = new ClientAccount();
				return null;
			}
		}

		account = CommandUtils.getAccountByName(context.getCompany(), string);
		if (account == null) {
			account = CommandUtils.getAccountByNumber(context.getCompany(),
					getNumberFromString(string));
		}
		if (account == null) {
			addFirstMessage(context, "Select an account to update.");
			return "Accounts " + string.trim();
		}

		get(ACCOUNT_TYPE)
				.setValue(getAccountTypes().get(account.getType() - 1));

		get(ACCOUNT_NAME).setValue(account.getName());
		get(ACCOUNT_NAME).setEditable(false);

		get(ACCOUNT_NUMBER).setValue(account.getNumber());
		get(ACCOUNT_NUMBER).setEditable(false);

		get(OPENINGBALANCE).setValue(account.getOpeningBalance());
		get(ACTIVE).setValue(account.getIsActive());
		get(CONSIDER_AS_CASH_ACCOUNT).setValue(
				account.isConsiderAsCashAccount());
		get(ASOF).setValue(new ClientFinanceDate(account.getAsOf()));
		get(COMMENTS).setValue(account.getComment());
		return null;
	}
}
