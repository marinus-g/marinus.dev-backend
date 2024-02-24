package dev.marinus.backend.service;

import dev.marinus.backend.security.authentication.CookieAuthenticationFilter;
import dev.marinus.backend.config.BackendConfiguration;
import dev.marinus.backend.config.PasswordConfig;
import dev.marinus.backend.dto.ContentProfileCredentialsDto;
import dev.marinus.backend.dto.UserCredentialsDto;
import dev.marinus.backend.model.Credentials;
import dev.marinus.backend.model.entity.Authenticatable;
import dev.marinus.backend.model.entity.content.profile.ContentProfile;
import dev.marinus.backend.model.entity.user.RegisteredUser;
import dev.marinus.backend.model.entity.user.User;
import dev.marinus.backend.repository.ContentProfileRepository;
import dev.marinus.backend.repository.RegisteredUserRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final RegisteredUserRepository userRepository;
    private final ContentProfileRepository contentProfileRepository;
    private final BackendConfiguration backendConfiguration;
    private final PasswordConfig passwordConfig;

    @Value("${security.token.secret}")
    private String tokenSecret;
    @Value("${security.token.separator}")
    private String tokenSeparator;

    public AuthenticationService(RegisteredUserRepository userRepository, PasswordConfig passwordConfig,
                                 ContentProfileRepository contentProfileRepository, BackendConfiguration backendConfiguration) {
        this.userRepository = userRepository;
        this.contentProfileRepository = contentProfileRepository;
        this.backendConfiguration = backendConfiguration;
        this.passwordConfig = passwordConfig;
    }

    public Optional<Authenticatable> authenticate(Credentials credentials) {
        if (credentials == null) {
            throw new BadCredentialsException("No credentials!");
        } else if (credentials.getLogin() == null) {
            throw new BadCredentialsException("No login provided!");
        } else if (credentials instanceof ContentProfileCredentialsDto contentCredentials) {
            try {
                final UUID tokenId = UUID.fromString(contentCredentials.getLogin());
                return contentProfileRepository.findByTokenId(tokenId)
                        .map(contentProfile -> contentProfile);
            } catch (Exception  e) {
                throw new BadCredentialsException("Invalid token!");
            }
        } else if (credentials instanceof UserCredentialsDto userCredentials) {
            if (userCredentials.getPassword() == null) {
                throw new BadCredentialsException("No password provided!");
            }
            return userRepository.findByUsername(userCredentials.getLogin())
                    .filter(user -> passwordConfig.passwordEncoder()
                            .matches(userCredentials.getPassword(), user.getPassword()))
                    .map(registeredUser -> registeredUser);
        } else {
            throw new BadCredentialsException("Invalid credentials!");
        }
    }

    public String createToken(Optional<Authenticatable> authenticatable) {
        if (authenticatable.isEmpty()) {
            throw new BadCredentialsException("ERROR");
        }
        return null;
    }

    public Optional<Authenticatable> findByToken(String token) {
        final String[] tokenParts = token.split(this.tokenSeparator);
        if (tokenParts.length == 0) {
            throw new BadCredentialsException("Invalid token!");
        }
        final CookieType tokenType = CookieType.values()[Integer.parseInt(tokenParts[0])];
        if (tokenType == CookieType.USER) {
            return this.findUserByToken(tokenParts);
        } else if (tokenType == CookieType.CONTENT_PROFILE) {
            return this.findContentProfileByToken(tokenParts);
        } else {
            throw new BadCredentialsException("Invalid token!");
        }
    }

    private Optional<Authenticatable> findContentProfileByToken(String[] tokenParts) {
        if (tokenParts.length != 3) {
            throw new BadCredentialsException("Invalid token!");
        }
        final String id = tokenParts[1];
        final String hmac = tokenParts[2];
        final ContentProfile contentProfile = contentProfileRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new BadCredentialsException("Invalid token!"));
        if (!hmac.equals(this.calculateHmac(contentProfile))) {
            throw new BadCredentialsException("Invalid token!");
        }
        return Optional.of(contentProfile);
    }

    private Optional<Authenticatable> findUserByToken(String[] tokenParts) {
        if (tokenParts.length != 4) {
            throw new BadCredentialsException("Invalid token!");
        }
        final String id = tokenParts[1];
        final String username = tokenParts[2];
        final String hmac = tokenParts[3];
        final RegisteredUser user = userRepository.findById(Long.parseLong(id))
                .filter(registeredUser -> registeredUser.getUsername().equals(username))
                .orElseThrow(() -> new BadCredentialsException("Invalid token, content profile not found!"));
        if (!this.calculateHmac(user).equals(hmac)) {
            throw new BadCredentialsException("Invalid token!");
        }
        return Optional.of(user);
    }

    private String calculateHmac(User user) {
        byte[] secretKeyBytes = Objects.requireNonNull(this.tokenSecret).getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = (user.getId() + this.tokenSeparator + user.getUsername()).getBytes(StandardCharsets.UTF_8);
        return this.calculateHmac(secretKeyBytes, valueBytes);
    }

    private String calculateHmac(ContentProfile contentProfile) {
        byte[] secretKeyBytes = Objects.requireNonNull(this.tokenSecret).getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = (contentProfile.getId() + this.tokenSeparator + contentProfile.getTokenId()).getBytes(StandardCharsets.UTF_8);
        return this.calculateHmac(secretKeyBytes, valueBytes);
    }

    private String calculateHmac(byte[] secretKeyBytes, byte[] valueBytes) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA512");
            mac.init(secretKeySpec);
            return Base64.getEncoder().encodeToString(mac.doFinal(valueBytes));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildToken(RegisteredUser user) {
        return CookieType.USER.ordinal() +
                this.tokenSeparator +
                user.getId() +
                this.tokenSeparator +
                user.getUsername() +
                this.tokenSeparator +
                this.calculateHmac(user);

    }

    private String buildToken(ContentProfile contentProfile) {
        return CookieType.CONTENT_PROFILE.ordinal() +
                this.tokenSeparator +
                contentProfile.getId() +
                this.tokenSeparator +
                this.calculateHmac(contentProfile);
    }

    public Cookie buildCookie(@NonNull Authenticatable authenticatable) {
        final Cookie cookie;
        if (authenticatable instanceof RegisteredUser user) {
            cookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME, this.buildToken(user));
        } else if (authenticatable instanceof ContentProfile contentProfile) {
            cookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME, this.buildToken(contentProfile));
        } else {
            throw new IllegalArgumentException("Invalid authenticatable!");
        }
        cookie.setPath("/");
        cookie.setDomain(backendConfiguration.getCookieDomain());
        cookie.setSecure(backendConfiguration.isCookieSecure());

        cookie.setMaxAge(30_000);
        return cookie;
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    enum CookieType {
        USER,
        CONTENT_PROFILE
    }
}