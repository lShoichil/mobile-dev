package edu.mobiledev.service;

import java.util.*;

import edu.mobiledev.exception.*;
import edu.mobiledev.model.User;
import edu.mobiledev.repository.*;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long userId) {
        return getUndeletedUserById(userId);
    }

    public void createUser(User user) {
        var login = user.getLogin();

        if (userRepository.existsByLoginAndDeletedIsFalse(login)) {
            throw new AlreadyExistsException("Пользователь с логином "
                                             + login + " уже существует!");
        }

        userRepository.save(user);
    }

    public List<User> getAllUsersByIds(List<Long> userIds) {
        return userIds.stream()
            .map(this::getUndeletedUserById)
            .toList();
    }

    public void deleteUserById(Long userId) {
        var user = getUndeletedUserById(userId);

        user.setDeleted(true);

        userRepository.save(user);
    }

    private User getUndeletedUserById(Long userId) {
        return userRepository.findByIdAndDeletedIsFalse(userId)
            .orElseThrow(() -> new NotFoundException("Пользователь с userId "
                                                     + userId + " не найден!"));
    }

    public Page<User> searchUsersByName(String userName, Pageable pageable) {
        return userRepository
            .findByFullNameContainingIgnoreCaseAndDeletedIsFalse(userName, pageable);
    }

}
