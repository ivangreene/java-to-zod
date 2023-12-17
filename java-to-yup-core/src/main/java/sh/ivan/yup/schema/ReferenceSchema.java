package sh.ivan.yup.schema;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.yup.schema.attribute.Attribute;

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
    protected String yupType() {
        throw new UnsupportedOperationException("Implements asYupSchema(String prefix) directly");
    }

    @Override
    public String asYupSchema(String prefix) {
        return prefix + "lazy(() => " + reference + ".default(undefined)" + Attribute.writeAttributes(getAttributes())
                + ")";
    }
}
