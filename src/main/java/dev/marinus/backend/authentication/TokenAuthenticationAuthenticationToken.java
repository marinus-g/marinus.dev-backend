package dev.marinus.backend.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import java.util.UUID;

public class TokenAuthenticationAuthenticationToken extends AbstractAuthenticationToken {

    private final UUID tokenAsUuid;

    public TokenAuthenticationAuthenticationToken(String token) throws IllegalArgumentException {
        super(null);
        try {
            this.tokenAsUuid = UUID.fromString(token);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Token is not a valid UUID");
        }
    }

    @Override
    public Object getCredentials() {
        return this.tokenAsUuid;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
