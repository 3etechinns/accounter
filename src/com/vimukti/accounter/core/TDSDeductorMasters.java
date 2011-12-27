package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class TDSDeductorMasters extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String deductorName;
	private String branch;
	private String flatNo;
	private String buildingName;
	private String roadName;
	private String area;
	private String city;
	private String state;
	private long pinCode;
	private boolean addressdChanged;
	private long telephoneNumber;
	private long faxNo;
	private String emailID;
	private String status;
	private String deductorType;
	private String govtState;
	private long paoCode;
	private long paoRegistration;
	private long ddoCode;
	private long ddoRegistration;
	private String ministryDeptName;
	private String ministryDeptOtherName;

	private String tanNumber;
	private String panNumber;
	private long stdCode;

	@Override
	public String getName() {
		return "TDSDeductorMasters";
	}

	@Override
	public void setName(String name) {

	}

	public String getDeductorName() {
		return deductorName;
	}

	public void setDeductorName(String deductorName) {
		this.deductorName = deductorName;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getPinCode() {
		return pinCode;
	}

	public void setPinCode(long pinCode) {
		this.pinCode = pinCode;
	}

	public boolean isAddressdChanged() {
		return addressdChanged;
	}

	public void setAddressdChanged(boolean addressdChanged) {
		this.addressdChanged = addressdChanged;
	}

	public long getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(long telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public long getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(long faxNo) {
		this.faxNo = faxNo;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeductorType() {
		return deductorType;
	}

	public void setDeductorType(String deductorType) {
		this.deductorType = deductorType;
	}

	public String getGovtState() {
		return govtState;
	}

	public void setGovtState(String govtState) {
		this.govtState = govtState;
	}

	public long getPaoCode() {
		return paoCode;
	}

	public void setPaoCode(long paoCode) {
		this.paoCode = paoCode;
	}

	public long getPaoRegistration() {
		return paoRegistration;
	}

	public void setPaoRegistration(long paoRegistration) {
		this.paoRegistration = paoRegistration;
	}

	public long getDdoCode() {
		return ddoCode;
	}

	public void setDdoCode(long ddoCode) {
		this.ddoCode = ddoCode;
	}

	public long getDdoRegistration() {
		return ddoRegistration;
	}

	public void setDdoRegistration(long ddoRegistration) {
		this.ddoRegistration = ddoRegistration;
	}

	public String getMinistryDeptName() {
		return ministryDeptName;
	}

	public void setMinistryDeptName(String ministryDeptName) {
		this.ministryDeptName = ministryDeptName;
	}

	public String getMinistryDeptOtherName() {
		return ministryDeptOtherName;
	}

	public void setMinistryDeptOtherName(String ministryDeptOtherName) {
		this.ministryDeptOtherName = ministryDeptOtherName;
	}

	public String getTanNumber() {
		return tanNumber;
	}

	public void setTanNumber(String tanNumber) {
		this.tanNumber = tanNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public long getStdCode() {
		return stdCode;
	}

	public void setStdCode(long stdCode) {
		this.stdCode = stdCode;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.TDSDEDUCTORMASTER;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

}
