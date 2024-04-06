package sh.ivan.zod.schema;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.zod.schema.attribute.Attribute;

@Getter
@ToString
@EqualsAndHashCode
public abstract class Schema {

    private final Set<Attribute> attributes;

    public Schema(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    protected abstract String zodType();

    protected String zodType(String prefix) {
        return prefix + zodType();
    }

    public final String asZodSchema() {
        return asZodSchema("");
    }

    public String asZodSchema(String prefix) {
        return zodType(prefix) + Attribute.writeAttributes(attributes);
    }
}
