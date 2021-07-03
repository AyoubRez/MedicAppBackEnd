package com.mdc.medic.apimedic.services.utils.mail;

import com.mdc.medic.apimedic.exceptions.FunctionalException;
import com.mdc.medic.apimedic.exceptions.MedicFrontExceptionCode;
import com.mdc.medic.apimedic.models.Mail;
import com.mdc.medic.apimedic.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@Transactional(dontRollbackOn = Exception.class)
@PropertySource(value = {"${com.mdc.medic.smtp.config}", "smtp.properties"}, ignoreResourceNotFound = true)
public class MailService {
    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${mail.template.sender}")
    private String sender;

    @Value("${mail.template.sendermail}")
    private String senderMail;

    @Value("${mail.template.otpSubject}")
    private String otpSubject;

    @Value("${mail.template.linkSubject}")
    private String linkSubject;

    @Value("${mail.template.addAgentSubject}")
    private String addAgentSubject;

    @Value("${mail.template.newAccountSubject}")
    private String newAccountSubject;


    public void sendEmail(Mail mail, String template) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(mail.getProps());

        String html = templateEngine.process(template, context);
        helper.setTo(mail.getMailTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom(),sender);
        javaMailSender.send(message);
    }



    public void sendOtpMail(String otp, Locale locale, User user) throws FunctionalException {
        try {
        logger.info("START... Sending email");

        Mail mail = new Mail();
        mail.setFrom(senderMail);
        mail.setMailTo(user.getEmail());
        mail.setSubject(otpSubject);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", user.getName());
        model.put("otp", otp);
        model.put("sign", sender);
        mail.setProps(model);

        sendEmail(mail,"otptemplate");
        logger.info("END... Email sent success");
        } catch (Exception e) {
            throw new FunctionalException(MedicFrontExceptionCode.MAIL_NOT_SENT,e.getMessage());
        }

    }

    public void sendNewMedicAccountCreationMail( User user,String decryptedPassword, Locale locale) throws FunctionalException {
        try {
            logger.info("START... Sending email");

            Mail mail = new Mail();
            mail.setFrom(senderMail);
            mail.setMailTo(user.getEmail());
            mail.setSubject(newAccountSubject);

            Map<String, Object> model = new HashMap<String, Object>();
            model.put("name", user.getName());
            model.put("username", user.getUsername());
            model.put("password", decryptedPassword);
            model.put("sign", sender);
            mail.setProps(model);

            sendEmail(mail,"newmedicaccountcreatedtemplate");
            logger.info("END... Email sent success");
        } catch (Exception e) {
            throw new FunctionalException(MedicFrontExceptionCode.MAIL_NOT_SENT,e.getMessage());
        }

    }



}
