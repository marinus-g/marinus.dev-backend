package dev.marinus.backend.model.entity.role;

import dev.marinus.backend.model.entity.user.RegisteredUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    private Set<RegisteredUser> users = new HashSet<>();

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "role_commands", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "role_commands", nullable = false)
    private final Set<String> commands = new HashSet<>();

    public void addCommand(String command) {
        commands.add(command);
    }

    public void removeCommand(String command) {
        commands.remove(command);
    }
}