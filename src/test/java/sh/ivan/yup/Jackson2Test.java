package sh.ivan.yup;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import sh.ivan.yup.schema.ObjectSchema;
import sh.ivan.yup.schema.Schema;
import sh.ivan.yup.schema.StringSchema;
import sh.ivan.yup.schema.attribute.RequiredAttribute;
import sh.ivan.yup.schema.attribute.SizeAttribute;

public class Jackson2Test {
    JavaToYupConverter converter = new JavaToYupConverter();

    @Test
    void shouldUseJsonPropertyNameAndStillGetAnnotationsFromField() {
        var schema = converter.buildSchema(Person.class);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert
                .extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .hasSize(1)
                .containsEntry("name", new StringSchema(Set.of(new RequiredAttribute(), new SizeAttribute(0, 400))));
        objectSchemaAssert
                .extracting(ObjectSchema::asYupSchema)
                .isEqualTo("object({ name: string().required().max(400), })");
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
