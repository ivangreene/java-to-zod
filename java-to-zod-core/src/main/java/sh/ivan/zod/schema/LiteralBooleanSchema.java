package sh.ivan.zod.schema;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.zod.schema.attribute.Attribute;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LiteralBooleanSchema extends Schema {
    private final boolean value;

    public LiteralBooleanSchema(boolean value, Set<Attribute> attributes) {
        super(attributes);
        this.value = value;
    }

    @Override
    protected String zodType() {
        return String.format("literal(%s)", value);
    }
}
