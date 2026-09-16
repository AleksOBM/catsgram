package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.model.FileData;
import ru.yandex.practicum.catsgram.model.Movie;
import ru.yandex.practicum.catsgram.service.files.MovieService;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController {
	private final MovieService movieService;

	@GetMapping("/posts/{postId}/movies")
	public List<Movie> getPostMovies(@PathVariable("postId") long postId) {
		return movieService.getPostMovies(postId);
	}

	@PostMapping("/posts/{postId}/movies")
	public List<Movie> addPostMovie(@PathVariable("postId") long postId,
	                                @RequestParam("movie") List<MultipartFile> files) {
		return movieService.saveMovies(postId, files);
	}

	@GetMapping(value = "/movies/{movieId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> downloadMovie(@PathVariable long movieId) {
		FileData movieData = movieService.getMovieData(movieId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(
				ContentDisposition.attachment()
						.filename(movieData.name(), StandardCharsets.UTF_8)
						.build()
		);

		return new ResponseEntity<>(movieData.data(), headers, HttpStatus.OK);
	}
}
