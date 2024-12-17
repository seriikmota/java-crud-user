package dev.erikmota.usermanager.controller;

import dev.erikmota.usermanager.dto.request.UserRequestDTO;
import dev.erikmota.usermanager.dto.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUserController {
    @Operation(description = "Endpoint to register a object", responses = {
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
    })
    ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO dtoCreate);
}
