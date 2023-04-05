package ru.yandex.practicum.filmorate.model.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private HashMap<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @GetMapping("/films")
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен post запрос к эндпоинту /films: '{}'", film.toString());
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }  else {
            film.setId(currentId);
            films.put(film.getId(), film);
            currentId++;
            return film;
        }
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен put запрос к эндпоинту /films: '{}'", film.toString());
        if (films.containsKey(film.getId())) {
            Film currentFilm = films.get(film.getId());
            currentFilm.setDescription(film.getDescription());
            currentFilm.setDuration(film.getDuration());
            currentFilm.setReleaseDate(film.getReleaseDate());
            currentFilm.setName(film.getName());
        } else {
            log.info("Фильм не найден с ID - {}",film.getId());
            throw new ValidationException("Фильм не найден с ID - "+film.getId());
        }
        return films.get(film.getId());
    }
}
