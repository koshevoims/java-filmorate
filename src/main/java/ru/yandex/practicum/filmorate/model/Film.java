package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@AllArgsConstructor
public class Film {
    private long id;
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @Size(max = 200, message = "Description must be less than 200 characters")
    private String description;
    @PastOrPresent(message = "releaseDate cannot be in future")
    @NotNull(message = "releaseDate cannot be null")
    private LocalDate releaseDate;
    @Positive(message = "Duration cannot be negative")
    private Integer duration;
    @JsonIgnore
    private Set<Long> likes;
    private RatingMpa ratingMpa;
    private final Set<Genre> genre;

    public Film() {
        this.likes = new HashSet<>();
        this.genre = new HashSet<>();
    }
}
