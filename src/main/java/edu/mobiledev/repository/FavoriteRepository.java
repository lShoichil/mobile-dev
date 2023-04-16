package edu.mobiledev.repository;

import java.util.*;

import edu.mobiledev.model.*;
import org.springframework.data.repository.*;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {

    List<Favorite> findAllByUserId(Long userId);

    boolean existsByUserIdAndFavoriteUserId(Long userId, Long favoriteUserId);

    Favorite findFavoriteByFavoriteUserIdAndUserId(Long favoriteUserId, Long userId);

}
