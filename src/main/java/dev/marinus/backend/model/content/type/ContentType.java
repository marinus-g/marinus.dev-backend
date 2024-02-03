package dev.marinus.backend.model.content.type;

import dev.marinus.backend.model.LineType;

public interface ContentType {

    String typeName();
    LineType lineType();

    class Type {
        public static final WelcomeContentType WELCOME = new WelcomeContentType();
        public static final CommandContentType COMMAND = new CommandContentType();
    }
}