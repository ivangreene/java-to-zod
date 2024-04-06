package sh.ivan.zod.schema.attribute;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public interface Attribute {
    int priority();

    String zodMethod();

    static String writeAttributes(Set<Attribute> attributes) {
        return attributes.stream()
                .sorted(Comparator.comparingInt(Attribute::priority))
                .map(Attribute::zodMethod)
                .map(method -> "." + method)
                .collect(Collectors.joining());
    }
}
