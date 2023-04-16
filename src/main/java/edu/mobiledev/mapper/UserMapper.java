package edu.mobiledev.mapper;

import edu.mobiledev.dto.*;
import edu.mobiledev.model.*;
import org.modelmapper.*;
import org.springframework.stereotype.*;

@Component
public class UserMapper implements Mapper {

    @Override
    public void init(ModelMapper modelMapper) {
        modelMapper.createTypeMap(User.class, UserResponseDto.class)
            .setConverter(mappingContext -> {
                var user = mappingContext.getSource();
                return UserResponseDto.builder()
                    .id(user.getId())
                    .login(user.getLogin())
                    .fullName(user.getFullName())
                    .mediaId(
                        user.getAvatar() == null
                            ? null
                            : user.getAvatar().getId()
                    )
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .role(user.getRole().getName())
                    .isFavorite(false)
                    .build();
            });

        modelMapper.createTypeMap(UserCreateDto.class, User.class)
            .setConverter(mappingContext -> {
                var userCreateDto = mappingContext.getSource();
                return User.builder()
                    .login(userCreateDto.getLogin())
                    .fullName(userCreateDto.getFullName())
                    .avatar(null)
                    .phone(userCreateDto.getPhone())
                    .email(userCreateDto.getEmail())
                    .deleted(false)
                    .build();
            });
    }

}
