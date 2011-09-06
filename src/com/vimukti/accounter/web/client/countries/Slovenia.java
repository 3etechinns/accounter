package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Slovenia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Gorenjska", "Gori�ka",
				"Jugov�odna Slovenija", "Koro�ka", "Notranjsko-kra�ka",
				"Obalno-kra�ka", "Osrednjeslovenska", "Podravska", "Pomurska",
				"Savinjska", "Spodnjeposavska", "Zasavska" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "EUR";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().january();
	}

}
