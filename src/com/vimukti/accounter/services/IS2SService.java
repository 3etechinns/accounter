/**
 * 
 */
package com.vimukti.accounter.services;

import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface IS2SService {

	public void createComapny(long companyID, String companyName,
			int companyType, ClientUser user) throws AccounterException;

	public boolean isAdmin(long companyID, String emailID);

	public void deleteUserFromCompany(long companyID, String email);

	public void deleteClientFromCompany(long serverCompanyId,
			String deletableEmail);

	public void inviteUser(long companyId, ClientUserInfo userInfo,
			String senderEmailId);

}
