package sh.ivan.zod;

import cz.habarta.typescript.generator.parser.PropertyModel;
import java.beans.Introspector;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;

@Data
public class TypeDescriptor {
    private final Type type;
    private final Set<AnnotatedElement> annotatedElements;

    public TypeDescriptor(Class<?> container, PropertyModel propertyModel) {
        this.type = propertyModel.getType();
        HashSet<AnnotatedElement> annotatedElements = new HashSet<>();
        if (container.isRecord()) {
            getParameterFromRecordConstructor(container, propertyModel).ifPresent(annotatedElements::add);
        } else {
            getField(container, propertyModel).ifPresent(annotatedElements::add);
        }
        annotatedElements.addAll(getAllMethods(propertyModel));
        this.annotatedElements = Set.copyOf(annotatedElements);
    }

    public TypeDescriptor(Type type, Set<AnnotatedElement> annotatedElements) {
        this.type = type;
        this.annotatedElements = annotatedElements;
    }

    private Optional<Parameter> getParameterFromRecordConstructor(Class<?> container, PropertyModel propertyModel) {
        return Stream.of(container.getDeclaredConstructors())
                .filter(constructor -> constructor.getParameterCount() == container.getRecordComponents().length)
                .flatMap(constructor -> Stream.of(constructor.getParameters()))
                .filter(parameter -> parameter
                        .getName()
                        .equals(propertyModel.getOriginalMember().getName()))
                .findAny();
    }

    private Set<Method> getAllMethods(PropertyModel propertyModel) {
        if (propertyModel.getOriginalMember() instanceof Method method) {

            if (method.getDeclaringClass().isRecord()) {
                return Set.of(method);
            }

            return Stream.iterate(method, Objects::nonNull, m -> {
                        if (m.getDeclaringClass().getSuperclass() == Object.class) {
                            return null;
                        }
                        try {
                            return m.getDeclaringClass()
                                    .getSuperclass()
                                    .getDeclaredMethod(m.getName(), m.getParameterTypes());
                        } catch (NoSuchMethodException ignored) {
                            return null;
                        }
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
