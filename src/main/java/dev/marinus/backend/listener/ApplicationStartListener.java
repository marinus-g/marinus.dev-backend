package dev.marinus.backend.listener;

import dev.marinus.backend.dto.ContentProfileDto;
import dev.marinus.backend.model.content.impl.WelcomeScreenContent;
import dev.marinus.backend.model.content.profile.ContentProfile;
import dev.marinus.backend.model.content.profile.ContentProfileType;
import dev.marinus.backend.model.user.GuestUser;
import dev.marinus.backend.service.ContentService;
import dev.marinus.backend.service.UserService;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartListener implements ApplicationListener<ApplicationStartedEvent> {

    private final ContentService contentService;
    private final UserService userService;

    public ApplicationStartListener(ContentService contentService, UserService userService) {
        this.contentService = contentService;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        System.out.println("Marinus.dev backend started!");
    }
}
