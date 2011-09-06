package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Honduras extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Atl�ntida", "Choluteca", "Col�n",
				"Comayagua", "Cop�n", "Cort�s", "Distrito Central",
				"El Para�so", "Francisco Moraz�n", "Gracias a Dios",
				"Intibuc�", "Islas de la Bah�a", "La Paz", "Lempira",
				"Ocotepeque", "Olancho", "Santa B�rbara", "Valle", "Yoro" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "HNL";
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
