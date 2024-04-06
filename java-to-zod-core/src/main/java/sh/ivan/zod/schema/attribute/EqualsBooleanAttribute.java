package sh.ivan.zod.schema.attribute;

import lombok.Data;

@Data
public class EqualsBooleanAttribute implements Attribute {
    private final boolean value;

    @Override
    public int priority() {
        return -1;
    }

    @Override
    public String zodMethod() {
        throw new UnsupportedOperationException(
                "This attribute is not written to the schema, transform to LiteralBooleanSchema before writing");
    }
}
