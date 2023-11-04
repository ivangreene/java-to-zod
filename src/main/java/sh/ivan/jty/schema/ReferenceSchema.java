package sh.ivan.jty.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import sh.ivan.jty.schema.attribute.Attribute;

import java.util.Set;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ReferenceSchema extends Schema {
    private final String reference;

    public ReferenceSchema(String reference, Set<Attribute> attributes) {
        super(attributes);
        this.reference = reference;
    }

    @Override
    public String yupType() {
        return reference;
    }
}
