package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class SaudiArabia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "al-Bah?ah", "al-H?udud-a�-�amaliyah",
				"al-Jawf", "al-Madinah", "al-Qasim", "'Asir", "a�-�arqiyah",
				"H?a'il", "Jizan", "Makkah", "Najran", "Riad", "Tabuk" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SAR";
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
