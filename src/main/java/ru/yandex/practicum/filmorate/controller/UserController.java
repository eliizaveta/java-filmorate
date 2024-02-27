package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List getAllUsers() {
        log.info("Получим список всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserId(@PathVariable Integer id) {
        log.info("Получим инфо о пользователе с id " + id);
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        log.info("Добавим пользователя " + user);
        return userService.addUser(user);
    }

    @PutMapping("/users")
    public User changeUser(@Valid @RequestBody User user) {
        log.info("Обновим инфо пользователя " + user);
        return userService.changeUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Добавим в друзья пользователем c id " + id + " пользователя с id " + friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Удалим из друзей пользователем c id " + id + " пользователя с id " + friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getListUser(@PathVariable Integer id) {
        log.info("Получим список друзей пользователя " + id);
        return userService.getListUser(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Запросим список общих друзей между пользователем c id " + id + " и пользователем с id " + otherId);
        return userService.getMutualFriends(id, otherId);
    }
}
