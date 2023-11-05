package sh.ivan.yup.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.yup.schema.attribute.Attribute;

import java.util.Set;

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
