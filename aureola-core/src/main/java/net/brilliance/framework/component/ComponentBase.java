/**
 * 
 */
package net.brilliance.framework.component;

import java.io.Serializable;

import javax.inject.Inject;

import net.brilliance.framework.logging.LogService;

/**
 * @author ducbq
 *
 */
public abstract class ComponentBase implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8228266753216183339L;
	//protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject 
	protected LogService log;
}
