/**
 * 
 */
package net.brilliance.manager;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.brilliance.framework.model.ExecutionContext;
import net.brilliance.framework.runnable.RunnableBase;

/**
 * @author ducbq
 *
 */
@Component
@Scope("prototype")
public class BookRunnable extends RunnableBase {

	public BookRunnable(ExecutionContext context){
		super.setContext(context);
	}

	@Override
	public void execute() {
		log.info("Start to run system sequence updating thread....");
		//System.out.println("SystemSequenceService: [" + systemSequenceService + "]");
		doExecute();
		log.info("System sequence updating thread is done");
	}

	private void doExecute(){
	}
}
