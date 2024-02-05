package dev.marinus.backend.model.content.impl;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.DependsOn;

@Entity
@Table(name = "dynamic_command_content")
@NoArgsConstructor
@Getter
@Setter
public class DynamicCommandContent extends CommandContent {

    @Column(name = "need_authentication")
    private boolean needAuthentication;
    @Column(name = "api_endpoint")
    private String apiEndpoint;

    public DynamicCommandContent(String name, String description, String command, boolean needAuthentication, String apiEndpoint) {
        super(name, description, command);
        this.needAuthentication = needAuthentication;
        this.apiEndpoint = apiEndpoint;
    }
}