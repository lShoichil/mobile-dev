package edu.mobiledev.service;

import java.util.*;

import edu.mobiledev.jwt.*;
import edu.mobiledev.model.*;
import edu.mobiledev.repository.*;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtHelper jwtHelper;

    private final UserService userService;

    public String getNewRefreshToken(User user) {
        var refreshToken = new RefreshToken();
        refreshToken.setOwner(user);
        refreshTokenRepository.save(refreshToken);

        return jwtHelper.generateRefreshToken(
            user,
            refreshToken.getId().toString());
    }

    public Long getRefreshTokenId(String refreshTokenString) {
        return Long.valueOf(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));
    }

    @Transactional
    public Boolean checkValidateAndExistRefreshToken(String refreshTokenString) {
        var refreshTokenId = getRefreshTokenId(refreshTokenString);
        return jwtHelper.validateRefreshToken(refreshTokenString) != null &&
               refreshTokenRepository.existsById(refreshTokenId);
    }

    @Transactional
    public void deleteRefreshTokenByString(String refreshTokenString) {
        var refreshTokenId = getRefreshTokenId(refreshTokenString);
        refreshTokenRepository.deleteById(refreshTokenId);
    }

    @Transactional
    public void deleteAllRefreshTokenByString(String refreshTokenString) {
        var userId = Long.valueOf(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
        List<RefreshToken> tokens = refreshTokenRepository.findAllByOwnerId(userId);
        tokens.forEach(token -> refreshTokenRepository.deleteById(token.getId()));
    }

    @Transactional
    public User getUserByRefreshToken(String refreshTokenString) {
        var userId = Long.valueOf(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
        return userService.getUserById(userId);
    }

}
