package sh.ivan.yup.schema;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.yup.schema.attribute.Attribute;

@Getter
@ToString
@EqualsAndHashCode
public abstract class Schema {

    private final Set<Attribute> attributes;

    public Schema(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    protected abstract String yupType();

    protected String yupType(String prefix) {
        return prefix + yupType();
    }

    public final String asYupSchema() {
        return asYupSchema("");
    }

    public String asYupSchema(String prefix) {
        return yupType(prefix) + Attribute.writeAttributes(attributes);
    }
}
