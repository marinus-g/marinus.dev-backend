package dev.marinus.backend.model.content.impl;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.DependsOn;

@Entity
@Table(name = "dynamic_command_content")
@Getter
@Setter
public class DynamicCommandContent extends CommandContent {

    @Column(name = "need_authentication")
    private boolean needAuthentication;
    @Column(name = "api_endpoint")
    private String apiEndpoint;

}
