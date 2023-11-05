package sh.ivan.yup.schema;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.yup.schema.attribute.Attribute;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NumberSchema extends Schema {
    public NumberSchema(Set<Attribute> attributes) {
        super(attributes);
    }

    @Override
    public String yupType() {
        return "number()";
    }
}
