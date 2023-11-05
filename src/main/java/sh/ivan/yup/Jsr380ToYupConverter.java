package sh.ivan.yup;

import com.google.common.collect.Sets;
import cz.habarta.typescript.generator.DefaultTypeProcessor;
import cz.habarta.typescript.generator.Settings;
import cz.habarta.typescript.generator.parser.Jackson2Parser;
import java.math.BigInteger;
import java.util.Set;
import sh.ivan.yup.schema.NumberSchema;
import sh.ivan.yup.schema.ObjectSchema;
import sh.ivan.yup.schema.ReferenceSchema;
import sh.ivan.yup.schema.Schema;
import sh.ivan.yup.schema.StringSchema;
import sh.ivan.yup.schema.attribute.Attribute;
import sh.ivan.yup.schema.attribute.IntegerAttribute;

public class Jsr380ToYupConverter {

    private static final Set<Class<?>> PRIMITIVE_NUMBER_TYPES =
            Set.of(int.class, long.class, float.class, double.class, byte.class, short.class);

    private static final Set<Class<?>> INTEGRAL_TYPES = Set.of(
            int.class,
            long.class,
            byte.class,
            short.class,
            Integer.class,
            Long.class,
            Byte.class,
            Short.class,
            BigInteger.class);

    private final ObjectSchemaBuilder objectSchemaBuilder;

    public Jsr380ToYupConverter() {
        objectSchemaBuilder = new ObjectSchemaBuilder(
                this, new AttributeProcessor(), new Jackson2Parser(new Settings(), new DefaultTypeProcessor()));
    }

    public Schema buildSchema(Class<?> clazz) {
        return buildSchema(clazz, Set.of());
    }

    public Schema buildSchema(Class<?> clazz, Set<Attribute> attributes) {
        if (clazz == String.class) {
            return new StringSchema(attributes);
        }
        if (isNumber(clazz)) {
            return buildNumberSchema(clazz, attributes);
        }
        return objectSchemaBuilder.build(clazz);
    }

    private boolean isNumber(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz) || PRIMITIVE_NUMBER_TYPES.contains(clazz);
    }

    private NumberSchema buildNumberSchema(Class<?> clazz, Set<Attribute> attributes) {
        if (INTEGRAL_TYPES.contains(clazz)) {
            return new NumberSchema(Sets.union(attributes, Set.of(new IntegerAttribute())));
        }
        return new NumberSchema(attributes);
    }

    private Class<? extends Schema> getSchemaClass(Class<?> clazz) {
        if (clazz == String.class) {
            return StringSchema.class;
        }
        if (isNumber(clazz)) {
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
