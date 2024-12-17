package dev.erikmota.usermanager.controller.impl;

import dev.erikmota.usermanager.controller.IUserController;
import dev.erikmota.usermanager.dto.list.UserListDTO;
import dev.erikmota.usermanager.dto.request.UserRequestDTO;
import dev.erikmota.usermanager.dto.response.UserResponseDTO;
import dev.erikmota.usermanager.mapper.UserMapper;
import dev.erikmota.usermanager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<UserListDTO>> listAll(){
        List<UserListDTO> listDTO = mapper.toDtoList(userService.listAll());
        return ResponseEntity.ok(listDTO);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<UserListDTO>> listAllPagination(@RequestParam(required = false) String search,
                                                               @RequestParam(required = false) Boolean enabled,
                                                               Pageable pageable){
        Page<UserListDTO> listDTO = userService.listAll(search, enabled, pageable).map(obj -> mapper.toDTOList(obj));
        return ResponseEntity.ok(listDTO);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize(value = "hasRole(#root.this.getRoleName('READ'))")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id){
        UserResponseDTO dtoResult = mapper.toDTO(userService.getById(id));
        return ResponseEntity.ok(dtoResult);
    }
}
