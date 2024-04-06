package sh.ivan.zod;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import sh.ivan.zod.schema.ObjectSchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.StringSchema;
import sh.ivan.zod.schema.attribute.NotBlankAttribute;
import sh.ivan.zod.schema.attribute.OptionalNullableAttribute;
import sh.ivan.zod.schema.attribute.SizeAttribute;
import sh.ivan.zod.schema.attribute.UuidAttribute;

class StringAttributesTest extends JavaToZodConverterTest {

    @Test
    void shouldSupportNotBlank() {
        assertThatField("notBlank")
                .isEqualTo(new StringSchema(Set.of(new NotBlankAttribute())))
                .extracting(Schema::asZodSchema)
                .isEqualTo("string().regex(/\\S/)");
    }

    @Test
    void shouldSupportNotEmpty() {
        assertThatField("notEmpty")
                .isEqualTo(new StringSchema(Set.of(new SizeAttribute(1, Integer.MAX_VALUE))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("string().min(1)");
    }

    @Test
    void shouldSupportSizeMin() {
        assertThatField("min5")
                .isEqualTo(new StringSchema(Set.of(new SizeAttribute(5, Integer.MAX_VALUE))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("string().min(5)");
    }

    @Test
    void shouldSupportSizeMax() {
        assertThatField("max10")
                .isEqualTo(new StringSchema(Set.of(new SizeAttribute(0, 10))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("string().max(10)");
    }

    @Test
    void shouldSupportSizeMinMaxxing() {
        assertThatField("min5Max10")
                .isEqualTo(new StringSchema(Set.of(new SizeAttribute(5, 10))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("string().min(5).max(10)");
    }

    @Test
    void shouldSupportUUID() {
        assertThatField("id")
                .isEqualTo(new StringSchema(Set.of(new OptionalNullableAttribute(), new UuidAttribute())))
                .extracting(Schema::asZodSchema)
                .isEqualTo("string().uuid().optional().nullable()");
    }

    static class StringHolder {
        @NotBlank
        public String notBlank;

        @NotEmpty
        public String notEmpty;

        @NotNull
        @Size(min = 5)
        public String min5;

        @NotNull
        @Size(max = 10)
        public String max10;

        @NotNull
        @Size(min = 5, max = 10)
        public String min5Max10;

        public UUID id;
    }

    private ObjectAssert<Schema> assertThatField(String fieldName) {
        var schema = (ObjectSchema) converter.buildSchema(StringHolder.class);
        return assertThat(schema.getFields().get(fieldName));
    }
}
