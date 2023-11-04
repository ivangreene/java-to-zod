package sh.ivan.jty.schema;

import lombok.Data;

@Data
public class StringSchema implements Schema {
    @Override
    public String asYupSchema() {
        return "string()";
    }
}
