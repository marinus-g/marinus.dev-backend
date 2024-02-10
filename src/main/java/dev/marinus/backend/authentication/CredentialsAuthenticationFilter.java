package dev.marinus.backend.authentication;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marinus.backend.dto.ContentProfileCredentialsDto;
import dev.marinus.backend.dto.UserCredentialsDto;
import dev.marinus.backend.model.Credentials;
import dev.marinus.backend.provider.AuthenticationProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.File;
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
        if ("/api/v1/public/authentication/authenticate".equals(request.getServletPath())) {
            System.out.println("HELLO AUTHENTICATION");
            try {
                Credentials credentialsDto = MAPPER.readValue(request.getInputStream(), Credentials.class);
                System.out.println("credentials: " + credentialsDto.getClass().getTypeName());
                if (credentialsDto instanceof UserCredentialsDto userCredentialsDto) {
                    System.out.println("user credentials: " + userCredentialsDto.getLogin());
                    SecurityContextHolder.getContext().setAuthentication(authenticationProvider.authenticate(
                            new UsernamePasswordAuthenticationToken(userCredentialsDto.getLogin(),
                                    userCredentialsDto.getPassword())));
                } else if (credentialsDto instanceof ContentProfileCredentialsDto contentProfileCredentialsDto) {
                    System.out.println("content profile credentials: " + contentProfileCredentialsDto.getLogin());
                    SecurityContextHolder.getContext().setAuthentication(authenticationProvider.authenticate(
                            new TokenAuthenticationAuthenticationToken(contentProfileCredentialsDto.getLogin())));
                } else {
                    System.out.println(credentialsDto.getClass().getTypeName());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        filterChain.doFilter(request, response);
    }
}
