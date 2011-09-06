package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Ecuador extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Azuay", "Bol�var", "Ca�ar", "Carchi",
				"Chimborazo", "Cotopaxi", "El Oro", "Esmeraldas", "Gal�pagos",
				"Guayas", "Imbabura", "Loja", "Los R�os", "Manab�",
				"Morona Santiago", "Napo", "Orellana", "Pastaza", "Pichincha",
				"Sucumb�os", "Tungurahua", "Zamora Chinchipe" };

		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ECS";
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
