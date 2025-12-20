package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    private final IdentifyService identifyService;

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
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

        user.setId(identifyService.getNextId(users));
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User newUser) {
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

    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }
}
