/**
 * 
 */
package net.brilliance.config.handler.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author ducbq
 *
 */
@WebListener
public class WebHttpSessionListener implements HttpSessionListener {
	/*@Inject 
	private LogService log;*/

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		/*if (sessionEvent.getSession().isNew()){
			log.info("New session: [" +sessionEvent.getSession().getId() + "] created at" + Calendar.getInstance().getTime());
		}*/
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		//log.info("Session: ["+ sessionEvent.getSession().getId()+ "] destroyed");
	}
}
