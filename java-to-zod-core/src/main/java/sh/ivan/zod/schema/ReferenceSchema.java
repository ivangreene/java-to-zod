package sh.ivan.zod.schema;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.zod.schema.attribute.Attribute;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ReferenceSchema extends Schema {
    private final String reference;

    public ReferenceSchema(String reference, Set<Attribute> attributes) {
        super(attributes);
        this.reference = reference;
    }

    @Override
    protected String zodType() {
        throw new UnsupportedOperationException("Implements asZodSchema(String prefix) directly");
    }

    @Override
    public String asZodSchema(String prefix) {
        return prefix + "lazy(() => " + reference + Attribute.writeAttributes(getAttributes()) + ")";
    }
}
