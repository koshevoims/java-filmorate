package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.MinimumDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Film {
    private Long id;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @Size(max = 200, message = "Description must be less than 200 characters")
    private String description;
    @MinimumDate
    @NotNull(message = "releaseDate cannot be null")
    private LocalDate releaseDate;
    @Positive(message = "Duration cannot be negative")
    private Long duration;
    @NotNull
    private RatingMpa mpa;
    private List<Genre> genres = new ArrayList<>();
    @JsonIgnore
    private List<Long> likes = new ArrayList<>();

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("FILM_TITLE", name);
        values.put("FILM_DESCRIPTION", description);
        values.put("FILM_RELEASE_DATE", releaseDate);
        values.put("FILM_DURATION", duration);
        values.put("FILM_MPA_ID", mpa.getId());
        return values;
    }
}
