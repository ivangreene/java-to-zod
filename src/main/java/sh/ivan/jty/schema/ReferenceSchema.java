package sh.ivan.jty.schema;

import lombok.Data;

@Data
public class ReferenceSchema implements Schema {
    private final String reference;

    public ReferenceSchema(String reference) {
        this.reference = reference;
    }

    @Override
    public String asYupSchema() {
        return reference;
    }
}
