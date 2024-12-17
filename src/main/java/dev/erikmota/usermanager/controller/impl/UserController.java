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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
public class UserController implements IUserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserMapper mapper;

    @PostMapping
    @Transactional
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO dtoCreate){
        UserResponseDTO resultDTO = mapper.toDTO(userService.create(dtoCreate));
        return ResponseEntity.status(HttpStatus.CREATED).body(resultDTO);
    }

    @PutMapping(path = "/{id}")
    @Transactional
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
        UserResponseDTO modelSaved = mapper.toDTO(userService.update(dto, id));
        return ResponseEntity.ok(modelSaved);
    }
}
