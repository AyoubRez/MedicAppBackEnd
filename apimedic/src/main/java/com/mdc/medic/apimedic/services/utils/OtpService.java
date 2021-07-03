package com.mdc.medic.apimedic.services.utils;

import com.mdc.medic.apimedic.exceptions.FunctionalException;
import com.mdc.medic.apimedic.models.ChangePassword;
import com.mdc.medic.apimedic.models.ChangePasswordOtp;
import com.mdc.medic.apimedic.repository.ChangePasswordOtpRepository;
import com.mdc.medic.apimedic.services.utils.mail.MailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

@Service
@Transactional
public class OtpService {
    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    ChangePasswordOtpRepository changePasswordOtpRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    MailService mailService;

    @Value("${mdc.app.changePasswordOtpExpirationMs}")
    private int changePasswordOtpExpirationMs;


    private static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    @Transactional (rollbackOn = Exception.class)
    public ChangePasswordOtp getOtpAndSendMail(ChangePassword changePassword) throws FunctionalException {
        logger.info("Start getOtp to user phoneNumber '{}'",changePassword.getUser().getPhoneNumber());

        String otp = OtpService.getRandomNumberString();
        logger.info("The otp is '{}'",otp);

        ChangePasswordOtp changePasswordOtp = new ChangePasswordOtp();

        changePasswordOtp.setChangePassword(changePassword); ;
        changePasswordOtp.setAddTime(new Date());
        changePasswordOtp.setExpireTime(new Date(System.currentTimeMillis()+changePasswordOtpExpirationMs));
        changePasswordOtp.setOtpValue(encoder.encode(otp));
        changePasswordOtp.setUsed(false);
        changePasswordOtp=changePasswordOtpRepository.save(changePasswordOtp);
        mailService.sendOtpMail(otp, Locale.FRENCH, changePassword.getUser());
        logger.info("End getOtp id = '{}'",changePasswordOtp.getOtpId());
        return changePasswordOtp;
    }
}
