package sh.ivan.zod;

import cz.habarta.typescript.generator.type.JParameterizedType;
import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import sh.ivan.zod.schema.RecordSchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.attribute.Attribute;

public class RecordSchemaBuilder {

    private final JavaToZodConverter converter;

    public RecordSchemaBuilder(JavaToZodConverter converter) {
        this.converter = converter;
    }

    public Schema build(TypeDescriptor typeDescriptor, Set<Attribute> attributes) {
        TypeDescriptor keyTypeDescriptor = getKeyTypeDescriptor(typeDescriptor);
        TypeDescriptor valueTypeDescriptor = getValueTypeDescriptor(typeDescriptor);

        return new RecordSchema(
                converter.getReferentialSchema(keyTypeDescriptor),
                converter.getReferentialSchema(valueTypeDescriptor),
                attributes);
    }

    private TypeDescriptor getKeyTypeDescriptor(TypeDescriptor typeDescriptor) {
        Type keyType = getKeyType(typeDescriptor.getType());
        Set<AnnotatedElement> keyAnnotatedElements = getKeyAnnotatedElements(typeDescriptor);
        return new TypeDescriptor(keyType, keyAnnotatedElements);
    }

    private TypeDescriptor getValueTypeDescriptor(TypeDescriptor typeDescriptor) {
        Type valueType = getValueType(typeDescriptor.getType());
        Set<AnnotatedElement> valueAnnotatedElements = getValueAnnotatedElements(typeDescriptor);
        return new TypeDescriptor(valueType, valueAnnotatedElements);
    }

    private Set<AnnotatedElement> getKeyAnnotatedElements(TypeDescriptor typeDescriptor) {
        HashSet<AnnotatedElement> annotatedElements = new HashSet<>();
        for (AnnotatedElement annotatedElement : typeDescriptor.getAnnotatedElements()) {
            if (annotatedElement instanceof Method) {
                if (((Method) annotatedElement).getParameterCount() == 0) {
                    AnnotatedType annotatedType = ((Method) annotatedElement).getAnnotatedReturnType();
                    getKeyAnnotatedElement(annotatedType).ifPresent(annotatedElements::add);
                }
            } else if (annotatedElement instanceof Field) {
                AnnotatedType annotatedType = ((Field) annotatedElement).getAnnotatedType();
                getKeyAnnotatedElement(annotatedType).ifPresent(annotatedElements::add);
            } else if (annotatedElement instanceof AnnotatedType) {
                getKeyAnnotatedElement((AnnotatedType) annotatedElement).ifPresent(annotatedElements::add);
            }
        }
        return annotatedElements;
    }

    private Optional<AnnotatedElement> getKeyAnnotatedElement(AnnotatedType annotatedType) {
        if (annotatedType instanceof AnnotatedParameterizedType) {
            return Optional.of(((AnnotatedParameterizedType) annotatedType).getAnnotatedActualTypeArguments()[0]);
        }
        return Optional.empty();
    }

    private Set<AnnotatedElement> getValueAnnotatedElements(TypeDescriptor typeDescriptor) {
        HashSet<AnnotatedElement> annotatedElements = new HashSet<>();
        for (AnnotatedElement annotatedElement : typeDescriptor.getAnnotatedElements()) {
            if (annotatedElement instanceof Method) {
                if (((Method) annotatedElement).getParameterCount() == 0) {
                    AnnotatedType annotatedType = ((Method) annotatedElement).getAnnotatedReturnType();
                    getValueAnnotatedElement(annotatedType).ifPresent(annotatedElements::add);
                }
            } else if (annotatedElement instanceof Field) {
                AnnotatedType annotatedType = ((Field) annotatedElement).getAnnotatedType();
                getValueAnnotatedElement(annotatedType).ifPresent(annotatedElements::add);
            } else if (annotatedElement instanceof AnnotatedType) {
                getValueAnnotatedElement((AnnotatedType) annotatedElement).ifPresent(annotatedElements::add);
            }
        }
        return annotatedElements;
    }

    private Optional<AnnotatedElement> getValueAnnotatedElement(AnnotatedType annotatedType) {
        if (annotatedType instanceof AnnotatedParameterizedType) {
            return Optional.of(((AnnotatedParameterizedType) annotatedType).getAnnotatedActualTypeArguments()[1]);
        }
        return Optional.empty();
    }

    private Type getKeyType(Type type) {
        if (type instanceof JParameterizedType parameterizedType) {
            return parameterizedType.getActualTypeArguments()[0]; // Key type
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    private Type getValueType(Type type) {
        if (type instanceof JParameterizedType parameterizedType) {
            return parameterizedType.getActualTypeArguments()[1]; // Value type
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}
