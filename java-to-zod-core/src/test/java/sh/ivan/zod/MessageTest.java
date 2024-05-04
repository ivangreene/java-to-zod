package sh.ivan.zod;

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
import sh.ivan.zod.schema.NumberSchema;
import sh.ivan.zod.schema.ObjectSchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.attribute.AttributeWithMessage;
import sh.ivan.zod.schema.attribute.IntegerAttribute;
import sh.ivan.zod.schema.attribute.MaxAttribute;
import sh.ivan.zod.schema.attribute.MinAttribute;
import sh.ivan.zod.schema.attribute.NegativeAttribute;
import sh.ivan.zod.schema.attribute.OptionalNullableAttribute;
import sh.ivan.zod.schema.attribute.PositiveAttribute;

class MessageTest extends JavaToZodConverterTest {
    @Test
    void shouldSupportMax() {
        assertThatField("maxed")
                .isEqualTo(new NumberSchema(Set.of(
                        new OptionalNullableAttribute(),
                        new IntegerAttribute(),
                        new AttributeWithMessage(new MaxAttribute(300L), "must not be over '300'"))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("number().int().max(300, { message: 'must not be over \\'300\\'' }).optional().nullable()");
    }

    @Test
    void shouldSupportMin() {
        assertThatField("minned")
                .isEqualTo(new NumberSchema(Set.of(
                        new IntegerAttribute(),
                        new AttributeWithMessage(new MinAttribute(100L), "must not be under '100'"))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("number().int().min(100, { message: 'must not be under \\'100\\'' })");
    }

    @Test
    void shouldSupportNegative() {
        assertThatField("negative")
                .isEqualTo(new NumberSchema(Set.of(
                        new OptionalNullableAttribute(),
                        new IntegerAttribute(),
                        new AttributeWithMessage(new NegativeAttribute(false), "must be \\negative\\"))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("number().int().negative({ message: 'must be \\\\negative\\\\' }).optional().nullable()");
    }

    @Test
    void shouldSupportPositive() {
        assertThatField("positive")
                .isEqualTo(new NumberSchema(Set.of(
                        new OptionalNullableAttribute(),
                        new IntegerAttribute(),
                        new AttributeWithMessage(new PositiveAttribute(false), "must be positive"))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("number().int().positive({ message: 'must be positive' }).optional().nullable()");
    }

    @Test
    void shouldSupportNegativeOrZero() {
        assertThatField("negativeOrZero")
                .isEqualTo(new NumberSchema(Set.of(
                        new OptionalNullableAttribute(),
                        new AttributeWithMessage(new NegativeAttribute(true), "must be negative or zero"))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("number().max(0, { message: 'must be negative or zero' }).optional().nullable()");
    }

    @Test
    void shouldSupportPositiveOrZero() {
        assertThatField("positiveOrZero")
                .isEqualTo(new NumberSchema(
                        Set.of(new AttributeWithMessage(new PositiveAttribute(true), "must be positive or zero"))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("number().min(0, { message: 'must be positive or zero' })");
    }

    static class Data {
        @Max(value = 300L, message = "must not be over '300'")
        public Integer maxed;

        @Min(value = 100L, message = "must not be under '100'")
        public short minned;

        @Negative(message = "must be \\negative\\")
        public Long negative;

        @Positive(message = "must be positive")
        public BigInteger positive;

        @NegativeOrZero(message = "must be negative or zero")
        public BigDecimal negativeOrZero;

        @PositiveOrZero(message = "must be positive or zero")
        public float positiveOrZero;
    }

    private ObjectAssert<Schema> assertThatField(String fieldName) {
        var schema = (ObjectSchema) converter.buildSchema(Data.class);
        return assertThat(schema.getFields().get(fieldName));
    }
}
