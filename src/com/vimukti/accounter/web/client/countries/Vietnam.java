package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Vietnam extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "B?c Trung B?",
				"�?ng b?ng s�ng C?u Long", "�?ng b?ng s�ng H?ng",
				"��ng B?c B?", "��ng Nam B?", "Duy�n h?i Nam Trung B?",
				"T�y B?c B?", "T�y Nguy�n" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "VND";
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
