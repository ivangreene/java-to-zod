package sh.ivan.yup;

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
import sh.ivan.yup.schema.ArraySchema;
import sh.ivan.yup.schema.Schema;
import sh.ivan.yup.schema.attribute.Attribute;

public class ArraySchemaBuilder {

    private final JavaToYupConverter converter;

    public ArraySchemaBuilder(JavaToYupConverter converter) {
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
            if (annotatedElement instanceof Method) {
                if (((Method) annotatedElement).getParameterCount() == 0) {
                    var annotatedType = ((Method) annotatedElement).getAnnotatedReturnType();
                    getComponentAnnotatedElement(annotatedType).ifPresent(annotatedElements::add);
                }
            } else if (annotatedElement instanceof Field) {
                var annotatedType = ((Field) annotatedElement).getAnnotatedType();
                getComponentAnnotatedElement(annotatedType).ifPresent(annotatedElements::add);
            } else if (annotatedElement instanceof AnnotatedType) {
                getComponentAnnotatedElement((AnnotatedType) annotatedElement).ifPresent(annotatedElements::add);
            }
        });
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
            var parameterizedType = (JParameterizedType) type;
            return parameterizedType.getActualTypeArguments()[0];
        } else if (type instanceof JGenericArrayType) {
            return ((JGenericArrayType) type).getGenericComponentType();
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}
