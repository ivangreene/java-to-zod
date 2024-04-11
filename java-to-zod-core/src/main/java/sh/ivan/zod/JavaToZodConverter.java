package sh.ivan.zod;

import com.google.common.collect.Sets;
import cz.habarta.typescript.generator.parser.Model;
import cz.habarta.typescript.generator.parser.ModelParser;
import cz.habarta.typescript.generator.type.JGenericArrayType;
import cz.habarta.typescript.generator.type.JParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import sh.ivan.zod.schema.BooleanSchema;
import sh.ivan.zod.schema.DateSchema;
import sh.ivan.zod.schema.EnumSchema;
import sh.ivan.zod.schema.LiteralBooleanSchema;
import sh.ivan.zod.schema.NumberSchema;
import sh.ivan.zod.schema.ObjectSchema;
import sh.ivan.zod.schema.ReferenceSchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.StringSchema;
import sh.ivan.zod.schema.attribute.Attribute;
import sh.ivan.zod.schema.attribute.EqualsBooleanAttribute;
import sh.ivan.zod.schema.attribute.IntegerAttribute;
import sh.ivan.zod.schema.attribute.UuidAttribute;

public class JavaToZodConverter {

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

    private final AttributeProcessor attributeProcessor;
    private final ObjectSchemaBuilder objectSchemaBuilder;
    private final ArraySchemaBuilder arraySchemaBuilder;

    private final Configuration configuration;

    public JavaToZodConverter(ModelParser modelParser, Configuration configuration) {
        this.attributeProcessor = new AttributeProcessor();
        objectSchemaBuilder = new ObjectSchemaBuilder(this, modelParser);
        arraySchemaBuilder = new ArraySchemaBuilder(this);
        this.configuration = configuration;
    }

    public Map<String, ObjectSchema> getBeanSchemas(Model model) {
        return objectSchemaBuilder.buildBeanSchemas(model);
    }

    public Schema buildSchema(Class<?> clazz) {
        return buildSchema(clazz, Set.of());
    }

    public Schema buildSchema(Type type, Set<Attribute> attributes) {
        return buildSchema(type, null, attributes, false);
    }

    private Schema buildSchema(
            Type type, TypeDescriptor typeDescriptor, Set<Attribute> attributes, boolean useReferenceForObject) {
        if (type == String.class) {
            return new StringSchema(attributes);
        }
        if (type == UUID.class) {
            return new StringSchema(Sets.union(attributes, Set.of(new UuidAttribute())));
        }
        if (isEnum(type)) {
            @SuppressWarnings("unchecked")
            var enumClass = (Class<? extends Enum<?>>) type;
            return new EnumSchema(enumClass, attributes);
        }
        if (isDate(type)) {
            return new DateSchema(attributes);
        }
        if (isNumber(type)) {
            return buildNumberSchema((Class<?>) type, attributes);
        }
        if (type == Boolean.class || type == boolean.class) {
            return getBooleanSchema(attributes);
        }
        if (isArray(type)) {
            return arraySchemaBuilder.build(typeDescriptor, attributes);
        }
        if (useReferenceForObject) {
            return new ReferenceSchema(getSchemaName(type), attributes);
        }
        return objectSchemaBuilder.build((Class<?>) type, attributes);
    }

    private Schema getBooleanSchema(Set<Attribute> attributes) {
        var equalsBooleanAttribute = attributes.stream()
                .filter(attribute -> attribute instanceof EqualsBooleanAttribute)
                .findAny();
        if (equalsBooleanAttribute.isPresent()) {
            return new LiteralBooleanSchema(
                    ((EqualsBooleanAttribute) equalsBooleanAttribute.get()).isValue(),
                    Sets.difference(attributes, Set.of(equalsBooleanAttribute.get())));
        }
        return new BooleanSchema(attributes);
    }

    private boolean isNumber(Type type) {
        return type instanceof Class<?>
                && (Number.class.isAssignableFrom((Class<?>) type) || PRIMITIVE_NUMBER_TYPES.contains(type));
    }

    private boolean isDate(Type type) {
        return type == Date.class || type == Instant.class;
    }

    private boolean isEnum(Type type) {
        return type instanceof Class<?> && ((Class<?>) type).isEnum();
    }

    private NumberSchema buildNumberSchema(Class<?> clazz, Set<Attribute> attributes) {
        if (INTEGRAL_TYPES.contains(clazz)) {
            return new NumberSchema(Sets.union(attributes, Set.of(new IntegerAttribute())));
        }
        return new NumberSchema(attributes);
    }

    public boolean isArray(Type type) {
        return type instanceof Class<?> && ((Class<?>) type).isArray()
                || type instanceof JParameterizedType && ((JParameterizedType) type).getRawType() == List.class
                || type instanceof JGenericArrayType;
    }

    Schema getReferentialSchema(TypeDescriptor typeDescriptor) {
        var attributes =
                attributeProcessor.getAttributes(typeDescriptor.getType(), typeDescriptor.getAnnotatedElements());
        return buildSchema(typeDescriptor.getType(), typeDescriptor, attributes, true);
    }

    protected String getSchemaName(Type type) {
        if (type instanceof Class<?>) {
            return configuration.getSchemaNamePrefix()
                    + ((Class<?>) type).getSimpleName()
                    + configuration.getSchemaNameSuffix();
        }
        throw new IllegalArgumentException("No name for type " + type);
    }
}
