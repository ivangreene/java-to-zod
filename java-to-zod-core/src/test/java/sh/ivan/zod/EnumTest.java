package sh.ivan.zod;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EnumTest extends JavaToZodConverterTest {
    @Test
    void shouldSupportEnum() {
        assertThat(converter.buildSchema(Status.class).asZodSchema())
                .isEqualTo("enum(['PASSED', 'FAILED', 'UNKNOWN'])");
    }

    enum Status {
        PASSED,
        FAILED,
        UNKNOWN,
    }
}
