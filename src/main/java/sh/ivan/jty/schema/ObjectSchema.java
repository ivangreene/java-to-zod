package sh.ivan.jty.schema;

import lombok.Data;

import java.util.Collections;
import java.util.Map;

@Data
public class ObjectSchema implements Schema {
    private final Map<String, Schema> fields;

    public ObjectSchema(Map<String, Schema> fields) {
        this.fields = Collections.unmodifiableMap(fields);
    }

    @Override
    public String asYupSchema() {
        var stringBuilder = new StringBuilder("object({ ");
        fields.forEach((name, schema) ->
                stringBuilder.append(name).append(": ").append(schema.asYupSchema()).append(", "));
        return stringBuilder.append("})").toString();
    }
}
