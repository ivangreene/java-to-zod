package sh.ivan.yup;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EnumTest extends JavaToYupConverterTest {
    @Test
    void shouldSupportEnum() {
        assertThat(converter.buildSchema(Status.class).asYupSchema())
                .isEqualTo("string().oneOf(['PASSED', 'FAILED', 'UNKNOWN'])");
    }

    enum Status {
        PASSED,
        FAILED,
        UNKNOWN,
        ;
    }
}
