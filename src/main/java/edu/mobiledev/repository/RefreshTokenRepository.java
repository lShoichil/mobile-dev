package edu.mobiledev.repository;

import java.util.*;

import edu.mobiledev.model.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    List<RefreshToken> findAllByOwnerId(Long userId);

}
