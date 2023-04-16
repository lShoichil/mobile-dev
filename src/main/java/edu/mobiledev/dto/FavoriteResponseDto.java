package edu.mobiledev.dto;

import java.util.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteResponseDto {

    private List<UserResponseDto> favorites;

}
