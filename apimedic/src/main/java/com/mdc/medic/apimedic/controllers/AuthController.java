package com.mdc.medic.apimedic.controllers;

import com.mdc.medic.apimedic.advice.executionTime.TrackExecutionTime;
import com.mdc.medic.apimedic.beans.reponse.MessageResponse;
import com.mdc.medic.apimedic.beans.reponse.auth.JwtResponse;
import com.mdc.medic.apimedic.beans.reponse.forgotpassword.ForgotPasswordStep1Response;
import com.mdc.medic.apimedic.beans.request.auth.LoginRequest;
import com.mdc.medic.apimedic.beans.request.auth.SignOutRequest;
import com.mdc.medic.apimedic.beans.request.auth.SignupRequest;
import com.mdc.medic.apimedic.beans.request.forgotpassword.ForgotPasswordResendRequest;
import com.mdc.medic.apimedic.beans.request.forgotpassword.ForgotPasswordStep1Request;
import com.mdc.medic.apimedic.beans.request.forgotpassword.ForgotPasswordStep2Request;
import com.mdc.medic.apimedic.exceptions.FunctionalException;
import com.mdc.medic.apimedic.services.AuthenticationService;
import com.mdc.medic.apimedic.services.ChangePasswordService;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Api(tags="Authentication")
public class AuthController extends BaseController {
    Logger logger = LogManager.getLogger(getClass());

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ChangePasswordService changePasswordService;

    @PostMapping("/login")
    @TrackExecutionTime
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws FunctionalException {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        logger.info("REST request start Login  user '{}'",username);
        JwtResponse authenticationResponse= authenticationService.authenticate(username,password);
        logger.info("Rest request end Login user '{}'",username);
        return authenticationResponse;
    }

    /*@PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String refreshTokenRequest= request.getRefreshToken();
        logger.info("REST request start generating refresh token .....");
        ResponseEntity<?> token = authenticationService.generateTokenFromRefreshToken(refreshTokenRequest);
        logger.info("Rest request end generating token from refresh token");
        return token;
    }
     */

    @PostMapping("/forgotPassword/step1")
    @TrackExecutionTime
    public ForgotPasswordStep1Response forgotPassword(@Valid @RequestBody ForgotPasswordStep1Request request) throws FunctionalException {
        String username = request.getUsername();
        String mail =request.getEmail();
        String phone = request.getPhoneNumber();
        logger.info("REST request Start Step 1 changing password for user :'{}', email :'{}',phone :'{}'",username,mail,phone);
        ForgotPasswordStep1Response forgotPasswordStep1Response = changePasswordService.forgotPasswordStep1(request);
        logger.info("REST Request End changing password Step 1 .... ");
        return forgotPasswordStep1Response;
    }

    @PostMapping("/forgotPassword/resend")
    @TrackExecutionTime
    public ForgotPasswordStep1Response resendOtp(@Valid @RequestBody ForgotPasswordResendRequest request) throws FunctionalException {
        String username = request.getUsername();
       logger.info("REST Request Start resend Otp for user '{}'",username);
        ForgotPasswordStep1Response resendResponse = changePasswordService.forgotPasswordResend(request);
        logger.info("REST Request End resend Otp for user '{}'",username);
        return resendResponse;
    }

    @PostMapping("/forgotPassword/step2")
    @TrackExecutionTime
    public MessageResponse verifyChangePassword(@Valid @RequestBody ForgotPasswordStep2Request request) throws FunctionalException {
        logger.info("REST Request Start Step 2 changing password ....");
        MessageResponse forgotPasswordStep2Response = changePasswordService.forgotPasswordStep2(request);
        logger.info("REST Request End Step 2 changing password ....");
        return forgotPasswordStep2Response;
    }

    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    @TrackExecutionTime
    public MessageResponse registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws FunctionalException {
       logger.info("REST Request Start signing user up ....");
       MessageResponse signUpResponse = authenticationService.signup(signUpRequest);
       logger.info("REST Request End signing user up ...");
       return signUpResponse;
    }


    @PostMapping("/logout")
    @TrackExecutionTime
    public MessageResponse disconnectUser(@Valid @RequestBody SignOutRequest signOutRequest) throws  FunctionalException {

      logger.info("Rest Request Start logging out user ");
      MessageResponse loggingOutResponse = authenticationService.logout(signOutRequest);
      logger.info("Rest Request End logging out user ");
      return loggingOutResponse;
    }


}
