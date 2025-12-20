package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
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
