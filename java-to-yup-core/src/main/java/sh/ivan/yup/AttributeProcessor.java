package sh.ivan.yup;

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
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import sh.ivan.yup.schema.attribute.Attribute;
import sh.ivan.yup.schema.attribute.EqualsBooleanAttribute;
import sh.ivan.yup.schema.attribute.MaxAttribute;
import sh.ivan.yup.schema.attribute.MinAttribute;
import sh.ivan.yup.schema.attribute.NegativeAttribute;
import sh.ivan.yup.schema.attribute.NotBlankAttribute;
import sh.ivan.yup.schema.attribute.NullableAttribute;
import sh.ivan.yup.schema.attribute.PositiveAttribute;
import sh.ivan.yup.schema.attribute.RequiredAttribute;
import sh.ivan.yup.schema.attribute.SizeAttribute;

public class AttributeProcessor {
    private static final Set<Class<? extends Annotation>> JSR_380_ANNOTATIONS = Set.of(
            AssertFalse.class,
            AssertTrue.class,
            DecimalMax.class, // Not yet implemented
            DecimalMin.class, // Not yet implemented
            Digits.class, // Not yet implemented
            Email.class, // Not yet implemented
            Future.class, // Not yet implemented
            FutureOrPresent.class, // Not yet implemented
            Max.class,
            Min.class,
            Negative.class,
            NegativeOrZero.class,
            NotBlank.class,
            NotEmpty.class,
            NotNull.class,
            Null.class, // Not yet implemented
            Past.class, // Not yet implemented
            PastOrPresent.class, // Not yet implemented
            Pattern.class, // Not yet implemented
            Positive.class,
            PositiveOrZero.class,
            Size.class);

    private static final Set<Class<? extends Annotation>> NOT_NULL_ANNOTATIONS =
            Set.of(NotBlank.class, NotEmpty.class, NotNull.class);

    private final JavaToYupConverter converter;

    public AttributeProcessor(JavaToYupConverter converter) {
        this.converter = converter;
    }

    public Set<Attribute> getAttributes(Type type, Set<AnnotatedElement> annotatedElements) {
        return getAttributesForAnnotations(
                type,
                annotatedElements.stream()
                        .flatMap(annotatedElement -> Stream.of(annotatedElement.getAnnotations()))
                        .collect(Collectors.toSet()));
    }

    public Set<Attribute> getAttributesForAnnotations(Type type, Set<Annotation> annotations) {
        var attributes = new HashSet<Attribute>();
        if (!(type instanceof Class<?> && ((Class<?>) type).isPrimitive())
                && annotations.stream().map(Annotation::annotationType).noneMatch(NOT_NULL_ANNOTATIONS::contains)) {
            attributes.add(new NullableAttribute());
        }
        attributes.addAll(processAnnotations(type, annotations));
        return Set.copyOf(attributes);
    }

    private Set<Attribute> processAnnotations(Type type, Set<Annotation> annotations) {
        return annotations.stream()
                .filter(annotation -> JSR_380_ANNOTATIONS.contains(annotation.annotationType()))
                .map(annotation -> processAnnotation(type, annotation))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Attribute processAnnotation(Type type, Annotation annotation) {
        if (annotation.annotationType() == Size.class) {
            return new SizeAttribute(((Size) annotation).min(), ((Size) annotation).max());
        }
        if (annotation.annotationType() == NotEmpty.class) {
            if (type == String.class) {
                return new RequiredAttribute();
            }
            if (converter.isArray(type)) {
                return new SizeAttribute(1, Integer.MAX_VALUE);
            }
        }
        if (annotation.annotationType() == NotBlank.class) {
            return new NotBlankAttribute();
        }
        if (annotation.annotationType() == AssertFalse.class) {
            return new EqualsBooleanAttribute(false);
        }
        if (annotation.annotationType() == AssertTrue.class) {
            return new EqualsBooleanAttribute(true);
        }
        if (annotation.annotationType() == Max.class) {
            return new MaxAttribute(((Max) annotation).value());
        }
        if (annotation.annotationType() == Min.class) {
            return new MinAttribute(((Min) annotation).value());
        }
        if (annotation.annotationType() == Negative.class) {
            return new NegativeAttribute();
        }
        if (annotation.annotationType() == Positive.class) {
            return new PositiveAttribute();
        }
        if (annotation.annotationType() == NegativeOrZero.class) {
            return new NegativeAttribute(true);
        }
        if (annotation.annotationType() == PositiveOrZero.class) {
            return new PositiveAttribute(true);
        }
        return null;
    }
}
