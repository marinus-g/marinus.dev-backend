package dev.marinus.backend.model.entity.content.type;


import dev.marinus.backend.model.entity.LineType;

public class CommandContentType implements ContentType {


    @Override
    public String typeName() {
        return "Command";
    }

    @Override
    public LineType lineType() {
        return null;
    }
}
