package edu.mobiledev.model;

import javax.persistence.*;

import lombok.*;

@Data
@Builder
@Entity(name = "role")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
