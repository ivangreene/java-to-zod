package sh.ivan.zod.schema;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.zod.schema.attribute.Attribute;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ObjectSchema extends Schema {
    private final Map<String, Schema> fields;

    public ObjectSchema(Map<String, Schema> fields, Set<Attribute> attributes) {
        super(attributes);
        this.fields = Collections.unmodifiableMap(fields);
    }

    @Override
    protected String zodType(String prefix) {
        // TODO: Actually implement formatting
        StringBuilder stringBuilder =
                new StringBuilder(prefix).append("object({").append(prefix.isEmpty() ? " " : "");
        fields.forEach((name, schema) -> stringBuilder
                .append(prefix.isEmpty() ? "" : "\n  ")
                .append(name)
                .append(": ")
                .append(schema.asZodSchema(prefix))
                .append(",")
                .append(prefix.isEmpty() ? " " : ""));
        return stringBuilder
                .append(!prefix.isEmpty() && !fields.isEmpty() ? "\n" : "")
                .append("})")
                .toString();
    }

    @Override
    protected String zodType() {
        throw new UnsupportedOperationException("Implements zodType(String prefix) directly");
    }
}
