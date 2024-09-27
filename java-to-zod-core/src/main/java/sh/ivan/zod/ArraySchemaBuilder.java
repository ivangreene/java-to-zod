package sh.ivan.zod;

import cz.habarta.typescript.generator.type.JGenericArrayType;
import cz.habarta.typescript.generator.type.JParameterizedType;
import java.lang.reflect.*;
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
        TypeDescriptor componentTypeDescriptor = getComponentTypeDescriptor(typeDescriptor);
        return new ArraySchema(converter.getReferentialSchema(componentTypeDescriptor), attributes);
    }

    private TypeDescriptor getComponentTypeDescriptor(TypeDescriptor typeDescriptor) {
        Type componentType = getComponentType(typeDescriptor.getType());
        Set<AnnotatedElement> componentAnnotatedElements = getComponentAnnotatedElements(typeDescriptor);
        return new TypeDescriptor(componentType, componentAnnotatedElements);
    }

    private Set<AnnotatedElement> getComponentAnnotatedElements(TypeDescriptor typeDescriptor) {
        HashSet<AnnotatedElement> annotatedElements = new HashSet<AnnotatedElement>();
        for (AnnotatedElement annotatedElement : typeDescriptor.getAnnotatedElements()) {
            if (annotatedElement instanceof Method) {
                if (((Method) annotatedElement).getParameterCount() == 0) {
                    AnnotatedType annotatedType = ((Method) annotatedElement).getAnnotatedReturnType();
                    getComponentAnnotatedElement(annotatedType).ifPresent(annotatedElements::add);
                }
            } else if (annotatedElement instanceof Field) {
                AnnotatedType annotatedType = ((Field) annotatedElement).getAnnotatedType();
                getComponentAnnotatedElement(annotatedType).ifPresent(annotatedElements::add);
            } else if (annotatedElement instanceof AnnotatedType) {
                getComponentAnnotatedElement((AnnotatedType) annotatedElement).ifPresent(annotatedElements::add);
            }
        }
        return annotatedElements;
    }

    private Optional<AnnotatedElement> getComponentAnnotatedElement(AnnotatedType annotatedType) {
        if (annotatedType instanceof AnnotatedParameterizedType) {
            return Optional.of(((AnnotatedParameterizedType) annotatedType).getAnnotatedActualTypeArguments()[0]);
        }
        return Optional.empty();
    }

    private Type getComponentType(Type type) {
        if (type instanceof Class<?> && ((Class<?>) type).isArray()) {
            return ((Class<?>) type).getComponentType();
        } else if (type instanceof JParameterizedType) {
            JParameterizedType parameterizedType = (JParameterizedType) type;
            return parameterizedType.getActualTypeArguments()[0];
        } else if (type instanceof JGenericArrayType) {
            return ((JGenericArrayType) type).getGenericComponentType();
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}
