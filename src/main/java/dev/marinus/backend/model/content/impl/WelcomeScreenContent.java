package dev.marinus.backend.model.content.impl;

import dev.marinus.backend.model.content.Content;
import dev.marinus.backend.model.content.type.WelcomeContentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "welcome_content")
@Getter
@Setter
public class WelcomeScreenContent extends Content<WelcomeContentType> {


    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "welcome_message", joinColumns = @JoinColumn(name = "content_id"))
    @Column(name = "welcome_message", nullable = false)
    private List<String> welcomeMessage = new ArrayList<>();

    public WelcomeScreenContent() {
        super();
        this.setType(new WelcomeContentType());
    }
}
