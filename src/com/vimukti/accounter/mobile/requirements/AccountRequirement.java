package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;

public abstract class AccountRequirement extends ListRequirement<ClientAccount> {

	public AccountRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientAccount> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ClientAccount value) {
		Record record = new Record(value);
		record.add("", value.getName());
		record.add("", value.getNumber());
		record.add("", value.getCurrentBalance());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientAccount value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(Global.get().Account()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(Global.get().Account());
	}

	@Override
	protected boolean filter(ClientAccount e, String name) {
		return e.getDisplayName().toLowerCase().startsWith(name.toLowerCase());
	}
}
