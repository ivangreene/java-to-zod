package sh.ivan.zod;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import sh.ivan.zod.schema.ObjectSchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.StringSchema;
import sh.ivan.zod.schema.attribute.EmailAttribute;
import sh.ivan.zod.schema.attribute.NotBlankAttribute;
import sh.ivan.zod.schema.attribute.OptionalNullableAttribute;
import sh.ivan.zod.schema.attribute.RegexAttribute;
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

    @Test
    void shouldSupportPattern() {
        assertThatField("pattern")
                .isEqualTo(new StringSchema(Set.of(new RegexAttribute("[a-z]\\./\\\\/[a-z0-9]*"))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("string().regex(/^[a-z]\\.\\/\\\\\\/[a-z0-9]*$/)");
    }

    @Test
    void shouldSupportCaseInsensitivePattern() {
        assertThatField("caseInsensitiveString")
                .isEqualTo(new StringSchema(Set.of(new RegexAttribute("[a-z]+", Pattern.Flag.CASE_INSENSITIVE))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("string().regex(/^[a-z]+$/i)");
    }

    @Test
    void shouldOnlyAddSupportedFlags() {
        assertThatField("allFlags")
                .isEqualTo(new StringSchema(Set.of(new RegexAttribute(
                        "[a-z]+",
                        Pattern.Flag.UNIX_LINES,
                        Pattern.Flag.CASE_INSENSITIVE,
                        Pattern.Flag.COMMENTS,
                        Pattern.Flag.MULTILINE,
                        Pattern.Flag.DOTALL,
                        Pattern.Flag.UNICODE_CASE,
                        Pattern.Flag.CANON_EQ))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("string().regex(/^[a-z]+$/ims)");
    }

    @Test
    void shouldSupportEmail() {
        assertThatField("email")
                .isEqualTo(new StringSchema(Set.of(new EmailAttribute(), new OptionalNullableAttribute())))
                .extracting(Schema::asZodSchema)
                .isEqualTo("string().email().optional().nullable()");
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

        @Pattern(regexp = "[a-z]\\./\\\\/[a-z0-9]*")
        @NotNull
        public String pattern;

        @Pattern(regexp = "[a-z]+", flags = Pattern.Flag.CASE_INSENSITIVE)
        @NotNull
        public String caseInsensitiveString;

        @Pattern(
                regexp = "[a-z]+",
                flags = {
                    Pattern.Flag.UNIX_LINES,
                    Pattern.Flag.CASE_INSENSITIVE,
                    Pattern.Flag.COMMENTS,
                    Pattern.Flag.MULTILINE,
                    Pattern.Flag.DOTALL,
                    Pattern.Flag.UNICODE_CASE,
                    Pattern.Flag.CANON_EQ
                })
        @NotNull
        public String allFlags;

        @Email
        public String email;
    }

    private ObjectAssert<Schema> assertThatField(String fieldName) {
        var schema = (ObjectSchema) converter.buildSchema(StringHolder.class);
        return assertThat(schema.getFields().get(fieldName));
    }
}
