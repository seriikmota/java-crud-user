package dev.erikmota.usermanager.mapper;

import dev.erikmota.usermanager.dto.request.UserRequestDTO;
import dev.erikmota.usermanager.dto.response.UserResponseDTO;
import dev.erikmota.usermanager.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
    User toModel(UserRequestDTO dto);
    UserResponseDTO toDTO(User entity);
}
