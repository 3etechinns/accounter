package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Panama extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bocas del Toro", "Chiriqu�", "Cocl�",
				"Col�n", "Dari�n", "Ember�", "Herrera", "Kuna de Madungand�",
				"Kuna de Wargand�", "Kuna Yala", "Los Santos", "Ng�be Bugl�",
				"Panam�", "Veraguas" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "PAB";
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
