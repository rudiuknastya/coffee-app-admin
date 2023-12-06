package project.serviceImpl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import project.service.MailService;

import java.io.IOException;

@Service
public class MailServiceImpl implements MailService {
    private final TemplateEngine templateEngine;
    private final SendGrid sendGrid;

    @Value("${sender}")
    private String sender;

    public MailServiceImpl(TemplateEngine templateEngine, SendGrid sendGrid) {
        this.templateEngine = templateEngine;
        this.sendGrid = sendGrid;
    }

    private Logger logger = LogManager.getLogger("serviceLogger");
    @Async
    @Override
    public void sendToken(String token, String to, HttpServletRequest httpRequest) {
        logger.info("sendToken() - Sending token to "+to);
        Email from = new Email(sender);
        String subject = "Встановлення нового паролю";
        Email toEmail = new Email(to);
        Content content = new Content("text/html", build(token,httpRequest));
        Mail mail = new Mail(from, subject, toEmail, content);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
            logger.info("sendToken() - Token was sent");
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
    private String build(String token, HttpServletRequest httpRequest) {
        Context context = new Context();
        final String baseUrl = ServletUriComponentsBuilder.fromRequestUri(httpRequest).build().toUriString();
        logger.info("url: "+baseUrl);
        String l = baseUrl+"/changePassword?token="+token;
        context.setVariable("link", l);
        return templateEngine.process("email/emailTemplate", context);
    }
}
