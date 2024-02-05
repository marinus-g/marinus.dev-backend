package dev.marinus.backend.model.entity.content.impl;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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