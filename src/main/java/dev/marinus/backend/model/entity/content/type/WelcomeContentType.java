package dev.marinus.backend.model.entity.content.type;


import dev.marinus.backend.model.entity.LineType;

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