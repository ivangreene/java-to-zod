package sh.ivan.zod.schema.attribute;

import lombok.Data;

@Data
public class MinAttribute implements Attribute {
    private final long min;

    @Override
    public int priority() {
        return 30;
    }

    @Override
    public String zodMethod() {
        return "min(" + min + ")";
    }
}
