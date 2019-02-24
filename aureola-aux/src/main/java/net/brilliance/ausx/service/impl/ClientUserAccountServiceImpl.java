/*package net.brilliance.ausx.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.brilliance.ausx.domain.admin.ClientUserAccount;
import net.brilliance.ausx.domain.security.UserAccount;
import net.brilliance.ausx.repository.ClientUserAccountRepository;
import net.brilliance.ausx.repository.UserAccountRepository;
import net.brilliance.ausx.service.ClientUserAccountService;
import net.brilliance.exceptions.ObjectNotFoundException;
import net.brilliance.framework.repository.BaseRepository;
import net.brilliance.framework.service.GenericServiceImpl;

@Service
public class ClientUserAccountServiceImpl extends GenericServiceImpl<ClientUserAccount, Long> implements ClientUserAccountService{
	*//**
	 * 
	 *//*
	private static final long serialVersionUID = 1601499409891229800L;
	@Inject 
	private UserAccountRepository userAccountRepository;

	@Inject 
	private ClientUserAccountRepository repository;
	
	protected BaseRepository<ClientUserAccount, Long> getRepository() {
		return this.repository;
	}

	@Override
	public ClientUserAccount getByUserSsoId(String ssoId) throws ObjectNotFoundException {
		UserAccount userAccount = userAccountRepository.findBySsoId(ssoId);
		return (ClientUserAccount)super.getOptionalObject(repository.findByUserAccount(userAccount));
	}

	@Override
	public ClientUserAccount getByUserAccount(UserAccount userAccount) throws ObjectNotFoundException {
		return repository.findByUserAccount(userAccount).orElse(null);
	}
}
*/