package net.brilliance.ausx.service;

import net.brilliance.ausx.domain.admin.ClientUserAccount;
import net.brilliance.ausx.domain.security.UserAccount;
import net.brilliance.exceptions.ObjectNotFoundException;
import net.brilliance.framework.service.GenericService;

public interface ClientUserAccountService extends GenericService<ClientUserAccount, Long> {

	/**
	 * Get one ClientUserAccount with the provided UserAccount.
	 * 
	 * @param userAccount
	 *            The ClientUserAccount userAccount
	 * @return The ClientUserAccount
	 * @throws ObjectNotFoundException
	 *             If no such ClientUserAccount exists.
	 */
	ClientUserAccount getByUserAccount(UserAccount userAccount) throws ObjectNotFoundException;

	/**
	 * Get one ClientUserAccount with the provided ssoId.
	 * 
	 * @param ssoId
	 *            The ClientUserAccount ssoId
	 * @return The ClientUserAccount
	 * @throws ObjectNotFoundException
	 *             If no such ClientUserAccount exists.
	 */
	ClientUserAccount getByUserSsoId(String ssoId) throws ObjectNotFoundException;
}
