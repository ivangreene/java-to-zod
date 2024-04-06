package sh.ivan.zod;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Test;
import sh.ivan.zod.schema.ObjectSchema;

class BooleanTest extends JavaToZodConverterTest {

    @Test
    void booleanShouldBeBoolean() {
        assertThat(converter.buildSchema(Boolean.class).asZodSchema()).isEqualTo("boolean()");
        assertThat(converter.buildSchema(boolean.class).asZodSchema()).isEqualTo("boolean()");
        assertThat(converter.buildSchema(Boolean.class)).isEqualTo(converter.buildSchema(boolean.class));
    }

    @Test
    void shouldHandleAssertFalse() {
        var schema = converter.buildSchema(SucceededResult.class);
        assertThat(schema).isInstanceOf(ObjectSchema.class);
        var objectSchema = (ObjectSchema) schema;
        assertThat(objectSchema.getFields()).hasSize(1);
        var failedSchema = objectSchema.getFields().get("failed");
        assertThat(failedSchema.asZodSchema()).isEqualTo("literal(false)");
    }

    static class SucceededResult {
        @AssertFalse
        public boolean failed;
    }

    @Test
    void shouldHandleAssertTrue() {
        var schema = converter.buildSchema(FailedResult.class);
        assertThat(schema).isInstanceOf(ObjectSchema.class);
        var objectSchema = (ObjectSchema) schema;
        assertThat(objectSchema.getFields()).hasSize(1);
        var failedSchema = objectSchema.getFields().get("failed");
        assertThat(failedSchema.asZodSchema()).isEqualTo("literal(true)");
    }

    static class FailedResult {
        @AssertTrue
        public boolean failed;
    }
}
