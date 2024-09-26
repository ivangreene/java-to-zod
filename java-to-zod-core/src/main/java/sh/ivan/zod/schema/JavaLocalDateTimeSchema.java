package sh.ivan.zod.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.zod.schema.attribute.Attribute;

import java.util.Set;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class JavaLocalDateTimeSchema extends Schema {
    public JavaLocalDateTimeSchema(Set<Attribute> attributes) {
        super(attributes);
    }

    @Override
    protected String zodType() {
        return "string().datetime()";
    }
}
