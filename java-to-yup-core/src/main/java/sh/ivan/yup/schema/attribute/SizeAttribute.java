package sh.ivan.yup.schema.attribute;

import lombok.Data;

@Data
public class SizeAttribute implements Attribute {
    private final int min;
    private final int max;

    @Override
    public int priority() {
        return min > 0 ? 50 : 51;
    }

    @Override
    public String yupMethod() {
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
