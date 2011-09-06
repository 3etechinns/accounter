package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Nicaragua extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Atl�ntico Norte", "Atl�ntico Sur",
				"Boaco", "Carazo", "Chinandega", "Chontales", "Estel�",
				"Granada", "Jinotega", "Le�n", "Madriz", "Managua", "Masaya",
				"Matagalpa", "Nueva Segovia", "R�o San Juan", "Rivas" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "Nicaragua";
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
