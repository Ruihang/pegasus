package net.brilliance.ausx.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.brilliance.ausx.domain.security.AccessRight;
import net.brilliance.ausx.repository.AccessRightRepository;
import net.brilliance.ausx.service.AccessRightService;
import net.brilliance.exceptions.ObjectNotFoundException;
import net.brilliance.framework.repository.BaseRepository;
import net.brilliance.framework.service.GenericServiceImpl;

@Service
public class AccessRightServiceImpl extends GenericServiceImpl<AccessRight, Long> implements AccessRightService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7529269076903983339L;

	@Inject 
	private AccessRightRepository repository;
	
	protected BaseRepository<AccessRight, Long> getRepository() {
		return this.repository;
	}

	@Override
	public AccessRight getByName(String name) throws ObjectNotFoundException {
		return repository.findByName(name);
	}
}
