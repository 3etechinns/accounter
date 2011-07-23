package com.vimukti.accounter.web.client.core;

import com.vimukti.accounter.web.client.ui.UIUtils;

@SuppressWarnings("serial")
public class ClientUserPreferences implements IAccounterCore {

	String dashBoardPreferences = "WELCOME,BANKING_SUMMARY,PROFIT_AND_LOSS,CREDIT_OVERVIEW,DEBIT_OVERVIEW,LATEST_QUOTE,EXPENSES";
	String customerSectionViewPreferences = "NEW_CUSTOMER,SALES_ITEM,PAYMENT_RECEIVED,CASH_SALES,CREDIT_AND_REFUNDS";
	String vendorSectionViewPreferences = UIUtils.getVendorString("NEW_SUPPLIER","NEW_VENDOR")+",ITEM_PURCHASE,BILL_PAID,CASH_PURCHASE";
	String bankingSectionViewPreferences = "BANKING_SUMMARY,CHECK_ISSUED,DEPOSITE,FUND_TRANSFERED,CREDIT_CARD_CHARGES";

	public ClientUserPreferences() {

	}

	/**
	 * @return the id
	 */

	/**
	 * 
	 * @return DashBoard Preferences made last time user visited
	 */
	public String getDashBoardPreferences() {
		return dashBoardPreferences;
	}

	/**
	 * 
	 * @param dashBoardPreferences
	 */

	public void setDashBoardPreferences(String dashBoardPreferences) {
		this.dashBoardPreferences = dashBoardPreferences;
	}

	public String getCustomerSectionViewPreferences() {
		return customerSectionViewPreferences;
	}

	public void setCustomerSectionViewPreferences(
			String customerSectionViewPreferences) {
		this.customerSectionViewPreferences = customerSectionViewPreferences;
	}

	public String getVendorSectionViewPreferences() {
		return vendorSectionViewPreferences;
	}

	public void setVendorSectionViewPreferences(
			String vendorSectionViewPreferences) {
		this.vendorSectionViewPreferences = vendorSectionViewPreferences;
	}

	public String getBankingSectionViewPreferences() {
		return bankingSectionViewPreferences;
	}

	public void setBankingSectionViewPreferences(
			String bankingSectionViewPreferences) {
		this.bankingSectionViewPreferences = bankingSectionViewPreferences;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return AccounterCoreType.USER_PREFERENCES;
	}

	@Override
	public long getID(){
		return 0;
	}

	@Override
	public void setID(long id){
		// this.id=id;

	}

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return "ClientUserPreferences";
	}

}
