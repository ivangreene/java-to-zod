package sh.ivan.zod;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import sh.ivan.zod.schema.ArraySchema;
import sh.ivan.zod.schema.ObjectSchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.StringSchema;
import sh.ivan.zod.schema.attribute.SizeAttribute;

class NestedArrayTest extends JavaToZodConverterTest {
    @Test
    void shouldCombineAnnotationsFromNestedArrayElements() {
        var schema = converter.buildSchema(Widget.class);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert
                .extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .hasSize(2)
                .containsEntry(
                        "names",
                        new ArraySchema(
                                new StringSchema(
                                        Set.of(new SizeAttribute(3, Integer.MAX_VALUE), new SizeAttribute(0, 24))),
                                Set.of(new SizeAttribute(1, Integer.MAX_VALUE))))
                .containsEntry(
                        "listOfLists",
                        new ArraySchema(
                                new ArraySchema(
                                        new StringSchema(Set.of(
                                                new SizeAttribute(5, Integer.MAX_VALUE), new SizeAttribute(0, 12))),
                                        Set.of(new SizeAttribute(2, Integer.MAX_VALUE), new SizeAttribute(0, 20))),
                                Set.of(new SizeAttribute(1, Integer.MAX_VALUE))));
        objectSchemaAssert
                .extracting(ObjectSchema::asZodSchema)
                .isEqualTo(
                        "object({ names: array(string().min(3).max(24)).min(1), listOfLists: array(array(string().min(5).max(12)).min(2).max(20)).min(1), })");
    }

    static class Widget {
        private List<@Size(min = 3) String> names;

        private List<@Size(min = 2) List<@Size(max = 12) @NotNull String>> listOfLists;

        @NotEmpty
        public List<@NotNull @Size(max = 24) String> getNames() {
            return names;
        }

        @NotEmpty
        public List<@Size(max = 20) @NotNull List<@Size(min = 5) String>> getListOfLists() {
            return listOfLists;
        }
    }
}
