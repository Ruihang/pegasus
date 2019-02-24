package net.brilliance.ausx.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.brilliance.ausx.domain.security.Authority;
import net.brilliance.ausx.repository.AuthorityRepository;
import net.brilliance.ausx.service.AuthorityService;
import net.brilliance.exceptions.ObjectNotFoundException;
import net.brilliance.framework.repository.BaseRepository;
import net.brilliance.framework.service.GenericServiceImpl;

@Service
public class AuthorityServiceImpl extends GenericServiceImpl<Authority, Long> implements AuthorityService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9012963823644955684L;

	@Inject 
	private AuthorityRepository repository;
	
	protected BaseRepository<Authority, Long> getRepository() {
		return this.repository;
	}

	@Override
	public Authority getOne(String code) throws ObjectNotFoundException {
		return (Authority)super.getOptionalObject(repository.findByCode(code));
	}
}
