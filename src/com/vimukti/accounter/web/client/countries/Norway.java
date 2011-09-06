package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Norway extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Akershus", "Aust-Agder", "Buskerud",
				"Finnmark", "Hedmark", "Hordaland", "M�re og Romsdal",
				"Nordland", "Nord-Tr�ndelag", "Oppland", "Oslo", "�stfold",
				"Rogaland", "Sogn og Fjordane", "S�r-Tr�ndelag", "Telemark",
				"Troms", "Vest-Agder", "Vestfold" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {

		return "NOK";
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
