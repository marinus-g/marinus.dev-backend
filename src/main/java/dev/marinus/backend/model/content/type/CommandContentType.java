package dev.marinus.backend.model.content.type;

import dev.marinus.backend.model.LineType;
import org.aspectj.apache.bcel.generic.RET;

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
