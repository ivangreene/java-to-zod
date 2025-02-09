package sh.ivan.zod.schema.attribute;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.zod.schema.MessageFormatter;

@Getter
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
        return MessageFormatter.addMessageToMethod(attribute.zodMethod(), message);
    }

    @Override
    public boolean isOfType(Class<? extends Attribute> clazz) {
        return attribute.isOfType(clazz);
    }
}
