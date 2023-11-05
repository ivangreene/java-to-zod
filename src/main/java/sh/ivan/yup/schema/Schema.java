package sh.ivan.yup.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.yup.schema.attribute.Attribute;

import java.util.Set;
import java.util.stream.Collectors;

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
        return yupType() + attributes.stream()
                .map(Attribute::yupMethod)
                .map(method -> "." + method)
                .collect(Collectors.joining());
    }
}
