package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;

public class AmountRequirement extends SingleRequirement<Double> {

	public AmountRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, displayString, recordName, isOptional2,
				isAllowFromContext2);
		setDefaultValue(0.0d);
	}

	@Override
	public boolean isDone() {
		Double value = getValue();
		if (isOptional()) {
			return true;
		}
		return value != 0;
	}

	@Override
	protected String getDisplayValue(Double value) {
		return value != null ? value.toString() : "";
	}

	@Override
	protected Double getInputFromContext(Context context) {
		return context.getDouble();
	}

}
