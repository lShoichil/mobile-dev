package edu.mobiledev.controller;

import java.util.*;

import javax.validation.*;

import edu.mobiledev.dto.*;
import edu.mobiledev.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.modelmapper.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.core.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Tag(name = "Пользователь")
@Slf4j
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    private final FavoriteService favoriteService;

    private final ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    @Operation(
        summary = "Получить информацию о пользователе",
        description = "Получение информации о пользователе по его валидному access токену"
    )
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto me(Authentication authentication) {
        var response = modelMapper.map(authentication.getPrincipal(), UserResponseDto.class);
        log.debug("Информация о пользователе: {}", authentication.getPrincipal());
        return response;
    }

    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    @Operation(
        summary = "Получить пользователя",
        description = "Получение информации о пользователе по его id"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public UserResponseDto getUserById(@Valid @PathVariable Long userId) {
        log.debug("Попытка получения пользователя с id {}", userId);
        var user = userService.getUserById(userId);
        var isFavorite = favoriteService.checkFavoriteViewedByUserId(user.getId(), userId);

        var response = modelMapper.map(user, UserResponseDto.class);
        response.setFavorite(isFavorite);

        log.debug("Информация о пользователе с id {} : {} ", user.getId(), response);
        return response;
    }

    @Operation(
        summary = "Удалить пользователя",
        description = "Удаление пользователя из системы по его id"
    )
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{userId}")
    public void deleteUserById(@Valid @PathVariable Long userId) {
        log.debug("Попытка удаления пользователя с id: {}", userId);
        userService.deleteUserById(userId);
        log.debug("Успешно удалён пользователь с id: {}", userId);
    }

    @Operation(
        summary = "Получить все роли",
        description = "Получение списка всех возможных ролей пользователей в приложении"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/roles")
    public List<RoleResponseDto> getAllUserRoles() {
        log.debug("Попытка получения списка всех ролей");
        var roles = roleService.getAllRoles();

        var response = roles.stream()
            .map(r -> modelMapper.map(r, RoleResponseDto.class))
            .toList();

        log.debug("Получены следующие роли: {}", roles);
        return response;
    }

    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    @Operation(
        summary = "Поиск пользователей",
        description = "Получение списка пользователей, с указанием страницы, кол-ва записей на " +
                      "стрнице, значение фильтра по избранным и искомого имени " +
                      "(поиск подстроки в строке). При пустой переменной userName " +
                      "будет выведен список из всех пользователей"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/search/{userId}", params = {"userName", "page", "size"})
    public Page<UserResponseDto> searchAllUsersByName(
        @Valid @PathVariable Long userId,
        @Valid @RequestParam(value = "page", defaultValue = "0") Integer page,
        @Valid @RequestParam(value = "size", defaultValue = "10") Integer size,
        @Valid @RequestParam(value = "userName", defaultValue = "") String userName,
        @Valid @RequestParam(value = "isFavorites", defaultValue = "false") boolean isFavorites
    ) {
        log.debug("Попытка получения всех пользователей." +
                  "Поиск по имени: {}, избранные = {} от лица пользователя с id {}",
            userName, isFavorites, userId);
        var sort = Sort.by("fullName");
        var request = PageRequest.of(page, size, sort);

        var users = userService.searchUsersByName(userName, request);
        var usersWithoutCurrentUser = users.stream()
            .filter(u -> !u.getId().equals(userId))
            .toList();

        var favorites = favoriteService.getFavoritesByUserId(userId);
        var favoriteIds = favorites.stream()
            .map(f -> f.getFavoriteUser().getId())
            .toList();

        List<UserResponseDto> dtos;

        log.debug("Полученные пользователи: {}",users);
        log.debug("Избранные пользователя, сделавшего запрос: {}", favorites);
        if (isFavorites) {
            dtos = usersWithoutCurrentUser.stream()
                .filter(u -> favoriteIds.contains(u.getId()))
                .map(u -> {
                    var dto = modelMapper.map(u, UserResponseDto.class);
                    dto.setFavorite(favoriteIds.contains(u.getId()));

                    return dto;
                })
                .toList();
        } else {
            dtos = usersWithoutCurrentUser.stream()
                .map(u -> {
                    var dto = modelMapper.map(u, UserResponseDto.class);
                    dto.setFavorite(favoriteIds.contains(u.getId()));

                    return dto;
                })
                .toList();
        }

        log.debug("Полученные пользователи по поиску по имени: {}, " +
                  "избранные = {} от лица пользователя с id {}: {}",
            userName, isFavorites, userId, dtos);
        var response = new PageImpl<>(dtos);

        return response;
    }

}
