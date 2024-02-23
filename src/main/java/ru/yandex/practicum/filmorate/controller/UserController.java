package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();

    private Integer id = 0;

    @GetMapping()
    public List<User> getAll() {
        log.info("Получили запрос. Количество пользователей: " + users.size());
        return List.copyOf(users.values());

    }

    @PostMapping()
    public User create(@RequestBody User user) {
        if (validCheck(user)){
            id++;
            user.setId(id);
            log.info("Сохраняем пользователя: " + user.toString());
            users.put(user.getId(), user);
            return user;
        }
        return user;
    }

    @PutMapping()
    public User put(@RequestBody User user) {
        if (validCheck(user)){
            if (users.containsKey(user.getId())) {
                log.info("Обновляем пользователя: " + user.toString());
                users.put(user.getId(), user);
            } else {
                log.info("Пользователя не существует: " + user.toString());
                throw new ValidationException("Пользователя не существует");
            }
            return user;
        }
        return user;
    }

    private Boolean validCheck(User user){
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.info("электронная почта не может быть пустой: " + user.toString());
            throw new ValidationException("электронная почта не может быть пустой");
        }
        if (!user.getEmail().contains("@")){
            log.info("электронная почта должна содержать символ @: " + user.toString());
            throw new ValidationException("электронная почта должна содержать символ @");
        }
        if (user.getLogin().isBlank()){
            log.info("логин не может быть пустым: " + user.toString());
            throw new ValidationException("логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")){
            log.info("логин не может содержать пробелы: " + user.toString());
            throw new ValidationException("логин не может содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())){
            log.info("дата рождения не может быть в будущем: " + user.toString());
            throw new ValidationException("дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()){
            log.info("имя для отображения может быть пустым — в таком случае будет использован логин: " + user.toString());
            user.setName(user.getLogin());
        }
        return true;
    }
}
