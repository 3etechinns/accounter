/**
 * 
 */
package com.vimukti.accounter.web.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.workspace.tool.AccounterException;

/**
 * @author Fernandez
 * 
 */
public interface IAccounterCRUDService extends RemoteService {

	long create(IAccounterCore coreObject) throws AccounterException;

	long update(IAccounterCore coreObject) throws AccounterException;

	boolean delete(AccounterCoreType type, long stringID)
			throws AccounterException;

	long updateCompanyPreferences(ClientCompanyPreferences preferences)
			throws AccounterException;

	Boolean voidTransaction(AccounterCoreType accounterCoreType, long id)
			throws AccounterException;

	boolean deleteTransaction(AccounterCoreType accounterCoreType, long stringID)
			throws AccounterException;

	boolean canEdit(AccounterCoreType accounterCoreType, long stringID)
			throws AccounterException;

	long updateCompany(ClientCompany clientCompany) throws AccounterException;

}
