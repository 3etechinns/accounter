package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Hungary extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "B�cs-Kiskun", "Baranya", "B�k�s",
				"Borsod-Aba�j-Zempl�n", "Budapest", "Csongr�d", "Fej�r",
				"Gyor-Moson-Sopron", "Hajd�-Bihar", "Heves",
				"J�sz-Nagykun-Szolnok", "Kom�rom-Esztergom", "N�gr�d", "Pest",
				"Somogy", "Szabolcs-Szatm�r-Bereg", "Tolna", "Vas", "Veszpr�m",
				"Zala" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "HUF";
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
