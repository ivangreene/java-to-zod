package sh.ivan.zod.schema.attribute;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class NotBlankAttribute implements Attribute {
    @Override
    public int priority() {
        return 3;
    }

    @Override
    public String zodMethod() {
        return "regex(/\\S/)";
    }
}
