package dev.marinus.backend.model.content.impl;

import dev.marinus.backend.model.content.Content;
import dev.marinus.backend.model.content.type.WelcomeContentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "welcome_content")
@Getter
@Setter
public class WelcomeScreenContent extends Content<WelcomeContentType> {


    private List<String> welcomeMessage = new ArrayList<>();

    public WelcomeScreenContent() {
        super();
        this.setType(new WelcomeContentType());
    }
}
