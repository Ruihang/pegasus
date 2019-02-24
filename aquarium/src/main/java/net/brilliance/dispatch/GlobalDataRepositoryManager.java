/**
 * 
 */
package net.brilliance.dispatch;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.brilliance.common.CommonConstants;
import net.brilliance.common.CommonManagerConstants;
import net.brilliance.deployment.GlobalDeploymentManager;
import net.brilliance.domain.entity.config.Configuration;
import net.brilliance.exceptions.EcosysException;
import net.brilliance.framework.component.BaseComponent;
import net.brilliance.framework.global.GlobalAppConstants;
import net.brilliance.framework.model.ExecutionContext;
import net.brilliance.manager.auth.AuthenticationServiceManager;
import net.brilliance.manager.configuration.ConfigurationManager;
import net.brilliance.manager.system.SystemSequenceManager;
import net.brilliance.service.api.config.ConfigurationService;

/**
 * @author ducbq
 *
 */
@Component
public class GlobalDataRepositoryManager extends BaseComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7532241073050763668L;

	/*@Inject 
	private GlobalServicesHelper globalServicesHelper;*/

	@Inject
	private SystemSequenceManager systemSequenceManager;
	
	@Inject 
	private AuthenticationServiceManager authenticationServiceManager;

	@Inject
	private ConfigurationService configurationService;

	@Inject 
	private ConfigurationManager configurationManager;

	@Inject 
	private GlobalDeploymentManager globalDeploymentManager;

	public void initializeGlobalData() throws EcosysException {
		log.info("Enter GlobalDataRepositoryManager::initializeGlobalData()");
		this.performInitializeGlobalMasterData();
		log.info("Leave GlobalDataRepositoryManager::initializeGlobalData()");
	}

	protected void performInitializeGlobalMasterData() throws EcosysException {
		log.info("Enter GlobalServicesHelper::initDefaultComponents(): Initializing the default components....");
		Configuration mdxConfig = null;
		ExecutionContext executionContext = ExecutionContext.builder().build();
		try {
			log.info("Master data for system sequence is in progress. ");
			systemSequenceManager.initializeSystemSequence();

			executionContext.putContextData(CommonManagerConstants.CONFIG_GROUP_DEPLOYMENT, "project");
			globalDeploymentManager.deploy(executionContext);

			mdxConfig = configurationService.getOne(GlobalAppConstants.GLOBAL_MASTER_DATA_INITIALIZE);
			if (null != mdxConfig && CommonConstants.TRUE_STRING.equalsIgnoreCase(mdxConfig.getValue()) ){
				log.info("The global data was initialized already. ");
				return;
			}

			log.info("Master data for configuration is in progress. ");
			configurationManager.initializeMasterData();

			log.info("Master data for authentication is in progress. ");
			authenticationServiceManager.initializeMasterData();

			/*log.info("Master data for system sequence is in progress. ");
			systemSequenceManager.initializeSystemSequence();*/

			//cLog.info("Master data for employee is in progress. ");
			//employeeManager.initDefaultData();
			if (null==mdxConfig){
				mdxConfig = new Configuration();
			}

			mdxConfig.setValue(CommonConstants.TRUE_STRING);
			mdxConfig.setName(GlobalAppConstants.GLOBAL_MASTER_DATA_INITIALIZE);
			configurationService.saveOrUpdate(mdxConfig);

			log.info("Master data initialization is done. ");
		} catch (Exception e) {
			throw new EcosysException(e);
		}
		log.info("Leave GlobalServicesHelper::initDefaultComponents()");
	}
}
