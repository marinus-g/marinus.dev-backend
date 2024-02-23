package dev.marinus.backend.authentication;

import dev.marinus.backend.provider.AuthenticationProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class CookieAuthenticationFilter extends OncePerRequestFilter {
    public static final String COOKIE_NAME = "token";

    private final AuthenticationProvider authenticationProvider;

    public CookieAuthenticationFilter(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final Optional<Cookie> authCookie = Stream.of(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .findFirst();

        authCookie.ifPresent(cookie -> System.out.println("cookie: " + cookie.getValue()));
        authCookie.ifPresent(cookie -> SecurityContextHolder.getContext()
                .setAuthentication(authenticationProvider.authenticate(new PreAuthenticatedAuthenticationToken(cookie.getValue(), null))));
        filterChain.doFilter(request, response);
    }
}