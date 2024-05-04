package sh.ivan.zod.schema.attribute;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class AttributeWithMessage implements Attribute {
    private final Attribute attribute;
    private final String message;

    public AttributeWithMessage(Attribute attribute, String message) {
        this.attribute = attribute;
        this.message = message;
    }

    @Override
    public int priority() {
        return attribute.priority();
    }

    @Override
    public String zodMethod() {
        return attribute
                .zodMethod()
                .replaceFirst("([^(])\\)$", "$1, { message: " + quoteMessage() + " })")
                .replaceFirst("\\(\\)$", "({ message: " + quoteMessage() + " })");
    }

    private String quoteMessage() {
        return "'" + message.replace("\\", "\\\\\\\\").replace("'", "\\\\'") + "'";
    }
}
