package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class SouthGeorgiaAndSandwich extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPreferredCurrency() {
		return "GBP";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.constants().january();
	}
}
