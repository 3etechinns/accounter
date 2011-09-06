package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Venezuela extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Amazonas", "Anzo�tegui", "Apure",
				"Aragua", "Barinas", "Bol�var", "Carabobo", "Cojedes",
				"Delta Amacuro", "Distrito Capital", "Falc�n", "Gu�rico",
				"Lara", "M�rida", "Miranda", "Monagas", "Nueva Esparta",
				"Portuguesa", "Sucre", "T�chira", "Trujillo", "Vargas",
				"Yaracuy", "Zulia" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "VEB";
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
