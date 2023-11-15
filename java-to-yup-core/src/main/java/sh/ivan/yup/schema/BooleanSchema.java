package sh.ivan.yup.schema;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.yup.schema.attribute.Attribute;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BooleanSchema extends Schema {
    public BooleanSchema(Set<Attribute> attributes) {
        super(attributes);
    }

    @Override
    public String yupType() {
        return "boolean()";
    }
}
