package edu.mobiledev.repository;

import java.util.*;

import edu.mobiledev.model.*;
import org.springframework.data.domain.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

    List<Role> findAll(Sort sort);

}
