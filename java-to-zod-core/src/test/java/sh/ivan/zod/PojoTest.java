package sh.ivan.zod;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import sh.ivan.zod.schema.NumberSchema;
import sh.ivan.zod.schema.ObjectSchema;
import sh.ivan.zod.schema.ReferenceSchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.StringSchema;
import sh.ivan.zod.schema.attribute.IntegerAttribute;
import sh.ivan.zod.schema.attribute.OptionalNullableAttribute;
import sh.ivan.zod.schema.attribute.SizeAttribute;

class PojoTest extends JavaToZodConverterTest {

    @Test
    void shouldConvertBasicPojo() {
        var schema = converter.buildSchema(Address.class);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert
                .extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .hasSize(4)
                .containsEntry("street", new StringSchema(Set.of()))
                .containsEntry("streetTwo", new StringSchema(Set.of(new OptionalNullableAttribute())))
                .containsEntry("city", new StringSchema(Set.of()))
                .containsEntry("country", new StringSchema(Set.of()));
        objectSchemaAssert
                .extracting(ObjectSchema::asZodSchema)
                .isEqualTo(
                        "object({ street: string(), streetTwo: string().optional().nullable(), city: string(), country: string(), })");
    }

    @Data
    static class Address {
        @NotNull
        private String street;

        private String streetTwo;

        @NotNull
        private String city;

        @NotNull
        private String country;
    }

    @Test
    void shouldConvertPojoWithReference() {
        var schema = converter.buildSchema(Person.class);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert
                .extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .hasSize(3)
                .containsEntry("name", new StringSchema(Set.of()))
                .containsEntry("age", new NumberSchema(Set.of(new OptionalNullableAttribute(), new IntegerAttribute())))
                .containsEntry("address", new ReferenceSchema("Address", Set.of(new OptionalNullableAttribute())));
        objectSchemaAssert
                .extracting(ObjectSchema::asZodSchema)
                .isEqualTo(
                        "object({ name: string(), age: number().int().optional().nullable(), address: lazy(() => Address.default(undefined).optional().nullable()), })");
    }

    static class Person {
        @NotNull
        public String name;

        public Integer age;
        public Address address;
    }

    @Test
    void shouldCombineAnnotationsFromFieldAndGetter() {
        var schema = converter.buildSchema(Book.class);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert
                .extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .hasSize(1)
                .containsEntry(
                        "name",
                        new StringSchema(Set.of(new SizeAttribute(1, 50), new SizeAttribute(1, Integer.MAX_VALUE))));
        objectSchemaAssert
                .extracting(ObjectSchema::asZodSchema)
                .isEqualTo("object({ name: string().min(1).min(1).max(50), })");
    }

    static class Book {
        @NotEmpty
        private String name;

        @Size(min = 1, max = 50)
        public String getName() {
            return name;
        }
    }
}
