package sh.ivan.jty.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import sh.ivan.jty.schema.attribute.Attribute;

import java.util.Set;

@Getter
@EqualsAndHashCode
public abstract class Schema {

    private final Set<Attribute> attributes;

    public Schema(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    public abstract String yupType();

    public String asYupSchema() {
        return yupType();
    }
}
