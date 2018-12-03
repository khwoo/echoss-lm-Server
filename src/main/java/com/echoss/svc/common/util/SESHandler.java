package com.echoss.svc.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.echoss.svc.common.exception.ComBizException;

public class SESHandler {

	private static final Log logger = LogFactory.getLog(SESHandler.class);

	private AmazonSimpleEmailServiceClient client = null;

	/**
	 * SES 접속
	 */
	public void connect(String accessKey, String secretKey) {

		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

		client = new AmazonSimpleEmailServiceClient(credentials);
		client.setRegion(Region.getRegion(Regions.US_WEST_2));

		if(logger.isInfoEnabled()) {
			logger.info("SESHandler :: " + Regions.US_WEST_2 + "에 접속하였습니다.");
		}
	}

	public void sendText(String FROM, String SUBJECT, String BODY, String to) {
		send(FROM, SUBJECT, BODY, new String[]{to}, false);
	}

	public void sendHtml(String FROM, String SUBJECT, String BODY, String to) {
		send(FROM, SUBJECT, BODY, new String[]{to}, true);
	}

	public void sendText(String FROM, String SUBJECT, String BODY, String[] to) {
		send(FROM, SUBJECT, BODY, to, false);
	}

	public void sendHtml(String FROM, String SUBJECT, String BODY, String[] to) {
		send(FROM, SUBJECT, BODY, to, true);
	}

	public void send(String FROM, String SUBJECT, String BODY, String[] to, boolean isHtml) {
		logger.debug("Email send start");
		if(FROM == null || FROM.trim().equals("")) throw new RuntimeException("sender email is empty!!");
		if(SUBJECT == null || SUBJECT.trim().equals("")) throw new RuntimeException("subject is empty!!");
		if(BODY == null || BODY.trim().equals("")) throw new RuntimeException("body is empty!!");
		if(to == null || to.length == 0) throw new RuntimeException("destination is empty!!");

		try {
			// Construct an object to contain the recipient address.
			Destination destination = new Destination().withToAddresses(to);
			logger.debug("Email send Destination");
			// Create the subject and body of the message.
			Content subject = new Content().withData(SUBJECT);
			Content textBody = new Content().withData(BODY);
			logger.debug("Email send before withText");
			Body body = null;
			if(isHtml) {
				body = new Body().withHtml(textBody);
			}
			else {
				logger.debug("Email send before withText");
				body = new Body().withText(textBody);
				logger.debug("Email send after withText");
			}

			// Create a message with the specified subject and body.
			Message message = new Message().withSubject(subject).withBody(body);
			logger.debug("Email send after Message");
			// Assemble the email.
			SendEmailRequest request = new SendEmailRequest().withSource(FROM).withDestination(destination).withMessage(message);
			logger.debug("Email send after SendEmailRequest");
			// Send the email.
			client.sendEmail(request);
			logger.debug("Email send after sendEmail");
		} catch(Exception e) {
			logger.error("Email send fail", e);
			throw new ComBizException("email error");
		}
	}
}
