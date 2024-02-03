package dev.marinus.backend.model.content.type;

import dev.marinus.backend.model.LineType;

public class WelcomeContentType implements ContentType {

    @Override
    public String typeName() {
        return "Welcome-Screen";
    }

    @Override
    public LineType lineType() {
        return LineType.INNER_HTML;
    }
}