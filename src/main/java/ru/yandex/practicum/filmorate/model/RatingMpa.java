package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.stereotype.Component;


@Component
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class RatingMpa {
    private int id;
    private String name;
}
