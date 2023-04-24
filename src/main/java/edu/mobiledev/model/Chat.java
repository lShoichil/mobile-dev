package edu.mobiledev.model;

import javax.persistence.*;

import lombok.*;

@Data
@Builder
@Entity(name = "chat")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne()
    @JoinColumn(name = "type_id")
    private ChatType chatType;

}
