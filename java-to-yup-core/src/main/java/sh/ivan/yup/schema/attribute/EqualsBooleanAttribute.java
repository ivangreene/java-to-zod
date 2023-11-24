package sh.ivan.yup.schema.attribute;

import lombok.Data;

@Data
public class EqualsBooleanAttribute implements Attribute {
    private final boolean value;

    @Override
    public int priority() {
        return 20;
    }

    @Override
    public String yupMethod() {
        return "equals([" + value + "])";
    }
}
