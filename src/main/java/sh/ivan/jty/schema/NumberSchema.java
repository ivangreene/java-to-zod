package sh.ivan.jty.schema;

import lombok.Data;

@Data
public class NumberSchema implements Schema {
    @Override
    public String asYupSchema() {
        return "number()";
    }
}
