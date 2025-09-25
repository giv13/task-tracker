package ru.giv13.mailer.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender sender;
    private final Configuration freemarkerConfig;

    @Value("${spring.mail.sender.email}")
    private String senderEmail;

    @Value("${spring.mail.sender.name}")
    private String senderName;

    public void sendEmail(Mail mail) throws IOException, TemplateException, MessagingException {
        Template template = freemarkerConfig.getTemplate(mail.getTemplate());
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
        sendEmail(mail.getTo(), mail.getSubject(), htmlBody);
    }

    public void sendEmail(String to, String subject, String htmlBody) throws MessagingException {
        ClassPathResource logo = new ClassPathResource("static/img/logo.png");
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(senderName + " <" + senderEmail + ">");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.addInline("logo", logo);
        sender.send(message);
    }
}
