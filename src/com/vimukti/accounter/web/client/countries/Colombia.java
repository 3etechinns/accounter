package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Colombia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Amazonas", "Antioquia", "Arauca",
				"Atl�ntico", "Bogot�", "Bol�var", "Boyac�", "Caldas",
				"Caquet�", "Casanare", "Cauca", "C�sar", "Choc�", "C�rdoba",
				"Cundinamarca", "Guain�a", "Guaviare", "Huila", "La Guajira",
				"Magdalena", "Meta", "Nari�o", "Norte de Santander",
				"Putumayo", "Quindi�", "Risaralda", "San Andr�s y Providencia",
				"Santander", "Sucre", "Tolima", "Valle del Cauca", "Vaup�s",
				"Vichada" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "COP";
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
