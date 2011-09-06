package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Egypt extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "ad-Daqahliyah", "al-Bah?r-al-Ah?mar",
				"al-Buh?ayrah", "Alexandria", "al-Fayyum", "al-Garbiyah",
				"al-Ismailiyah", "al-Minufiyah", "al-Minya", "al-Qalyubiyah",
				"al-Wadi al-Jadid", "a�-�arqiyah", "Assiut", "Assuan",
				"as-Suways", "Bani Suwayf", "Bur Sa'id", "Dumyat", "Giseh",
				"Kafr-a�-�ayh_", "Kairo", "Luxor", "Matruh", "Qina",
				"�amal Sina", "Sawhaj", "South Sinai" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "EGP";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.constants().july();
	}

}
