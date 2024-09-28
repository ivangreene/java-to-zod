package sh.ivan.zod;

import cz.habarta.typescript.generator.type.JGenericArrayType;
import cz.habarta.typescript.generator.type.JParameterizedType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import sh.ivan.zod.schema.ArraySchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.attribute.Attribute;

public class ArraySchemaBuilder {

    private final JavaToZodConverter converter;

    public ArraySchemaBuilder(JavaToZodConverter converter) {
        this.converter = converter;
    }

    public Schema build(TypeDescriptor typeDescriptor, Set<Attribute> attributes) {
        var componentTypeDescriptor = getComponentTypeDescriptor(typeDescriptor);
        return new ArraySchema(converter.getReferentialSchema(componentTypeDescriptor), attributes);
    }

    private TypeDescriptor getComponentTypeDescriptor(TypeDescriptor typeDescriptor) {
        var componentType = getComponentType(typeDescriptor.getType());
        var componentAnnotatedElements = getComponentAnnotatedElements(typeDescriptor);
        return new TypeDescriptor(componentType, componentAnnotatedElements);
    }

    private Set<AnnotatedElement> getComponentAnnotatedElements(TypeDescriptor typeDescriptor) {
        var annotatedElements = new HashSet<AnnotatedElement>();
        typeDescriptor.getAnnotatedElements().forEach(annotatedElement -> {
            if (annotatedElement instanceof Method method) {
                if (method.getParameterCount() == 0) {
                    var annotatedType = method.getAnnotatedReturnType();
                    getComponentAnnotatedElement(annotatedType).ifPresent(annotatedElements::add);
                }
            } else if (annotatedElement instanceof Field field) {
                var annotatedType = field.getAnnotatedType();
                getComponentAnnotatedElement(annotatedType).ifPresent(annotatedElements::add);
            } else if (annotatedElement instanceof AnnotatedType annotatedType) {
                getComponentAnnotatedElement(annotatedType).ifPresent(annotatedElements::add);
            }
        });
        return annotatedElements;
    }

    private Optional<AnnotatedElement> getComponentAnnotatedElement(AnnotatedType annotatedType) {
        if (annotatedType instanceof AnnotatedParameterizedType annotatedParameterizedType) {
            return Optional.of(annotatedParameterizedType.getAnnotatedActualTypeArguments()[0]);
        }
        return Optional.empty();
    }

    private Type getComponentType(Type type) {
        if (type instanceof Class<?> clazz && clazz.isArray()) {
            return clazz.getComponentType();
        } else if (type instanceof JParameterizedType parameterizedType) {
            return parameterizedType.getActualTypeArguments()[0];
        } else if (type instanceof JGenericArrayType genericArrayType) {
            return genericArrayType.getGenericComponentType();
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}
