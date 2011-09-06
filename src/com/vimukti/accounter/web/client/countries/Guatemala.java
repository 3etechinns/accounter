package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Guatemala extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Alta Verapaz", "Baja Verapaz",
				"Chimaltenango", "Chiquimula", "El Progreso", "Escuintla",
				"Guatemala", "Huehuetenango", "Izabal", "Jalapa", "Jutiapa",
				"Pet�n", "Quezaltenango", "Quich�", "Retalhuleu",
				"Sacatep�quez", "San Marcos", "Santa Rosa", "Solol�",
				"Suchitep�quez", "Totonicap�n", "Zacapa" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "GTQ";
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
