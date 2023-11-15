package sh.ivan.yup;

import cz.habarta.typescript.generator.type.JGenericArrayType;
import cz.habarta.typescript.generator.type.JParameterizedType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import sh.ivan.yup.schema.ArraySchema;
import sh.ivan.yup.schema.Schema;
import sh.ivan.yup.schema.attribute.Attribute;

public class ArraySchemaBuilder {

    private final JavaToYupConverter converter;
    private final AttributeProcessor attributeProcessor;

    public ArraySchemaBuilder(JavaToYupConverter converter, AttributeProcessor attributeProcessor) {
        this.converter = converter;
        this.attributeProcessor = attributeProcessor;
    }

    public Schema build(PropertyDescriptor propertyDescriptor, Set<Attribute> attributes) {
        var componentAnnotatedElements = getComponentAnnotatedElements(propertyDescriptor);
        return build(
                propertyDescriptor.getPropertyModel().getType(),
                attributes,
                attributeProcessor.getAttributes(
                        getComponentType(propertyDescriptor.getPropertyModel().getType()), componentAnnotatedElements));
    }

    private Set<AnnotatedElement> getComponentAnnotatedElements(PropertyDescriptor propertyDescriptor) {
        var annotatedElements = new HashSet<AnnotatedElement>();
        if (propertyDescriptor.getField() != null) {
            var annotatedType = propertyDescriptor.getField().getAnnotatedType();
            var componentAnnotatedElement = getComponentAnnotatedElement(annotatedType);
            if (componentAnnotatedElement != null) {
                annotatedElements.add(componentAnnotatedElement);
            }
        }
        propertyDescriptor.getAnnotatedElements().forEach(annotatedElement -> {
            if (annotatedElement instanceof Method) {
                if (((Method) annotatedElement).getParameterCount() == 0) {
                    var annotatedType = ((Method) annotatedElement).getAnnotatedReturnType();
                    var componentAnnotatedElement = getComponentAnnotatedElement(annotatedType);
                    if (componentAnnotatedElement != null) {
                        annotatedElements.add(componentAnnotatedElement);
                    }
                }
            }
        });
        return annotatedElements;
    }

    private AnnotatedElement getComponentAnnotatedElement(AnnotatedType annotatedType) {
        if (annotatedType instanceof AnnotatedParameterizedType) {
            return ((AnnotatedParameterizedType) annotatedType).getAnnotatedActualTypeArguments()[0];
        }
        return null;
    }

    public Schema build(Type type, Set<Attribute> attributes) {
        return build(type, attributes, Set.of());
    }

    private Schema build(Type type, Set<Attribute> arrayAttributes, Set<Attribute> componentAttributes) {
        var componentType = getComponentType(type);
        return new ArraySchema(converter.buildSchema(componentType, componentAttributes), arrayAttributes);
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
