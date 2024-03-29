package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class User {

    private int id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{1,20}$")
    private String login;

    private String name;

    @PastOrPresent
    @NotNull
    private LocalDate birthday;

    private Set<Integer> friends;
}
