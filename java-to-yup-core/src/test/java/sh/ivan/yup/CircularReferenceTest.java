package sh.ivan.yup;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import sh.ivan.yup.schema.ArraySchema;
import sh.ivan.yup.schema.ObjectSchema;
import sh.ivan.yup.schema.ReferenceSchema;
import sh.ivan.yup.schema.Schema;
import sh.ivan.yup.schema.StringSchema;
import sh.ivan.yup.schema.attribute.SizeAttribute;

class CircularReferenceTest extends JavaToYupConverterTest {
    @Test
    void shouldSupportCircularReferences() {
        var schema = converter.buildSchema(Person.class);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert
                .extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .hasSize(4)
                .containsEntry("name", new StringSchema(Set.of()))
                .containsEntry("parent", new ReferenceSchema("Person", Set.of()))
                .containsEntry("children", new ArraySchema(new ReferenceSchema("Person", Set.of()), Set.of()))
                .containsEntry(
                        "listOfListsOfChildren",
                        new ArraySchema(
                                new ArraySchema(
                                        new ReferenceSchema("Person", Set.of()),
                                        Set.of(new SizeAttribute(1, Integer.MAX_VALUE))),
                                Set.of()));
        assertThat(schema.asYupSchema())
                .isEqualTo("object({ name: string(), parent: Person, children: array().of(Person), "
                        + "listOfListsOfChildren: array().of(array().of(Person).min(1)), })");
    }

    static class Person {
        @NotNull
        public String name;

        @NotNull
        public Person parent;

        @NotNull
        public List<@NotNull Person> children;

        @NotNull
        public List<@NotEmpty List<@NotNull Person>> listOfListsOfChildren;
    }
}
