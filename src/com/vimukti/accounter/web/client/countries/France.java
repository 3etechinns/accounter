package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class France extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Alsace", "Aquitaine", "Auvergne",
				"Basse-Normandie", "Bourgogne", "Bretagne", "Centre",
				"Champagne-Ardenne", "Corse", "Franche-Comt�",
				"Haute-Normandie", "�le-de-France", "Languedoc-Roussillon",
				"Limousin", "Lorraine", "Midi-Pyr�n�es", "Nord-Pas-de-Calais",
				"Pays-de-la-Loire", "Picardie", "Poitou-Charentes",
				"Provence-Alpes-C�te-d'Azur", "Rh�ne-Alpes" };
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
