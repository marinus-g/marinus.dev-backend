package dev.marinus.backend.service;

import dev.marinus.backend.model.entity.user.GuestUser;
import dev.marinus.backend.model.entity.user.RegisteredUser;
import dev.marinus.backend.repository.GuestUserRepository;
import dev.marinus.backend.repository.RegisteredUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final RegisteredUserRepository registeredUserRepository;
    private final GuestUserRepository guestUserRepository;

    public UserService(RegisteredUserRepository registeredUserRepository, GuestUserRepository guestUserRepository) {
        this.registeredUserRepository = registeredUserRepository;
        this.guestUserRepository = guestUserRepository;
        this.init();
    }

    private void init() {
        if (this.registeredUserRepository.count() == 0) {
            final RegisteredUser registeredUser = new RegisteredUser();
            registeredUser.setUsername("root");
            registeredUser.setPassword("password");
            this.registeredUserRepository.save(registeredUser);
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
}
