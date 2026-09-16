package ru.yandex.practicum.catsgram.service.files;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Image;
import ru.yandex.practicum.catsgram.model.FileData;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.util.IdentifyService;
import ru.yandex.practicum.catsgram.service.PostService;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final Map<Long, Image> images = new HashMap<>();

    // Укажите директорию для хранения изображений
    @Value("${catsgram.image-directory}")
    private String imageDirectory;

    private final PostService postService;
    private final IdentifyService identifyService;
    private final FileService fileService;

    // получение данных об изображениях указанного поста
    public List<Image> getPostImages(long postId) {
        return images.values()
                .stream()
                .filter(image -> image.getPostId() == postId)
                .collect(Collectors.toList());
    }

    // сохранение списка изображений, связанных с указанным постом
    public List<Image> saveImages(long postId, List<MultipartFile> files) {
        return files.stream().map((MultipartFile file) -> saveImage(postId, file)).collect(Collectors.toList());
    }

    // сохранение отдельного изображения, связанного с указанным постом
    private Image saveImage(long postId, MultipartFile file) {
        Post post = postService.findById(postId)
                .orElseThrow(() -> new ConditionsNotMetException("Указанный пост не найден"));

        // сохраняем изображение на диск и возвращаем путь к файлу
        Path filePath = fileService.saveFile(imageDirectory, file, post);

        // создаём объект для хранения данных изображения
        long imageId = identifyService.getNextId(images);

        // создание объекта изображения и заполнение его данными
        Image image = new Image();
        image.setId(imageId);
        image.setFilePath(filePath.toString());
        image.setPostId(postId);
        // запоминаем название файла, которое было при его передаче
        image.setOriginalFileName(file.getOriginalFilename());

        images.put(imageId, image);

        return image;
    }

    // загружаем данные указанного изображения с диска
    public FileData getImageData(long imageId) {
        if (!images.containsKey(imageId)) {
            throw new NotFoundException("Изображение с id = " + imageId + " не найдено");
        }
        Image image = images.get(imageId);
        // загрузка файла с диска
        byte[] data = fileService.loadFile(image);

        return new FileData(data, image.getOriginalFileName());
    }
}
