package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.Optional;

@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;

	@GetMapping
	public Collection<Post> findAll(@RequestParam(defaultValue = "desc") String sort,
	                                @RequestParam(defaultValue = "0") int from,
	                                @RequestParam(defaultValue = "10") int size) {

		return postService.findAll(sort, from, size);
	}

	@PostMapping
	public Post create(@RequestBody Post post) {
		return postService.create(post);
	}

	@PutMapping
	public Post update(@RequestBody Post newPost) {
		return postService.update(newPost);
	}

	@GetMapping("/{id}")
	public Optional<Post> findById(@PathVariable long id) {
		return postService.findById(id);
	}
}