package edu.mobiledev.model;

import javax.persistence.*;

import lombok.*;

@Data
@Builder
@Entity(name = "role_connection")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private Boolean deletingEnabled;
}
