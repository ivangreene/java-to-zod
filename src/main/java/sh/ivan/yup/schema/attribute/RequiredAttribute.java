package sh.ivan.yup.schema.attribute;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class RequiredAttribute implements Attribute {
    @Override
    public int priority() {
        return 5;
    }

    @Override
    public String yupMethod() {
        return "required()";
    }
}
