package edu.mobiledev.repository;

import java.util.*;

import edu.mobiledev.model.*;
import org.springframework.data.domain.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByLoginAndDeletedIsFalse(String login);

    Optional<User> findByIdAndDeletedIsFalse(Long id);

    boolean existsByLoginAndDeletedIsFalse(String login);

    Page<User> findByFullNameContainingIgnoreCaseAndDeletedIsFalse(String fullName, Pageable pageable);

}
