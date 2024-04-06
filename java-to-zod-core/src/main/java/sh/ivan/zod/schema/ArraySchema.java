package sh.ivan.zod.schema;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.zod.schema.attribute.Attribute;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ArraySchema extends Schema {
    private final Schema componentType;

    public ArraySchema(Schema componentType, Set<Attribute> attributes) {
        super(attributes);
        this.componentType = componentType;
    }

    @Override
    protected String zodType(String prefix) {
        return prefix + "array(" + componentType.asZodSchema(prefix) + ")";
    }

    @Override
    protected String zodType() {
        throw new UnsupportedOperationException("Implements zodType(String prefix) directly");
    }
}
