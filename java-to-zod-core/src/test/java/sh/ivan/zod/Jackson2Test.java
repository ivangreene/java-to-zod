package sh.ivan.zod;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import sh.ivan.zod.schema.ObjectSchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.StringSchema;
import sh.ivan.zod.schema.attribute.SizeAttribute;

class Jackson2Test extends JavaToZodConverterTest {

    @Test
    void shouldUseJsonPropertyNameAndStillGetAnnotationsFromField() {
        var schema = converter.buildSchema(Person.class);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert
                .extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .hasSize(1)
                .containsEntry(
                        "name",
                        new StringSchema(Set.of(new SizeAttribute(1, Integer.MAX_VALUE), new SizeAttribute(0, 400))));
        objectSchemaAssert
                .extracting(ObjectSchema::asZodSchema)
                .isEqualTo("object({ name: string().min(1).max(400), })");
    }

    static class Person {
        @JsonProperty("name")
        @NotEmpty
        private String theName;

        @Size(max = 400)
        public String getTheName() {
            return theName;
        }
    }
}
