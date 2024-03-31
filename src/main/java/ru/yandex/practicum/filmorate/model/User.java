package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@Component
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class User {
    private long id;

    @Email(message = "Email should be valid")
    @NotNull(message = "Email should be")
    private String email;

    @NotNull(message = "Login should be")
    @NotBlank(message = "Login cannot be empty")
    @Pattern(regexp = "\\S+", message = "Login consist space")
    private String login;

    private String name;

    @NotNull(message = "Birthday cannot be null")
    @Past(message = "Birthday cannot be in future")
    private LocalDate birthday;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("USER_EMAIL", email);
        values.put("USER_LOGIN", login);
        values.put("USER_NAME", name);
        values.put("USER_BIRTHDAY", birthday);
        return values;
    }
}
