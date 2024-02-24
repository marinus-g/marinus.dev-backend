package dev.marinus.backend.service;

import dev.marinus.backend.config.PasswordConfig;
import dev.marinus.backend.dto.GuestUserDto;
import dev.marinus.backend.dto.RoleDto;
import dev.marinus.backend.model.entity.role.Role;
import dev.marinus.backend.model.entity.user.GuestUser;
import dev.marinus.backend.model.entity.user.RegisteredUser;
import dev.marinus.backend.repository.GuestUserRepository;
import dev.marinus.backend.repository.RegisteredUserRepository;
import dev.marinus.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final RegisteredUserRepository registeredUserRepository;
    private final RoleRepository roleRepository;
    private final GuestUserRepository guestUserRepository;
    private final PasswordConfig passwordConfig;

    @Value("${security.default.root.password}")
    private String defaultRootPassword;

    public UserService(RegisteredUserRepository registeredUserRepository, GuestUserRepository guestUserRepository,
                       final RoleRepository roleRepository, PasswordConfig passwordConfig) {
        this.registeredUserRepository = registeredUserRepository;
        this.guestUserRepository = guestUserRepository;
        this.roleRepository = roleRepository;
        this.passwordConfig = passwordConfig;
    }


    public void init() {
        if (this.registeredUserRepository.count() == 0) {
            System.out.println("No users found, creating root user!");
            System.out.println("password: " + this.defaultRootPassword);
            final RegisteredUser registeredUser = new RegisteredUser();
            registeredUser.setUsername("root");
            registeredUser.setPassword(passwordConfig.passwordEncoder().encode(this.defaultRootPassword));
            Role role = new Role();
            role.setName("root");
            role.addCommand("adduser");
            role.addCommand("deluser");
            role.addCommand("addrole");
            role.addCommand("delrole");
            role.addCommand("addcommand");
            role.addCommand("delcommand");
            role.addCommand("editcommand");

            role.addCommand("content");
            role.addCommand("editrole");
            role.addCommand("usermod");
            role.addCommand("users");
            role.addCommand("roles");
            role.addCommand("projects");
            role = this.roleRepository.save(role);
            registeredUser.addRole(role);
            this.registeredUserRepository.save(registeredUser);
            System.out.println("Created root user!");
        } else if (this.roleRepository.findById(1L).isPresent()) {
            this.roleRepository.findById(1L).ifPresent(role -> {
                role.addCommand("projects");
                this.roleRepository.save(role);
            });
        }
    }

    public GuestUser saveGuestUser(GuestUser guestUser) {
        return guestUserRepository.save(guestUser);
    }

    public RegisteredUser saveRegisteredUser(RegisteredUser registeredUser) {
        return registeredUserRepository.save(registeredUser);
    }

    public Optional<GuestUser> findGuestUserById(Long id) {
        return guestUserRepository.findById(id);
    }

    public Optional<GuestUser> findGuestUserByUsername(String username) {
        return guestUserRepository.findByUsername(username);
    }

    public List<RegisteredUser> findAllRegisteredUsers() {
        return registeredUserRepository.findAll();
    }

    public List<GuestUser> findAllGuestUsers() {
        return guestUserRepository.findAll();
    }

    public Optional<RegisteredUser> findRegisteredUserByUsername(String username) {
        return registeredUserRepository.findByUsername(username);
    }

    public Set<RoleDto> convertToDto(Set<Role> roles) {
        return roles.stream().map(this::convertToDto).collect(Collectors.toSet());
    }

    public RoleDto convertToDto(Role role) {
        return new RoleDto(role.getId(), role.getName(), new ArrayList<>(role.getCommands()));
    }

    public Optional<GuestUser> createOrFindGuestUser(GuestUserDto guestUserDto) {
        return Optional.ofNullable(guestUserDto)
                .map(GuestUserDto::getUsername)
                .flatMap(this::findGuestUserByUsername)
                .or(() -> Optional.ofNullable(guestUserDto)
                        .map(guestUser -> new GuestUser(guestUser.isSaveInLocalStorage()))
                        .map(guestUser -> {
                            guestUser.setUsername(guestUserDto.getUsername());
                            return this.saveGuestUser(guestUser);
                        }));
    }
}
