package sh.ivan.yup;

import cz.habarta.typescript.generator.parser.PropertyModel;
import java.beans.Introspector;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;

@Data
public class PropertyDescriptor {
    private final Type type;
    private final Set<AnnotatedElement> annotatedElements;

    public PropertyDescriptor(Class<?> container, PropertyModel propertyModel) {
        this.type = propertyModel.getType();
        var annotatedElements = new HashSet<AnnotatedElement>();
        getField(container, propertyModel).ifPresent(annotatedElements::add);
        annotatedElements.addAll(getAllMethods(propertyModel));
        this.annotatedElements = Set.copyOf(annotatedElements);
    }

    public PropertyDescriptor(Type type, Set<AnnotatedElement> annotatedElements) {
        this.type = type;
        this.annotatedElements = annotatedElements;
    }

    private Set<Method> getAllMethods(PropertyModel propertyModel) {
        if (!(propertyModel.getOriginalMember() instanceof Method)) {
            return Set.of();
        }
        return Stream.iterate((Method) propertyModel.getOriginalMember(), Objects::nonNull, method -> {
                    if (method.getDeclaringClass().getSuperclass() == Object.class) {
                        return null;
                    }
                    try {
                        return method.getDeclaringClass()
                                .getSuperclass()
                                .getDeclaredMethod(method.getName(), method.getParameterTypes());
                    } catch (NoSuchMethodException ignored) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Optional<Field> getField(Class<?> container, PropertyModel propertyModel) {
        if (container == Object.class) {
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
