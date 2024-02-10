package dev.marinus.backend.dto;

import dev.marinus.backend.model.entity.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisteredUserDto {

    private long id;
    private String username;
    private Set<RoleDto> roles;

}
