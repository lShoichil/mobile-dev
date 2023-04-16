package edu.mobiledev.controller;

import javax.validation.*;

import edu.mobiledev.dto.*;
import edu.mobiledev.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.modelmapper.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@AllArgsConstructor
@Tag(name = "Избранные пользователи")
@Slf4j
public class FavoriteController {

    private final UserService userService;

    private final FavoriteService favoriteService;

    private final ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    @Operation(
        summary = "Получить избранных пользователя",
        description = "Получить список избранных пользователя по его id"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public FavoriteResponseDto getFavoritesByUser(@Valid @PathVariable Long userId) {
        log.debug("Попытка получения избранных пользователя с id: " + userId);
        var user = userService.getUserById(userId);
        var favorites = favoriteService.getFavoritesByUserId(user.getId());
        var favoriteDtos = favorites.stream()
            .map(f -> {
                var favorite = modelMapper.map(f.getFavoriteUser(), UserResponseDto.class);
                favorite.setFavorite(true);

                return favorite;
            })
            .toList();

        var response = new FavoriteResponseDto(favoriteDtos);
        log.debug("Список избранных пользователя с id " + userId + " :" + response);
        return response;
    }

    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    @Operation(
        summary = "Добавить пользователя в избранные",
        description = "Требуется указаться id (favoriteId) пользователя, " +
                      "который будет добавлен в избранные к пользователю с id (userId)"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{favoriteId}/users/{userId}")
    public void addToFavorite(
        @Valid @PathVariable Long favoriteId,
        @Valid @PathVariable Long userId
    ) {
        log.debug("Попытка добавления пользователя с id " + favoriteId +
                  " в избранные, пользователю с id " + userId);
        var user = userService.getUserById(userId);
        var favorite = userService.getUserById(favoriteId);
        favoriteService.createFavoriteByUser(user, favorite);
        log.debug("Пользователя с id " + favoriteId +
                  " успешно добавлен в избранные, пользователю с id " + userId);
    }

    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    @Operation(
        summary = "Удалить пользователя из избранных",
        description = "Удаление у пользователя с id userId из избранных пользователя с id favoriteId"
    )
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{favoriteId}/users/{userId}")
    public void deleteFromFavorite(
        @Valid @PathVariable Long favoriteId,
        @Valid @PathVariable Long userId
    ) {
        log.debug("Попытка удаления избранного с id " + favoriteId +
                  " из избранных пользователя с id " + userId);
        var user = userService.getUserById(userId);

        favoriteService.deleteFromFavorite(favoriteId, user.getId());
        log.debug("Попытка удаления избранного с id " + favoriteId +
                  " из избранных пользователя с id " + userId + "успешна");
    }

}
