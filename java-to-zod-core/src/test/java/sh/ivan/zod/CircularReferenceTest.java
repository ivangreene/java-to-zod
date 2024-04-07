package sh.ivan.zod;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import sh.ivan.zod.schema.ArraySchema;
import sh.ivan.zod.schema.ObjectSchema;
import sh.ivan.zod.schema.ReferenceSchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.StringSchema;
import sh.ivan.zod.schema.attribute.SizeAttribute;

class CircularReferenceTest extends JavaToZodConverterTest {
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
        assertThat(schema.asZodSchema())
                .isEqualTo("object({ name: string(), parent: lazy(() => Person), children: array(lazy(() => Person)), "
                        + "listOfListsOfChildren: array(array(lazy(() => Person)).min(1)), })");
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
