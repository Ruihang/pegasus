package net.brilliance.ausx.service;

import net.brilliance.ausx.domain.security.Authority;
import net.brilliance.exceptions.ObjectNotFoundException;
import net.brilliance.framework.service.GenericService;

public interface AuthorityService extends GenericService<Authority, Long> {

	/**
	 * Get one Authority with the provided code.
	 * 
	 * @param code
	 *            The Authority code
	 * @return The Authority
	 * @throws ObjectNotFoundException
	 *             If no such Authority exists.
	 */
	Authority getOne(String code) throws ObjectNotFoundException;
}
