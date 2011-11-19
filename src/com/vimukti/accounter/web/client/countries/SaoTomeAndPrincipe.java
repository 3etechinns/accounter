package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class SaoTomeAndPrincipe extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "�gua Grande", "Cantagalo", "Cau�", "Lemba",
				"Lobata", "M�-Zochi", "Pagu�" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "STD";
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
