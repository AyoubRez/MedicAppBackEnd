package com.mdc.medic.apimedic.services;

import com.mdc.medic.apimedic.beans.reponse.MessageResponse;
import com.mdc.medic.apimedic.beans.reponse.auth.JwtResponse;
import com.mdc.medic.apimedic.beans.request.auth.SignOutRequest;
import com.mdc.medic.apimedic.beans.request.auth.SignupRequest;
import com.mdc.medic.apimedic.config.Status;
import com.mdc.medic.apimedic.exceptions.FunctionalException;
import com.mdc.medic.apimedic.exceptions.MedicFrontExceptionCode;
import com.mdc.medic.apimedic.models.*;
import com.mdc.medic.apimedic.repository.*;
import com.mdc.medic.apimedic.security.jwt.JwtUtils;
import com.mdc.medic.apimedic.security.services.RefreshTokenService;
import com.mdc.medic.apimedic.security.services.UserDetailsImpl;
import com.mdc.medic.apimedic.services.utils.mail.MailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AuthenticationService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SectorRepository sectorRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BlackListTokenRepository blackListTokenRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    MailService mailService;



    private final Logger logger = LogManager.getLogger(getClass());

    public JwtResponse authenticate (String username, String password) throws AuthenticationException, FunctionalException {

        User user  = userRepository.findByUsername(username)
                .orElseThrow(() -> new FunctionalException(MedicFrontExceptionCode.BAD_CREDENTIALS,"BAD_CREDENTIALS"));
        if(user.getDeleted()||! user.getEnabled()) {
            throw new FunctionalException(MedicFrontExceptionCode.USER_DELETED_OR_DISABLED, "USER_DELETED_OR_DISABLED");
        }
        try {

            logger.info("Start authenticating user '{}' ....", username);
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            logger.info("Start Generating Jwt token...... ");
            String jwt = jwtUtils.generateJwtToken(userDetails);

            logger.info("End generating Jwt token ....");

            logger.info("Getting user roles .... ");

            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            refreshTokenService.deleteByUserId(userDetails.getId());
            RefreshTokenRawModel refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            logger.info("setting user new logging time  '{}'",new Date());
            user.setLastLoginTime(new Date());
            logger.info("Flushing data to database .... ");
            userRepository.save(user);
            return new JwtResponse(jwt, refreshToken.getDecryptedRefreshToken(), userDetails.getId(),
                    userDetails.getUsername(), userDetails.getName(), userDetails.getPhoneNumber(), userDetails.getEmail(), roles, userDetails.getSector());
        }catch (AuthenticationException e){
            if(e.getMessage()=="Bad credentials"){
               throw new FunctionalException(MedicFrontExceptionCode.BAD_CREDENTIALS,"BAD_CREDENTIALS");
            }
            throw new FunctionalException(MedicFrontExceptionCode.DEFAULT_ERROR,"DEFAULT_ERROR");
        }
    }

   /* public ResponseEntity<?> generateTokenFromRefreshToken (String refreshToken) {
        logger.info("Checking if token exist or used ...");
        Calendar oldExpirationDate = Calendar.getInstance();
        if(refreshTokenService.findByToken(refreshToken).isPresent() &&  refreshTokenService.findByToken(refreshToken).get().getUsed()){
            logger.info("Deleting old refresh token ....");
            refreshTokenService.deleteByUserId(refreshTokenService.findByToken(refreshToken).get().getUser().getId());
        }
        logger.info("Generating new token from refreshToken and generating new refresh token .... ");
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    oldExpirationDate.setTime(refreshTokenService.findByToken(refreshToken).get().getExpiryDate());
                    refreshTokenService.setRefreshTokenToUsed(user.getId());
                    RefreshTokenRawModel  newRefreshToken=refreshTokenService.iterativeCreateRefreshToken(user.getId(),oldExpirationDate.getTime());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, newRefreshToken.getDcryptedRefreshToken()));
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken,
                        "Refresh token is not in database!"));
    }*/

    public MessageResponse signup (SignupRequest signupRequest) throws FunctionalException {
        logger.info("Checking if Username already exists");

        Optional<User> userByUsername = userRepository.findByUsername(signupRequest.getUsername());

        if(userByUsername.isPresent()){
            logger.info("Checking if user is not deleted....");
            if(!userByUsername.get().getDeleted()){
                throw new FunctionalException(MedicFrontExceptionCode.USERNAME_ALREADY_USED,"USERNAME_ALREADY_USED");
            }
            throw new FunctionalException(MedicFrontExceptionCode.USER_CAN_BE_RECOVERED_BY_USERNAME,"USER_CAN_BE_RECOVERED_BY_USERNAME");
        }

        logger.info("Checking if email already exists");

        Optional<User> userByEmail = userRepository.findByEmail(signupRequest.getEmail());

        if(userByEmail.isPresent()){
            logger.info("Checking if user is not deleted....");
            if(!userByEmail.get().getDeleted()){
                throw new FunctionalException(MedicFrontExceptionCode.MAIL_ALREADY_USED,"MAIL_ALREADY_USED");
            }
            throw new FunctionalException(MedicFrontExceptionCode.USER_CAN_BE_RECOVERED_BY_EMAIL,"USER_CAN_BE_RECOVERED_BY_EMAIL");
        }

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new FunctionalException(MedicFrontExceptionCode.ROLE_NOT_FOUND,"ROLE_NOT_FOUND"));
            roles.add(userRole);
        } else {
            for (String role : strRoles)
            {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new FunctionalException(MedicFrontExceptionCode.ROLE_NOT_FOUND,"ROLE_NOT_FOUND"));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new FunctionalException(MedicFrontExceptionCode.ROLE_NOT_FOUND,"ROLE_NOT_FOUND"));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new FunctionalException(MedicFrontExceptionCode.ROLE_NOT_FOUND,"ROLE_NOT_FOUND"));
                        roles.add(userRole);
                }

            }
        }

        logger.info("Checking bank code if user role is user....");
        Optional<Sector> sector = sectorRepository.findBySectorCode(signupRequest.getSectorCode());
            if(signupRequest.getSectorCode().isEmpty()||!sector.isPresent()){
                throw new FunctionalException(MedicFrontExceptionCode.BANK_CODE_NOT_FOUND,"BANK_CODE_NOT_FOUND");
            }


        logger.info("Creating New User with username '{}'.... ",signupRequest.getUsername());
        // Create new user's account
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        user.setName(signupRequest.getName());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setAddTime(new Date());
        user.setEditTime(new Date());
        user.setDeleted(false);
        user.setEnabled(true);
        user.setEditable(true);
        logger.info("Setting User Roles .... ");
        user.setRoles(roles);
        logger.info("Setting user agency ... ");
        user.setSector(sector.get());
        logger.info("Saving user to database");
        userRepository.save(user);
        logger.info("Sending Mail for new GHM account creation");
        //mailService.sendNewMedicAccountCreationMail(user,signupRequest.getPassword(),Locale.FRENCH);
        logger.info("End ... Sending Mail for new GHM account creation");

        return new MessageResponse("User registered successfully!",Status.USER_REGISTERED_SUCCESSFULLY);
    }

    @Transactional
    public MessageResponse logout(SignOutRequest signOutRequest) throws FunctionalException {
        logger.info("Initiating token BlackList...");
        TokenBlackList blackListResponse = new TokenBlackList();
        logger.info("Getting User from token .... ");
        User user = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(signOutRequest.getToExpireToken()))
                .orElseThrow(() -> new FunctionalException(MedicFrontExceptionCode.USER_NOT_FOUND,"USER_NOT_FOUND"));

        blackListResponse.setRefreshToken(signOutRequest.getRefreshToken());
        blackListResponse.setToken(signOutRequest.getToExpireToken());
        blackListResponse.setUser(user);
        blackListResponse.setLogOutDate(new Date());

        logger.info("Pushing Token Black List to redis repository...");
        blackListTokenRepository.save(blackListResponse);
        refreshTokenRepository.deleteByUser(user);
        return new MessageResponse("User disconnected !", Status.USER_DISCONNECTED_SUCCESSFULLY);
    }
}
