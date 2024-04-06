package sh.ivan.yup.schema;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.yup.schema.attribute.Attribute;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EnumSchema extends Schema {
    private final String enumElements;

    public EnumSchema(Class<? extends Enum<?>> enumClass, Set<Attribute> attributes) {
        super(attributes);
        this.enumElements = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .map(e -> "'" + e + "'")
                .collect(Collectors.joining(", "));
    }

    @Override
    protected String yupType() {
        return "enum([" + enumElements + "])";
    }
}
