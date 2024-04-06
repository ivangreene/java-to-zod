package sh.ivan.yup.schema;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.yup.schema.attribute.Attribute;

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
    protected String yupType(String prefix) {
        return prefix + "array(" + componentType.asYupSchema(prefix) + ")";
    }

    @Override
    protected String yupType() {
        throw new UnsupportedOperationException("Implements yupType(String prefix) directly");
    }
}
