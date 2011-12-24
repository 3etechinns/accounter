package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class AccountRegisterCommand extends AbstractCommand {

	private static final String SHOWTRANSACTION_TYPE = "ShowTransactions Type";
	private static final String ACCOUNT = "account";
	private ClientFinanceDate startDate, endDate;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		Account account = CommandUtils.getaccount(context.getCompany(), string);
		get(ACCOUNT).setValue(account);
		context.setString("");
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
		get(SHOWTRANSACTION_TYPE).setDefaultValue(getMessages().all());
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new AccountRequirement(ACCOUNT, getMessages().pleaseSelect(
				getMessages().account()), getMessages().account(), false, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().account());
			}

			@Override
			protected List<Account> getLists(Context context) {
				return new ArrayList<Account>(context.getCompany()
						.getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().account());
			}
		});

		list.add(new CommandsRequirement(SHOWTRANSACTION_TYPE) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().all());
				list.add(getMessages().today());
				list.add(getMessages().last30Days());
				list.add(getMessages().last45Days());
				return list;
			}
		});

		list.add(new ShowListRequirement<AccountRegister>(getMessages()
				.accountRegister(), getMessages().pleaseEnter(
				getMessages().name()), 20) {

			@Override
			protected String onSelection(AccountRegister value) {
				return "editTransaction " + value.getTransactionId();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(AccountRegister accRegister) {
				Record record = new Record(accRegister);
				record.add(getMessages().date(), accRegister.getDate());
				record.add(getMessages().transactionType(),
						Utility.getTransactionName(accRegister.getType()));
				record.add(getMessages().docNo(), accRegister.getNumber());
				record.add(getMessages().increase(), accRegister.getPayTo());
				record.add(getMessages().decrease(), accRegister.getAmount());
				record.add(getMessages().account(), accRegister.getAccount());
				record.add(getMessages().memo(), accRegister.getMemo());
				record.add(getMessages().balance(), accRegister.getBalance());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
			}

			@Override
			protected boolean filter(AccountRegister e, String name) {
				return false;
			}

			@Override
			protected List<AccountRegister> getLists(Context context) {
				dateRangeChanged(context);
				return getAccountRegister();

			}

			@Override
			protected String getShowMessage() {
				return null;
			}
		});
	}

	/**
	 * getting the account register.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param accountId
	 * @return
	 */
	public ArrayList<AccountRegister> getAccountRegister() {
		ArrayList<AccountRegister> accountRegisterList = new ArrayList<AccountRegister>();
		Account account = get(ACCOUNT).getValue();
		FinanceDate[] financeDates = CommandUtils.getMinimumAndMaximumDates(
				startDate, endDate, getCompanyId());
		try {
			accountRegisterList = new FinanceTool().getAccountRegister(
					financeDates[0], financeDates[1], account.getID(),
					getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountRegisterList;
	}

	/**
	 * changed show transaction type
	 */
	private void dateRangeChanged(Context context) {
		ClientFinanceDate todaydate = new ClientFinanceDate();
		String selectedOption = get(SHOWTRANSACTION_TYPE).getValue();
		if (selectedOption.equals(getMessages().all())) {
			startDate = CommandUtils.getCurrentFiscalYearStartDate(context
					.getPreferences());
			endDate = new ClientFinanceDate();
		} else if (selectedOption.equals(getMessages().today())) {
			startDate = todaydate;
			endDate = todaydate;
		} else if (selectedOption.equals(getMessages().last30Days())) {
			startDate = new ClientFinanceDate(todaydate.getYear(),
					todaydate.getMonth() - 1, todaydate.getDay());
			endDate = todaydate;
		} else if (selectedOption.equals(getMessages().last45Days())) {
			startDate = new ClientFinanceDate(todaydate.getYear(),
					todaydate.getMonth() - 2, todaydate.getDay() + 16);
			endDate = todaydate;
		}
	}
}
