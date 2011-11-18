package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Chile extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Aisen", "Antofagasta", "Araucania",
				"Atacama", "Bio Bio", "Coquimbo",
				"Libertador General Bernardo O'Higgins", "Los Lagos",
				"Magellanes", "Maule", "Metropolitana", "Tarapaca",
				"Valparaiso" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "CLP";
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
