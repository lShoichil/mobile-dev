package edu.mobiledev.service;

import java.util.*;

import edu.mobiledev.exception.*;
import edu.mobiledev.model.*;
import edu.mobiledev.repository.*;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
            .orElseThrow(() -> new NotFoundException("Роль с названием " + name + " не найдена!"));
    }

    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
            .orElseThrow(() -> new NotFoundException("Роль с roleId " + roleId + " не найдена!"));
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll(Sort.by("id"));
    }

}
