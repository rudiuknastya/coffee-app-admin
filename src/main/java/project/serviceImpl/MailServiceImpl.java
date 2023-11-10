package project.serviceImpl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import project.service.MailService;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Value("${secret-key}")
    private String SECRET_KEY;
    @Value("${sender}")
    private String sender;

    public MailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Async
    @Override
    public void sendToken(String token, String to) {
        logger.info("sendToken() - Sending token to "+to);
        Email from = new Email(sender);
        String subject = "Встановлення нового паролю";
        Email toEmail = new Email(to);
        Content content = new Content("text/html", build(token));
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(SECRET_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            logger.info("sendToken() - Token was sent");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    private String build(String token) {
        Context context = new Context();
        String l = "http://localhost:8080/changePassword?token="+token;
        context.setVariable("link", l);
        return templateEngine.process("email/emailTemplate", context);
    }
}
