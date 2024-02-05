package dev.marinus.backend.model.entity.content.impl;

import dev.marinus.backend.model.entity.content.Content;
import dev.marinus.backend.model.entity.content.type.CommandContentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
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
