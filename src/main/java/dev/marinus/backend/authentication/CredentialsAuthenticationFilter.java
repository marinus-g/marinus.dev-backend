package dev.marinus.backend.authentication;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marinus.backend.dto.ContentProfileCredentialsDto;
import dev.marinus.backend.dto.UserCredentialsDto;
import dev.marinus.backend.provider.AuthenticationProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class CredentialsAuthenticationFilter extends OncePerRequestFilter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final AuthenticationProvider authenticationProvider;

    public CredentialsAuthenticationFilter(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/v1/auth/authenticate".equals(request.getServletPath())) {
            UserCredentialsDto credentialsDto = MAPPER.readValue(request.getInputStream(), UserCredentialsDto.class);
            SecurityContextHolder.getContext().setAuthentication(authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(credentialsDto.getLogin(),
                            credentialsDto.getPassword())));
        } else {
            Optional<String> contentOptional = Optional.ofNullable(request.getParameter("content"));
            contentOptional.ifPresent(content -> {
                try {
                    ContentProfileCredentialsDto credentialsDto = MAPPER.readValue(content, ContentProfileCredentialsDto.class);
                    SecurityContextHolder.getContext().setAuthentication(authenticationProvider.authenticate(
                            new TokenAuthenticationAuthenticationToken(credentialsDto.getLogin())));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        filterChain.doFilter(request, response);
    }
}
