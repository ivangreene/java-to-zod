package sh.ivan.yup;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Test;
import sh.ivan.yup.schema.ObjectSchema;

class BooleanTest {
    Jsr380ToYupConverter converter = new Jsr380ToYupConverter();

    @Test
    void booleanShouldBeBoolean() {
        assertThat(converter.buildSchema(Boolean.class).asYupSchema()).isEqualTo("boolean()");
        assertThat(converter.buildSchema(boolean.class).asYupSchema()).isEqualTo("boolean()");
        assertThat(converter.buildSchema(Boolean.class)).isEqualTo(converter.buildSchema(boolean.class));
    }

    @Test
    void shouldHandleAssertFalse() {
        var schema = converter.buildSchema(SucceededResult.class);
        assertThat(schema).isInstanceOf(ObjectSchema.class);
        var objectSchema = (ObjectSchema) schema;
        assertThat(objectSchema.getFields()).hasSize(1);
        var failedSchema = objectSchema.getFields().get("failed");
        assertThat(failedSchema.asYupSchema()).isEqualTo("boolean().equals([false])");
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
        assertThat(failedSchema.asYupSchema()).isEqualTo("boolean().equals([true])");
    }

    static class FailedResult {
        @AssertTrue
        public boolean failed;
    }
}
