package ru.springtest.service;

import ru.springtest.domain.User;

import java.util.UUID;

public interface UserService {
    User getById(UUID id);
    User save(User user);
}
