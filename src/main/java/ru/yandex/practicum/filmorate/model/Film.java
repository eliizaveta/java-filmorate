package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class Film {

    private int id;

    @NotBlank
    private String name;

    @Size(max = 200)
    @NotNull
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @PositiveOrZero
    private long duration;

    private Set<Integer> likes;

    @NotNull
    private Mpa mpa;

    private Set<Genre> genres;
}
