package sh.ivan.zod.schema.attribute;

import lombok.Data;

@Data
public class PositiveAttribute implements Attribute {
    private final boolean includeZero;

    public PositiveAttribute() {
        this(false);
    }

    public PositiveAttribute(boolean includeZero) {
        this.includeZero = includeZero;
    }

    @Override
    public int priority() {
        return 25;
    }

    @Override
    public String zodMethod() {
        return includeZero ? "min(0)" : "positive()";
    }
}
