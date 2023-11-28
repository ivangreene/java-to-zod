package sh.ivan.yup;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import sh.ivan.yup.schema.NumberSchema;
import sh.ivan.yup.schema.ObjectSchema;
import sh.ivan.yup.schema.ReferenceSchema;
import sh.ivan.yup.schema.Schema;
import sh.ivan.yup.schema.StringSchema;
import sh.ivan.yup.schema.attribute.IntegerAttribute;
import sh.ivan.yup.schema.attribute.NullableAttribute;
import sh.ivan.yup.schema.attribute.RequiredAttribute;
import sh.ivan.yup.schema.attribute.SizeAttribute;

class PojoTest extends JavaToYupConverterTest {

    @Test
    void shouldConvertBasicPojo() {
        var schema = converter.buildSchema(Address.class);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert
                .extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .hasSize(4)
                .containsEntry("street", new StringSchema(Set.of()))
                .containsEntry("streetTwo", new StringSchema(Set.of(new NullableAttribute())))
                .containsEntry("city", new StringSchema(Set.of()))
                .containsEntry("country", new StringSchema(Set.of()));
        objectSchemaAssert
                .extracting(ObjectSchema::asYupSchema)
                .isEqualTo(
                        "object({ street: string(), streetTwo: string().nullable(), city: string(), country: string(), })");
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
                .containsEntry("age", new NumberSchema(Set.of(new NullableAttribute(), new IntegerAttribute())))
                .containsEntry("address", new ReferenceSchema("Address", Set.of(new NullableAttribute())));
        objectSchemaAssert
                .extracting(ObjectSchema::asYupSchema)
                .isEqualTo(
                        "object({ name: string(), age: number().nullable().integer(), address: lazy(() => Address.default(undefined)).nullable(), })");
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
                .containsEntry("name", new StringSchema(Set.of(new SizeAttribute(1, 50), new RequiredAttribute())));
        objectSchemaAssert
                .extracting(ObjectSchema::asYupSchema)
                .isEqualTo("object({ name: string().required().min(1).max(50), })");
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
