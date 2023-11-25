package sh.ivan.yup;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Configuration {
    private final String schemaNamePrefix;
    private final String schemaNameSuffix;
}
