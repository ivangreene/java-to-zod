package sh.ivan.yup;

import cz.habarta.typescript.generator.DefaultTypeProcessor;
import cz.habarta.typescript.generator.Settings;
import cz.habarta.typescript.generator.parser.Jackson2Parser;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import sh.ivan.yup.schema.NumberSchema;
import sh.ivan.yup.schema.ObjectSchema;
import sh.ivan.yup.schema.ReferenceSchema;
import sh.ivan.yup.schema.Schema;
import sh.ivan.yup.schema.StringSchema;
import sh.ivan.yup.schema.attribute.Attribute;

public class Jsr380ToYupConverter {

    private static final Set<Class<? extends Schema>> PRIMITIVE_SCHEMA_TYPES =
            Set.of(StringSchema.class, NumberSchema.class);

    private final ObjectSchemaBuilder objectSchemaBuilder;

    public Jsr380ToYupConverter() {
        objectSchemaBuilder = new ObjectSchemaBuilder(
                this, new AttributeProcessor(), new Jackson2Parser(new Settings(), new DefaultTypeProcessor()));
    }

    public Schema buildSchema(Class<?> clazz) {
        return buildSchema(clazz, Set.of());
    }

    public Schema buildSchema(Class<?> clazz, Set<Attribute> attributes) {
        Class<? extends Schema> schemaClass = getSchemaClass(clazz);

        if (PRIMITIVE_SCHEMA_TYPES.contains(schemaClass)) {
            try {
                return schemaClass.getConstructor(Set.class).newInstance(attributes);
            } catch (InstantiationException
                    | IllegalAccessException
                    | InvocationTargetException
                    | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return objectSchemaBuilder.build(clazz);
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

    Schema getPropertySchema(Class<?> clazz, Set<Attribute> attributes) {
        Class<? extends Schema> schemaClass = getSchemaClass(clazz);
        if (ObjectSchema.class == schemaClass) {
            return new ReferenceSchema(getTypeName(clazz), attributes);
        }
        return buildSchema(clazz, attributes);
    }

    private String getTypeName(Class<?> clazz) {
        return clazz.getSimpleName();
    }
}
