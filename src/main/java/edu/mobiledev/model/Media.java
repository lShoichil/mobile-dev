package edu.mobiledev.model;

import javax.persistence.Entity;
import javax.persistence.*;

import lombok.*;

@Data
@Builder
@Entity(name = "media")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String link;
}

