package edu.mobiledev.model;

import javax.persistence.*;

import lombok.*;

@Data
@Builder
@Entity(name = "chat_permission")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatPermissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private Boolean deletingEnabled;

}
