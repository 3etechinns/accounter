package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class DominicanRepublic extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Azua", "Baoruco", "Barahona",
				"Dajab�n", "Duarte", "El�as Pi�a", "El Seibo", "Espaillat",
				"Hato Mayor", "Independencia", "La Altagracia", "La Romana",
				"La Vega", "Mar�a Trinidad S�nchez", "Monse�or Nouel",
				"Monte Cristi", "Monte Plata", "Pedernales", "Peravia",
				"Puerto Plata", "Salcedo", "Saman�", "S�nchez Ram�rez",
				"San Crist�bal", "San Jos� de Ocoa", "San Juan",
				"San Pedro de Macor�s", "Santiago", "Santiago Rodr�guez",
				"Santo Domingo", "Valverde" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "DOP";
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
