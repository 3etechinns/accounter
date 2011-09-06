package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Finland extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Ahvenanmaa", "Etel�-Karjala",
				"Etel�-Pohjanmaa", "Etel�-Savo", "It�-Uusimaa", "Kainuu",
				"Kanta-H�me", "Keski-Pohjanmaa", "Keski-Suomi", "Kymenlaakso",
				"Lappland", "P�ij�t-H�me", "Pirkanmaa", "Pohjanmaa",
				"Pohjois-Karjala", "Pohjois-Pohjanmaa", "Pohjois-Savo",
				"Satakunta", "Uusimaa", "Varsinais-Suomi" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "EUR";
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
