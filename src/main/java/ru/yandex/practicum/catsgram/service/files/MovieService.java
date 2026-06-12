package ru.yandex.practicum.catsgram.service.files;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.*;
import ru.yandex.practicum.catsgram.service.util.IdentifyService;
import ru.yandex.practicum.catsgram.service.PostService;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final Map<Long, Movie> movies = new HashMap<>();

    // Укажите директорию для хранения изображений
    @Value("${catsgram.movie-directory}")
    private String movieDirectory;

    private final PostService postService;
    private final IdentifyService identifyService;
    private final FileService fileService;

    public List<Movie> getPostMovies(long postId) {
        return movies.values()
                .stream()
                .filter(movie -> movie.getPostId() == postId)
                .collect(Collectors.toList());
    }

    public List<Movie> saveMovies(long postId, List<MultipartFile> files) {
        return files.stream().map((MultipartFile file) -> saveMovie(postId, file)).collect(Collectors.toList());
    }

    public FileData getMovieData(long movieId) {
        if (!movies.containsKey(movieId)) {
            throw new NotFoundException("Изображение с id = " + movieId + " не найдено");
        }
        Movie movie = movies.get(movieId);
        // загрузка файла с диска
        byte[] data = fileService.loadFile(movie);

        return new FileData(data, movie.getOriginalFileName());
    }

    private Movie saveMovie(long postId, MultipartFile file) {
        Post post = postService.findById(postId)
                .orElseThrow(() -> new ConditionsNotMetException("Указанный пост не найден"));

        // сохраняем изображение на диск и возвращаем путь к файлу
        Path filePath = fileService.saveFile(movieDirectory, file, post);

        // создаём объект для хранения данных изображения
        long movieId = identifyService.getNextId(movies);

        // создание объекта изображения и заполнение его данными
        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setFilePath(filePath.toString());
        movie.setPostId(postId);
        // запоминаем название файла, которое было при его передаче
        movie.setOriginalFileName(file.getOriginalFilename());

        movies.put(movieId, movie);

        return movie;
    }
}
