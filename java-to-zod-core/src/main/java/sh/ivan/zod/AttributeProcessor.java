package sh.ivan.zod;

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
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import sh.ivan.zod.schema.attribute.Attribute;
import sh.ivan.zod.schema.attribute.AttributeWithMessage;
import sh.ivan.zod.schema.attribute.EmailAttribute;
import sh.ivan.zod.schema.attribute.EqualsBooleanAttribute;
import sh.ivan.zod.schema.attribute.MaxAttribute;
import sh.ivan.zod.schema.attribute.MinAttribute;
import sh.ivan.zod.schema.attribute.NegativeAttribute;
import sh.ivan.zod.schema.attribute.NotBlankAttribute;
import sh.ivan.zod.schema.attribute.OptionalNullableAttribute;
import sh.ivan.zod.schema.attribute.PositiveAttribute;
import sh.ivan.zod.schema.attribute.RegexAttribute;
import sh.ivan.zod.schema.attribute.SizeAttribute;

public class AttributeProcessor {
    private static final Set<Class<? extends Annotation>> JSR_380_ANNOTATIONS = Set.of(
            AssertFalse.class,
            AssertTrue.class,
            DecimalMax.class, // Not yet implemented
            DecimalMin.class, // Not yet implemented
            Digits.class, // Not yet implemented
            Email.class,
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
            Pattern.class,
            Positive.class,
            PositiveOrZero.class,
            Size.class);

    private static final Set<Class<? extends Annotation>> NOT_NULL_ANNOTATIONS =
            Set.of(NotBlank.class, NotEmpty.class, NotNull.class);

    private static final Map<Class<? extends Annotation>, Attribute> CONSTANT_ATTRIBUTES = Map.of(
            NotBlank.class, new NotBlankAttribute(),
            AssertFalse.class, new EqualsBooleanAttribute(false),
            AssertTrue.class, new EqualsBooleanAttribute(true),
            Negative.class, new NegativeAttribute(),
            Positive.class, new PositiveAttribute(),
            NegativeOrZero.class, new NegativeAttribute(true),
            PositiveOrZero.class, new PositiveAttribute(true),
            Email.class, new EmailAttribute(),
            NotEmpty.class, new SizeAttribute(1, Integer.MAX_VALUE));

    public Set<Attribute> getAttributes(Type type, Set<AnnotatedElement> annotatedElements) {
        return getAttributesForAnnotations(
                type,
                annotatedElements.stream()
                        .flatMap(annotatedElement -> Stream.of(annotatedElement.getAnnotations()))
                        .collect(Collectors.toSet()));
    }

    public Set<Attribute> getAttributesForAnnotations(Type type, Set<Annotation> annotations) {
        var attributes = new HashSet<>(processAnnotations(type, annotations));
        if (isNullable(type, annotations)) {
            attributes.add(new OptionalNullableAttribute());
        }
        if (attributes.contains(new SizeAttribute(1, Integer.MAX_VALUE))
                && attributes.stream()
                        .anyMatch(attribute -> attribute instanceof SizeAttribute
                                && ((SizeAttribute) attribute).getMin() > 0
                                && (((SizeAttribute) attribute).getMin() > 1
                                        || ((SizeAttribute) attribute).getMax() != Integer.MAX_VALUE))) {
            attributes.remove(new SizeAttribute(1, Integer.MAX_VALUE));
        }
        return Set.copyOf(attributes);
    }

    private boolean isNullable(Type type, Set<Annotation> annotations) {
        return !(type instanceof Class<?> && ((Class<?>) type).isPrimitive())
                && annotations.stream().map(Annotation::annotationType).noneMatch(NOT_NULL_ANNOTATIONS::contains);
    }

    private Set<Attribute> processAnnotations(Type type, Set<Annotation> annotations) {
        return annotations.stream()
                .filter(annotation -> JSR_380_ANNOTATIONS.contains(annotation.annotationType()))
                .map(annotation -> processAnnotation(type, annotation))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Attribute processAnnotation(Type type, Annotation annotation) {
        Attribute attribute = getAttribute(type, annotation);
        if (attribute != null) {
            Optional<String> message = getMessage(annotation);
            if (message.isPresent()) {
                return new AttributeWithMessage(attribute, message.get());
            }
        }
        return attribute;
    }

    private Attribute getAttribute(Type type, Annotation annotation) {
        if (CONSTANT_ATTRIBUTES.containsKey(annotation.annotationType())) {
            return CONSTANT_ATTRIBUTES.get(annotation.annotationType());
        }
        if (annotation.annotationType() == Size.class) {
            return new SizeAttribute(((Size) annotation).min(), ((Size) annotation).max());
        }
        if (annotation.annotationType() == Max.class) {
            return new MaxAttribute(((Max) annotation).value());
        }
        if (annotation.annotationType() == Min.class) {
            return new MinAttribute(((Min) annotation).value());
        }
        if (type == String.class) {
            if (annotation.annotationType() == Pattern.class) {
                return new RegexAttribute(((Pattern) annotation).regexp(), ((Pattern) annotation).flags());
            }
        }
        return null;
    }

    private Optional<String> getMessage(Annotation annotation) {
        try {
            Method messageMethod = annotation.annotationType().getMethod("message");
            Object message = messageMethod.invoke(annotation);
            if (!Objects.equals(messageMethod.getDefaultValue(), message)) {
                return Optional.of((String) message);
            }
            return Optional.empty();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not get message from annotation", e);
        }
    }
}
