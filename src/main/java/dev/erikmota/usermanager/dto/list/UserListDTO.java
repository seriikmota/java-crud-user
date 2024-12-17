package dev.erikmota.usermanager.dto.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserListDTO {
    private Long id;
    private String name;
    private String email;
    private Boolean enabled;
}
