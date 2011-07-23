package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.user.client.ui.ListBox;
import com.vimukti.accounter.web.client.ui.Accounter;

public class RequiredFieldValidator implements Validator {

	@Override
	public boolean validate(FormItem formItem) {
		if (formItem.getMainWidget() instanceof ListBox) {
			if (formItem.getValue().toString().equalsIgnoreCase(
					Accounter.getAccounterComboConstants()
							.emptyValue()))

				return false;
		}
		if (formItem.getValue() != null) {
			if (formItem.getValue().toString().trim().isEmpty()) {
				return false;
			} else {
				return true;
			}
		} else
			return false;

	}

}
