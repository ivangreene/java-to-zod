package sh.ivan.jty;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import sh.ivan.jty.schema.attribute.Attribute;
import sh.ivan.jty.schema.attribute.NullableAttribute;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class AttributeProcessor {
    private static final Set<Class<? extends Annotation>> JSR_380_ANNOTATIONS = Set.of(
            AssertFalse.class,
            AssertTrue.class,
            DecimalMax.class,
            DecimalMin.class,
            Digits.class,
            Email.class,
            Future.class,
            FutureOrPresent.class,
            Max.class,
            Min.class,
            Negative.class,
            NegativeOrZero.class,
            NotBlank.class,
            NotEmpty.class,
            NotNull.class,
            Null.class,
            Past.class,
            PastOrPresent.class,
            Pattern.class,
            Positive.class,
            PositiveOrZero.class,
            Size.class
    );

    private static final Set<Class<? extends Annotation>> NOT_NULL_ANNOTATIONS = Set.of(
            NotBlank.class,
            NotEmpty.class,
            NotNull.class
    );

    public Set<Attribute> getAttributes(Class<?> container, Method method, String propertyName) {
        var attributes = new HashSet<Attribute>();
        Field field;
        try {
            field = container.getDeclaredField(propertyName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Could not find field for property " + propertyName, e);
        }
        if (isNullable(field) && isNullable(method)) {
            attributes.add(new NullableAttribute());
        }
        return Set.copyOf(attributes);
    }

    private boolean isNullable(AnnotatedElement annotatedElement) {
        return Stream.of(annotatedElement.getAnnotations())
                .map(Annotation::annotationType)
                .noneMatch(NOT_NULL_ANNOTATIONS::contains);
    }
}
