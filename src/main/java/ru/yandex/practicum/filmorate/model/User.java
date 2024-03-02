package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private Integer id;
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email should be")
    private String email;
    @NotBlank(message = "Login cannot be empty")
    private String login;
    private String name;
    @Past(message = "Birthday cannot be in future")
    @NotNull(message = "Birthday cannot be null")
    private LocalDate birthday;
}
