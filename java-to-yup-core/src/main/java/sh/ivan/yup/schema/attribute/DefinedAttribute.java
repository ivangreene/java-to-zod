package sh.ivan.yup.schema.attribute;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class DefinedAttribute implements Attribute {
    @Override
    public int priority() {
        return 0;
    }

    @Override
    public String yupMethod() {
        return "defined()";
    }
}
