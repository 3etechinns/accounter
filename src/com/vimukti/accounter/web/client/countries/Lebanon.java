package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Lebanon extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "al-Biqa'a", "al-Janub",
				"an-Nabatiyah", "as-Samal", "Jabal Lubnan" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "LBP";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.messages().january();
	}

}
