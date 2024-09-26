package sh.ivan.zod.schema.attribute;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface Attribute {
    List<Class<? extends Attribute>> ATTRIBUTES_BY_PRIORITY = List.of(
            EqualsBooleanAttribute.class,
            NotBlankAttribute.class,
            IntegerAttribute.class,
            RegexAttribute.class,
            UuidAttribute.class,
            EmailAttribute.class,
            NegativeAttribute.class,
            PositiveAttribute.class,
            MinAttribute.class,
            MaxAttribute.class,
            SizeAttribute.class,
            OptionalAttribute.class,
            NullableAttribute.class
    );

    default int priority() {
        int priority = ATTRIBUTES_BY_PRIORITY.indexOf(getClass());
        if (priority == -1) {
            throw new IllegalStateException("Attribute " + getClass() + " not registered in ATTRIBUTES_BY_PRIORITY");
        }
        return priority * 10;
    }

    String zodMethod();

    static String writeAttributes(Set<Attribute> attributes) {
        return attributes.stream()
                .sorted(Comparator.comparingInt(Attribute::priority))
                .map(Attribute::zodMethod)
                .map(method -> "." + method)
                .collect(Collectors.joining());
    }
}
