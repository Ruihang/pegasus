/**
 * 
 */
package net.brilliance.ausx.mailing;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import net.brilliance.ausx.domain.security.UserAccount;
import net.brilliance.ausx.model.mailing.Mail;
import net.brilliance.common.CommonUtility;
import net.brilliance.framework.logging.LogService;

/**
 * @author ducbq
 *
 */
@Service
public class EmailServiceHelper {
	@Inject 
	private LogService log;

	@Inject
	private FreeMarkerMailService mailService;

	@Inject
	private MailConfigurationProperties emailProperties;

  @Inject
  private Configuration freemarkerConfig;

	public void sendClientRegisterMail(Locale locale, UserAccount clientUser, String emailTemplate) throws Exception {
		String confirmUrl = emailProperties.getAppUrl() + "auth/confirm/" + CommonUtility.encodeHex(clientUser.getEmail());
		Mail mail = new Mail();
		mail.setMailFrom(emailProperties.getSender());
		mail.setMailTo(clientUser.getEmail());
		mail.setSubject("Client register confirmation");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("firstName", clientUser.getFirstName() + " " + clientUser.getLastName());
		model.put("email", clientUser.getEmail());
		model.put("password", clientUser.getPassword());
		model.put("location", "Sài Gòn");
		model.put("signature", emailProperties.getSenderSignature());
		model.put("confirm", confirmUrl);
		mail.setModel(model);

		this.freemarkerConfig.setClassForTemplateLoading(this.getClass(), emailProperties.getEncoding());
    Template template = freemarkerConfig.getTemplate(emailTemplate, locale);
    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());

    mail.setMessageBody(msgContent);
    
		mailService.send(mail);

		log.info("EmailServiceHelper::sendClientRegisterMail() is Done!");
	}
}
