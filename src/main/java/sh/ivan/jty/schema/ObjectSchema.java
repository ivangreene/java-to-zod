package sh.ivan.jty.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import sh.ivan.jty.schema.attribute.Attribute;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ObjectSchema extends Schema {
    private final Map<String, Schema> fields;

    public ObjectSchema(Map<String, Schema> fields, Set<Attribute> attributes) {
        super(attributes);
        this.fields = Collections.unmodifiableMap(fields);
    }

    @Override
    public String yupType() {
        var stringBuilder = new StringBuilder("object({ ");
        fields.forEach((name, schema) ->
                stringBuilder.append(name).append(": ").append(schema.yupType()).append(", "));
        return stringBuilder.append("})").toString();
    }
}
