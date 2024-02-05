package dev.marinus.backend.config;

import dev.marinus.backend.authentication.AuthenticationEntryPoint;
import dev.marinus.backend.authentication.CookieAuthenticationFilter;
import dev.marinus.backend.authentication.CredentialsAuthenticationFilter;
import dev.marinus.backend.provider.AuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(AuthenticationEntryPoint entryPoint, AuthenticationProvider authenticationProvider) {
        this.authenticationEntryPoint = entryPoint;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(authenticationEntryPoint))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(new CredentialsAuthenticationFilter(authenticationProvider), BasicAuthenticationFilter.class)
                .addFilterBefore(new CookieAuthenticationFilter(authenticationProvider), CredentialsAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(request -> {
                            CorsConfiguration configuration = new CorsConfiguration();
                            configuration.setAllowedOriginPatterns(List.of("*"));
                            configuration.setAllowCredentials(true);
                            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));
                            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                            source.registerCorsConfiguration("/**", configuration.applyPermitDefaultValues());
                            return configuration;
                        }))
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(configurer -> configurer.deleteCookies(CookieAuthenticationFilter.COOKIE_NAME))
                .authorizeHttpRequests(configure -> configure
                        .requestMatchers("/api/v1/auth/authenticate").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/v1/public/**").permitAll()
                        .requestMatchers("/api/v1/content/default").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();

    }

}
