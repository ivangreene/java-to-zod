package sh.ivan.zod.schema.attribute;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class UuidAttribute implements Attribute {
    @Override
    public int priority() {
        return 10;
    }

    @Override
    public String zodMethod() {
        return "uuid()";
    }
}
