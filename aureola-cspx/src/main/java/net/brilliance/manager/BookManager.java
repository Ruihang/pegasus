/**
 * 
 */
package net.brilliance.manager;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import net.brilliance.exceptions.ExecutionContextException;
import net.brilliance.framework.manager.ManagerBase;
import net.brilliance.framework.model.ExecutionContext;

/**
 * @author ducbq
 *
 */
@Service
public class BookManager extends ManagerBase {
	@Inject
	private TaskExecutor taskExecutor;

	@Inject
	private ApplicationContext applicationContext;

	/**
	 * 
	 */
	private static final long serialVersionUID = -3264276390290269705L;

	public void deploy(ExecutionContext context) throws ExecutionContextException{
		BookRunnable runnableObject = applicationContext.getBean(BookRunnable.class, context);
		taskExecutor.execute(runnableObject);
	}
}
