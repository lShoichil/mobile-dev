package edu.mobiledev.controller;

import javax.validation.*;

import edu.mobiledev.dto.*;
import edu.mobiledev.exception.*;
import edu.mobiledev.jwt.*;
import edu.mobiledev.model.*;
import edu.mobiledev.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.modelmapper.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Аутентификация")
@Slf4j
public class AuthController {

    private final RefreshTokenService refreshTokenService;

    private final JwtHelper jwtHelper;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final RoleService roleService;

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder encoder;

    private static final String INVALID_TOKEN_MESSAGE = "Невалидный токен";

    @Operation(
        summary = "Аутентифицироваться",
        description = "Аутентифицироваться по логину и паролю"
    )
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public TokenDto login(@Valid @RequestBody LoginDto loginDto) {
        log.debug("Попытка аутентификации пользователя: " + loginDto.getLogin());
        var user = userService.getUserByLogin(loginDto.getLogin());
        var password = loginDto.getPassword();

        if (!encoder.matches(password, user.getPassword())) {
            throw new JwtException("Неверный пароль");
        }

        var accessToken = jwtHelper.generateAccessToken(user);
        var refreshTokenString = refreshTokenService.getNewRefreshToken(user);

        var response = TokenDto.builder()
            .userId(user.getId())
            .role(user.getRole().getName())
            .accessToken(accessToken)
            .refreshToken(refreshTokenString)
            .build();

        log.debug("Новая пара токенов: " + response);
        return response;
    }

    @Operation(
        summary = "Создать пользователя",
        description = "Создать пользователя, указав все необходимые поля"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public TokenDto signup(@Valid @RequestBody UserCreateDto userCreateDto) {
        log.debug("Попытка создания пользователя со следующими данными: "
                  + userCreateDto);
        var role = roleService.getRoleById(userCreateDto.getRole());
        var user = modelMapper.map(userCreateDto, User.class);

        user.setRole(role);
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        userService.createUser(user);

        var accessToken = jwtHelper.generateAccessToken(user);
        var refreshTokenString = refreshTokenService.getNewRefreshToken(user);

        var response = TokenDto.builder()
            .userId(user.getId())
            .role(user.getRole().getName())
            .accessToken(accessToken)
            .refreshToken(refreshTokenString)
            .build();

        log.debug("Новая пара токенов для нового пользователя: " + response);
        return response;
    }

    @Operation(
        summary = "Выход с текущего устройства по refresh-token",
        description = "Удаление текушего токена из БД с токенами"
    )
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout/{refreshTokenString}")
    public void logout(@PathVariable String refreshTokenString) {
        var user = refreshTokenService.getUserByRefreshToken(refreshTokenString);
        log.debug(
            "Попытка выхода с устройства по refresh-token-у (удаление этого токена из БД) " +
            "для пользователя " + user.getLogin());
        if (refreshTokenService.checkValidateAndExistRefreshToken(refreshTokenString)) {
            log.debug("Попытка выхода с устройства по refresh-token-у удачна " +
                      "для пользователя " + user.getLogin());
            refreshTokenService.deleteRefreshTokenByString(refreshTokenString);
        } else {
            log.debug("Попытка выхода с устройства по refresh-token-у неудачна " +
                      "для пользователя " + user.getLogin());
            throw new BadCredentialsException(INVALID_TOKEN_MESSAGE);
        }
    }

    @Operation(
        summary = "Выход со всех устройств по refresh-token",
        description = "Удаление всех токенов из БД по пользователю введённого токена"
    )
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logoutAll/{refreshTokenString}")
    public void logoutAll(@PathVariable String refreshTokenString) {
        var user = refreshTokenService.getUserByRefreshToken(refreshTokenString);
        log.debug(
            "Попытка логаута (удаление всех рефреш токенов пользователя) по реф.токену " +
            "для пользователя " + user.getLogin());
        if (refreshTokenService.checkValidateAndExistRefreshToken(refreshTokenString)) {
            log.debug("Попытка логаута по реф.токену успешна для пользователя " + user.getLogin());
            refreshTokenService.deleteAllRefreshTokenByString(refreshTokenString);
        } else {
            log.debug("Попытка логаута по реф.токену неуспешна для пользователя " + user.getLogin());
            throw new BadCredentialsException(INVALID_TOKEN_MESSAGE);
        }
    }

    @Operation(
        summary = "Получить новый access-token по refresh-token",
        description = "По refresh токену получить новый access токен"
    )
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/access-token/{refreshTokenString}")
    public TokenDto accessToken(@PathVariable String refreshTokenString) {
        var user = refreshTokenService.getUserByRefreshToken(refreshTokenString);
        log.debug("Попытка обновление access токена по refresh токену " +
                  "для пользователя " + user.getLogin());
        if (refreshTokenService.checkValidateAndExistRefreshToken(refreshTokenString)) {
            var newAccessToken = jwtHelper.generateAccessToken(user);

            var response = TokenDto.builder()
                .userId(user.getId())
                .role(user.getRole().getName())
                .accessToken(newAccessToken)
                .refreshToken(refreshTokenString)
                .build();

            log.debug("Новая пара токенов (с обновлённым access токеном): " + response);
            return response;
        }
        log.debug("Попытка обновление access токена по refresh токену неуспешна " +
                  "для пользователя " + user.getLogin());
        throw new BadCredentialsException(INVALID_TOKEN_MESSAGE);
    }

    @Operation(
        summary = "Обновить оба токена по refresh-token (возможно не валидному)",
        description = "Получить новую пару токенов по refresh токену"
    )
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh-token/{refreshTokenString}")
    public TokenDto refreshToken(@PathVariable String refreshTokenString) {
        var user = refreshTokenService.getUserByRefreshToken(refreshTokenString);
        log.debug("Попытка обновление обоих токенок по refresh токену " +
                  "для пользователя " + user.getLogin());
        if (refreshTokenService.checkValidateAndExistRefreshToken(refreshTokenString)) {
            refreshTokenService.deleteRefreshTokenByString(refreshTokenString);

            var accessToken = jwtHelper.generateAccessToken(user);
            var newRefreshTokenString = refreshTokenService.getNewRefreshToken(user);

            var response = TokenDto.builder()
                .userId(user.getId())
                .role(user.getRole().getName())
                .accessToken(accessToken)
                .refreshToken(newRefreshTokenString)
                .build();
            log.debug("Новая пара токенов: " + response);
            return response;
        }
        log.debug("Попытка обновление обоих токенок по refresh токену неуспешна " +
                  "для пользователя " + user.getLogin());
        throw new BadCredentialsException(INVALID_TOKEN_MESSAGE);
    }

}
