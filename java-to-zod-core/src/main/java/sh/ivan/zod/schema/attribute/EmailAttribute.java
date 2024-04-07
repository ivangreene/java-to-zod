package sh.ivan.zod.schema.attribute;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class EmailAttribute implements Attribute {
    @Override
    public int priority() {
        return 15;
    }

    @Override
    public String zodMethod() {
        return "email()";
    }
}
