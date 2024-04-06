package sh.ivan.zod.schema.attribute;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class IntegerAttribute implements Attribute {
    @Override
    public int priority() {
        return 5;
    }

    @Override
    public String zodMethod() {
        return "int()";
    }
}
