package com.mdc.medic.apimedic.services;

import com.mdc.medic.apimedic.beans.reponse.MessageResponse;
import com.mdc.medic.apimedic.beans.reponse.forgotpassword.ForgotPasswordStep1Response;
import com.mdc.medic.apimedic.beans.request.forgotpassword.ForgotPasswordResendRequest;
import com.mdc.medic.apimedic.beans.request.forgotpassword.ForgotPasswordStep1Request;
import com.mdc.medic.apimedic.beans.request.forgotpassword.ForgotPasswordStep2Request;
import com.mdc.medic.apimedic.config.ChangePasswordStatus;
import com.mdc.medic.apimedic.config.Status;
import com.mdc.medic.apimedic.exceptions.FunctionalException;
import com.mdc.medic.apimedic.exceptions.MedicFrontExceptionCode;
import com.mdc.medic.apimedic.models.ChangePassword;
import com.mdc.medic.apimedic.models.ChangePasswordOtp;
import com.mdc.medic.apimedic.models.User;
import com.mdc.medic.apimedic.repository.ChangePasswordOtpRepository;
import com.mdc.medic.apimedic.repository.ChangePasswordRepository;
import com.mdc.medic.apimedic.repository.UserRepository;
import com.mdc.medic.apimedic.services.utils.OtpService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ChangePasswordService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OtpService otpService;

    @Autowired
    ChangePasswordRepository changePasswordRepository;

    @Autowired
    ChangePasswordOtpRepository changePasswordOtpRepository;

    @Autowired
    PasswordEncoder encoder;


    private final Logger logger = LogManager.getLogger(getClass());


    public ForgotPasswordStep1Response forgotPasswordStep1 (ForgotPasswordStep1Request request) throws FunctionalException {
        String username=request.getUsername();
        String mail=request.getEmail();
        String phone=request.getPhoneNumber();
        logger.info("Checking if user '{}' exists and getting it  ...",username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new FunctionalException(MedicFrontExceptionCode.USER_NOT_FOUND,"USER_NOT_FOUND"));

        if(!user.getEmail().equalsIgnoreCase(mail) || !user.getPhoneNumber().equals(phone)) {
            throw new FunctionalException(MedicFrontExceptionCode.DEFAULT_ERROR, "DEFAULT_ERROR");
        }
        logger.info("Initiating Change password .....");
                ChangePassword changePassword = new ChangePassword();
                changePassword.setUser(user);
                changePassword.setStatus(ChangePasswordStatus.PENDING);
                changePassword.setAddTime(new Date());
                changePassword.setEditTime(new Date());
                changePassword.setExecutionTime(new Date());
                logger.info("Saving request to database ...");
                ChangePassword savedChangePassword = changePasswordRepository.save(changePassword);
                ChangePasswordOtp changePasswordOtp=otpService.getOtpAndSendMail(savedChangePassword);
                ForgotPasswordStep1Response forgotPasswordStep1Response =
                        new ForgotPasswordStep1Response(user.getPhoneNumber(),savedChangePassword.getId(),changePasswordOtp.getOtpId());
                return  forgotPasswordStep1Response;
    }

    public ForgotPasswordStep1Response forgotPasswordResend (ForgotPasswordResendRequest request) throws FunctionalException {

        logger.info("Checking if user '{}' exists and getting it  ...",request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new FunctionalException(MedicFrontExceptionCode.USER_NOT_FOUND,"USER_NOT_FOUND"));

        logger.info("Checking if OtpId '{}' exists ....",request.getOtpId());

        ChangePasswordOtp  changePasswordOtp = changePasswordOtpRepository.findByOtpId(request.getOtpId())
                .orElseThrow(() -> new FunctionalException(MedicFrontExceptionCode.USER_NOT_FOUND,"USER_NOT_FOUND"));

        logger.info("Checking if username '{}' is correct ... ",request.getUsername());
        if(!changePasswordOtp.getChangePassword().getUser().getUsername().equals( request.getUsername())){
            throw new FunctionalException(MedicFrontExceptionCode.DEFAULT_ERROR,"DEFAULT_ERROR");
        }

        logger.info("Checking if changing password already executed ....");
        if(changePasswordOtp.getChangePassword().getStatus().equals(ChangePasswordStatus.EXECUTED)) {
            throw new FunctionalException(MedicFrontExceptionCode.OPERATION_ALREADY_EXECUTED,"OPERATION_ALREADY_EXECUTED");
        }

        logger.info("Generating new Otp for changing password request .... ");

        ChangePasswordOtp newChangePasswordOtp=otpService.getOtpAndSendMail(changePasswordOtp.getChangePassword());

        logger.info("Setting Old Otp status to used");
        changePasswordOtp.setUsed(true);
        return new ForgotPasswordStep1Response(user.getPhoneNumber()
                ,newChangePasswordOtp.getChangePassword().getId(),newChangePasswordOtp.getOtpId());
    }


    public MessageResponse forgotPasswordStep2 (ForgotPasswordStep2Request request) throws FunctionalException {
        logger.info("Checking if OtpId exists '{}' ....",request.getOtpId());

        ChangePasswordOtp changePasswordOtp = changePasswordOtpRepository.findByOtpId(request.getOtpId())
                .orElseThrow(()->new FunctionalException(MedicFrontExceptionCode.DEFAULT_ERROR,"DEFAULT_ERROR"));

        logger.info("Checking is otp value is correct....");
        if(!encoder.matches(request.getOtpValue(),changePasswordOtp.getOtpValue())){
            throw new FunctionalException(MedicFrontExceptionCode.OTP_INCORRECT,"OTP_INCORRECT");
        }
        logger.info("Checking is otp value is expired....");
        if(changePasswordOtp.getExpireTime().compareTo(new Date())<0) {
            throw new FunctionalException(MedicFrontExceptionCode.OTP_EXPIRED,"OTP_EXPIRED");
        }
        logger.info("Checking is otp value is used....");
        if(changePasswordOtp.getUsed()) {
            throw new FunctionalException(MedicFrontExceptionCode.OTP_USED,"OTP_USED");
        }

        User user = changePasswordOtp.getChangePassword().getUser();

        logger.info("Setting new Password");
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);
        logger.info("Setting Change password otp to used ...");
        changePasswordOtp.setUsed(true);
        changePasswordOtpRepository.save(changePasswordOtp);
        logger.info("Setting change password request to Executed");
        ChangePassword changePassword = changePasswordOtp.getChangePassword();
        changePassword.setStatus(ChangePasswordStatus.EXECUTED);
        changePassword.setExecutionTime(new Date());
        changePassword.setEditTime(new Date());
        changePasswordRepository.save(changePassword);
        return new MessageResponse("Password Changed Successfully", Status.PASSWORD_CHANGED_SUCCESSFULLY);
    }
}
