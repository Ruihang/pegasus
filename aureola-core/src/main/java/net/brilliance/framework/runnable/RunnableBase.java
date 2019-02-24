/**
 * 
 */
package net.brilliance.framework.runnable;

import javax.inject.Inject;

import net.brilliance.framework.logging.LogService;
import net.brilliance.framework.model.ExecutionContext;

/**
 * @author ducbq
 *
 */
public abstract class RunnableBase implements Runnable {
	@Inject 
	protected LogService log;

	private ExecutionContext context;

	@Override
	public void run() {
		execute();
	}

	protected void execute(){		
	}

	public ExecutionContext getContext() {
		return context;
	}

	public void setContext(ExecutionContext context) {
		if (null==this.context){
			this.context = ExecutionContext
					.builder()
					.build();
		}
		this.context.buildFromOther(context);
	}
}
