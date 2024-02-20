package dev.marinus.backend.listener;

import dev.marinus.backend.service.UserService;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartListener implements ApplicationListener<ApplicationStartedEvent> {

    private final UserService userService;

    public ApplicationStartListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        this.userService.init();
        System.out.println("Marinus.dev backend started!");
    }
}
