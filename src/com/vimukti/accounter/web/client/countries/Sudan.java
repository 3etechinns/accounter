package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Sudan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "A'ali-an-Nil", "al-Bah?r-al-Ah?mar",
				"al-Buh?ayrat", "al-Jazirah", "al-Qad?arif", "al-Wah?dah",
				"an-Nil-al-Abyad?", "an-Nil-al-Azraq", "a�-�amaliyah",
				"Bah?r-al-Jabal", "Garb-al-Istiwa'iyah", "Garb Bah?r-al-Gazal",
				"Garb Darfur", "Garb Kurdufan", "Janub Darfur",
				"Janub Kurdufan", "Junqali", "Kassala", "Khartum",
				"Nahr-an-Nil", "�amal Bah?r-al-Gazal", "�amal Darfur",
				"�amal Kurdufan", "�arq-al-Istiwa'iyah", "Sinnar", "Warab" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SDG";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.constants().january();
	}
}
