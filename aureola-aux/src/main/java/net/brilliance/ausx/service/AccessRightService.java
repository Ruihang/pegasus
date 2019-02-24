package net.brilliance.ausx.service;

import net.brilliance.ausx.domain.security.AccessRight;
import net.brilliance.exceptions.ObjectNotFoundException;
import net.brilliance.framework.service.GenericService;

public interface AccessRightService extends GenericService<AccessRight, Long> {

	/**
	 * Get one AccessRight with the provided code.
	 * 
	 * @param code
	 *            The AccessRight code
	 * @return The AccessRight
	 * @throws ObjectNotFoundException
	 *             If no such AccessRight exists.
	 */
	AccessRight getByName(String name) throws ObjectNotFoundException;
}
