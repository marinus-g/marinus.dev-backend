package dev.marinus.backend.model.role;

import dev.marinus.backend.model.user.RegisteredUser;
import dev.marinus.backend.model.user.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    private Set<RegisteredUser> users = new HashSet<>();
}
