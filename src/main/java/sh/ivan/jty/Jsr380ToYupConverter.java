package sh.ivan.jty;

import sh.ivan.jty.schema.NumberSchema;
import sh.ivan.jty.schema.ObjectSchema;
import sh.ivan.jty.schema.ReferenceSchema;
import sh.ivan.jty.schema.Schema;
import sh.ivan.jty.schema.StringSchema;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class Jsr380ToYupConverter {

    private static final Set<Class<? extends Schema>> PRIMITIVE_SCHEMA_TYPES = Set.of(
            StringSchema.class,
            NumberSchema.class
    );

    private static final ObjectSchemaBuilder OBJECT_SCHEMA_BUILDER = new ObjectSchemaBuilder();

    public Schema buildSchema(Class<?> clazz) {
        Class<? extends Schema> schemaClass = getSchemaClass(clazz);
        if (PRIMITIVE_SCHEMA_TYPES.contains(schemaClass)) {
            try {
                return schemaClass.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return OBJECT_SCHEMA_BUILDER.build(clazz);
    }

    private Class<? extends Schema> getSchemaClass(Class<?> clazz) {
        if (clazz == String.class) {
            return StringSchema.class;
        }
        if (Number.class.isAssignableFrom(clazz)) {
            return NumberSchema.class;
        }
        return ObjectSchema.class;
    }

    Schema getPropertySchema(Class<?> clazz) {
        Class<? extends Schema> schemaClass = getSchemaClass(clazz);
        if (ObjectSchema.class == schemaClass) {
            return new ReferenceSchema(getTypeName(clazz));
        }
        return buildSchema(clazz);
    }

    private String getTypeName(Class<?> clazz) {
        return clazz.getSimpleName();
    }
}
