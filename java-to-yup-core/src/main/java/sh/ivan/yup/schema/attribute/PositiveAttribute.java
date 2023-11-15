package sh.ivan.yup.schema.attribute;

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
        return 35;
    }

    @Override
    public String yupMethod() {
        return includeZero ? "min(0)" : "positive()";
    }
}
