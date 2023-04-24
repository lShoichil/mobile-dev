package edu.mobiledev.model;

import javax.persistence.*;

import lombok.*;

@Data
@Builder
@Entity(name = "user")
@Table(name = "user", schema = "workflow")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String password;

    private String fullName;

    @ManyToOne
    @JoinColumn(name = "media_id")
    private Media avatar;

    private String phone;

    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private Boolean deleted;

}
