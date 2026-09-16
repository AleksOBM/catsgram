package ru.yandex.practicum.catsgram.service.util;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IdentifyService {
    public <T> long getNextId(Map<Long, T> map) {
        long currentMaxId = map.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
