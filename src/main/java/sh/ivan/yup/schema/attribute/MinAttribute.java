package sh.ivan.yup.schema.attribute;

import lombok.Data;

@Data
public class MinAttribute implements Attribute {
    private final long min;

    @Override
    public int priority() {
        return 40;
    }

    @Override
    public String yupMethod() {
        return "min(" + min + ")";
    }
}
