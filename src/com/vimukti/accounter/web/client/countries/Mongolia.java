package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Mongolia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Arhangaj", "Bajanhongor", "Bajan-�lgij", "Bulgan",
				"Darhan-Uul", "Dornod", "Dornogovi", "Dundgovi", "Govi-Altaj",
				"Govisumber", "H�ntij", "Hovd", "H�vsg�l", "�mn�govi", "Orhon",
				"�v�rhangaj", "S�l�ng�", "S�hbaatar", "T�v", "Ulaanbaatar",
				"Uvs", "Zavhan" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MNT";
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
