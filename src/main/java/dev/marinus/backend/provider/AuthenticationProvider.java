package dev.marinus.backend.provider;

import dev.marinus.backend.authentication.TokenAuthenticationAuthenticationToken;
import dev.marinus.backend.dto.ContentProfileCredentialsDto;
import dev.marinus.backend.dto.UserCredentialsDto;
import dev.marinus.backend.model.Authenticatable;
import dev.marinus.backend.service.AuthenticationService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    private final AuthenticationService authenticationService;

    public AuthenticationProvider(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<Authenticatable> authenticatable;
        String token;
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            final UserCredentialsDto credentialsDto = new UserCredentialsDto((String) authentication.getPrincipal(), (String) authentication.getCredentials());
            authenticatable = authenticationService.authenticate(credentialsDto);
            token = authenticationService.createToken(authenticatable);
        } else if (authentication instanceof TokenAuthenticationAuthenticationToken) {
            final ContentProfileCredentialsDto credentialsDto = new ContentProfileCredentialsDto((String) authentication.getCredentials());
            authenticatable = authenticationService.authenticate(credentialsDto);
            token = authenticationService.createToken(authenticatable);
        } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            token = (String) authentication.getPrincipal();
            authenticatable = authenticationService.findByToken(token);
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
        return new UsernamePasswordAuthenticationToken(authenticatable.orElse(null), token, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
