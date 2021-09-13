package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.tradiumapp.swingtradealerts.models.Alert;
import com.tradiumapp.swingtradealerts.models.User;
import com.tradiumapp.swingtradealerts.repositories.AlertRepository;
import com.tradiumapp.swingtradealerts.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class SendAlertTask {
    private static final Logger logger = LoggerFactory.getLogger(SendAlertTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Scheduled(cron = "0 0 0 */1 * *")
    public void sendAlerts() throws IOException {
        List<Alert> alerts = (List<Alert>) alertRepository.findAll();
        List<User> users = (List<User>) userRepository.findAll();

        sendEmail(users.get(0));

        logger.info("The time is now {}", dateFormat.format(new Date()));
    }

    private void sendEmail(User user) throws IOException {
        Email from = new Email("info@tradiumapp.com");
        String subject = "Swing Alert is triggered.";
        Email to = new Email(user.email);
        Content content = new Content("text/plain", "this is sample alert from swing trade.");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}