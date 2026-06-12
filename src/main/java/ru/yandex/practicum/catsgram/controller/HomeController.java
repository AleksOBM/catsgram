package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class HomeController {

	@GetMapping
	public String homePage() {
		return """
				<pre>
				     .d8888b.           888              .d8888b.
				    d88P  Y88b          888             d88P  Y88b
				    888    888          888             888    888
				    888         8888b.  888888 .d8888b  888        888d888 8888b.  88888b.d88b.
				    888            "88b 888    88K      888  88888 888P"      "88b 888 "888 "88b
				    888    888 .d888888 888    "Y8888b. 888    888 888    .d888888 888  888  888
				    Y88b  d88P 888  888 Y88b.       X88 Y88b  d88P 888    888  888 888  888  888
				     "Y8888P"  "Y888888  "Y888  88888P'  "Y8888P88 888    "Y888888 888  888  888
				</pre>
				""";
	}
}
