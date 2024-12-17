package dev.erikmota.usermanager.mapper;

import dev.erikmota.usermanager.dto.list.UserListDTO;
import dev.erikmota.usermanager.dto.request.UserRequestDTO;
import dev.erikmota.usermanager.dto.response.UserResponseDTO;
import dev.erikmota.usermanager.entities.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
    User toModel(UserRequestDTO dto);
    UserResponseDTO toDTO(User entity);
    void updateModelFromModel(@MappingTarget User data, User updateData);

    @Named(value = "toDTOList")
    UserListDTO toDTOList(User model);

    @IterableMapping(qualifiedByName = "toDTOList")
    List<UserListDTO> toDtoList(List<User> modelList);
}
