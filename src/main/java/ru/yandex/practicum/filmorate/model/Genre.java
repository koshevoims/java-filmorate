package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.OneToMany;

@Component
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Genre {
    @OneToMany(mappedBy = "GENRES")
    private int id;
    private String name;
}
