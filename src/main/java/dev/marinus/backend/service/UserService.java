package dev.marinus.backend.service;

import dev.marinus.backend.model.user.GuestUser;
import dev.marinus.backend.model.user.RegisteredUser;
import dev.marinus.backend.repository.GuestUserRepository;
import dev.marinus.backend.repository.RegisteredUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final RegisteredUserRepository registeredUserRepository;
    private final GuestUserRepository guestUserRepository;

    public UserService(RegisteredUserRepository registeredUserRepository, GuestUserRepository guestUserRepository) {
        this.registeredUserRepository = registeredUserRepository;
        this.guestUserRepository = guestUserRepository;
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

    public Optional<GuestUser> findRegisteredUserByUsername(String username) {
        return guestUserRepository.findByUsernameEqualsIgnoreCase(username);
    }
}
