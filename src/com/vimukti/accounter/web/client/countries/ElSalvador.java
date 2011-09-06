package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class ElSalvador extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Ahuachap�n", "Caba�as",
				"Chalatenango", "Cuscatl�n", "La Libertad", "La Paz",
				"La Uni�n", "Moraz�n", "San Miguel", "San Salvador",
				"Santa Ana", "San Vicente", "Sonsonate", "Usulut�n" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SVC";
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
