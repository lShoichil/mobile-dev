package edu.mobiledev.service;

import java.util.*;

import edu.mobiledev.exception.*;
import edu.mobiledev.model.*;
import edu.mobiledev.repository.*;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
@AllArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public void createFavoriteByUser(User user, User favorite) {
        favoriteRepository.save(new Favorite(null, user, favorite));
    }

    public boolean checkFavoriteViewedByUserId(Long userId, Long viewedUserId) {
        return favoriteRepository.existsByUserIdAndFavoriteUserId(userId, viewedUserId);
    }

    public List<Favorite> getFavoritesByUserId(Long userId) {
        return favoriteRepository.findAllByUserId(userId);
    }

    @Transactional
    public void deleteFromFavorite(Long favoriteUserId, Long userId) {
        var favorite = favoriteRepository.findFavoriteByFavoriteUserIdAndUserId(
            favoriteUserId,
            userId);

        if (favorite == null) {
            throw new NotFoundException("У пользователся с id " + userId
                                        + " нет избранного пользователя с id " + favoriteUserId);
        }

        favoriteRepository.delete(favorite);
    }

}
