package sh.ivan.zod.schema;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.zod.schema.attribute.Attribute;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RecordSchema extends Schema {
    private final Schema keyTypeDescriptor;
    private final Schema valueTypeDescriptor;

    public RecordSchema(Schema keyTypeDescriptor, Schema valueTypeDescriptor, Set<Attribute> attributes) {
        super(attributes);
        this.keyTypeDescriptor = keyTypeDescriptor;
        this.valueTypeDescriptor = valueTypeDescriptor;
    }

    @Override
    protected String zodType(String prefix) {
        return String.format(
                "%srecord(%s, %s)",
                prefix, keyTypeDescriptor.asZodSchema(prefix), valueTypeDescriptor.asZodSchema(prefix));
    }

    @Override
    protected String zodType() {
        throw new UnsupportedOperationException("Implements zodType(String prefix) directly");
    }
}
