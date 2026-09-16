package ru.yandex.practicum.catsgram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class UserDto {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private long id;

	private String username;

	private String email;

	private String password = "*****";

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Instant registrationDate;
}
