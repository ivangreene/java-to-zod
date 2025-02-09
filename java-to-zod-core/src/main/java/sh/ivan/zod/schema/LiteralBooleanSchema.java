package sh.ivan.zod.schema;

import com.google.common.collect.Sets;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sh.ivan.zod.schema.attribute.Attribute;
import sh.ivan.zod.schema.attribute.AttributeWithMessage;
import sh.ivan.zod.schema.attribute.EqualsBooleanAttribute;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LiteralBooleanSchema extends Schema {
    private final boolean value;
    private final String message;

    private LiteralBooleanSchema(boolean value, Set<Attribute> attributes, String message) {
        super(attributes);
        this.value = value;
        this.message = message;
    }

    @Override
    protected String zodType() {
        String method = String.format("literal(%s)", value);
        if (message != null) {
            return MessageFormatter.addMessageToMethod(method, message);
        }
        return method;
    }

    public static LiteralBooleanSchema fromAttributes(Attribute booleanAttribute, Set<Attribute> attributes) {
        if (booleanAttribute instanceof EqualsBooleanAttribute equalsBooleanAttribute) {
            return new LiteralBooleanSchema(
                    equalsBooleanAttribute.isValue(),
                    Sets.difference(attributes, Set.of(equalsBooleanAttribute)),
                    null);
        }
        if (booleanAttribute instanceof AttributeWithMessage attributeWithMessage) {
            return new LiteralBooleanSchema(
                    ((EqualsBooleanAttribute) attributeWithMessage.getAttribute()).isValue(),
                    Sets.difference(attributes, Set.of(attributeWithMessage)),
                    attributeWithMessage.getMessage());
        }
        throw new IllegalArgumentException("Unknown boolean attribute type " + booleanAttribute);
    }
}
