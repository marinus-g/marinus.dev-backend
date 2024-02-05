package dev.marinus.backend.controller;

import dev.marinus.backend.dto.UserListRequestDto;
import dev.marinus.backend.dto.UsernameAndIdDto;
import dev.marinus.backend.model.entity.user.User;
import dev.marinus.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/public/user/{username}/exists")
    public ResponseEntity<Boolean> doesUserExist(@PathVariable final String username) {
        return ResponseEntity.ok(this.userService.findRegisteredUserByUsername(username).isPresent());
    }

    @GetMapping("/user/list")
    public ResponseEntity<List<?>> getUserList(@RequestBody(required = false) final Optional<UserListRequestDto> optionalRequestType) {
        return ResponseEntity.ok(optionalRequestType.map(UserListRequestDto::getType)
                .map(requestType -> {
                    switch (requestType) {
                        case ID -> {
                            return this.userService.findAllRegisteredUsers().stream().map(User::getId).toList();
                        }
                        case USERNAME -> {
                            return this.userService.findAllRegisteredUsers().stream().map(User::getUsername).toList();
                        }
                        default -> {
                            return this.userService.findAllRegisteredUsers()
                                    .stream()
                                    .map(registeredUser ->
                                            new UsernameAndIdDto(
                                                    registeredUser.getUsername(),
                                                    registeredUser.getId()
                                            )
                                    ).toList();
                        }
                    }
                }).orElseGet(() ->
                        this.userService.findAllRegisteredUsers()
                                .stream()
                                .map(registeredUser -> new UsernameAndIdDto(
                                        registeredUser.getUsername(),
                                        registeredUser.getId())
                                )
                                .toList()));

    }
}
