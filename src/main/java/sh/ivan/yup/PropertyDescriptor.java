package sh.ivan.yup;

import cz.habarta.typescript.generator.parser.PropertyModel;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class PropertyDescriptor {
    private final PropertyModel propertyModel;
    private final Field field;
    private final Set<AnnotatedElement> annotatedElements;

    public PropertyDescriptor(Class<?> container, PropertyModel propertyModel) {
        this.propertyModel = propertyModel;
        Field field = null;
        try {
            field = container.getDeclaredField(propertyModel.getName());
        } catch (NoSuchFieldException ignored) {
        }
        this.field = field;
        var annotatedElements = new HashSet<AnnotatedElement>();
        if (field != null) {
            annotatedElements.add(field);
        }
        if (propertyModel.getOriginalMember() instanceof AnnotatedElement) {
            annotatedElements.add((AnnotatedElement) propertyModel.getOriginalMember());
        }
        this.annotatedElements = Set.copyOf(annotatedElements);
    }
}
