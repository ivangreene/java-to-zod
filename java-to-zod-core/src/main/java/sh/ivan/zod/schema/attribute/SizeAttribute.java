package sh.ivan.zod.schema.attribute;

import lombok.Data;

@Data
public class SizeAttribute implements Attribute {
    private final int min;
    private final int max;

    @Override
    public int priority() {
        return Attribute.super.priority() + (min > 0 ? 0 : 1);
    }

    @Override
    public String zodMethod() {
        StringBuilder sb = new StringBuilder();
        if (min > 0) {
            sb.append("min(").append(min).append(")");
        }
        if (max < Integer.MAX_VALUE) {
            if (!sb.isEmpty()) {
                sb.append(".");
            }
            sb.append("max(").append(max).append(")");
        }
        return sb.toString();
    }
}
