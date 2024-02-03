package dev.marinus.backend.model.content.impl;

import dev.marinus.backend.model.content.Content;
import dev.marinus.backend.model.content.type.CommandContentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class CommandContent extends Content<CommandContentType> {
    @Column(name = "command_name")
    private String command;
    @Column(name = "command_description")
    private String description;
    @Column(name = "command_usage")
    private String usage;
}
