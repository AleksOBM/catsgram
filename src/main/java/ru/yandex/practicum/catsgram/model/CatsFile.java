package ru.yandex.practicum.catsgram.model;

public interface CatsFile {

    Long getId();

    long getPostId();

    String getFilePath();

    String getOriginalFileName();
}
