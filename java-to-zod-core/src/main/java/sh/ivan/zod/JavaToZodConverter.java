package sh.ivan.zod;

import com.google.common.collect.Sets;
import cz.habarta.typescript.generator.parser.Model;
import cz.habarta.typescript.generator.parser.ModelParser;
import cz.habarta.typescript.generator.type.JGenericArrayType;
import cz.habarta.typescript.generator.type.JParameterizedType;
import sh.ivan.zod.schema.*;
import sh.ivan.zod.schema.attribute.Attribute;
import sh.ivan.zod.schema.attribute.EqualsBooleanAttribute;
import sh.ivan.zod.schema.attribute.IntegerAttribute;
import sh.ivan.zod.schema.attribute.UuidAttribute;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.time.*;
import java.util.*;

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

    private static final Set<Class<?>> JAVA_ZONED_DATE_TIME_TYPES = Set.of(
            ZonedDateTime.class
    );
    private static final Set<Class<?>> JAVA_LOCAL_DATE_TIME_TYPES = Set.of(
            Instant.class,
            LocalDateTime.class
    );
    private static final Set<Class<?>> JAVA_LOCAL_DATE = Set.of(
            LocalDate.class
    );
    private static final Set<Class<?>> JAVA_LOCAL_TIME_TYPES = Set.of(
            LocalTime.class
    );
    public static final String ZOD_ANY = "any()";

    private final AttributeProcessor attributeProcessor;
    private final ObjectSchemaBuilder objectSchemaBuilder;
    private final ArraySchemaBuilder arraySchemaBuilder;
    private final RecordSchemaBuilder recordSchemaBuilder;

    private final Configuration configuration;

    public JavaToZodConverter(ModelParser modelParser, Configuration configuration) {
        this.attributeProcessor = new AttributeProcessor();
        objectSchemaBuilder = new ObjectSchemaBuilder(this, modelParser);
        arraySchemaBuilder = new ArraySchemaBuilder(this);
        recordSchemaBuilder = new RecordSchemaBuilder(this);
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
            Class<? extends Enum<? extends Enum<?>>> enumClass = (Class<? extends Enum<?>>) type;
            return new EnumSchema(enumClass, attributes);
        }
        if (isDate(type)) {
            return new DateSchema(attributes);
        }
        if (isJavaDateTime(type)) {
            return new JavaDateTimeSchema(attributes);
        }
        if (isJavaLocalDate(type)) {
            return new JavaLocalDateSchema(attributes);
        }
        if (isJavaLocalDateTime(type)) {
            return new JavaLocalDateTimeSchema(attributes);
        }
        if (isJavaLocalTime(type)) {
            return new JavaLocalTimeSchema(attributes);
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
        if (isMap(type)) {
            return recordSchemaBuilder.build(typeDescriptor, attributes);
        }
        if (useReferenceForObject) {

            return new ReferenceSchema(getSchemaName(type), attributes);
        }
        return objectSchemaBuilder.build((Class<?>) type, attributes);
    }

    private boolean isMap(Type type) {
        return type instanceof JParameterizedType && ((JParameterizedType) type).getRawType() == Map.class;

    }

    private Schema getBooleanSchema(Set<Attribute> attributes) {
        Optional<Attribute> equalsBooleanAttribute = attributes.stream()
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
        return type == Date.class;
    }

    private boolean isJavaDateTime(Type type) {
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            return JAVA_ZONED_DATE_TIME_TYPES.contains(clazz);
        }
        return false;
    }
    private boolean isJavaLocalTime(Type type) {
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            return JAVA_LOCAL_TIME_TYPES.contains(clazz);
        }
        return false;
    }
    private boolean isJavaLocalDateTime(Type type) {
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            return JAVA_LOCAL_DATE_TIME_TYPES.contains(clazz);
        }
        return false;
    }
    private boolean isJavaLocalDate(Type type) {
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            return JAVA_LOCAL_DATE.contains(clazz);
        }
        return false;
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
        Set<Attribute> attributes =
                attributeProcessor.getAttributes(typeDescriptor.getType(), typeDescriptor.getAnnotatedElements());
        return buildSchema(typeDescriptor.getType(), typeDescriptor, attributes, true);
    }

    protected String getSchemaName(Type type) {
        if (type instanceof Class<?>) {
            return configuration.getSchemaNamePrefix()
                    + ((Class<?>) type).getSimpleName()
                    + configuration.getSchemaNameSuffix();
        }
        System.out.printf("No name for %s%n", type.toString());
        return ZOD_ANY;
    }
}
