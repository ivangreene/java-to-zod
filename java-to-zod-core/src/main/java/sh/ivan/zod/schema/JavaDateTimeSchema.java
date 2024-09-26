package sh.ivan.zod.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.zod.schema.attribute.Attribute;

import java.util.Set;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class JavaDateTimeSchema extends Schema {
    public JavaDateTimeSchema(Set<Attribute> attributes) {
        super(attributes);
    }

    @Override
    protected String zodType() {
        return "string().datetime({ offset: true})";
    }
}
