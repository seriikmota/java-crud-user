package dev.erikmota.usermanager.controller.impl;

import dev.erikmota.usermanager.controller.IUserController;
import dev.erikmota.usermanager.dto.request.UserRequestDTO;
import dev.erikmota.usermanager.dto.response.UserResponseDTO;
import dev.erikmota.usermanager.mapper.UserMapper;
import dev.erikmota.usermanager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
public class UserController implements IUserController {

    @Autowired
    private IUserService userService;

    @Autowired
    protected UserMapper mapper;

    @PostMapping
    @Transactional
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO dtoCreate){
        UserResponseDTO resultDTO = mapper.toDTO(userService.create(dtoCreate));
        return ResponseEntity.status(HttpStatus.CREATED).body(resultDTO);
    }
}
