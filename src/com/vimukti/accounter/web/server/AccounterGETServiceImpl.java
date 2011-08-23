package com.vimukti.accounter.web.server;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.HelpLink;
import com.vimukti.accounter.web.client.core.HrEmployee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.KeyFinancialIndicators;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Fernandez
 * 
 */

public class AccounterGETServiceImpl extends AccounterRPCBaseServiceImpl
		implements IAccounterGETService {
	private static List<String> countriesList, statesList /* ,citiesList */;
	private String[] countries, currencyCodes, countryNames, currencyNames;
	private String[][] states;
	private List<ClientCurrency> currencies;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccounterGETServiceImpl() {
		super();

	}

	@Override
	public <T extends IAccounterCore> T getObjectById(AccounterCoreType type,
			long id) throws AccounterException {

		FinanceTool tool = getFinanceTool();

		try {
			return tool.getObjectById(type, id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public <T extends IAccounterCore> T getObjectByName(AccounterCoreType type,
			String name) throws AccounterException {

		FinanceTool tool = getFinanceTool();

		try {
			return tool.getObjectByName(type, name);
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public <T extends IAccounterCore> ArrayList<T> getObjects(
			AccounterCoreType type) {

		try {

			return getFinanceTool().getObjects(type);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ClientCompany getCompany() throws AccounterException {

		FinanceTool tool = (FinanceTool) getFinanceTool();
		String cid = getCookie(BaseServlet.COMPANY_COOKIE);
		if (cid == null) {
			// Throw Exception
		}
		return tool.getClientCompany(getUserEmail());
	}

	@Override
	public KeyFinancialIndicators getKeyFinancialIndicators() {
		KeyFinancialIndicators keyFinancialIndicators = new KeyFinancialIndicators();
		try {
			keyFinancialIndicators = getFinanceTool()
					.getKeyFinancialIndicators();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return keyFinancialIndicators;
	}

	@Override
	public ArrayList<HrEmployee> getHREmployees() throws AccounterException {
		FinanceTool tool = (FinanceTool) getFinanceTool();
		return tool.getHREmployees();
	}

	public ArrayList<HelpLink> getHelpLinks(int type) {

		HelpLink link = new HelpLink(
				"How to EnterBill transaction?",
				"Click on the Enter Bill action or in the Supplier drop down select Enter Bill.");
		HelpLink link1 = new HelpLink(
				"How to Invoice transaction?",
				"Click on the New Invoice action or in the Customer drop down select New and then New Invoice");
		HelpLink link2 = new HelpLink(
				"How to Quote transaction?",
				"Click on the Quote action or in the Customer drop down select New and then New Quote");
		HelpLink link3 = new HelpLink("How to Paybill transaction?",
				"Click on the PayBill action or in the Supplier drop down select PayBill");
		HelpLink link4 = new HelpLink("How to CustomerPayment transaction?",
				"In the Customer drop down select Customer payment");
		HelpLink link5 = new HelpLink("How to Makedeposit transaction?",
				"In the Banking drop down select Deposit/Transfer Funds");

		List<HelpLink> helpLinks = new ArrayList<HelpLink>();

		helpLinks.add(link);
		helpLinks.add(link1);
		helpLinks.add(link2);
		helpLinks.add(link3);
		helpLinks.add(link4);
		helpLinks.add(link5);

		return new ArrayList<HelpLink>(helpLinks);

	}

	public List<ClientCurrency> getCurrencies() {
		currencies = new ArrayList<ClientCurrency>();
		currencyCodes = new String[] { "AUD", "CAD", "EUR", "GBP", "JPY",
				"NZD", "USD", "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS",
				"AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD",
				"BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYR", "BZD", "CDF",
				"CHF", "CLP", "CNY", "COP", "CRC", "CUC", "CVE", "CZK", "DJF",
				"DKK", "DOP", "DZD", "EEK", "EGP", "ERN", "ETB", "FJD", "FKP",
				"GEL", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL",
				"HRK", "HTG", "HUF", "IDR", "ILS", "IQD", "IRR", "ISK", "JMD",
				"JOD", "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD",
				"KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LTL", "LVL", "LYD",
				"MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO", "MUR",
				"MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK",
				"NPR", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG",
				"QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG",
				"SEK", "SGD", "SHP", "SKK", "SLL", "SOS", "SRD", "STD", "SVC",
				"SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD",
				"TWD", "TZS", "UAH", "UGX", "UYU", "UZS", "VEF", "VND", "VUV",
				"WST", "XAF", "XCD", "XOF", "XPF", "YER", "ZAR", "ZMK", "ZWD" };
		countryNames = new String[] { "Australian", "Canadian", "British",
				"Japanese", "New Zealand", "United States",
				"United Arab Emirates", "Afghan", "Albanian", "Armenian",
				"Netherlands Antillean", "Angolan", "Argentine", "Aruban",
				"Azerbaijani", "Bosnia and Herzegovina Convertible",
				"Barbadian", "Bangladeshi", "Bulgarian", "Bahraini",
				"Burundian", "Bermudian", "Brunei", "Bolivian", "Brazilian",
				"Bahamian", "Bhutanese", "Botswana", "Belarusian", "Belize ",
				"Congolese", "Swiss", "Chilean", "Chinese", "Colombian",
				"Costa Rican", "Cuban Convertible", "Cape Verdean", "Czech",
				"Djiboutian", "Danish", "Dominican", "Algerian", "Estonian",
				"Egyptian", "Eritrean", "Ethiopian", "Fijian",
				"Falkland Island", "Georgian", "Ghanaian", "Gibraltar",
				"Gambian", "Guinean", "Guatemalan", "Guyanese", "Hong Kong",
				"Honduran", "Croatian", "Haitian", "Hungarian", "Indonesian",
				"Israeli", "Iraqi", "Iranian", "Icelandic", "Jamaican",
				"Jordanian", "Kenyan", "Kyrgyzstani", "Cambodian", "Comorian",
				"North Korean", "South Korean", "Kuwaiti", "Cayman Islands",
				"Kazakhstani", "Lao", "Lebanese", "Sri Lankan", "Liberian",
				"Lesotho", "Lithuanian", "Latvian", "Libyan", "Moroccan",
				"Moldovan", "Malagasy", "Macedonian", "Myanmar", "Mongolian",
				"Macanese", "Mauritanian", "Mauritian", "Maldivian",
				"Malawian", "Mexican", "Malaysian", "Mozambican", "Namibian",
				"Nigerian", "Nicaraguan", "Norwegian", "Nepalese", "Omani",
				"Panamanian", "Peruvian Nuevo", "Papua New Guinean",
				"Philippine", "Pakistani", "Polish", "Paraguayan", "Qatari",
				"Romanian", "Serbian", "Russian", "Rwandan", "Saudi",
				"Solomon Islands", "Seychellois", "Sudanese", "Swedish",
				"Singapore", "Saint Helenian", "Slovak", "Sierra Leonean",
				"Somali ", "Surinamese", "S�o Tom� and Pr�ncipe", "Salvadoran",
				"Syrian", "Swazi", "Thai", "Tajikistani", "Turkmenistani",
				"Tunisian", "Tongan", "Turkish", "Trinidad and Tobago",
				"Taiwanese New", "Tanzanian", "Ukrainian", "Ugandan",
				"Uruguayan", "Uzbekistani", "Venezuelan Bol�var", "Vietnamese",
				"Vanuatu", "Samoan", "Central African CFA", "East Caribbean",
				"West African CFA", "CFP", "Yemeni", "South African",
				"Zambian", "Zimbabwean" };
		currencyNames = new String[] { "Dollar", "Dollar", "Euro", "Pound",
				"Yen", "Dollar", "Dollar", "", "Dirham", "Afghani", "Lek",
				"Dram", "Guilder", "Kwanza", "Peso", "Guilder", "Manat",
				"Mark", "Dollar", "Taka", "Lev", "Dinar", "Franc", "Dollar",
				"Dollar", "Boliviano", "Real", "Dollar", "Ngultrum", "Pula",
				"Ruble", "Dollar", "Franc", "Franc", "Peso", "Yuan", "Peso",
				"Col�n", "Peso", "Escudo", "Koruna", "Franc", "Krone", "Peso",
				"Dinar", "Kroon", "Pound", "Nakfa", "Birr", "Dollar", "Pound",
				" Lari", "Cedi", "Pound", "alasi", "Franc", "Quetzal",
				"Dollar", "Dollar", "Lempira", "Kuna", "Gourde", "Forint",
				"Rupiah", "New Sheqel", "Dinar", "Rial", "Kr�na", "Dollar",
				"Dinar", "Shilling", "Som", "Riel", "Franc", "Won", "Won",
				"Dinar", "Dollar", "Tenge", "Pound", "Pound", "Rupee",
				"Dollar", " Loti", "Litas", "Lats", "Dinar", "Dirham", "Leu",
				"Ariary", "Denar", "Kyat", " Tugrik", " Pataca", "Ouguiya",
				"Rupee", "Rufiyaa", "Kwacha", "Peso", "Ringgit", "Metical",
				"Dollar", " Naira", "C�rdoba", "Krone", "Rupee", "Rial",
				"Balboa", "Sol", "Kina", "Peso", "Rupee", "Zloty", "Guaran�",
				"Riyal", "Leu", "Dinar", "Ruble", "Franc", "Riyal", "Dollar",
				"Rupee", "Pound", "Krona", "Dollar", "Pound", "Koruna",
				"Leone", "Shilling", "Dollar", "Dobra", "Col�n", "Pound",
				"Lilangeni", "Baht", "Somoni", "Manat", "Dinar", "Pa?anga",
				"Lira", "Dollar", "Dollar", "Shilling", "Hryvnia", "Shilling",
				"Peso", "Som", "Fuertes", "Dong", "Vatu", "Tala", "Franc",
				"Dollar", "Franc", "Franc", "Rial", "Rand", "Kwacha", "Dollar" };
		for (int i = 0; i < currencyCodes.length; i++) {
			ClientCurrency clientCurrency = new ClientCurrency();
			clientCurrency.setCountryName(countryNames[i]);
			clientCurrency.setFormalName(currencyCodes[i]);
			clientCurrency.setName(currencyNames[i]);
			currencies.add(clientCurrency);
		}
		return currencies;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.IAccounterGETService#getUser(java.lang
	 * .String, java.lang.String, boolean, int)
	 */
	@Override
	public ClientUser getUser(String userName, String password,
			boolean isremeber, int offset) {
		return login(userName, password, isremeber, offset);
	}

	public List<String> getCountries() {
		countries = new String[] { "United Kingdom", "United States",
				"Afghanistan", "Albania", "Algeria", "American Samoa",
				"Andorra", "Angola", "Anguilla", "Antarctica",
				"Antigua and Barbuda", "Argentina", "Armenia", "Aruba",
				"Austria", "Australia", "Azerbaijan", "Bahamas, The",
				"Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
				"Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
				"Bosnia and Herzegovina", "Botswana", "Bouvet Island",
				"Brazil", "British Indian Ocean Territory", "Brunei",
				"Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon",
				"Canada", "Cape Verde", "Cayman Islands",
				"Central African Republic", "Chad", "Chile", "China",
				"Christmas Island", "Cocos (Keeling) Islands", "Colombia",
				"Comoros", "Congo, Democratic Republic of the",
				"Congo, Republic of the", "Cook Islands", "Costa Rica",
				"Cote d'Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic",
				"Denmark", "Djibouti", "Dominica", "Dominican Republic",
				"Ecuador", "Egypt", "El Salvador", "Equatorial Guinea",
				"Eritrea", "Estonia", "Ethiopia",
				"Falkland Islands (Islas Malvinas)", "Faroe Islands", "Fiji",
				"Finland", "France", "French Guiana", "French Polynesia",
				"French Southern and Antarctic Lands", "Gabon", "Gambia, The",
				"Georgia", "Germany", "Ghana", "Gibraltar", "Greece",
				"Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala",
				"Guinea", "Guinea-Bissau", "Guyana", "Haiti",
				"Heard Island and McDonald Islands", "Holy See (Vatican City)",
				"Honduras", "Hong Kong", "Hungary", "Iceland", "India",
				"Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man",
				"Israel", "Italy", "Jamaica", "Jan Mayen", "Japan", "Jordan",
				"Kazakhstan", "Kenya", "Kiribati", "Korea, North",
				"Korea, South", "Kuwait", "Kyrgyzstan", "Laos", "Latvia",
				"Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein",
				"Lithuania", "Luxembourg", "Macao", "Macedonia", "Madagascar",
				"Malawi", "Malaysia", "Maldives", "Mali", "Malta",
				"Marshall Islands", "Martinique", "Mauritania", "Mauritius",
				"Mayotte", "Mexico", "Micronesia, Federated States of",
				"Moldova", "Monaco", "Mongolia", "Montserrat", "Morocco",
				"Mozambique", "Namibia", "Nauru", "Nepal", "Netherlands",
				"Netherlands Antilles", "New Caledonia", "New Zealand",
				"Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island",
				"Northern Mariana Islands", "Norway", "Oman", "Pakistan",
				"Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru",
				"Philippines", "Pitcairn Islands", "Poland", "Portugal",
				"Puerto Rico", "Qatar", "Reunion", "Romania", "Russia",
				"Rwanda", "Saint Helena", "Saint Kitts and Nevis",
				"Saint Lucia", "Saint Pierre and Miquelon",
				"Saint Vincent and the Grenadines", "Samoa", "San Marino",
				"Sao Tome and Principe", "Saudi Arabia", "Senegal",
				"Serbia and Montenegro", "Seychelles", "Sierra Leone",
				"Singapore", "Slovakia", "Slovenia", "Solomon Islands",
				"Somalia", "South Africa",
				"South Georgia and the South Sandwich Islands", "Spain",
				"Sri Lanka", "Sudan", "Suriname", "Svalbard", "Swaziland",
				"Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan",
				"Tanzania", "Thailand", "Timor-Leste", "Togo", "Tokelau",
				"Tonga", "Trinidad and Tobago", "Tunisia", "Turkey",
				"Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda",
				"Ukraine", "United Arab Emirates", "Uruguay", "Uzbekistan",
				"Vanuatu", "Venezuela", "Vietnam", "Virgin Islands",
				"Wallis and Futuna", "Western Sahara", "Yemen", "Zambia",
				"Zimbabwe" };
		countriesList = new ArrayList<String>();
		for (int i = 0; i < countries.length; i++) {
			countriesList.add(countries[i]);
		}
		return countriesList;
	}

	@Override
	public List<String> getStates(String country) {
		states = new String[][] {
				// UNITED KINGDOM
				{ "Buckinghamshire", "Hampshire", "Cambridgeshire", "Hackney",
						"East Riding of Yorkshire", "Worcestershire",
						"Barking and Dagenham", "Moray", "Staffordshire",
						"Devon", "Harrow", "Falkirk", "Rhondda Cynon Taff",
						"East Ayrshire", "Sutton", "Larne", "Bedfordshire",
						"Tameside", "Cleveland", "Lothian Region", "Coventry",
						"Nottinghamshire", "Leicestershire", "Knowsley",
						"Torbay", "Wrexham", "Derbyshire", "Darlington",
						"Renfrewshire", "Hammersmith and Fulham", "Highland",
						"Orkney Islands", "West Yorkshire", "Merton",
						"Hertfordshire", "Merseyside", "Bristol", "Bexley",
						"East Renfrewshire", "Edinburgh", "Thurrock",
						"North Lanarkshire", "Barnet", "Conwy",
						"Bracknell Forest", "North Yorkshire", "Milton Keynes",
						"Rotherham", "London", "Angus", "Scottish Borders",
						"Dumfries and Galloway", "Greater London",
						"Tyne and Wear", "Surrey", "Suffolk", "Leeds",
						"Fermanagh", "Oxfordshire", "Moyle", "West Midlands",
						"East Lothian", "Aberdeenshire", "Calderdale",
						"Newport", "Ceredigion", "Limavady", "Clwyd",
						"Dungannon", "Rutland", "Sheffield", "Wokingham",
						"Slough", "Dyfed", "Cookstown", "Richmond upon Thames",
						"Blackpool", "Doncaster", "North Lincolnshire",
						"Denbighshire", "Bolton", "Caerphilly", "Lewisham",
						"Sunderland", "Armagh", "Stockton-on-Tees",
						"North Tyneside", "Greenwich", "Lincolnshire",
						"Solihull", "Vale of Glamorgan", "Grampian Region",
						"Islington", "Oldham", "Magherafelt", "Waltham Forest",
						"Bury", "South Yorkshire", "Northumberland",
						"Middlesbrough", "Southampton", "Durham", "Enfield",
						"Greater Manchester", "Redcar and Cleveland", "Sefton",
						"StHelens", "West Sussex", "Trafford", "Newham",
						"West Glamorgan", "Kensington and Chelsea", "Dorset",
						"Argyll and Bute", "Hillingdon", "East Dunbartonshire",
						"South Gloucestershire", "Neath Port Talbot", "Fife",
						"Down", "Merthyr Tydfil", "Castlereagh",
						"Wolverhampton", "Liverpool", "Plymouth", "Croydon",
						"Ealing", "Brent", "Nottingham", "Norfolk",
						"Lancashire", "Strabane", "Halton", "Camden",
						"Ballymoney", "Windsor and maidenhead",
						"Isles of Scilly", "Coleraine", "Reading", "Wirral",
						"Derry", "Cumbria", "Carrickfergus", "Southwark",
						"Gateshead", "Manchester", "Wiltshire",
						"South Glamorgan", "Gloucestershire",
						"South Lanarkshire", "Essex", "North Down",
						"Warrington", "East Sussex", "Belfast",
						"Herefordshire", "North Somerset", "Antrim",
						"Banbridge", "Carmarthenshire", "Northamptonshire",
						"Walsall", "Clackmannanshire", "South Ayrshire",
						"North Ayrshire", "Bradford", "Ballymena",
						"Dundee City", "Tayside Region", "Glasgow City",
						"Omagh", "Medway", "Warwickshire", "Haringey",
						"South Tyneside", "Kirklees", "Mid Glamorgan",
						"Flintshire", "Ards", "Lambeth", "Shropshire",
						"Telford and Wrekin", "Hounslow", "Craigavon",
						"Birmingham", "Monmouthshire", "Isle of Wight",
						"Portsmouth", "Somerset", "Shetland Islands", "Poole",
						"Sandwell", "West Dunbartonshire", "Borders Region",
						"Aberdeen City", "Berkshire", "Stirling",
						"Southend-on-Sea", "Bath and North East Somerset",
						"Western Isles", "Inverclyde", "Midlothian",
						"Perth and Kinross", "Leicester", "Swansea",
						"Kingston upon Hull", "Wandsworth", "West Berkshire",
						"Tower Hamlets", "Lisburn", "Bournemouth", "Cardiff",
						"Eilean Siar", "Dudley", "North East Lincolnshire",
						"Wigan", "Gwent", "Strathclyde Region", "West Lothian",
						"Redbridge", "Cheshire", "Newtownabbey",
						"Brighton and Hove", "Wakefield",
						"Newcastle upon Tyne", "Bromley", "Stockport",
						"Torfaen", "Newry and Mourne", "Hartlepool",
						"Pembrokeshire", "Hereford and Worcester",
						"Peterborough", "Stoke-on-Trent", "Powys", "Barnsley",
						"Kent", "Westminster", "Bridgend", "Derby", "Cornwall",
						"Gwynedd", "Blackburn with Darwen", "Humberside",
						"York", "Swindon", "Isle of Anglesey" },
				// US
				{ "Alabama", "Alaska", "Arizona", "Arkansas", "California",
						"Colorado", "Connecticut", "Delaware",
						"District of Columbia", "Florida", "Georgia", "Hawaii",
						"Idaho", "Illinois", "Indiana", "Iowa", "Kansas",
						"Kentucky", "Louisiana", "Maine", "Maryland",
						"Massachusetts", "Michigan", "Minnesota",
						"Mississippi", "Missouri", "Montana", "Nebraska",
						"Nevada", "New Hampshire", "New Jersey", "New Mexico",
						"New York", "North Carolina", "North Dakota", "Ohio",
						"Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
						"South Carolina", "South Dakota", "Tennessee", "Texas",
						"Utah", "Vermont", "Virginia", "Washington",
						"West Virginia", "Wisconsin", "Wyoming"

				},
				// AFGHANISTAN
				{ "Badah_�an", "Badgis", "Baglan", "Balh_", "Bamiyan", "Farah",
						"Faryab", "Gawr", "Gazni", "H_awst", "Herat",
						"Hilmand", "Jawzjan", "Kabul", "Kandahar", "Kapisa",
						"Kunarha", "Kunduz", "Lagman", "Lawgar",
						"Maydan-Wardak", "Nangarhar", "Nimruz", "Nuristan",
						"Paktika", "Paktiya", "Parwan", "Samangan",
						"Sar-e Pul", "Tah_ar", "Uruzgan", "Zabul" },// ALBANIA
				{ "Berat", "Bulqiz�", "Delvin�", "Devoll", "Dibr�", "Durr�s",
						"Elbasan", "Fier", "Gjirokast�r", "Gramsh", "Has",
						"Kavaj�", "Kolonj�", "Kor��", "Kruj�", "Ku�ov�",
						"Kuk�s", "Kurbin", "Lezh�", "Librazhd", "Lushnj�",
						"Mallakast�r", "Malsi e Madhe", "Mat", "Mirdit�",
						"Peqin", "P�rmet", "Pogradec", "Puk�", "Sarand�",
						"Shkod�r", "Skrapar", "Tepelen�", "Tirana", "Tropoj�",
						"Vlor�" },
				// ALGERIA
				{ "Adrar", "al-Agwat", "al-Bayad", "al-Buirah", "Algier",
						"al-Jilfah", "al-Masilah", "al-Midyah", "al-Wad",
						"an-Na'amah", "Annabah", "a�-�alif", "at-Tarif",
						"'Ayn ad-Dafla", "'Ayn Timu�anat", "Ba��ar", "Batnah",
						"Bijayah", "Biskrah", "Blidah", "Bumardas",
						"Burj Bu Arririj", "Galizan", "Gardayah", "H_an�alah",
						"Ilizi", "Jijili", "Milah", "Mu'askar", "Mustaganam",
						"Qalmah", "Qusantinah", "Sa'idah", "Sakikdah", "Satif",
						"Sidi bal'abbas", "Suq Ahras", "Tamanrasat", "Tibazah",
						"Tibissah", "Tilimsan", "Tinduf", "Tisamsilt",
						"Tiyarat", "Tizi Wazu", "Umm-al-Bawagi", "Wahran",
						"Warqla" },
				// AMERICAN SAMOA
				{ "Eastern", "Manu'a", "Swains Island", "Western" },
				// ANDORRA
				{ "Andorra la Vella", "Canillo", "Encamp",
						"Escaldes-Engordany", "La Massana", "Ordino",
						"Sant Juli� de L�ria" }
				// ANGOLA
				,
				{ "Bengo", "Benguela", "Bi�", "Cabinda", "Cuando-Cubango",
						"Cuanza-Norte", "Cuanza-Sul", "Cunene", "Huambo",
						"Hu�la", "Luanda", "Lunda Norte", "Lunda Sul",
						"Malanje", "Moxico", "Namibe", "U�ge", "Zaire" }
				// ANGUILLA
				,
				{ "" }
				// ANTARCTICA
				,
				{}
				// ANTIGUA AND BARBUDA
				,
				{ "Barbuda", "Saint George", "Saint John", "Saint Mary",
						"Saint Paul", "Saint Peter", "Saint Philip" }
				// ARGENTINA
				,
				{ "Buenos Aires", "Catamarca", "Chaco", "Chubut", "C�rdoba",
						"Corrientes", "Distrito Federal", "Entre R�os",
						"Formosa", "Jujuy", "La Pampa", "La Rioja", "Mendoza",
						"Misiones", "Neuqu�n", "R�o Negro", "Salta",
						"San Juan", "San Luis", "Santa Cruz", "Santa F�",
						"Santiago del Estero", "Tierra del Fuego", "Tucum�n" }
				// ARMENIA
				,
				{ "Aragatsotn", "Ararat", "Armavir", "Gegharkunik", "Kotayk",
						"Lori", "Shirak", "Syunik", "Tavush", "Vayots Dzor",
						"Yerevan" }
				// ARUBA
				,
				{ "" }
				// AUSTRIA
				,
				{ "Burgenland", "K�rnten", "Nieder�sterreich",
						"Ober�sterreich", "Salzburg", "Steiermark", "Tirol",
						"Vorarlberg", "Wien" }
				// AUSTRALIA
				,
				{ "Australian Capital Territory", "New South Wales",
						"Northern Territory", "Queensland", "South Australia",
						"Tasmania", "Victoria", "Western Australia" }
				// AZERBAIJAN
				,
				{ "Abseron", "Aran", "Baki", "Dagliq Sirvan", "G?nc?-Qazax",
						"K?lb?c?r-La�in", "L?nk?ran", "Nax�ivan",
						"Quba-Xa�maz", "S?ki-Zaqatala", "Yuxari Qarabag" }
				// BAHAMAS, THE
				,
				{ "Abaco", "Acklins Island", "Andros", "Berry Islands",
						"Biminis", "Cat Island", "Crooked Island", "Eleuthera",
						"Exuma and Cays", "Grand Bahama", "Inagua Islands",
						"Long Island", "Mayaguana", "New Providence",
						"Ragged Island", "Rum Cay", "San Salvador" }
				// BAHRAIN
				,
				{ "al-Garbiyah", "al-H?idd", "al-Muh?arraq", "al-Wusta",
						"ar-Rifa'a", "a�-�amaliyah", "'Isa", "Jidh?afs",
						"Madinat H?amad", "Manama", "Sitrah" }
				// BANGLADESH
				,
				{ "Bagar Hat", "Bandarban", "Barguna", "Barisal", "Bhola",
						"Bogora", "Brahman Bariya", "Chandpur", "Chattagam",
						"Chuadanga", "Dhaka", "Dinajpur", "Faridpur", "Feni",
						"Gaybanda", "Gazipur", "Gopalganj", "Habiganj",
						"Jaipur Hat", "Jamalpur", "Jessor", "Jhalakati",
						"Jhanaydah", "Khagrachhari", "Khulna", "Kishorganj",
						"Koks Bazar", "Komilla", "Kurigram", "Kushtiya",
						"Lakshmipur", "Lalmanir Hat", "Madaripur", "Magura",
						"Maimansingh", "Manikganj", "Maulvi Bazar", "Meherpur",
						"Munshiganj", "Naral", "Narayanganj", "Narsingdi",
						"Nator", "Naugaon", "Nawabganj", "Netrakona",
						"Nilphamari", "Noakhali", "Pabna", "Panchagarh",
						"Patuakhali", "Pirojpur", "Rajbari", "Rajshahi",
						"Rangamati", "Rangpur", "Satkhira", "Shariatpur",
						"Sherpur", "Silhat", "Sirajganj", "Sunamganj",
						"Tangayal", "Thakurgaon" }
				// BARBADOS
				,
				{ "Christ Church", "Saint Andrew", "Saint George",
						"Saint James", "Saint John", "Saint Joseph",
						"Saint Lucy", "Saint Michael", "Saint Peter",
						"Saint Philip", "Saint Thomas" }
				// BELARUS
				,
				{ "Christ Church", "Saint Andrew", "Saint George",
						"Saint James", "Saint John", "Saint Joseph",
						"Saint Lucy", "Saint Michael", "Saint Peter",
						"Saint Philip", "Saint Thomas" }
				// BELGIUM
				,
				{ "Antwerpen", "Brabant Wallon", "Br�ssel", "Fl�misch Brabant",
						"Hennegau", "Limburg", "L�ttich", "Luxemburg", "Namur",
						"Ost-Flandern", "West-Flandern" }
				// BELIZE
				,
				{ "Belize", "Cayo", "Corozal", "Orange Walk", "Stann Creek",
						"Toledo" }
				// BENIN
				,
				{ "Alibori", "Atacora", "Atlantique", "Borgou", "Collines",
						"Couffo", "Donga", "Littoral", "Mono", "Ou�m�",
						"Plateau", "Zou" }
				// BERMUDA
				,
				{ "Hamilton", "Saint George" }
				// BHUTAN
				,
				{ "Bumthang", "Chhukha", "Dagana", "Gasa", "Haa", "Lhuntse",
						"Mongar", "Paro", "Pemagatshel", "Punakha",
						"Samdrup Jongkhar", "Samtse", "Sarpang", "Thimphu",
						"Trashigang", "Trashiyangtse", "Trongsa", "Tsirang",
						"Wangdue Phodrang", "Zhemgang" }
				// BOLIVIA
				,
				{ "Tarija" }
				// BOSNIA AND HERZEGOVINA
				,
				{ "Distrikt Brcko", "Federacija Bosna i Hercegovina",
						"Republika Srpska" }
				// BOTSWANA
				,
				{ "Bobonong", "Boteti", "Chobe", "Francistown", "Gaborone",
						"Ghanzi", "Jwaneng", "Kgalagadi North",
						"Kgalagadi South", "Kgatleng", "Kweneng", "Lobatse",
						"Mahalapye", "Ngamiland", "Ngwaketse", "North East",
						"Okavango", "Orapa", "Selibe Phikwe", "Serowe-Palapye",
						"South East", "Sowa", "Tutume" }
				// BOUVET ISLAND
				,
				{}
				// BRAZIL
				,
				{ "Acre", "Alagoas", "Amap�", "Amazonas", "Bahia", "Cear�",
						"Distrito Federal", "Esp�rito Santo", "Goi�s",
						"Maranh�o", "Mato Grosso", "Mato Grosso do Sul",
						"Minas Gerais", "Par�", "Para�ba", "Paran�",
						"Pernambuco", "Piau�", "Rio de Janeiro",
						"Rio Grande do Norte", "Rio Grande do Sul", "Rond�nia",
						"Roraima", "Santa Catarina", "S�o Paulo", "Sergipe",
						"Tocantins" }
				// BRITISH INDIAN OCEAN TERRITORY
				,
				{ "Tortola" }
				// BRUNEI
				,
				{ "Belait", "Brunei-Muara", "Temburong", "Tutong" }
				// BULGARIA
				,
				{ "Blagoevgrad", "Burgas", "Dobric", "Gabrovo", "Haskovo",
						"Jambol", "Kard�ali", "Kjustendil", "Lovec", "Montana",
						"Pazard�ik", "Pernik", "Pleven", "Plovdiv", "Razgrad",
						"Ruse", "Silistra", "Sliven", "Smoljan", "Sofija grad",
						"Sofijska oblast", "Stara Zagora", "�umen",
						"Targovi�te", "Varna", "Veliko Tarnovo", "Vidin",
						"Vraca" }
				// BURKINA FASO
				,
				{ "Bal�", "Bam", "Banwa", "Baz�ga", "Bougouriba", "Boulgou",
						"Boulkiemd�", "Como�", "Ganzourgou", "Gnagna",
						"Gourma", "Houet", "Ioba", "Kadiogo", "K�n�dougou",
						"Komandjoari", "Kompienga", "Kossi", "Koulp�logo",
						"Kouritenga", "Kourw�ogo", "L�raba", "Loroum",
						"Mouhoun", "Nahouri", "Namentenga", "Nayala",
						"Noumbiel", "Oubritenga", "Oudalan", "Passor�", "Poni",
						"Sangui�", "Sanmatenga", "S�no", "Sissili", "Soum",
						"Sourou", "Tapoa", "Tuy", "Yagha", "Yatenga", "Ziro",
						"Zondoma", "Zoundw�ogo" }
				// BURUNDI
				,
				{ "Bubanza", "Bujumbura", "Bururi", "Cankuzo", "Cibitoke",
						"Gitega", "Karuzi", "Kayanza", "Kirundo", "Makamba",
						"Muramvya", "Muyinga", "Ngozi", "Rutana", "Ruyigi" }
				// CAMBODIA
				,
				{ "Banteay Mean Chey", "Bat D�mb�ng", "K�mp�ng Cham",
						"K�mp�ng Chhnang", "K�mp�ng Spoeu", "K�mp�ng Thum",
						"K�mp�t", "K�ndal", "Ka�h K�ng", "Kr�ch�h",
						"Krong Kaeb", "Krong Pailin", "Krong Preah Sihanouk",
						"M�nd�l Kiri", "Otdar Mean Chey", "Phnum P�nh",
						"Pousat", "Preah Vih�ar", "Prey Veaeng",
						"R�tanak Kiri", "Siem Reab", "Stueng Traeng",
						"Svay Rieng", "Takaev" }
				// CAMEROON
				,
				{ "Adamaoua", "Centre", "Est", "Littoral", "Nord",
						"Nord Extr�me", "Nordouest", "Ouest", "Sud", "Sudouest" }
				// CANADA
				,
				{ "Alberta", "British Columbia", "Manitoba", "New Brunswick",
						"Newfoundland and Labrador", "Northwest Territories",
						"Nova Scotia", "Nunavut", "Ontario",
						"Prince Edward Island", "Qu�bec", "Saskatchewan",
						"Yukon" }
				// CAPE VERDE
				,
				{ "Boavista", "Brava", "Fogo", "Maio", "Sal", "Santo Ant�o",
						"S�o Nicolau", "S�o Tiago", "S�o Vicente" }
				// CAYMAN ISLANDS
				,
				{ "Grand Cayman" }
				// CENTRAL AFRICAN REPUBLIC
				,
				{ "Bamingui-Bangoran", "Bangui", "Basse-Kotto", "Haute-Kotto",
						"Haut-Mbomou", "K�mo", "Lobaye", "Mamb�r�-Kad��",
						"Mbomou", "Nana-Gribizi", "Nana-Mamb�r�",
						"Ombella Mpoko", "Ouaka", "Ouham", "Ouham-Pend�",
						"Sangha-Mba�r�", "Vakaga" }
				// CHAD
				,
				{ "Batha", "Biltine", "Bourkou-Ennedi-Tibesti",
						"Chari-Baguirmi", "Gu�ra", "Kanem", "Lac",
						"Logone Occidental", "Logone Oriental", "Mayo-K�bbi",
						"Moyen-Chari", "Ouadda�", "Salamat", "Tandjil�" }
				// CHILE
				,
				{ "Ais�n", "Antofagasta", "Araucan�a", "Atacama", "B�o B�o",
						"Coquimbo", "Libertador General Bernardo O'Higgins",
						"Los Lagos", "Magellanes", "Maule", "Metropolitana",
						"Tarapac�", "Valpara�so" }
				// CHINA
				,
				{ "Anhui", "Aomen", "Beijing", "Chongqing", "Fujian", "Gansu",
						"Guangdong", "Guangxi", "Guizhou", "Hainan", "Hebei",
						"Heilongjiang", "Henan", "Hongkong", "Hubei", "Hunan",
						"Jiangsu", "Jiangxi", "Jilin", "Liaoning", "Neimenggu",
						"Ningxia", "Qinghai", "Shaanxi", "Shandong",
						"Shanghai", "Shanxi", "Sichuan", "Tianjin", "Xinjiang",
						"Xizang", "Yunnan", "Zhejiang" }
				// CHRISTMAS ISLAND
				,
				{}
				// COCOS (KEELING) ISLANDS
				,
				{ "Home Island", "West Island" }
				// COLOMBIA
				,
				{ "Amazonas", "Antioquia", "Arauca", "Atl�ntico", "Bogot�",
						"Bol�var", "Boyac�", "Caldas", "Caquet�", "Casanare",
						"Cauca", "C�sar", "Choc�", "C�rdoba", "Cundinamarca",
						"Guain�a", "Guaviare", "Huila", "La Guajira",
						"Magdalena", "Meta", "Nari�o", "Norte de Santander",
						"Putumayo", "Quindi�", "Risaralda",
						"San Andr�s y Providencia", "Santander", "Sucre",
						"Tolima", "Valle del Cauca", "Vaup�s", "Vichada" }
				// COMOROS
				,
				{ "Mwali", "Ndzouani", "Ngazidja" }
				// CONGO, DEMOCRATIC REPUBLIC OF THE
				,
				{ "Bandundu", "Bas-Congo", "�quateur", "Haut-Congo",
						"Kasai-Occidental", "Kasai-Oriental", "Katanga",
						"Kinshasa", "Maniema", "Nord-Kivu", "Sud-Kivu" }
				// CONGO, REPUBLIC OF THE
				,
				{ "Bouenza", "Brazzaville", "Cuvette", "Cuvette-Ouest",
						"Kouilou", "L�koumou", "Likouala", "Niari", "Plateaux",
						"Pool", "Sangha" }
				// COOK ISLANDS
				,
				{ "Aitutaki", "Atiu", "Mangaia", "Manihiki", "Mauke",
						"Mitiaro", "Nassau", "Pukapuka", "Rakahanga",
						"Rarotonga", "Tongareva" }
				// COSTA RICA
				,
				{ "Alajuela", "Cartago", "Guanacaste", "Heredia", "Lim�n",
						"Puntarenas", "San Jos�" }
				// COTE D'IVOIRE
				,
				{ "Abidjan", "Bouak�", "Daloa", "Korhogo", "Yamoussoukro" }
				// CROATIA
				,
				{ "Bjelovar-Bilogora", "Dubrovnik-Neretva", "Grad Zagreb",
						"Istra", "Karlovac", "Koprivnica-Kri�evci",
						"Krapina-Zagorje", "Lika-Senj", "Medimurje",
						"Osijek-Baranja", "Po�ega-Slavonija",
						"Primorje-Gorski Kotar", "�ibenik-Knin",
						"Sisak-Moslavina", "Slavonski Brod-Posavina",
						"Split-Dalmacija", "Vara�din", "Virovitica-Podravina",
						"Vukovar-Srijem", "Zadar", "Zagreb" }
				// CUBA
				,
				{ "Camag�ey", "Ciego de �vila", "Cienfuegos",
						"Ciudad de la Habana", "Granma", "Guant�namo",
						"Holgu�n", "Isla de la Juventud", "La Habana",
						"Las Tunas", "Matanzas", "Pinar del R�o",
						"Sancti Sp�ritus", "Santiago de Cuba", "Villa Clara" }
				// CYPRUS
				,
				{ "Government controlled area", "Turkish controlled area" }
				// CZECH REPUBLIC
				,
				{ "Jihocesk�", "Jihomoravsk�", "Karlovarsk�",
						"Kr�lov�hradeck�", "Libereck�", "Moravskoslezsk�",
						"Olomouck�", "Pardubick�", "Plzensk�", "Prag",
						"Stredocesk�", "�steck�", "Vysocina", "Zl�nsk�" }
				// DENMARK
				,
				{ "Hovedstaden", "Midtjylland", "Nordjylland", "Sj�lland",
						"Syddanmark" }
				// DJIBOUTI
				,
				{ "Ali Sabieh", "Dikhil", "Djibouti", "Obock", "Tadjoura" }
				// DOMINICA
				,
				{ "Saint Andrew", "Saint David", "Saint George", "Saint John",
						"Saint Joseph", "Saint Luke", "Saint Mark",
						"Saint Patrick", "Saint Paul", "Saint Peter" }
				// DOMINICAN REPUBLIC
				,
				{ "Azua", "Baoruco", "Barahona", "Dajab�n", "Duarte",
						"El�as Pi�a", "El Seibo", "Espaillat", "Hato Mayor",
						"Independencia", "La Altagracia", "La Romana",
						"La Vega", "Mar�a Trinidad S�nchez", "Monse�or Nouel",
						"Monte Cristi", "Monte Plata", "Pedernales", "Peravia",
						"Puerto Plata", "Salcedo", "Saman�", "S�nchez Ram�rez",
						"San Crist�bal", "San Jos� de Ocoa", "San Juan",
						"San Pedro de Macor�s", "Santiago",
						"Santiago Rodr�guez", "Santo Domingo", "Valverde" }
				// ECUADOR
				,
				{ "Azuay", "Bol�var", "Ca�ar", "Carchi", "Chimborazo",
						"Cotopaxi", "El Oro", "Esmeraldas", "Gal�pagos",
						"Guayas", "Imbabura", "Loja", "Los R�os", "Manab�",
						"Morona Santiago", "Napo", "Orellana", "Pastaza",
						"Pichincha", "Sucumb�os", "Tungurahua",
						"Zamora Chinchipe" }
				// EGYPT
				,
				{ "ad-Daqahliyah", "al-Bah?r-al-Ah?mar", "al-Buh?ayrah",
						"Alexandria", "al-Fayyum", "al-Garbiyah",
						"al-Ismailiyah", "al-Minufiyah", "al-Minya",
						"al-Qalyubiyah", "al-Wadi al-Jadid", "a�-�arqiyah",
						"Assiut", "Assuan", "as-Suways", "Bani Suwayf",
						"Bur Sa'id", "Dumyat", "Giseh", "Kafr-a�-�ayh_",
						"Kairo", "Luxor", "Matruh", "Qina", "�amal Sina",
						"Sawhaj", "South Sinai" }
				// EL SALVADOR
				,
				{ "Ahuachap�n", "Caba�as", "Chalatenango", "Cuscatl�n",
						"La Libertad", "La Paz", "La Uni�n", "Moraz�n",
						"San Miguel", "San Salvador", "Santa Ana",
						"San Vicente", "Sonsonate", "Usulut�n" }
				// EQUATORIAL GUINEA
				,
				{ "Annob�n", "Bioko Norte", "Bioko Sur", "Centro Sur",
						"Ki�-Ntem", "Litoral", "Wele-Nzas" }
				// ERITREA
				,
				{ "Anseba", "Debub", "Debub-Keih-Bahri", "Gash-Barka",
						"Maekel", "Semien-Keih-Bahri" }
				// ESTONIA
				,
				{ "Harju", "Hiiu", "Ida-Viru", "J�rva", "Jogeva", "L��ne",
						"L��ne-Viru", "P�rnu", "Polva", "Rapla", "Saare",
						"Tartu", "Valga", "Viljandi", "Voru" }
				// ETHIOPIA
				,
				{ "Addis Abeba", "Afar", "Amhara", "Benishangul", "Diredawa",
						"Gambella", "Harar", "Oromia", "Somali", "Southern",
						"Tigray" }
				// FALKLAND ISLANDS (ISLAS MALVINAS)
				,
				{ "Falkland Islands", "South Georgia" }
				// FAROE ISLANDS
				,
				{ "Klaksv�k", "Nor�ara Eysturoy", "Nor�oy", "Sandoy",
						"Streymoy", "Su�uroy", "Sy�ra Eysturoy", "T�rshavn",
						"V�ga" }
				// FIJI
				,
				{ "Central", "Eastern", "Northern", "Western" }
				// FINLAND
				,
				{ "Ahvenanmaa", "Etel�-Karjala", "Etel�-Pohjanmaa",
						"Etel�-Savo", "It�-Uusimaa", "Kainuu", "Kanta-H�me",
						"Keski-Pohjanmaa", "Keski-Suomi", "Kymenlaakso",
						"Lappland", "P�ij�t-H�me", "Pirkanmaa", "Pohjanmaa",
						"Pohjois-Karjala", "Pohjois-Pohjanmaa", "Pohjois-Savo",
						"Satakunta", "Uusimaa", "Varsinais-Suomi" }
				// FRANCE
				,
				{ "Alsace", "Aquitaine", "Auvergne", "Basse-Normandie",
						"Bourgogne", "Bretagne", "Centre", "Champagne-Ardenne",
						"Corse", "Franche-Comt�", "Haute-Normandie",
						"�le-de-France", "Languedoc-Roussillon", "Limousin",
						"Lorraine", "Midi-Pyr�n�es", "Nord-Pas-de-Calais",
						"Pays-de-la-Loire", "Picardie", "Poitou-Charentes",
						"Provence-Alpes-C�te-d'Azur", "Rh�ne-Alpes" }
				// FRENCH GUIANA
				,
				{ "Cayenne", "Saint-Laurent-du-Maroni" }
				// FRENCH POLYNESIA
				,
				{ "�les du Vent", "�les sous le Vent", "Marquises",
						"Tuamotu-Gambier", "Tubuai" }
				// FRENCH SOUTHERN AND ANTARCTIC LANDS
				,
				{ "Amsterdam", "Crozet Islands", "Kerguelen" }
				// GABON
				,
				{ "Estuaire", "Haut-Ogoou�", "Moyen-Ogoou�", "Ngouni�",
						"Nyanga", "Ogoou�-Ivindo", "Ogoou�-Lolo",
						"Ogoou�-Maritime", "Woleu-Ntem" }
				// GAMBIA, THE
				,
				{ "Banjul", "Basse", "Brikama", "Janjanbureh", "Kanifing",
						"Kerewan", "Kuntaur", "Mansakonko" }
				// GEORGIA
				,
				{ "Abkhasia", "Ajaria", "Guria", "Imereti", "Kakheti",
						"Kvemo Kartli", "Mtskheta-Mtianeti",
						"Raga-Lechkumi and Kverno Svaneti",
						"Samagrelo and Zemo Svaneti", "Samtskhe-Javakheti",
						"Shida Kartli", "Tiflis" }
				// GERMANY
				,
				{ "Baden-W�rttemberg", "Bayern", "Berlin", "Brandenburg",
						"Bremen", "Hamburg", "Hessen",
						"Mecklenburg-Vorpommern", "Niedersachsen",
						"Nordrhein-Westfalen", "Rheinland-Pfalz", "Saarland",
						"Sachsen", "Sachsen-Anhalt", "Schleswig-Holstein",
						"Th�ringen" }
				// GHANA
				,
				{ "Ashanti", "Brong-Ahafo", "Central", "Eastern",
						"Greater Accra", "Northern", "Upper East",
						"Upper West", "Volta", "Western" }
				// GIBRALTAR
				,
				{ "" }
				// GREECE
				,
				{ "Attik�", "Kentriki Ellada", "Nisia Aigaiou Kriti",
						"Voria Ellada" }
				// GREENLAND
				,
				{ "Aasiaat", "Ammassalik", "Illoqqortoormiut", "Ilulissat",
						"Ivittuut", "Kangaatsiaq", "Maniitsoq", "Nanortalik",
						"Narsaq", "Nuuk", "Paamiut", "Qaanaaq", "Qaqortoq",
						"Qasigiannguit", "Qeqertarsuaq", "Sisimiut",
						"Udenfor kommunal inddeling", "Upernavik", "Uummannaq"

				}
				// GRENADA
				,
				{ "Carriacou-Petite Martinique", "Saint Andrew",
						"Saint Davids", "Saint George", "Saint John",
						"Saint Mark", "Saint Patrick"

				}
				// GUADELOUPE
				,
				{ "Basse-Terre", "Grande-Terre", "�les des Saintes",
						"La D�sirade", "Marie-Galante", "Saint Barth�lemy",
						"Saint Martin" }
				// GUAM
				,
				{ "Agana Heights", "Agat", "Barrigada", "Chalan-Pago-Ordot",
						"Dededo", "Hagat�a", "Inarajan", "Mangilao", "Merizo",
						"Mongmong-Toto-Maite", "Santa Rita", "Sinajana",
						"Talofofo", "Tamuning", "Yigo", "Yona" }
				// GUATEMALA
				,
				{ "Alta Verapaz", "Baja Verapaz", "Chimaltenango",
						"Chiquimula", "El Progreso", "Escuintla", "Guatemala",
						"Huehuetenango", "Izabal", "Jalapa", "Jutiapa",
						"Pet�n", "Quezaltenango", "Quich�", "Retalhuleu",
						"Sacatep�quez", "San Marcos", "Santa Rosa", "Solol�",
						"Suchitep�quez", "Totonicap�n", "Zacapa" }
				// GUINEA
				,
				{ "Basse Guin�e", "Conakry", "Guin�e Foresti�re",
						"Haute Guin�e", "Moyenne Guin�e" }
				// GUINEA-BISSAU
				,
				{ "Bafat�", "Biombo", "Bissau", "Bolama", "Cacheu", "Gab�",
						"Oio", "Quinara", "Tombali" }
				// GUYANA
				,
				{ "Barima-Waini", "Cuyuni-Mazaruni", "Demerara-Mahaica",
						"East Berbice-Corentyne",
						"Essequibo Islands-West Demerara", "Mahaica-Berbice",
						"Pomeroon-Supenaam", "Upper Demerara-Berbice",
						"Upper Takutu-Upper Essequibo" }
				// HAITI
				,
				{ "Artibonite", "Centre", "Grand'Anse", "Nord", "Nord-Est",
						"Nord-Ouest", "Ouest", "Sud", "Sud-Est" }
				// HEARD ISLAND AND MCDONALD ISLANDS
				,
				{}
				// HOLY SEE (VATICAN CITY)
				,
				{}
				// HONDURAS
				,
				{ "Atl�ntida", "Choluteca", "Col�n", "Comayagua", "Cop�n",
						"Cort�s", "Distrito Central", "El Para�so",
						"Francisco Moraz�n", "Gracias a Dios", "Intibuc�",
						"Islas de la Bah�a", "La Paz", "Lempira", "Ocotepeque",
						"Olancho", "Santa B�rbara", "Valle", "Yoro" }
				// HONG KONG
				,
				{ "Hongkong", "Kowloon and New Kowl" }
				// HUNGARY
				,
				{ "B�cs-Kiskun", "Baranya", "B�k�s", "Borsod-Aba�j-Zempl�n",
						"Budapest", "Csongr�d", "Fej�r", "Gyor-Moson-Sopron",
						"Hajd�-Bihar", "Heves", "J�sz-Nagykun-Szolnok",
						"Kom�rom-Esztergom", "N�gr�d", "Pest", "Somogy",
						"Szabolcs-Szatm�r-Bereg", "Tolna", "Vas", "Veszpr�m",
						"Zala" }
				// ICELAND
				,
				{ "Austurland", "H�fu�borgarsv��i", "Nor�urland eystra",
						"Nor�urland vestra", "Su�urland", "Su�urnes",
						"Vestfir�ir", "Vesturland" }
				// INDIA
				,
				{ "Andaman and Nicobar Islands", "Andhra Pradesh",
						"Arunachal Pradesh", "Assam", "Bangla", "Bihar",
						"Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli",
						"Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana",
						"Himachal Pradesh", "Jammu and Kashmir", "Jharkhand",
						"Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh",
						"Maharashtra", "Manipur", "Meghalaya", "Mizoram",
						"Nagaland", "Orissa", "Pondicherry", "Punjab",
						"Rajasthan", "Sikkim", "Tamil Nadu", "Tripura",
						"Uttaranchal", "Uttar Pradesh" }
				// INDONESIA
				,
				{ "Aceh", "Bali", "Bangka-Belitung", "Banten", "Bengkulu",
						"Gorontalo", "Jakarta", "Jambi", "Jawa Barat",
						"Jawa Tengah", "Jawa Timur", "Kalimantan Barat",
						"Kalimantan Selatan", "Kalimantan Tengah",
						"Kalimantan Timur", "Lampung", "Maluku",
						"Maluku Utara", "Nusa Tenggara Barat",
						"Nusa Tenggara Timur", "Papua", "Riau",
						"Riau Kepulauan", "Sulawesi Selatan",
						"Sulawesi Tengah", "Sulawesi Tenggara",
						"Sulawesi Utara", "Sumatera Barat", "Sumatera Selatan",
						"Sumatera Utara", "Yogyakarta" }
				// IRAN
				,
				{ "Ardabil", "Azarbayejan-e Gharbi", "Azarbayejan-e Sharqi",
						"Bushehr", "Chahar Mahal-e Bakhtiari", "Esfahan",
						"Fars", "Gilan", "Golestan", "Hamadan", "Hormozgan",
						"Ilam", "Kerman", "Kermanshah", "Khorasan-e Razavi",
						"Khorasan Janubi", "Khorasan Shamali", "Khuzestan",
						"Kohgiluyeh-e Boyerahmad", "Kordestan", "Lorestan",
						"Markazi", "Mazandaran", "Qazvin", "Qom", "Semnan",
						"Sistan-e Baluchestan", "Teheran", "Yazd", "Zanjan" }
				// IRAQ
				,
				{ "al-Anbar", "al-Basrah", "al-Mut_anna", "al-Qadisiyah",
						"an-Najaf", "as-Sulaymaniyah", "at-Ta'mim", "Babil",
						"Bagdad", "Dahuk", "�i Qar", "Diyala", "Irbil",
						"Karbala", "Maysan", "Ninawa", "Salah?-ad-Din", "Wasit" }
				// IRELAND
				,
				{ "Carlow", "Cavan", "Clare", "Cork", "Donegal", "Dublin",
						"Galway", "Kerry", "Kildare", "Kilkenny", "Laois",
						"Leitrim", "Limerick", "Longford", "Louth", "Mayo",
						"Meath", "Monaghan", "Offaly", "Roscommon", "Sligo",
						"Tipperary North Riding", "Tipperary South Riding",
						"Waterford", "Westmeath", "Wexford", "Wicklow" }
				// ISLE OF MAN
				,
				{ "Castletown", "Douglas", "Laxey", "Onchan", "Peel",
						"Port Erin", "Port Saint Mary", "Ramsey" }
				// ISRAEL
				,
				{ "Hadarom", "Haifa", "Hamerkaz", "Haz_afon", "Jerusalem",
						"Judea and Samaria", "Tel Aviv" }
				// ITALY
				,
				{ "Abruzzen", "Apulien", "Basilicata", "Calabria", "Campania",
						"Emilia-Romagna", "Friuli-Venezia Giulia", "Lazio",
						"Ligurien", "Lombardei", "Marken", "Molise",
						"Piemonte", "Sardinien", "Sizilien", "Toscana",
						"Trentino-Alto Adige", "Umbria", "Valle d'Aosta",
						"Veneto" }
				// JAMAICA
				,
				{ "Clarendon", "Hanover", "Kingston", "Manchester", "Portland",
						"Saint Andrew", "Saint Ann", "Saint Catherine",
						"Saint Elizabeth", "Saint James", "Saint Mary",
						"Saint Thomas", "Trelawney", "Westmoreland" }
				// JAN MAYEN
				,
				{ "L�nsimaa " }
				// JAPAN
				,
				{ "Aichi", "Akita", "Aomori", "Chiba", "Ehime", "Fukui",
						"Fukuoka", "Fukushima", "Gifu", "Gumma", "Hiroshima",
						"Hokkaido", "Hyogo", "Ibaraki", "Ishikawa", "Iwate",
						"Kagawa", "Kagoshima", "Kanagawa", "Kochi", "Kumamoto",
						"Kyoto", "Mie", "Miyagi", "Miyazaki", "Nagano",
						"Nagasaki", "Nara", "Niigata", "Oita", "Okayama",
						"Okinawa", "Osaka", "Saga", "Saitama", "Shiga",
						"Shimane", "Shizuoka", "Tochigi", "Tokio", "Tokushima",
						"Tottori", "Toyama", "Wakayama", "Yamagata",
						"Yamaguchi", "Yamanashi" }
				// JORDAN
				,
				{ "'Ajlun", "al-'Aqabah", "al-Balqa'", "al-Karak", "al-Mafraq",
						"'Amman", "at-Tafilah", "az-Zarqa'", "Irbid", "Jara�",
						"Ma'an", "Madaba" }
				// KAZAKHSTAN
				,
				{ "Akmechet", "Almaty", "Aqmola", "Aqt�be", "Atyrau",
						"Batis Kazakstan", "Mankistau", "Ont�stik Kazakstan",
						"Pavlodar", "Qaragandy", "Qostanay", "Sigis Kazakstan",
						"Solt�stik Kazakstan", "Taraz" }
				// KENYA
				,
				{ "Central", "Coast", "Eastern", "Nairobi", "North Eastern",
						"Nyanza", "Rift Valley", "Western" }
				// KIRIBATI
				,
				{ "Abaiang", "Abemana", "Aranuka", "Arorae", "Banaba", "Beru",
						"Butaritari", "Kiritimati", "Kuria", "Maiana", "Makin",
						"Marakei", "Nikunau", "Nonouti", "Onotoa",
						"Phoenix Islands", "Tabiteuea North",
						"Tabiteuea South", "Tabuaeran", "Tamana",
						"Tarawa North", "Tarawa South", "Teraina" }
				// KOREA, NORTH
				,
				{ "Chagangdo", "Hamgyongbukto", "Hamgyongnamdo",
						"Hwanghaebukto", "Hwanghaenamdo", "Kangwon",
						"Pyonganbukto", "Pyongannamdo", "Pyongyang", "Rason",
						"Yanggang" }
				// KOREA, SOUTH
				,
				{ "Cheju", "Chollabuk", "Chollanam", "Chungchongbuk",
						"Chungchongnam", "Inchon", "Kangwon", "Kwangju",
						"Kyonggi", "Kyongsangbuk", "Kyongsangnam", "Pusan",
						"Soul", "Taegu", "Taejon", "Ulsan" }

				// KUWAIT
				,
				{ "al-Ah?madi", "al-'Asamah", "al-Farwaniyah", "al-Jahra'",
						"Hawalli", "Mubarak al-Kabir" }
				// KYRGYZSTAN
				,
				{ "Batken", "Biskek", "Celalabad", "�uy", "Issik-G�l", "Narin",
						"Os", "Talas" }
				// LAOS
				,
				{ "Attopu", "Bokeo", "Bolikhamsay", "Champasak", "Houaphanh",
						"Khammouane", "Luang Nam Tha", "Luang Prabang",
						"Oudomxay", "Phongsaly", "Saravan", "Savannakhet",
						"Sekong", "Viangchan Prefecture", "Viangchan Province",
						"Xaignabury", "Xiang Khuang" }
				// LATVIA
				,
				{ "Aizkraukles", "Aluksnes", "Balvu", "Bauskas", "Cesu",
						"Daugavpils", "Daugavpils pilseta", "Dobeles",
						"Gulbenes", "Jekabspils", "Jelgava", "Jelgavas",
						"Jurmala pilseta", "Kraslavas", "Kuldigas", "Liepaja",
						"Liepajas", "Limba�u", "Ludzas", "Madonas", "Ogres",
						"Preilu", "Rezekne", "Rezeknes", "Riga", "Rigas",
						"Saldus", "Talsu", "Tukuma", "Valkas", "Valmieras",
						"Ventspils", "Ventspils pilseta" }
				// LEBANON
				,
				{ "al-Biqa'a", "al-Janub", "an-Nabatiyah", "a�-�amal",
						"Jabal Lubnan" }
				// LESOTHO
				,
				{ "Berea", "Butha-Buthe", "Leribe", "Mafeteng", "Maseru",
						"Mohale's Hoek", "Mokhotlong", "Qacha's Nek",
						"Quthing", "Thaba-Tseka" }
				// LIBERIA
				,
				{ "Bomi", "Bong", "Grand Bassa", "Grand Cape Mount",
						"Grand Gedeh", "Maryland and Grand Kru", "Montserrado",
						"Nimba" }
				// LIBYA
				,
				{ "Ajdabiya", "al-Butnan", "al-Hizam al-Ah_d?ar",
						"al-Jabal al-Ahd?ar", "al-Jifarah", "al-Jufrah",
						"al-Kufrah", "al-Marj", "al-Marqab", "al-Qubbah",
						"al-Wah?at", "an-Nuqat al-Hums", "az-Zawiyah",
						"Bangazi", "Bani Walid", "Darnah", "Gadamis", "Garyan",
						"Gat", "Marzuq", "Misratah", "Mizdah", "Nalut",
						"Sabha", "Sabratah wa Surman", "Surt", "Tarabulus",
						"Tarhunah wa Masallatah", "Wadi al-H?ayat",
						"Wadi a�-�ati", "Yafran wa Jadu" }
				// LIECHTENSTEIN
				,
				{ "Balzers", "Eschen", "Gamprin", "Mauren", "Planken",
						"Ruggell", "Schaan", "Schellenberg", "Triesen",
						"Triesenberg", "Vaduz", }
				// LITHUANIA
				,
				{ "Kaunas", "Klaipeda", "Panevezys", "�iauliai", "Vilna" }
				// LUXEMBOURG
				,
				{ "Capellen", "Clervaux", "Diekirch", "Echternach",
						"Esch-sur-Alzette", "Grevenmacher", "Luxemburg",
						"Mersch", "Redange", "Remich", "Vianden", "Wiltz" }
				// MACAU
				,
				{ "Macau" }
				// MACEDONIA
				,
				{ "Berovo", "Bitola", "Brod", "Debar", "Delcevo",
						"Demir Hisar", "Gevgelija", "Gostivar", "Kavadarci",
						"Kicevo", "Kocani", "Kratovo", "Kriva Palanka",
						"Kru�evo", "Kumanovo", "Negotino", "Ohrid", "Prilep",
						"Probi�tip", "Radovi�", "Resen", "Skopje", "�tip",
						"Struga", "Strumica", "Sveti Nikole", "Tetovo",
						"Valandovo", "Veles", "Vinica" }
				// MADAGASCAR
				,
				{ "Antananarivo", "Antsiranana", "Fianarantsoa", "Mahajanga",
						"Toamasina", "Toliary" }
				// MALAWI
				,
				{ "Balaka", "Blantyre City", "Chikwawa", "Chiradzulu",
						"Chitipa", "Dedza", "Dowa", "Karonga", "Kasungu",
						"Lilongwe City", "Machinga", "Mangochi", "Mchinji",
						"Mulanje", "Mwanza", "Mzimba", "Mzuzu City",
						"Nkhata Bay", "Nkhotakota", "Nsanje", "Ntcheu",
						"Ntchisi", "Phalombe", "Rumphi", "Salima", "Thyolo",
						"Zomba Municipality" }
				// MALAYSIA
				,
				{ "Johor", "Kedah", "Kelantan", "Kuala Lumpur", "Labuan",
						"Melaka", "Negeri Sembilan", "Pahang", "Perak",
						"Perlis", "Pulau Pinang", "Sabah", "Sarawak",
						"Selangor", "Terengganu" }
				// MALDIVES
				,
				{ "Alif Alif", "Alif Dhaal", "Baa", "Dhaal", "Faaf",
						"Gaaf Alif", "Gaaf Dhaal", "Ghaviyani", "Haa Alif",
						"Haa Dhaal", "Kaaf", "Laam", "Lhaviyani", "Mal�",
						"Miim", "Nuun", "Raa", "Shaviyani", "Siin", "Thaa",
						"Vaav" }
				// MALI
				,
				{ "Bamako", "Gao", "Kayes", "Kidal", "Koulikoro", "Mopti",
						"S�gou", "Sikasso", "Tombouctou" }
				// MALTA
				,
				{ "Gozo and Comino", "Northern", "Northern Harbour",
						"South Eastern", "Southern Harbour", "Western" }
				// MARSHALL ISLANDS
				,
				{ "Ailinlaplap", "Ailuk", "Arno", "Aur", "Bikini", "Ebon",
						"Enewetak", "Jabat", "Jaluit", "Kili", "Kwajalein",
						"Lae", "Lib", "Likiep", "Majuro", "Maloelap", "Mejit",
						"Mili", "Namorik", "Namu", "Rongelap", "Ujae", "Utrik",
						"Wotho", "Wotje" }
				// MARTINIQUE
				,
				{ "Fort-de-France", "La Trinit�", "Le Marin", "Saint-Pierre" }
				// MAURITANIA
				,
				{ "Adrar", "Assaba", "Brakna", "�ah_lat Nawadibu",
						"Guidimagha", "Gurgul", "Hud-al-Garbi", "Hud-a�-�arqi",
						"In�iri", "Nawak�ut", "Takant", "Tiris Zammur",
						"Trarza" }
				// MAURITIUS
				,
				{ "Black River", "Flacq", "Grand Port", "Moka",
						"Pamplemousses", "Plaines Wilhelm",
						"Riviere du Rempart", "Rodrigues", "Savanne" }
				// MAYOTTE
				,
				{ "Mayotte", "Pamanzi" }
				// MEXICO
				,
				{ "Aguascalientes", "Baja California", "Baja California Sur",
						"Campeche", "Chiapas", "Chihuahua", "Coahuila",
						"Colima", "Distrito Federal", "Durango", "Guanajuato",
						"Guerrero", "Hidalgo", "Jalisco", "M�xico",
						"Michoac�n", "Morelos", "Nayarit", "Nuevo Le�n",
						"Oaxaca", "Puebla", "Quer�taro", "Quintana Roo",
						"San Luis Potos�", "Sinaloa", "Sonora", "Tabasco",
						"Tamaulipas", "Tlaxcala", "Veracruz", "Yucat�n",
						"Zacatecas" }
				// MICRONESIA, FEDERATED STATES OF
				,
				{ "Chuuk", "Kusaie", "Pohnpei", "Yap" }
				// MOLDOVA
				,
				{ "Anenii Noi", "Balti", "Basarabeasca", "Briceni", "Cahul",
						"Calarasi", "Camenca", "Cantemir", "Causeni",
						"Chisinau", "Cimislia", "Criuleni", "Donduseni",
						"Drochia", "Dubasari municipiu", "Edinet", "Falesti",
						"Floresti", "Gagauzia", "Glodeni", "Grigoriopol",
						"H�ncesti", "Ialoveni", "Leova", "Nisporeni", "Ocnita",
						"Orhei", "Rezina", "R�bnita", "R�scani", "S�ngerei",
						"Slobozia", "Soldanesti", "Soroca", "Stefan Voda",
						"Straseni", "Taraclia", "Telenesti", "Tighina",
						"Tiraspol", "Ungheni" }
				// MONACO
				,
				{ "Fontvieille", "La Condamine", "Monaco-Ville", "Monte Carlo" }
				// MONGOLIA
				,
				{ "Arhangaj", "Bajanhongor", "Bajan-�lgij", "Bulgan",
						"Darhan-Uul", "Dornod", "Dornogovi", "Dundgovi",
						"Govi-Altaj", "Govisumber", "H�ntij", "Hovd",
						"H�vsg�l", "�mn�govi", "Orhon", "�v�rhangaj",
						"S�l�ng�", "S�hbaatar", "T�v", "Ulaanbaatar", "Uvs",
						"Zavhan" }
				// MONTSERRAT
				,
				{ "Plymouth " }
				// MOROCCO
				,
				{ "Casablanca", "Chaouia-Ouardigha", "Doukkala-Abda",
						"F�s-Boulemane", "Gharb-Chrarda-B�ni Hssen", "Guelmim",
						"Marrakech-Tensift-Al Haouz", "Meknes-Tafilalet",
						"Oriental", "Rabat-Sal�-Zammour-Zaer",
						"Souss Massa-Dra�", "Tadla-Azilal", "Tangier-T�touan",
						"Taza-Al Hoceima-Taounate" }
				// MOZAMBIQUE
				,
				{ "Cabo Delgado", "Gaza", "Inhambane", "Manica", "Maputo",
						"Maputo Provincia", "Nampula", "Niassa", "Sofala",
						"Tete", "Zambezia" }
				// NAMIBIA
				,
				{ "Caprivi", "Erongo", "Hardap", "Karas", "Kavango", "Khomas",
						"Kunene", "Ohangwena", "Omaheke", "Omusati", "Oshana",
						"Oshikoto", "Otjozondjupa" }
				// NAURU
				,
				{ "Aiwo" }
				// NEPAL
				,
				{ "Baglung", "Banke", "Bara", "Bardiya", "Bhaktapur",
						"Chitwan", "Dadeldhura", "Dailekh", "Dang Deokhuri",
						"Darchula", "Dhankuta", "Dhanusa", "Dolakha", "Doti",
						"Gorkha", "Ilam", "Jhapa", "Jumla", "Kailali",
						"Kanchanpur", "Kapilvastu", "Kaski", "Kathmandu",
						"Kavrepalanchok", "Lalitpur", "Mahottari", "Makwanpur",
						"Morang", "Nawalparasi", "Nuwakot", "Palpa", "Parsa",
						"Rautahat", "Rupandehi", "Sankhuwasabha", "Saptari",
						"Sarlahi", "Sindhuli", "Siraha", "Sunsari", "Surkhet",
						"Syangja", "Tanahu", "Udayapur" }
				// NETHERLANDS
				,
				{ "Drenthe", "Flevoland", "Friesland", "Gelderland",
						"Groningen", "Limburg", "Noord-Brabant",
						"Noord-Holland", "Overijssel", "Utrecht", "Zeeland",
						"Zuid-Holland" }
				// NETHERLANDS ANTILLES
				,
				{ "Bonaire", "Cura�ao", "Saba", "Sint Eustatius",
						"Sint Maarten" }
				// NEW CALEDONIA
				,
				{ "�les", "Nord", "Sud" }
				// NEW ZEALAND
				,
				{ "Auckland", "Bay of Plenty", "Canterbury", "Gisborne",
						"Hawke's Bay", "Manawatu-Wanganui", "Marlborough",
						"Nelson", "Northland", "Otago", "Southland",
						"Taranaki", "Tasman", "Waikato", "Wellington",
						"West Coast" },
				// NICARAGUA
				{ "Atl�ntico Norte", "Atl�ntico Sur", "Boaco", "Carazo",
						"Chinandega", "Chontales", "Estel�", "Granada",
						"Jinotega", "Le�n", "Madriz", "Managua", "Masaya",
						"Matagalpa", "Nueva Segovia", "R�o San Juan", "Rivas" },
				// NIGER
				{ "Agadez", "Diffa", "Dosso", "Maradi", "Niamey", "Tahoua",
						"Tillab�ry", "Zinder" },
				// NIGERIA
				{ "Abia", "Abuja Federal Capital Territory", "Adamawa",
						"Akwa Ibom", "Anambra", "Bauchi", "Bayelsa", "Benue",
						"Borno", "Cross River", "Delta", "Ebonyi", "Edo",
						"Ekiti", "Enugu", "Gombe", "Imo", "Jigawa", "Kaduna",
						"Kano", "Katsina", "Kebbi", "Kogi", "Kwara", "Lagos",
						"Nassarawa", "Niger", "Ogun", "Ondo", "Osun", "Oyo",
						"Plateau", "Rivers", "Sokoto", "Taraba", "Yobe",
						"Zamfara" },
				// NIUE
				{ "" },
				// NORFOLK ISLAND
				{ "" },
				// NORTHERN MARIANA ISLANDS
				{ "Northern Islands", "Rota", "Saipan", "Tinian" },
				// NORWAY
				{ "Akershus", "Aust-Agder", "Buskerud", "Finnmark", "Hedmark",
						"Hordaland", "M�re og Romsdal", "Nordland",
						"Nord-Tr�ndelag", "Oppland", "Oslo", "�stfold",
						"Rogaland", "Sogn og Fjordane", "S�r-Tr�ndelag",
						"Telemark", "Troms", "Vest-Agder", "Vestfold" },
				// OMAN
				{ "ad-Dah_iliyah", "al-Batinah", "a�-�arqiyah", "az?-Z?ahirah",
						"Maskat", "Musandam", "Z?ufar" },
				// PAKISTAN
				{ "Azad Kashmir", "Baluchistan", "Federal Capital Area",
						"Federally administered Tribal Areas",
						"Northern Areas", "North-West Frontier", "Punjab",
						"Sind" },
				// PALAU
				{ "Aimeliik", "Airai", "Angaur", "Hatobohei", "Kayangel",
						"Koror", "Melekeok", "Ngaraard", "Ngardmau",
						"Ngaremlengui", "Ngatpang", "Ngchesar", "Ngerchelong",
						"Ngiwal", "Peleliu", "Sonsorol" },
				// PANAMA
				{ "Bocas del Toro", "Chiriqu�", "Cocl�", "Col�n", "Dari�n",
						"Ember�", "Herrera", "Kuna de Madungand�",
						"Kuna de Wargand�", "Kuna Yala", "Los Santos",
						"Ng�be Bugl�", "Panam�", "Veraguas" }
				// PAPUA NEW GUINEA
				,
				{ "Eastern Highlands", "East New Britain", "East Sepik",
						"Enga", "Fly River", "Gulf", "Madang", "Manus",
						"Milne Bay", "Morobe", "National Capital District",
						"New Ireland", "North Solomons", "Oro", "Sandaun",
						"Simbu", "Southern Highlands", "Western Highlands",
						"West New Britain" },
				// PARAGUAY

				{ "Alto Paraguay", "Alto Paran�", "Amambay", "Asunci�n",
						"Boquer�n", "Caaguaz�", "Caazap�", "Canendiy�",
						"Central", "Concepci�n", "Cordillera", "Guair�",
						"Itap�a", "Misiones", "�eembuc�", "Paraguar�",
						"Presidente Hayes", "San Pedro" },
				// PERU

				{ "Amazonas", "Ancash", "Apur�mac", "Arequipa", "Ayacucho",
						"Cajamarca", "Callao", "Cusco", "Huancavelica",
						"Hu�nuco", "Ica", "Jun�n", "La Libertad", "Lambayeque",
						"Lima Provincias", "Loreto", "Madre de Dios",
						"Moquegua", "Pasco", "Piura", "Puno", "San Mart�n",
						"Tacna", "Tumbes", "Ucayali" },
				// PHILIPPINES

				{ "Bicol", "Cagayan Valley", "Caraga", "Central Luzon",
						"Central Mindanao", "Central Visayas", "Cordillera",
						"Eastern Visayas", "Ilocos", "Muslim Mindanao",
						"National Capital Region", "Northern Mindanao",
						"Southern Mindanao", "Southern Tagalog",
						"Western Mindanao", "Western Visayas" },

				// POLAND
				{ "Dolnoslaskie", "Kujawsko-Pomorskie", "L�dzkie", "Lubelskie",
						"Lubuskie", "Malopolskie", "Mazowieckie", "Opolskie",
						"Podkarpackie", "Podlaskie", "Pomorskie", "Slaskie",
						"Swietokrzyskie", "Warminsko-Mazurskie",
						"Wielkopolskie", "Zachodnio-Pomorskie" },
				// PORTUGAL

				{ "A�ores", "Alentejo", "Algarve", "Centro",
						"Lisboa e Vale do Tejo", "Madeira", "Norte" },
				// PUERTO RICO

				{ "Arecibo", "Bayam�n", "Carolina", "Guayama", "Humacao",
						"Mayag�ez-Aguadilla", "Ponce", "San Juan" },
				// QATAR

				{ "al-Guwayriyah", "al-H_awr", "al-Jumayliyah", "al-Wakrah",
						"ar-Rayyan", "a�-�amal", "Doha", "Jariyan al-Batnah",
						"Musay'id", "Umm Salal" },
				// REUNION
				{ "Saint-Beno�t", "Saint-Denis", "Saint-Paul", "Saint-Pierre" },
				// ROMANIA
				{ "Alba", "Arad", "Arges", "Bacau", "Bihor", "Bistrita-Nasaud",
						"Botosani", "Braila", "Brasov", "Bukarest", "Buzau",
						"Calarasi", "Caras-Severin", "Cluj", "Constanta",
						"Covasna", "D�mbovita", "Dolj", "Galati", "Giurgiu",
						"Gorj", "Harghita", "Hunedoara", "Ialomita", "Iasi",
						"Ilfov", "Maramures", "Mehedinti", "Mures", "Neamt",
						"Olt", "Prahova", "Salaj", "Satu Mare", "Sibiu",
						"Suceava", "Teleorman", "Timis", "Tulcea", "V�lcea",
						"Vaslui", "Vrancea" },
				// RUSSIA
				{ "Adygeja", "Aga", "Alanija", "Altaj", "Amur", "Arhangelsk",
						"Astrahan", "Ba�kortostan", "Belgorod", "Brjansk",
						"Burjatija", "Cecenija", "Celjabinsk", "Cita",
						"Cukotka", "Cuva�ija", "Dagestan", "Evenkija",
						"Gorno-Altaj", "Habarovsk", "Hakasija",
						"Hanty-Mansija", "Ingu�etija", "Irkutsk", "Ivanovo",
						"Jamalo-Nenets", "Jaroslavl", "Jevrej",
						"Kabardino-Balkarija", "Kaliningrad", "Kalmykija",
						"Kaluga", "Kamcatka", "Karacaj-Cerkessija", "Karelija",
						"Kemerovo", "Kirov", "Komi", "Komi-Permjak",
						"Korjakija", "Kostroma", "Krasnodar", "Krasnojarsk",
						"Kurgan", "Kursk", "Leningrad", "Lipeck", "Magadan",
						"Marij El", "Mordovija", "Moskau",
						"Moskovskaja Oblast", "Murmansk", "Nenets",
						"Ni�nij Novgorod", "Novgorod", "Novosibirsk", "Omsk",
						"Orenburg", "Orjol", "Penza", "Perm", "Primorje",
						"Pskov", "Rjazan", "Rostov", "Saha", "Sahalin",
						"Samara", "Sankt Petersburg", "Saratov", "Smolensk",
						"Stavropol", "Sverdlovsk", "Tajmyr", "Tambov",
						"Tatarstan", "Tjumen", "Tomsk", "Tula", "Tver", "Tyva",
						"Udmurtija", "Uljanovsk", "Ust-Orda", "Vladimir",
						"Volgograd", "Vologda", "Vorone�" },

				// RWANDA
				{ "Butare", "Byumba", "Cyangugu", "Gikongoro", "Gisenyi",
						"Gitarama", "Kibungo", "Kibuye", "Ruhengeri",
						"Ville de Kigali" },
				// SAINT HELENA
				{ "Ascension", "Saint Helena", "Tristan da Cunha" },
				// SAINT KITTS AND NEVIS
				{ "Christ Church Nichola Town", "Saint Anne Sandy Point",
						"Saint George Basseterre", "Saint George Gingerland",
						"Saint James Windward", "Saint John Capesterre",
						"Saint John Figtree", "Saint Mary Cayon",
						"Saint Paul Capesterre", "Saint Paul Charlestown",
						"Saint Peter Basseterre", "Saint Thomas Lowland",
						"Saint Thomas Middle Island", "Trinity Palmetto Point" },
				// SAINT LUCIA
				{ "Anse-la-Raye", "Canaries", "Castries", "Choiseul",
						"Dennery", "Gros Inlet", "Laborie", "Micoud",
						"Soufri�re", "Vieux Fort" },
				// SAINT PIERRE AND MIQUELON
				{ "Miquelon-Langlade", "Saint-Pierre" },
				// SAINT VINCENT AND THE GRENADINES
				{ "Charlotte", "Grenadines", "Saint David", "Saint George" },
				// SAMOA
				{ "Apia Urban Area", "North West Upolu", "Rest of Upolu",
						"Savaii" },
				// SAN MARINO
				{ "Acquaviva", "Borgo Maggiore", "Chiesanuova", "Domagnano",
						"Faetano", "Fiorentino", "Montegiardino", "San Marino",
						"Serravalle" },
				// SAO TOME AND PRINCIPE
				{ "�gua Grande", "Cantagalo", "Cau�", "Lemba", "Lobata",
						"M�-Zochi", "Pagu�" },
				// SAUDI ARABIA
				{ "al-Bah?ah", "al-H?udud-a�-�amaliyah", "al-Jawf",
						"al-Madinah", "al-Qasim", "'Asir", "a�-�arqiyah",
						"H?a'il", "Jizan", "Makkah", "Najran", "Riad", "Tabuk" },
				// SENEGAL
				{ "Dakar", "Diourbel", "Fatick", "Kaolack", "Kolda", "Louga",
						"Matam", "Saint-Louis", "Tambacounda", "Thi�s",
						"Ziguinchor" },
				// SERBIA AND MONTENEGRO
				{ "Central Serbia", "Vojvodina" },
				// SEYCHELLES
				{ "Mah�" },
				// SIERRA LEONE
				{ "Eastern", "Northern", "Southern", "Western" },
				// SINGAPORE
				{ " " },
				// SLOVAKIA
				{ "Banskobystrick�", "Bratislavsk�", "Ko�ick�", "Nitriansk�",
						"Pre�ovsk�", "Trenciansk�", "Trnavsk�", "�ilinsk�" },
				// SLOVENIA
				{ "Gorenjska", "Gori�ka", "Jugov�odna Slovenija", "Koro�ka",
						"Notranjsko-kra�ka", "Obalno-kra�ka",
						"Osrednjeslovenska", "Podravska", "Pomurska",
						"Savinjska", "Spodnjeposavska", "Zasavska" },
				// SOLOMON ISLANDS
				{ "Central", "Choiseul", "Guadalcanal", "Isabel",
						"Makira and Ulawa", "Malaita", "Rennell and Bellona",
						"Temotu", "Western" },
				// SOMALIA
				{ "Awdaal", "Baakool", "Baarii", "Baay", "Banaadir",
						"Gaalguuduud", "Gedoo", "Hiiraan", "Jubbada Dhexe",
						"Jubbada Hoose", "Mudug", "Nuugaal", "Sanaag",
						"Shabeellaha Dhexe", "Shabeellaha Hoose", "Sool",
						"Togdeer", "Woqooyi Galbeed" },
				// SOUTH AFRICA
				{ "Eastern Cape", "Free State", "Gauteng", "KwaZulu Natal",
						"Limpopo", "Mpumalanga", "Northern Cape", "North West",
						"Western Cape" },
				// SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS
				{},
				// SPAIN
				{ "Andalusien", "Aragonien", "Asturien", "Balearen",
						"Baskenland", "Ceuta", "Extremadura", "Galizien",
						"Kanaren", "Kantabrien", "Kastilien-La Mancha",
						"Kastilien-Le�n", "Katalonien", "La Rioja", "Madrid",
						"Melilla", "Murcia", "Navarra", "Valencia" },
				// SRI LANKA

				{ "Amparai", "Anuradhapuraya", "Badulla", "Colombo", "Galla",
						"Gampaha", "Hambantota", "Kalatura", "Kegalla",
						"Kilinochchi", "Kurunegala", "Mad?akalpuwa",
						"Maha Nuwara", "Mannarama", "Matale", "Matara",
						"Monaragala", "Nuwara Eliya", "Puttalama",
						"Ratnapuraya", "Tirikunamalaya", "Vavuniyawa",
						"Yapanaya" },
				// SUDAN

				{ "A'ali-an-Nil", "al-Bah?r-al-Ah?mar", "al-Buh?ayrat",
						"al-Jazirah", "al-Qad?arif", "al-Wah?dah",
						"an-Nil-al-Abyad?", "an-Nil-al-Azraq", "a�-�amaliyah",
						"Bah?r-al-Jabal", "Garb-al-Istiwa'iyah",
						"Garb Bah?r-al-Gazal", "Garb Darfur", "Garb Kurdufan",
						"Janub Darfur", "Janub Kurdufan", "Junqali", "Kassala",
						"Khartum", "Nahr-an-Nil", "�amal Bah?r-al-Gazal",
						"�amal Darfur", "�amal Kurdufan",
						"�arq-al-Istiwa'iyah", "Sinnar", "Warab" },
				// SURINAME
				{ "Brokopondo", "Commewijne", "Coronie", "Marowijne",
						"Nickerie", "Para", "Paramaribo", "Saramacca", "Wanica" },
				// SVALBARD
				{ "Jan Mayen", "Svalbard" },
				// SWAZILAND
				{ "Hhohho", "Lubombo", "Manzini", "Shiselweni" },
				// SWEDEN
				{ "Blekinge", "Dalarna", "G�vleborg", "Gotland", "Halland",
						"J�mtland", "J�nk�ping", "Kalmar", "Kronoberg",
						"Norrbotten", "�rebro", "�sterg�tland", "Sk�ne",
						"S�dermanland", "Stockholm", "Uppsala", "V�rmland",
						"V�sterbotten", "V�sternorrland", "V�stmanland",
						"V�stra G�taland" },
				// SWITZERLAND
				{ "Aargau", "Appenzell-Ausser Rhoden",
						"Appenzell Inner-Rhoden", "Basel-Landschaft",
						"Basel-Stadt", "Bern", "Freiburg", "Genf", "Glarus",
						"Graub�nden", "Jura", "Luzern", "Neuenburg",
						"Nidwalden", "Obwalden", "Sankt Gallen",
						"Schaffhausen", "Schwyz", "Solothurn", "Tessin",
						"Thurgau", "Uri", "Waadt", "Wallis", "Zug", "Z�rich" },

				// SYRIA
				{ "al-H?asakah", "al-Ladiqiyah", "al-Qunaytirah", "ar-Raqqah",
						"as-Suwayda", "Damaskus", "Dar'a", "Dayr-az-Zawr",
						"H?alab", "H?amah", "H?ims", "Idlib", "Tartus" },

				// TAIWAN
				{ "Changhwa", "Chiayi Hsien", "Chiayi Shih", "Hsinchu Hsien",
						"Hsinchu Shih", "Hualien", "Ilan", "Kaohsiung Hsien",
						"Kaohsiung Shih", "Keelung Shih", "Kinmen", "Miaoli",
						"Nantou", "Penghu", "Pingtung", "Taichung Hsien",
						"Taichung Shih", "Tainan Hsien", "Tainan Shih",
						"Taipei Hsien", "Taitung", "Taoyuan", "Yunlin" }
				// TAJIKISTAN
				,
				{ "Dushanbe", "Gorno-Badakhshan", "Karotegin", "Khatlon",
						"Sughd" }
				// TANZANIA
				,
				{ "Arusha", "Dar es Salaam", "Dodoma", "Iringa", "Kagera",
						"Kigoma", "Kilimanjaro", "Lindi", "Manyara", "Mara",
						"Mbeya", "Morogoro", "Mtwara", "Mwanza", "Pwani",
						"Rukwa", "Ruvuma", "Shinyanga", "Singida", "Tabora",
						"Tanga", "Zanzibar and Pemba" }
				// THAILAND
				,
				{ "Central", "Krung Thep", "Northeastern", "Northern",
						"Southern" }
				// TIMOR-LESTE
				,
				{}
				// TOGO
				,
				{ "Centre", "Kara", "Maritime", "Plateaux", "Savanes" }
				// TOKELAU
				,
				{ "Atafu", "Fakaofo", "Nukunonu" }
				// TONGA
				,
				{ "Eua", "Ha'apai", "Niuas", "Tongatapu", "Vava'u" }
				// TRINIDAD AND TOBAGO
				,
				{ "Arima", "Chaguanas", "Couva-Tabaquite-Talparo",
						"Diego Mart�n", "Mayaro-R�o Claro", "Pe�al D�b�",
						"Point Fort�n", "Port of Spain", "Princes Town",
						"San Fernando", "Sangre Grande",
						"San Juan-Laventville", "Siparia", "Tobago",
						"Tunapuna-Piarco" }
				// TUNISIA
				,
				{ "al-Kaf", "al-Mahdiyah", "al-Munastir", "al-Qasrayn",
						"al-Qayrawan", "Aryanah", "Bajah", "Bin 'Arus",
						"Binzart", "Jundubah", "Madaniyin", "Manubah", "Nabul",
						"Qabis", "Qafsah", "Qibili", "Safaqis", "Sidi Bu Zayd",
						"Silyanah", "Susah", "Tatawin", "Tawzar", "Tunis",
						"Zagwan" }
				// TURKEY
				,
				{ "Adana", "Adiyaman", "Afyonkarahisar", "Agri", "Aksaray",
						"Amasya", "Ankara", "Antalya", "Ardahan", "Artvin",
						"Aydin", "Balikesir", "Bartin", "Batman", "Bayburt",
						"Bilecik", "Bing�l", "Bitlis", "Bolu", "Burdur",
						"Bursa", "�anakkale", "�ankiri", "�orum", "Denizli",
						"Diyarbakir", "D�zce", "Edirne", "Elazig", "Erzincan",
						"Erzurum", "Eskisehir", "Gaziantep", "Giresun",
						"G�m�shane", "Hakkari", "Hatay", "Igdir", "Isparta",
						"Istanbul", "Izmir", "Kahramanmaras", "Karab�k",
						"Karaman", "Kars", "Kastamonu", "Kayseri", "Kilis",
						"Kirikkale", "Kirklareli", "Kirsehir", "Kocaeli",
						"Konya", "K�tahya", "Malatya", "Manisa", "Mardin",
						"Mersin", "Mugla", "Mus", "Nevsehir", "Nigde", "Ordu",
						"Osmaniye", "Rize", "Sakarya", "Samsun", "Sanliurfa",
						"Siirt", "Sinop", "Sirnak", "Sivas", "Tekirdag",
						"Tokat", "Trabzon", "Tunceli", "Usak", "Van", "Yalova",
						"Yozgat", "Zonguldak" }
				// TURKMENISTAN
				,
				{ "Ahal", "Asgabat", "Balkan", "Dasoguz", "Lebap", "Mari" }
				// TURKS AND CAICOS ISLANDS
				,
				{ "Grand Turk", "Middle Caicos", "North Caicos",
						"Providenciales and West Caicos", "Salt Cay",
						"South Caicos and East Caicos" }
				// TUVALU
				,
				{ "Funafuti", "Nanumanga", "Nanumea", "Niutao", "Nui",
						"Nukufetau", "Nukulaelae", "Vaitupu" }
				// UGANDA
				,
				{ "Central", "Eastern", "Northern", "Western" }
				// UKRAINE
				,
				{ "Central", "Eastern", "Northern", "Western" }
				// UNITED ARAB EMIRATES
				,
				{ "Abu Dhabi", "'Ajman", "al-Fujayrah", "a�-�ariqah", "Dubai",
						"Ra's al-H_aymah", "Umm al-Qaywayn" }
				// URUGUAY
				,
				{ "Artigas", "Canelones", "Cerro Largo", "Colonia", "Durazno",
						"Flores", "Florida", "Lavalleja", "Maldonado",
						"Montevideo", "Paysand�", "R�o Negro", "Rivera",
						"Rocha", "Salto", "San Jos�", "Soriano", "Tacuaremb�",
						"Treinta y Tres" }
				// UZBEKISTAN
				,
				{ "Andijon", "Buhoro", "Cizah", "Fargona", "Horazm",
						"Kaskadarya", "Korakalpogiston", "Namangan", "Navoi",
						"Samarkand", "Sirdare", "Surhondar", "Taschkent" }
				// VANUATU
				,
				{ "Malampa", "Penama", "Sanma", "Shefa", "Tafea", "Torba" }
				// VENEZUELA
				,
				{ "Amazonas", "Anzo�tegui", "Apure", "Aragua", "Barinas",
						"Bol�var", "Carabobo", "Cojedes", "Delta Amacuro",
						"Distrito Capital", "Falc�n", "Gu�rico", "Lara",
						"M�rida", "Miranda", "Monagas", "Nueva Esparta",
						"Portuguesa", "Sucre", "T�chira", "Trujillo", "Vargas",
						"Yaracuy", "Zulia" }
				// VIETNAM
				,
				{ "B?c Trung B?", "�?ng b?ng s�ng C?u Long",
						"�?ng b?ng s�ng H?ng", "��ng B?c B?", "��ng Nam B?",
						"Duy�n h?i Nam Trung B?", "T�y B?c B?", "T�y Nguy�n" }
				// VIRGIN ISLANDS
				,
				{ "Saint Croix", "Saint John", "Saint Thomas" }
				// WALLIS AND FUTUNA
				,
				{ "Alo", "Hahake", "Hihifo", "Mua", "Sigave" }
				// WESTERN SAHARA
				,
				{ "al-'Ayun", "as-Samarah", "Bu Jaydur", "Wad-ad-�ahab" }
				// YEMEN
				,
				{ "Abyan", "ad-Dali'", "Aden", "al-Bayda", "al-H?udaydah",
						"al-Jawf", "al-Mahrah", "al-Mahwit",
						"Amanah al-'Asmah", "Amran", "�amar", "Hadramaut",
						"Hajjah", "Ibb", "Lahij", "Ma'rib", "Raymah", "�abwah",
						"Sa'dah", "San'a", "Ta'izz" }
				// ZAMBIA
				,
				{ "Central", "Copperbelt", "Eastern", "Luapala", "Lusaka",
						"Northern", "North-Western", "Southern", "Western" },
				// ZIMBABWE,
				{ "Bulawayo", "Harare", "Manicaland", "Mashonaland Central",
						"Mashonaland East", "Mashonaland West", "Masvingo",
						"Matabeleland North", "Matabeleland South", "Midlands" } };

		// cities = new String[][][] {
		//
		// };

		statesList = new ArrayList<String>();
		for (int i = 0; i < states[countriesList.indexOf(country)].length; i++) {
			statesList.add(states[countriesList.indexOf(country)][i]);
		}
		return statesList;
	}
}