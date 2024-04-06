package sh.ivan.zod.schema.attribute;

import lombok.Data;

@Data
public class SizeAttribute implements Attribute {
    private final int min;
    private final int max;

    @Override
    public int priority() {
        // TODO: Do not add duplicate min = 1 if already have another min > 0
        return (min == 1 && max == Integer.MAX_VALUE) ? 50 : min > 0 ? 51 : 52;
    }

    @Override
    public String zodMethod() {
        StringBuilder sb = new StringBuilder();
        if (min > 0) {
            sb.append("min(").append(min).append(")");
        }
        if (max < Integer.MAX_VALUE) {
            if (sb.length() > 0) {
                sb.append(".");
            }
            sb.append("max(").append(max).append(")");
        }
        return sb.toString();
    }
}
