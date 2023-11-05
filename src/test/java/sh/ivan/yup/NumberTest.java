package sh.ivan.yup;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class NumberTest {
    Jsr380ToYupConverter converter = new Jsr380ToYupConverter();

    @Test
    void testFloat() {
        assertThat(converter.buildSchema(Float.class).asYupSchema()).isEqualTo("number()");
        assertThat(converter.buildSchema(float.class).asYupSchema()).isEqualTo("number()");
        assertThat(converter.buildSchema(Float.class)).isEqualTo(converter.buildSchema(float.class));
    }

    @Test
    void testDouble() {
        assertThat(converter.buildSchema(Double.class).asYupSchema()).isEqualTo("number()");
        assertThat(converter.buildSchema(double.class).asYupSchema()).isEqualTo("number()");
        assertThat(converter.buildSchema(Double.class)).isEqualTo(converter.buildSchema(double.class));
    }

    @Test
    void testBigDecimal() {
        assertThat(converter.buildSchema(BigDecimal.class).asYupSchema()).isEqualTo("number()");
    }

    @Test
    void testLong() {
        assertThat(converter.buildSchema(Long.class).asYupSchema()).isEqualTo("number().integer()");
        assertThat(converter.buildSchema(long.class).asYupSchema()).isEqualTo("number().integer()");
        assertThat(converter.buildSchema(Long.class)).isEqualTo(converter.buildSchema(long.class));
    }

    @Test
    void testInteger() {
        assertThat(converter.buildSchema(Integer.class).asYupSchema()).isEqualTo("number().integer()");
        assertThat(converter.buildSchema(int.class).asYupSchema()).isEqualTo("number().integer()");
        assertThat(converter.buildSchema(Integer.class)).isEqualTo(converter.buildSchema(int.class));
    }

    @Test
    void testByte() {
        assertThat(converter.buildSchema(Byte.class).asYupSchema()).isEqualTo("number().integer()");
        assertThat(converter.buildSchema(byte.class).asYupSchema()).isEqualTo("number().integer()");
        assertThat(converter.buildSchema(Byte.class)).isEqualTo(converter.buildSchema(byte.class));
    }

    @Test
    void testShort() {
        assertThat(converter.buildSchema(Short.class).asYupSchema()).isEqualTo("number().integer()");
        assertThat(converter.buildSchema(short.class).asYupSchema()).isEqualTo("number().integer()");
        assertThat(converter.buildSchema(Short.class)).isEqualTo(converter.buildSchema(short.class));
    }

    @Test
    void testBigInteger() {
        assertThat(converter.buildSchema(BigInteger.class).asYupSchema()).isEqualTo("number().integer()");
    }
}
