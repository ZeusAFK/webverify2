package tasks;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import utils.StringUtils;
import data.models.Site;
import data.models.SiteVerificationResult;

public class EmailSiteVerificationResultTask {

	private SiteVerificationResult result;
	private Site site;

	public EmailSiteVerificationResultTask(Site site, SiteVerificationResult result) {
		this.result = result;
		this.site = site;
	}

	public boolean send() {
		final String username = "";
		final String password = "";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(username));
			message.setSubject("webverify2: Site verification result for " + site.getUrl());
			message.setText("Verification completed for " + site.getUrl() + ", links found: " + result.getLinks() + ", created: " + result.getCreated()
					+ ", modified: " + result.getModified() + ", deleted: " + result.getDeleted());
			Transport.send(message);

			StringUtils.printInfo("Mail sended to: " + username);

			return true;
		} catch (AddressException e) {
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
}
