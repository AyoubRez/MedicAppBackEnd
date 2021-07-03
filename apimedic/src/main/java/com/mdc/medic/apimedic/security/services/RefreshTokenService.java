package com.mdc.medic.apimedic.security.services;

import com.mdc.medic.apimedic.models.RefreshToken;
import com.mdc.medic.apimedic.models.RefreshTokenRawModel;
import com.mdc.medic.apimedic.repository.RefreshTokenRepository;
import com.mdc.medic.apimedic.repository.UserRepository;
import com.mdc.medic.apimedic.security.jwt.JwtUtils;
import com.mdc.medic.apimedic.security.jwt.exception.TokenRefreshException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${mdc.app.jwtRefreshExpirationMs}")
    private int refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public Optional<RefreshToken> findByToken(String token) {
        for (RefreshToken refreshToken:
        refreshTokenRepository.findAll()) {
            if(encoder.matches(token,refreshToken.getToken())){
                return refreshTokenRepository.findByToken(refreshToken.getToken());
            }

        }
        return null;
    }


    public RefreshTokenRawModel createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        String DCryptedRawToken=UUID.randomUUID().toString();
        refreshToken.setUser(userRepository.findById(userId).get());
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.add(Calendar.MILLISECOND,refreshTokenDurationMs);
        refreshToken.setExpiryDate( now.getTime());
        refreshToken.setToken(encoder.encode(DCryptedRawToken));
        refreshToken.setUsed(false);
        refreshToken = refreshTokenRepository.save(refreshToken);
        RefreshTokenRawModel refreshRawToken = new RefreshTokenRawModel();
        refreshRawToken.setRefreshToken(refreshToken);
        refreshRawToken.setDecryptedRefreshToken(DCryptedRawToken);
        return refreshRawToken;
    }
    public RefreshTokenRawModel iterativeCreateRefreshToken(Long userId ,Date oldExpirationDate){
        RefreshToken refreshToken = new RefreshToken();
        String DCryptedRawToken=UUID.randomUUID().toString();
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setToken(encoder.encode(DCryptedRawToken));
        refreshToken.setExpiryDate(oldExpirationDate);
        refreshToken.setUsed(false);
        refreshToken = refreshTokenRepository.save(refreshToken);

        RefreshTokenRawModel refreshRawToken = new RefreshTokenRawModel();
        refreshRawToken.setRefreshToken(refreshToken);
        refreshRawToken.setDecryptedRefreshToken(DCryptedRawToken);
        return refreshRawToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(new Date()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    public void setRefreshTokenToUsed(Long userId){
        refreshTokenRepository.setIsUsedByUserId(true,userId);
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }


}
