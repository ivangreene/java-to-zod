package sh.ivan.yup.schema.attribute;

import lombok.Data;

@Data
public class MaxAttribute implements Attribute {
    private final long max;

    @Override
    public int priority() {
        return 45;
    }

    @Override
    public String yupMethod() {
        return "max(" + max + ")";
    }
}
