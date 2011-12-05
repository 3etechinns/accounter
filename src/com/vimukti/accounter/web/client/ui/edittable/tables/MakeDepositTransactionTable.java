package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.AccountDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;
import com.vimukti.accounter.web.client.ui.edittable.CustomerDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.VendorDropDownTable;

public abstract class MakeDepositTransactionTable extends
		EditTable<ClientTransactionMakeDeposit> {

	List<Integer> selectedValues = new ArrayList<Integer>();
	private Double totallinetotal = 0.0;
	private ICurrencyProvider currencyProvider;

	public MakeDepositTransactionTable(ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
	}

	protected void initColumns() {
		this.addColumn(new TextEditColumn<ClientTransactionMakeDeposit>() {

			@Override
			protected String getValue(ClientTransactionMakeDeposit row) {
				switch (row.getType()) {
				case ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:
					return messages.transfer();
				case ClientTransactionMakeDeposit.TYPE_VENDOR:
					return Global.get().vendor();
				case ClientTransactionMakeDeposit.TYPE_CUSTOMER:
					return Global.get().customer();
				default:
					return "";
				}
			}

			@Override
			protected void setValue(ClientTransactionMakeDeposit row,
					String value) {
				// No Need
			}

			@Override
			public int getWidth() {
				return -1;
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return messages.receivedFrom();
			}
		});

		final AccountDropDownTable accountDropDownTable = new AccountDropDownTable(
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						if (Arrays.asList(ClientAccount.TYPE_BANK,
								ClientAccount.TYPE_OTHER_CURRENT_ASSET,
								ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
								ClientAccount.TYPE_EQUITY)
								.contains(e.getType())) {
							return true;
						}
						return false;
					}
				}, Arrays.asList(ClientAccount.TYPE_OTHER_CURRENT_ASSET,
						ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
						ClientAccount.TYPE_BANK, ClientAccount.TYPE_EQUITY));

		final CustomerDropDownTable customerDropDownTable = new CustomerDropDownTable(
				new ListFilter<ClientCustomer>() {

					@Override
					public boolean filter(ClientCustomer e) {
						return true;
					}
				});

		final VendorDropDownTable vendorDropDownTable = new VendorDropDownTable(
				new ListFilter<ClientVendor>() {

					@Override
					public boolean filter(ClientVendor e) {
						return true;
					}
				});

		this.addColumn(new ComboColumn<ClientTransactionMakeDeposit, IAccounterCore>() {

			@Override
			protected IAccounterCore getValue(ClientTransactionMakeDeposit obj) {
				switch (obj.getType()) {
				case ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:
					return Accounter.getCompany().getAccount(obj.getAccount());
				case ClientTransactionMakeDeposit.TYPE_VENDOR:
					return Accounter.getCompany().getVendor(obj.getVendor());
				case ClientTransactionMakeDeposit.TYPE_CUSTOMER:
					return Accounter.getCompany()
							.getCustomer(obj.getCustomer());
				}
				return null;
			}

			@Override
			protected void setValue(ClientTransactionMakeDeposit row,
					IAccounterCore newValue) {
				switch (row.getType()) {
				case ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:
					row.setAccount(newValue.getID());
					break;
				case ClientTransactionMakeDeposit.TYPE_VENDOR:
					row.setVendor(newValue.getID());
					break;
				case ClientTransactionMakeDeposit.TYPE_CUSTOMER:
					row.setCustomer(newValue.getID());
					break;
				}
			}

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public AbstractDropDownTable getDisplayTable(
					ClientTransactionMakeDeposit row) {
				switch (row.getType()) {
				case ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:
					return accountDropDownTable;
				case ClientTransactionMakeDeposit.TYPE_VENDOR:
					return vendorDropDownTable;
				case ClientTransactionMakeDeposit.TYPE_CUSTOMER:
					return customerDropDownTable;
				}
				return null;
			}

			@Override
			protected String getColumnName() {
				return messages.accountFrom();
			}

			@Override
			public int getWidth() {
				return 180;
			}
		});

		this.addColumn(new TextEditColumn<ClientTransactionMakeDeposit>() {

			@Override
			protected String getValue(ClientTransactionMakeDeposit row) {
				return row.getReference() != null ? row.getReference() : "";
			}

			@Override
			protected void setValue(ClientTransactionMakeDeposit row,
					String value) {
				row.setReference(value.toString() != null
						|| value.toString().length() != 0 ? value.toString()
						: "");

			}

			@Override
			protected String getColumnName() {
				return messages.reference();
			}
		});

		this.addColumn(new AmountColumn<ClientTransactionMakeDeposit>(
				currencyProvider, true) {

			@Override
			protected String getColumnName() {
				return messages.amount();
			}

			@Override
			protected Double getAmount(ClientTransactionMakeDeposit row) {
				return row.getAmount();
			}

			@Override
			protected void setAmount(ClientTransactionMakeDeposit row,
					Double lineTotal) {
				if (!AccounterValidator.isPositiveAmount(lineTotal)) {
					lineTotal = 0.0D;
					row.setAmount(lineTotal);
				} else {
					row.setAmount(lineTotal);
				}
				update(row);
			}
		});

		this.addColumn(new DeleteColumn<ClientTransactionMakeDeposit>());
	}

	public void removeAllRecords() {
		clear();
	}

	public void setRecords(List<ClientTransactionMakeDeposit> result) {
		setAllRows(result);
	}

	public List<ClientTransactionMakeDeposit> getRecords() {
		return getAllRows();
	}

	public Double getTotal() {
		return totallinetotal != null ? totallinetotal.doubleValue() : 0.0d;
	}

	public ValidationResult validateGrid(long depositInId) {
		ValidationResult result = new ValidationResult();

		for (ClientTransactionMakeDeposit record : getRecords()) {
			if (((record.getType() == ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT && record
					.getAccount() == 0)
					|| (record.getType() == ClientTransactionMakeDeposit.TYPE_VENDOR && record
							.getVendor() == 0) || (record.getType() == ClientTransactionMakeDeposit.TYPE_CUSTOMER && record
					.getCustomer() == 0))) {
				result.addError(
						this,
						messages.pleaseselectvalidtransactionGrid(getTypeAsString(record
								.getType())));
			}

			if (record.getAccount() == depositInId) {
				result.addError(this,
						messages.depositAndAccountFromShouldNotBeSame());
			}
		}
		if (DecimalUtil.isLessThan(totallinetotal, 0.0)) {
			// FIXME
			result.addError("GridTotalLineTotal",
					messages.invalidTransactionAmount());
		}
		return result;
	}

	private String getTypeAsString(int type) {
		switch (type) {
		case ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:
			return messages.Account();
		case ClientTransactionMakeDeposit.TYPE_VENDOR:
			return Global.get().Vendor();
		case ClientTransactionMakeDeposit.TYPE_CUSTOMER:
			return Global.get().Customer();
		default:
			return messages.type();
		}
	}

	public void updateTotals() {
		List<ClientTransactionMakeDeposit> records = getRecords();
		totallinetotal = 0.0;
		for (ClientTransactionMakeDeposit rec : records)
			totallinetotal += rec.getAmount();
		updateNonEditableItems();
	}

	@Override
	public void update(ClientTransactionMakeDeposit row) {
		super.update(row);
		updateTotals();
	}

	protected abstract void updateNonEditableItems();

	public void addLoadingImagePanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(ClientTransactionMakeDeposit row) {
		super.delete(row);
		updateTotals();
	}
}
