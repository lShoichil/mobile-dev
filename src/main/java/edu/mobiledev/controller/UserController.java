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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Tag(name = "Пользователь")
@Slf4j
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Operation(
        summary = "Получить пользователя",
        description = "Получение информации о пользователе по его id"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public UserResponseDto getUserById(@Valid @PathVariable Long userId) {
        log.debug("Попытка получения пользователя с id {}", userId);
        var user = userService.getUserById(userId);

        var response = modelMapper.map(user, UserResponseDto.class);

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
        @Valid @RequestParam(value = "userName", defaultValue = "") String userName
    ) {
        log.debug("Попытка получения всех пользователей." +
                  "Поиск по имени: {} от лица пользователя с id {}",
            userName, userId);
        var sort = Sort.by("fullName");
        var request = PageRequest.of(page, size, sort);

        var users = userService.searchUsersByName(userName, request);
        var usersWithoutCurrentUser = users.stream()
            .filter(u -> !u.getId().equals(userId))
            .toList();

        List<UserResponseDto> dtos;

        log.debug("Полученные пользователи: {}", users);
        dtos = usersWithoutCurrentUser.stream()
            .map(u -> {
                var dto = modelMapper.map(u, UserResponseDto.class);
                return dto;
            })
            .toList();

        log.debug("Полученные пользователи по поиску по имени: {}, " +
                  "от лица пользователя с id {}: {}",
            userName, userId, dtos);
        var response = new PageImpl<>(dtos);

        return response;
    }

}
