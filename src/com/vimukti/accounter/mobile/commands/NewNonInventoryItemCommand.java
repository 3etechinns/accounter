package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;

public class NewNonInventoryItemCommand extends AbstractItemCreateCommand {

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);
		list.add(new NumberRequirement(WEIGHT, getMessages().pleaseEnter(
				getConstants().weight()), getConstants().weight(), true, true));
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Creating Non Inventory Item";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

}
