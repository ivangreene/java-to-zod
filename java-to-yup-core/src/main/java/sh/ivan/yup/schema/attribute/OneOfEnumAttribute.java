package sh.ivan.yup.schema.attribute;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class OneOfEnumAttribute implements Attribute {
    private final String enumElements;

    public OneOfEnumAttribute(Class<? extends Enum<?>> enumClass) {
        this.enumElements = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .map(e -> "'" + e + "'")
                .collect(Collectors.joining(", "));
    }

    @Override
    public int priority() {
        return 15;
    }

    @Override
    public String yupMethod() {
        return "oneOf([" + enumElements + "])";
    }
}
