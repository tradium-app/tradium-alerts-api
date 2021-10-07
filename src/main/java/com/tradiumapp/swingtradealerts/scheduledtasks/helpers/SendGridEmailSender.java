package com.tradiumapp.swingtradealerts.scheduledtasks.helpers;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.tradiumapp.swingtradealerts.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SendGridEmailSender {
    private static final Logger logger = LoggerFactory.getLogger(SendGridEmailSender.class);

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    public void sendEmail(User user, String subject, String message) throws IOException {
        Email from = new Email("info@tradiumapp.com", "Tradium Alert");
        Email to = new Email(user.email, user.name);
        Content content = new Content("text/html", message);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            logger.error("Error while sending email: ", ex);
            throw ex;
        }
    }
}
