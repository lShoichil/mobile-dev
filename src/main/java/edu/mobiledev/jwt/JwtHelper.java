package edu.mobiledev.jwt;

import java.time.*;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.*;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.*;
import edu.mobiledev.exception.*;
import edu.mobiledev.model.*;
import lombok.extern.log4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
@Log4j2
public class JwtHelper {

    static final String ISSUER = "MyApp";

    @Value("#{${accessTokenExpirationMinutes} * 60 * 1000}")
    private int accessTokenExpirationMs;

    private final long refreshTokenExpirationMs;

    private final Algorithm accessTokenAlgorithm;

    private final Algorithm refreshTokenAlgorithm;

    private final JWTVerifier accessTokenVerifier;

    private final JWTVerifier refreshTokenVerifier;

    public JwtHelper(
        @Value("${accessTokenSecret}") String accessTokenSecret,
        @Value("${refreshTokenSecret}") String refreshTokenSecret,
        @Value("${refreshTokenExpirationDays}") int refreshTokenExpirationDays
    ) {
        refreshTokenExpirationMs = Duration.ofDays(refreshTokenExpirationDays).toMillis();
        accessTokenAlgorithm = Algorithm.HMAC512(accessTokenSecret);
        refreshTokenAlgorithm = Algorithm.HMAC512(refreshTokenSecret);
        accessTokenVerifier = JWT.require(accessTokenAlgorithm)
            .withIssuer(ISSUER)
            .build();
        refreshTokenVerifier = JWT.require(refreshTokenAlgorithm)
            .withIssuer(ISSUER)
            .build();
    }

    public String generateAccessToken(User user) {
        return JWT.create()
            .withIssuer(ISSUER)
            .withSubject(user.getId().toString())
            .withClaim("role", user.getRole().toString())
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusMillis(accessTokenExpirationMs))
            .sign(accessTokenAlgorithm);
    }

    public String generateRefreshToken(User user, String tokenId) {
        return JWT.create()
            .withIssuer(ISSUER)
            .withSubject(user.getId().toString())
            .withClaim("tokenId", tokenId)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusMillis(refreshTokenExpirationMs))
            .sign(refreshTokenAlgorithm);
    }

    private DecodedJWT decodedAccessToken(String token) {
        try {
            var decodedJWT = accessTokenVerifier.verify(token);
            if (decodedJWT == null) {
                throw new JwtException("Что-то не так с access token");
            }
            return accessTokenVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new JwtException("Не валидный access token", e);
        }
    }

    private DecodedJWT decodedRefreshToken(String token) {
        try {
            var decodedJWT = refreshTokenVerifier.verify(token);
            if (decodedJWT == null) {
                throw new JwtException("Что-то не так с refresh token");
            }
            return refreshTokenVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new JwtException("Не валидный refresh token", e);
        }
    }

    public String getUserIdFromAccessToken(String token) {
        return decodedAccessToken(token).getSubject();
    }

    public String getUserIdFromRefreshToken(String token) {
        return decodedRefreshToken(token).getSubject();
    }

    public String getTokenIdFromRefreshToken(String token) {
        return decodedRefreshToken(token).getClaim("tokenId").asString();
    }

    public DecodedJWT validateAccessToken(String token) {
        return decodedAccessToken(token);
    }

    public DecodedJWT validateRefreshToken(String token) {
        return decodedRefreshToken(token);
    }

}
