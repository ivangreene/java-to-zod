package sh.ivan.yup.schema.attribute;

import lombok.Data;

@Data
public class NegativeAttribute implements Attribute {
    private final boolean includeZero;

    public NegativeAttribute() {
        this(false);
    }

    public NegativeAttribute(boolean includeZero) {
        this.includeZero = includeZero;
    }

    @Override
    public int priority() {
        return 30;
    }

    @Override
    public String yupMethod() {
        return includeZero ? "max(0)" : "negative()";
    }
}
