package sh.ivan.zod.schema.attribute;

import lombok.Data;

@Data
public class MaxAttribute implements Attribute {
    private final long max;

    @Override
    public String zodMethod() {
        return "max(" + max + ")";
    }
}
