package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientFixedAssetHistory implements IAccounterCore {

	private long id;

	public static final String ACTION_TYPE_NOTE = "NONE";
	public static final String ACTION_TYPE_STATUS = "Status";
	public static final String ACTION_TYPE_ROLLBACK = "Rollback";
	public static final String ACTION_TYPE_CREATED = "Created";
	public static final String ACTION_TYPE_DELETED = "Deleted";
	public static final String ACTION_TYPE_REGISTERED = "Registered";
	public static final String ACTION_TYPE_SOLD = "Sold";
	public static final String ACTION_TYPE_DISPOSED = "Disposed";
	public static final String ACTION_TYPE_DISPOSAL_REVERSED = "Disposal reversed";
	public static final String ACTION_TYPE_DEPRECIATED = "Depreciation";

	String actionType;
	long actionDate;
	String details;
	String user;
	long postedJournalEntry;

	public ClientFixedAssetHistory() {
	}

	@Override
	public String getClientClassSimpleName() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.FIXEDASSETHISTORY;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	// public String getActionAsString(String actionType) {
	// if(actionType.equalsIgnoreCase(anotherString))
	//
	// switch (actionType) {
	// case 1:
	// return "Note";
	// case 2:
	// return "Status";
	// case 3:
	// return "Rollback";
	// case 4:
	// return "Created";
	// case 5:
	// return "Deleted";
	// case 6:
	// return "Registered";
	// case 7:
	// return "Sold";
	// case 8:
	// return "Disposed";
	// case 9:
	// return "Disposal Reversed";
	// case 10:
	// return "Depreciated";
	// default:
	// return "";
	//
	// }
	// }

	public long getPostedJournalEntry() {
		return postedJournalEntry;
	}

	public void setPostedJournalEntry(long postedJournalEntry) {
		this.postedJournalEntry = postedJournalEntry;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public long getActionDate() {
		return actionDate;
	}

	public void setActionDate(long actionDate) {
		this.actionDate = actionDate;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public ClientFixedAssetHistory clone() {
		return null;

	}

}
