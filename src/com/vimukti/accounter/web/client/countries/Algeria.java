package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.client.util.OrganizationType;

public class Algeria implements ICountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Adrar", "al-Agwat", "al-Bayad",
				"al-Buirah", "Algier", "al-Jilfah", "al-Masilah", "al-Midyah",
				"al-Wad", "an-Na'amah", "Annabah", "a�-�alif", "at-Tarif",
				"'Ayn ad-Dafla", "'Ayn Timu�anat", "Ba��ar", "Batnah",
				"Bijayah", "Biskrah", "Blidah", "Bumardas", "Burj Bu Arririj",
				"Galizan", "Gardayah", "H_an�alah", "Ilizi", "Jijili", "Milah",
				"Mu'askar", "Mustaganam", "Qalmah", "Qusantinah", "Sa'idah",
				"Sakikdah", "Satif", "Sidi bal'abbas", "Suq Ahras",
				"Tamanrasat", "Tibazah", "Tibissah", "Tilimsan", "Tinduf",
				"Tisamsilt", "Tiyarat", "Tizi Wazu", "Umm-al-Bawagi", "Wahran",
				"Warqla" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultTimeZone(String state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganizationType[] getOrganizationTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		// TODO Auto-generated method stub
		return null;
	}

}
