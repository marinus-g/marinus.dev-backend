package dev.marinus.backend.controller;

import dev.marinus.backend.dto.AuthenticationDto;
import dev.marinus.backend.model.entity.Authenticatable;
import dev.marinus.backend.model.entity.user.User;
import dev.marinus.backend.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/public/authentication")
public class AuthenticationController {


    private static final Duration ONE_DAY = Duration.ofDays(1L);


    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationDto> authenticate(HttpServletResponse response,
                                                          @AuthenticationPrincipal Authenticatable authenticatable) {
        return Optional.ofNullable(authenticatable).map(auth -> {
            response.addCookie(authenticationService.buildCookie(auth));
            return ResponseEntity.ok(new AuthenticationDto(System.currentTimeMillis() + ONE_DAY.getSeconds() * 1000L));
        }).orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkAuthentication(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user not found");
        }

        System.out.println("user is authenticated");
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not logged in");
        }
        this.authenticationService.logout();
        return ResponseEntity.ok().build();
    }
}