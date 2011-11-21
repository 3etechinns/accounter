package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class ActionRequirement extends AbstractRequirement<String> {

	private ChangeListner<String> listner;

	public ActionRequirement(String requirementName,
			ChangeListner<String> listner) {
		super(requirementName, null, null, true, false);
		this.listner = listner;
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		String value = context.getSelection(getName());
		if (value != null) {
			setValue(value);
			if (listner != null) {
				listner.onSelection(value);
			}
			context.setString("");
		}
		ResultList resultList = new ResultList(getName());
		List<String> actionNames = getList();
		for (String n : actionNames) {
			Record record = new Record(n);
			record.add("", n);
			resultList.add(record);
		}
		makeResult.add(resultList);
		return null;
	}

	protected abstract List<String> getList();

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_NONE);
	}
}
