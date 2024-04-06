package sh.ivan.zod;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class NumberTest extends JavaToZodConverterTest {

    @Test
    void testFloat() {
        assertThat(converter.buildSchema(Float.class).asZodSchema()).isEqualTo("number()");
        assertThat(converter.buildSchema(float.class).asZodSchema()).isEqualTo("number()");
        assertThat(converter.buildSchema(Float.class)).isEqualTo(converter.buildSchema(float.class));
    }

    @Test
    void testDouble() {
        assertThat(converter.buildSchema(Double.class).asZodSchema()).isEqualTo("number()");
        assertThat(converter.buildSchema(double.class).asZodSchema()).isEqualTo("number()");
        assertThat(converter.buildSchema(Double.class)).isEqualTo(converter.buildSchema(double.class));
    }

    @Test
    void testBigDecimal() {
        assertThat(converter.buildSchema(BigDecimal.class).asZodSchema()).isEqualTo("number()");
    }

    @Test
    void testLong() {
        assertThat(converter.buildSchema(Long.class).asZodSchema()).isEqualTo("number().int()");
        assertThat(converter.buildSchema(long.class).asZodSchema()).isEqualTo("number().int()");
        assertThat(converter.buildSchema(Long.class)).isEqualTo(converter.buildSchema(long.class));
    }

    @Test
    void testInteger() {
        assertThat(converter.buildSchema(Integer.class).asZodSchema()).isEqualTo("number().int()");
        assertThat(converter.buildSchema(int.class).asZodSchema()).isEqualTo("number().int()");
        assertThat(converter.buildSchema(Integer.class)).isEqualTo(converter.buildSchema(int.class));
    }

    @Test
    void testByte() {
        assertThat(converter.buildSchema(Byte.class).asZodSchema()).isEqualTo("number().int()");
        assertThat(converter.buildSchema(byte.class).asZodSchema()).isEqualTo("number().int()");
        assertThat(converter.buildSchema(Byte.class)).isEqualTo(converter.buildSchema(byte.class));
    }

    @Test
    void testShort() {
        assertThat(converter.buildSchema(Short.class).asZodSchema()).isEqualTo("number().int()");
        assertThat(converter.buildSchema(short.class).asZodSchema()).isEqualTo("number().int()");
        assertThat(converter.buildSchema(Short.class)).isEqualTo(converter.buildSchema(short.class));
    }

    @Test
    void testBigInteger() {
        assertThat(converter.buildSchema(BigInteger.class).asZodSchema()).isEqualTo("number().int()");
    }
}
