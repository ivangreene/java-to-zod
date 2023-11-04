package sh.ivan.jty;

import sh.ivan.jty.schema.NumberSchema;
import sh.ivan.jty.schema.Schema;
import sh.ivan.jty.schema.StringSchema;

public class Jsr380ToYupConverter {
    private static final ObjectSchemaBuilder OBJECT_SCHEMA_BUILDER = new ObjectSchemaBuilder();

    public Schema buildSchema(Class<?> clazz) {
        if (clazz == String.class) {
            return new StringSchema();
        }
        if (Number.class.isAssignableFrom(clazz)) {
            return new NumberSchema();
        }
        return OBJECT_SCHEMA_BUILDER.build(clazz);
    }
}
