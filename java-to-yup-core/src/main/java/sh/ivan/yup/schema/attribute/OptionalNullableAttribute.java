package sh.ivan.yup.schema.attribute;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class OptionalNullableAttribute implements Attribute {
    @Override
    public int priority() {
        return 60;
    }

    @Override
    public String yupMethod() {
        return "optional().nullable()";
    }
}
