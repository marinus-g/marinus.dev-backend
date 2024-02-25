package dev.marinus.backend.security.aspects;

import dev.marinus.backend.model.entity.Authenticatable;
import dev.marinus.backend.model.entity.user.RegisteredUser;
import dev.marinus.backend.security.annotation.RequiresCommand;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class RequiredCommandCheckAspect {

    @Before("@annotation(requiresCommand) && (@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping) || @annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public void checkRole(JoinPoint joinPoint, RequiresCommand requiresCommand) {
        String requiredRole = requiresCommand.value();
        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof Authenticatable)
                .map(principal -> (Authenticatable) principal)
                .filter(authenticatable -> authenticatable instanceof RegisteredUser)
                .map(authenticatable -> (RegisteredUser) authenticatable)
                .filter(registeredUser -> registeredUser.getRoles()
                        .stream()
                        .anyMatch(role
                                -> role.getCommands()
                                .stream()
                                .anyMatch(command -> command.equalsIgnoreCase(requiredRole))))
                .orElseThrow(() -> new SecurityException("User is lacking the permission for this action."));
    }
}
