package dev.erikmota.usermanager.service;

import dev.erikmota.usermanager.dto.request.UserRequestDTO;
import dev.erikmota.usermanager.entities.User;

public interface IUserService {
    User create(UserRequestDTO dtoCreate);
}
