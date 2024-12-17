package dev.erikmota.usermanager.controller;

import dev.erikmota.usermanager.dto.list.UserListDTO;
import dev.erikmota.usermanager.dto.request.UserRequestDTO;
import dev.erikmota.usermanager.dto.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IUserController {
    @Operation(description = "Endpoint to register a object", responses = {
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
    })
    ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO dtoCreate);

    @Operation(description = "Endpoint to edit a object", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
    })
    ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody UserRequestDTO dto);

    @Operation(description = "Endpoint to list all objects without pagination", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<List<UserListDTO>> listAll();

    @Operation(description = "Endpoint to list all and search objects with pagination", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<Page<UserListDTO>> listAllPagination(@RequestParam(required = false) String search,
                                                        @RequestParam(required = false) Boolean enabled,
                                                        Pageable pageable);

    @Operation(description = "Endpoint to search for an object by primary key", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<UserResponseDTO> getById(@PathVariable Long id);

    @Operation(description = "Endpoint to remove a object", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<UserResponseDTO> delete(@PathVariable Long id);
}
