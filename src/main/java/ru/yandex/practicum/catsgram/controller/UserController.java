package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (users.values().stream().map(User::getEmail).anyMatch(u -> u.equals(user.getEmail()))) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ConditionsNotMetException("Имя должно быть указано");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ConditionsNotMetException("Пароль должен быть указан");
        }

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (!users.containsKey(newUser.getId())) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        User oldUser = users.get(newUser.getId());

        if (newUser.getUsername() == null) {
            newUser.setUsername(oldUser.getUsername());
        }
        if (newUser.getUsername().isBlank()) {
            throw new ConditionsNotMetException("Имя не может быть пустым");
        }

        if (newUser.getEmail() == null) {
            newUser.setEmail(oldUser.getEmail());
        }
        if (newUser.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Email не может быть пустым");
        }

        if (newUser.getPassword() == null) {
            newUser.setPassword(oldUser.getPassword());
        }
        if (newUser.getPassword().isBlank()) {
            throw new ConditionsNotMetException("Пароль не может быть пустым");
        }

        oldUser = newUser.toBuilder()
                .registrationDate(oldUser.getRegistrationDate())
                .build();

        users.remove(oldUser.getId());
        users.put(oldUser.getId(), oldUser);

        return oldUser;

    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
