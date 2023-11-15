package sh.ivan.yup;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import sh.ivan.yup.schema.NumberSchema;
import sh.ivan.yup.schema.ObjectSchema;
import sh.ivan.yup.schema.Schema;
import sh.ivan.yup.schema.attribute.IntegerAttribute;
import sh.ivan.yup.schema.attribute.MaxAttribute;
import sh.ivan.yup.schema.attribute.MinAttribute;
import sh.ivan.yup.schema.attribute.NegativeAttribute;
import sh.ivan.yup.schema.attribute.NullableAttribute;
import sh.ivan.yup.schema.attribute.PositiveAttribute;

class NumberAttributesTest {
    JavaToYupConverter converter = new JavaToYupConverter();

    @Test
    void shouldSupportMax() {
        assertThatField("maxed")
                .isEqualTo(new NumberSchema(
                        Set.of(new NullableAttribute(), new IntegerAttribute(), new MaxAttribute(300L))))
                .extracting(Schema::asYupSchema)
                .isEqualTo("number().nullable().integer().max(300)");
    }

    @Test
    void shouldSupportMin() {
        assertThatField("minned")
                .isEqualTo(new NumberSchema(Set.of(new IntegerAttribute(), new MinAttribute(100L))))
                .extracting(Schema::asYupSchema)
                .isEqualTo("number().integer().min(100)");
    }

    @Test
    void shouldSupportNegative() {
        assertThatField("negative")
                .isEqualTo(new NumberSchema(
                        Set.of(new NullableAttribute(), new IntegerAttribute(), new NegativeAttribute(false))))
                .extracting(Schema::asYupSchema)
                .isEqualTo("number().nullable().integer().negative()");
    }

    @Test
    void shouldSupportPositive() {
        assertThatField("positive")
                .isEqualTo(new NumberSchema(
                        Set.of(new NullableAttribute(), new IntegerAttribute(), new PositiveAttribute(false))))
                .extracting(Schema::asYupSchema)
                .isEqualTo("number().nullable().integer().positive()");
    }

    @Test
    void shouldSupportNegativeOrZero() {
        assertThatField("negativeOrZero")
                .isEqualTo(new NumberSchema(Set.of(new NullableAttribute(), new NegativeAttribute(true))))
                .extracting(Schema::asYupSchema)
                .isEqualTo("number().nullable().max(0)");
    }

    @Test
    void shouldSupportPositiveOrZero() {
        assertThatField("positiveOrZero")
                .isEqualTo(new NumberSchema(Set.of(new PositiveAttribute(true))))
                .extracting(Schema::asYupSchema)
                .isEqualTo("number().min(0)");
    }

    static class NumberHolder {
        @Max(300L)
        public Integer maxed;

        @Min(100L)
        public short minned;

        @Negative
        public Long negative;

        @Positive
        public BigInteger positive;

        @NegativeOrZero
        public BigDecimal negativeOrZero;

        @PositiveOrZero
        public float positiveOrZero;
    }

    private ObjectAssert<Schema> assertThatField(String fieldName) {
        var schema = (ObjectSchema) converter.buildSchema(NumberHolder.class);
        return assertThat(schema.getFields().get(fieldName));
    }
}
