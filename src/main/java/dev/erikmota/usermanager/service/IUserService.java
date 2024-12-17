package dev.erikmota.usermanager.service;

import dev.erikmota.usermanager.dto.request.UserRequestDTO;
import dev.erikmota.usermanager.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    User create(UserRequestDTO dtoCreate);
    User update(UserRequestDTO dtoUpdate, Long id);
    List<User> listAll();
    Page<User> listAll(String search, Boolean enabled, Pageable pageable);
    User getById(Long id);
    User deleteById(Long id);
}
