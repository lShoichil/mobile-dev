package edu.mobiledev.model;

import java.util.*;

import javax.persistence.*;

import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;

@Data
@Builder
@Entity(name = "user")
@Table(name = "user", schema = "workflow")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
