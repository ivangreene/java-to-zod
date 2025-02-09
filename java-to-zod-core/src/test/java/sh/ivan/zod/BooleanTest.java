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
        assertThat(objectSchema.getFields()).hasSize(2);
        var failedSchema = objectSchema.getFields().get("failed");
        assertThat(failedSchema.asZodSchema()).isEqualTo("literal(false)");
        var didFailSchema = objectSchema.getFields().get("didFail");
        assertThat(didFailSchema.asZodSchema()).isEqualTo("literal(false).optional().nullable()");
    }

    static class SucceededResult {
        @AssertFalse
        public boolean failed;

        @AssertFalse
        public Boolean didFail;
    }

    @Test
    void shouldHandleAssertFalseWithMessage() {
        var schema = converter.buildSchema(SucceededResultWithMessage.class);
        assertThat(schema).isInstanceOf(ObjectSchema.class);
        var objectSchema = (ObjectSchema) schema;
        assertThat(objectSchema.getFields()).hasSize(2);
        var failedSchema = objectSchema.getFields().get("failed");
        assertThat(failedSchema.asZodSchema()).isEqualTo("literal(false, { message: 'Must be false!' })");
        var didFailSchema = objectSchema.getFields().get("didFail");
        assertThat(didFailSchema.asZodSchema())
                .isEqualTo("literal(false, { message: 'Must be false!' }).optional().nullable()");
    }

    static class SucceededResultWithMessage {
        @AssertFalse(message = "Must be false!")
        public boolean failed;

        @AssertFalse(message = "Must be false!")
        public Boolean didFail;
    }

    @Test
    void shouldHandleAssertTrue() {
        var schema = converter.buildSchema(FailedResult.class);
        assertThat(schema).isInstanceOf(ObjectSchema.class);
        var objectSchema = (ObjectSchema) schema;
        assertThat(objectSchema.getFields()).hasSize(2);
        var failedSchema = objectSchema.getFields().get("failed");
        assertThat(failedSchema.asZodSchema()).isEqualTo("literal(true)");
        var didFailSchema = objectSchema.getFields().get("didFail");
        assertThat(didFailSchema.asZodSchema()).isEqualTo("literal(true).optional().nullable()");
    }

    static class FailedResult {
        @AssertTrue
        public boolean failed;

        @AssertTrue
        public Boolean didFail;
    }

    @Test
    void shouldHandleAssertTrueWithMessage() {
        var schema = converter.buildSchema(FailedResultWithMessage.class);
        assertThat(schema).isInstanceOf(ObjectSchema.class);
        var objectSchema = (ObjectSchema) schema;
        assertThat(objectSchema.getFields()).hasSize(2);
        var failedSchema = objectSchema.getFields().get("failed");
        assertThat(failedSchema.asZodSchema()).isEqualTo("literal(true, { message: 'Must be true!' })");
        var didFailSchema = objectSchema.getFields().get("didFail");
        assertThat(didFailSchema.asZodSchema())
                .isEqualTo("literal(true, { message: 'Must be true!' }).optional().nullable()");
    }

    static class FailedResultWithMessage {
        @AssertTrue(message = "Must be true!")
        public boolean failed;

        @AssertTrue(message = "Must be true!")
        public Boolean didFail;
    }
}
