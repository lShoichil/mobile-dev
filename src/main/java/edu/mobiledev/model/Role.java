package edu.mobiledev.model;

import javax.persistence.*;

import lombok.*;
import org.springframework.security.core.*;

@Data
@Builder
@Entity(name = "role")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

}
