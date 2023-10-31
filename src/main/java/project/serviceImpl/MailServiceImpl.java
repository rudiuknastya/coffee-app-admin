package project.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import project.service.MailService;
@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public MailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");

    @Override
    public void sendToken(String token, String to) {
        logger.info("sendToken() - Sending token to "+to);
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setFrom(new InternetAddress("ruduknasta13@gmail.com"));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Встановлення нового паролю");

            message.setContent(build(token), "text/html; charset=utf-8");
            mailSender.send(message);
            logger.info("sendToken() - Token was sent");
        } catch (MessagingException e) {
            logger.warn(e.getMessage());
        }
    }
    private String build(String token) {
        Context context = new Context();
        String l = "http://localhost:8080/changePassword?token="+token;
        context.setVariable("link", l);
        return templateEngine.process("email/emailTemplate", context);
    }
}
