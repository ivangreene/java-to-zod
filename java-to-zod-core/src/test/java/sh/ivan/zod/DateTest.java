package sh.ivan.zod;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import sh.ivan.zod.schema.DateSchema;
import sh.ivan.zod.schema.ObjectSchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.attribute.OptionalNullableAttribute;

class DateTest extends JavaToZodConverterTest {

    @Test
    void shouldSupportBasicDateTypes() {
        assertThatField("instant")
                .isEqualTo(new DateSchema(Set.of(new OptionalNullableAttribute())))
                .extracting(Schema::asZodSchema)
                .isEqualTo("date().optional().nullable()");

        assertThatField("date")
                .isEqualTo(new DateSchema(Set.of()))
                .extracting(Schema::asZodSchema)
                .isEqualTo("date()");
    }

    static class DateLikeHolder {
        public Instant instant;

        @NotNull
        public Date date;
    }

    private ObjectAssert<Schema> assertThatField(String fieldName) {
        var schema = (ObjectSchema) converter.buildSchema(DateLikeHolder.class);
        return assertThat(schema.getFields().get(fieldName));
    }
}
