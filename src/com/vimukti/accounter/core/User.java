package com.vimukti.accounter.core;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPortletPageConfiguration;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserPermissions;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class User extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8262438863405809492L;

	// private String firstName;
	//
	// private String lastName;
	//
	// private String email;

	private String userRole;

	private Client client;

	private UserPermissions permissions;

	private boolean canDoUserManagement;

	// private String displayName;

	private boolean isAdmin;

	private boolean isDeleted;

	private boolean isActive;

	private Set<PortletPageConfiguration> portletPages = new HashSet<PortletPageConfiguration>();
	/**
	 * The full name of the user.
	 */
	// private String fullName;

	/**
	 * Preferences that a user want to set.
	 */
	private UserPreferences userPreferences = new UserPreferences();

	public User() {
		UserPreferences userPreferences = new UserPreferences();

		this.setUserPreferences(userPreferences);

	}

	public User(ClientUser clientUser) {
		// User user = new User();
		this.setId(clientUser.getID());
		// this.setFirstName(clientUser.getFirstName());
		// this.setLastName(clientUser.getLastName());
		// this.setFullName(clientUser.getFullName());
		// this.setEmail(clientUser.getEmail());
		// this.setActive(clientUser.isActive());
		this.setCanDoUserManagement(clientUser.isCanDoUserManagement());
		this.setUserRole(clientUser.getUserRole());
		this.setAdmin(clientUser.isAdmin());
		ClientUserPermissions permissions = clientUser.getPermissions();
		if (permissions != null) {
			UserPermissions userPermissions = new UserPermissions();
			userPermissions.setTypeOfBankReconcilation(permissions
					.getTypeOfBankReconcilation());
			userPermissions.setTypeOfInvoices(permissions.getTypeOfInvoices());
			userPermissions.setTypeOfExpences(permissions.getTypeOfExpences());
			userPermissions.setTypeOfSystemSettings(permissions
					.getTypeOfSystemSettings());
			userPermissions.setTypeOfViewReports(permissions
					.getTypeOfViewReports());
			userPermissions.setTypeOfPublishReports(permissions
					.getTypeOfPublishReports());
			userPermissions
					.setTypeOfLockDates(permissions.getTypeOfLockDates());
			this.setPermissions(userPermissions);
		}
	}

	// /**
	// * @return the fullName
	// */
	// public String getFullName() {
	// return fullName;
	// }

	/**
	 * @return the userPreferences
	 */
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	/**
	 * @param userPreferences
	 *            the userPreferences to set
	 */
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	// public void setFullName(String fname) {
	// this.fullName = fname;
	//
	// }

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		return true;
	}

	// public String getFirstName() {
	// return firstName;
	// }
	//
	// public void setFirstName(String firstName) {
	// this.firstName = firstName;
	// }
	//
	// public String getLastName() {
	// return lastName;
	// }
	//
	// public void setLastName(String lastName) {
	// this.lastName = lastName;
	// }
	//
	// public String getEmail() {
	// return email;
	// }
	//
	// public void setEmail(String emailId) {
	// this.email = emailId;
	// }

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public boolean isCanDoUserManagement() {
		return canDoUserManagement;
	}

	public void setCanDoUserManagement(boolean canDoUserManagement) {
		this.canDoUserManagement = canDoUserManagement;
	}

	public void setPermissions(UserPermissions permissions) {
		this.permissions = permissions;
	}

	public UserPermissions getPermissions() {
		return permissions;
	}

	public String getName() {
		return null;
		// if (getFirstName() == null && getLastName() == null)
		// return "";
		// else if (getFirstName() == null)
		// return getLastName();
		// else if (getLastName() == null)
		// return getFirstName();
		// return getFirstName() + " " + getLastName();

	}

	public ClientUser getClientUser() {
		ClientUser user = new ClientUser();
		user.setFirstName(this.getClient().getFirstName());
		user.setLastName(this.getClient().getLastName());
		user.setFullName(this.getClient().getFullName());
		user.setEmail(this.getClient().getEmailId());
		user.setCanDoUserManagement(this.isCanDoUserManagement());
		user.setUserRole(this.getUserRole());
		user.setAdmin(this.isAdmin);
		if (getPermissions() != null) {
			ClientUserPermissions userPermissions = new ClientUserPermissions();
			userPermissions.setTypeOfBankReconcilation(this.getPermissions()
					.getTypeOfBankReconcilation());
			userPermissions.setTypeOfInvoices(this.getPermissions()
					.getTypeOfInvoices());
			userPermissions.setTypeOfSystemSettings(this.getPermissions()
					.getTypeOfSystemSettings());
			userPermissions.setTypeOfViewReports(this.getPermissions()
					.getTypeOfViewReports());
			userPermissions.setTypeOfPublishReports(this.getPermissions()
					.getTypeOfPublishReports());
			userPermissions.setTypeOfLockDates(this.getPermissions()
					.getTypeOfLockDates());
			user.setPermissions(userPermissions);
		}
		Set<PortletPageConfiguration> portletPages2 = this.getPortletPages();
		Set<ClientPortletPageConfiguration> set = new HashSet<ClientPortletPageConfiguration>();
		ClientConvertUtil utils = new ClientConvertUtil();
		try {
			for (PortletPageConfiguration pc : this.getPortletPages()) {
				ClientPortletPageConfiguration clientObject = utils
						.toClientObject(pc,
								ClientPortletPageConfiguration.class);
				set.add(clientObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		user.setPortletPages(set);
		user.setID(getID());
		return user;
	}

	// public void setDisplayName(String displayName) {
	// this.displayName = displayName;
	// }
	//
	// public String getDisplayName() {
	// return displayName;
	// }

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.USER);
		ChangeTracker.put(accounterCore);
		return false;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public void setName(String name) {
		// this.displayName = name;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.USER;
	}

	public Set<PortletPageConfiguration> getPortletPages() {
		return portletPages;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

}
