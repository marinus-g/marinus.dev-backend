package dev.marinus.backend.model.user;

import dev.marinus.backend.model.Authenticatable;
import dev.marinus.backend.model.role.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "registered_user")
@Getter
@Setter
public class RegisteredUser extends User implements Authenticatable {


    private String password;
    @ManyToMany
    private Set<Role> roles = new HashSet<>();
}
