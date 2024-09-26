package sh.ivan.zod;

import cz.habarta.typescript.generator.parser.PropertyModel;
import lombok.Data;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class TypeDescriptor {
    private final Type type;
    private final Set<AnnotatedElement> annotatedElements;

    public TypeDescriptor(Class<?> container, PropertyModel propertyModel) {
        this.type = propertyModel.getType();
        HashSet<AnnotatedElement> annotatedElements = new HashSet<>();
        // Check if the class is a record
        if (container.isRecord()) {
            addAnnotationsFromRecordConstructor(container, propertyModel, annotatedElements);
        } else {
            // Existing field handling
            getField(container, propertyModel).ifPresent(annotatedElements::add);
            annotatedElements.addAll(getAllMethods(propertyModel));
        }
        this.annotatedElements = Set.copyOf(annotatedElements);
    }

    private static void addAnnotationsFromRecordConstructor(Class<?> container, PropertyModel propertyModel, HashSet<AnnotatedElement> annotatedElements) {
        // Get the primary constructor of the record
        Constructor<?>[] constructors = container.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == container.getRecordComponents().length) {
                // We found the primary constructor
                Parameter[] parameters = constructor.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    // Match the parameter with the record component
                    if (parameters[i].getName().equals(propertyModel.getOriginalMember().getName())) {
                        // Get the annotations from the constructor parameter
                        Annotation[] annotations = parameters[i].getAnnotations();
                        // Process these annotations
                        for (Annotation annotation : annotations) {
                            annotatedElements.add(parameters[i]); // Or handle as needed
                        }
                    }
                }
            }
        }
    }

    public TypeDescriptor(Type type, Set<AnnotatedElement> annotatedElements) {
        this.type = type;
        this.annotatedElements = annotatedElements;
    }

    private Set<Method> getAllMethods(PropertyModel propertyModel) {
        if (propertyModel.getOriginalMember() instanceof Method method) {

            // Check if this is a record and handle accessor methods for record components
            if (method.getDeclaringClass().isRecord()) {
                return Set.of(method);
            }

            // Regular class handling
            return Stream.iterate(method, Objects::nonNull, m -> {
                        if (m.getDeclaringClass().getSuperclass() == Object.class) {
                            return null;
                        }
                        return Optional.of(m)
                                .map(Method::getDeclaringClass)
                                .map(Class::getSuperclass)
                                .map(clazz -> {
                                    try {
                                        return clazz.getDeclaredMethod(m.getName(), m.getParameterTypes());
                                    } catch (NoSuchMethodException ignored) {
                                        return null;
                                    }
                                }).orElse(null);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    private Optional<Field> getField(Class<?> container, PropertyModel propertyModel) {
        if (container == null || container == Object.class) {
            return Optional.empty();
        }

        // Check if the class is a record
        if (container.isRecord()) {
            try {
                // Get the accessor method for the record component
                return Optional.of(container.getDeclaredField(propertyModel.getOriginalMember().getName()));
            } catch (NoSuchFieldException e) {
                return Optional.empty();
            }
        }

        // Handle ordinary classes.
        if (propertyModel.getOriginalMember() instanceof Field) {
            return Optional.of((Field) propertyModel.getOriginalMember());
        }
        try {
            return Optional.of(container.getDeclaredField(
                    getFieldNameFromGetter(propertyModel.getOriginalMember().getName())));
        } catch (NoSuchFieldException ignored) {
            return getField(container.getSuperclass(), propertyModel);
        }
    }

    private String getFieldNameFromGetter(String getterName) {
        return Introspector.decapitalize(getterName.replaceFirst("^get", ""));
    }
}
