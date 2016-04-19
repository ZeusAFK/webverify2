/**
Copyright 2016 ZeusAFK

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package tasks;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
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
		// TODO: Move this to config.properties file
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
		} catch (Exception e) {
			StringUtils.printWarning("Error sending email report: " + e.getMessage());
			return false;
		}
	}
}
