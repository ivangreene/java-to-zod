package sh.ivan.yup;

import com.google.common.collect.Sets;
import cz.habarta.typescript.generator.DefaultTypeProcessor;
import cz.habarta.typescript.generator.Settings;
import cz.habarta.typescript.generator.parser.Jackson2Parser;
import cz.habarta.typescript.generator.type.JParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import sh.ivan.yup.schema.ArraySchema;
import sh.ivan.yup.schema.BooleanSchema;
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
    private final ArraySchemaBuilder arraySchemaBuilder;

    public Jsr380ToYupConverter() {
        objectSchemaBuilder = new ObjectSchemaBuilder(
                this, new AttributeProcessor(), new Jackson2Parser(new Settings(), new DefaultTypeProcessor()));
        arraySchemaBuilder = new ArraySchemaBuilder();
    }

    public Schema buildSchema(Class<?> clazz) {
        return buildSchema(clazz, Set.of());
    }

    public Schema buildSchema(Type type, Set<Attribute> attributes) {
        if (type == String.class) {
            return new StringSchema(attributes);
        }
        if (isNumber(type)) {
            return buildNumberSchema((Class<?>) type, attributes);
        }
        if (type == Boolean.class || type == boolean.class) {
            return new BooleanSchema(attributes);
        }
        if (isArray(type)) {
            return arraySchemaBuilder.build(type, attributes);
        }
        return objectSchemaBuilder.build((Class<?>) type, attributes);
    }

    private boolean isNumber(Type type) {
        return type instanceof Class<?>
                && (Number.class.isAssignableFrom((Class<?>) type) || PRIMITIVE_NUMBER_TYPES.contains(type));
    }

    private NumberSchema buildNumberSchema(Class<?> clazz, Set<Attribute> attributes) {
        if (INTEGRAL_TYPES.contains(clazz)) {
            return new NumberSchema(Sets.union(attributes, Set.of(new IntegerAttribute())));
        }
        return new NumberSchema(attributes);
    }

    private Class<? extends Schema> getSchemaClass(Type type) {
        if (type == String.class) {
            return StringSchema.class;
        }
        if (isNumber(type)) {
            return NumberSchema.class;
        }
        if (type == Boolean.class || type == boolean.class) {
            return BooleanSchema.class;
        }
        if (isArray(type)) {
            return ArraySchema.class;
        }
        return ObjectSchema.class;
    }

    private boolean isArray(Type type) {
        return type instanceof Class<?> && ((Class<?>) type).isArray()
                || type instanceof JParameterizedType && ((JParameterizedType) type).getRawType() == List.class;
    }

    Schema getPropertySchema(Type type, Set<Attribute> attributes) {
        Class<? extends Schema> schemaClass = getSchemaClass(type);
        if (ObjectSchema.class == schemaClass) {
            return new ReferenceSchema(getTypeName(type), attributes);
        }
        return buildSchema(type, attributes);
    }

    private String getTypeName(Type type) {
        if (type instanceof Class<?>) {
            return ((Class<?>) type).getSimpleName();
        }
        throw new IllegalArgumentException("No name for type");
    }
}
