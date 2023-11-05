package sh.ivan.yup.schema;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.yup.schema.attribute.Attribute;

@Getter
@ToString
@EqualsAndHashCode
public abstract class Schema {

    private final Set<Attribute> attributes;

    public Schema(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    public abstract String yupType();

    public String asYupSchema() {
        return yupType()
                + attributes.stream()
                        .sorted(Comparator.comparingInt(Attribute::priority))
                        .map(Attribute::yupMethod)
                        .map(method -> "." + method)
                        .collect(Collectors.joining());
    }
}
