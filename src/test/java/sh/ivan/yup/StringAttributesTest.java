package sh.ivan.yup;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import sh.ivan.yup.schema.ObjectSchema;
import sh.ivan.yup.schema.Schema;
import sh.ivan.yup.schema.StringSchema;
import sh.ivan.yup.schema.attribute.NotBlankAttribute;
import sh.ivan.yup.schema.attribute.RequiredAttribute;
import sh.ivan.yup.schema.attribute.SizeAttribute;

import java.util.Set;

class StringAttributesTest {
    JavaToYupConverter converter = new JavaToYupConverter();

    @Test
    void shouldSupportNotBlank() {
        assertThatField("notBlank")
                .isEqualTo(new StringSchema(Set.of(new NotBlankAttribute())))
                .extracting(Schema::asYupSchema)
                .isEqualTo("string().matches(/[^\\s]/)");
    }

    @Test
    void shouldSupportNotEmpty() {
        assertThatField("notEmpty")
                .isEqualTo(new StringSchema(Set.of(new RequiredAttribute())))
                .extracting(Schema::asYupSchema)
                .isEqualTo("string().required()");
    }

    @Test
    void shouldSupportSizeMin() {
        assertThatField("min5")
                .isEqualTo(new StringSchema(Set.of(new SizeAttribute(5, Integer.MAX_VALUE))))
                .extracting(Schema::asYupSchema)
                .isEqualTo("string().min(5)");
    }

    @Test
    void shouldSupportSizeMax() {
        assertThatField("max10")
                .isEqualTo(new StringSchema(Set.of(new SizeAttribute(0, 10))))
                .extracting(Schema::asYupSchema)
                .isEqualTo("string().max(10)");
    }

    @Test
    void shouldSupportSizeMinMaxxing() {
        assertThatField("min5Max10")
                .isEqualTo(new StringSchema(Set.of(new SizeAttribute(5, 10))))
                .extracting(Schema::asYupSchema)
                .isEqualTo("string().min(5).max(10)");
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
    }

    private ObjectAssert<Schema> assertThatField(String fieldName) {
        var schema = (ObjectSchema) converter.buildSchema(StringHolder.class);
        return assertThat(schema.getFields().get(fieldName));
    }
}
