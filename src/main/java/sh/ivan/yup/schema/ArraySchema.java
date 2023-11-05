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
    public String yupType() {
        return "array().of(" + componentType.asYupSchema() + ")";
    }
}
