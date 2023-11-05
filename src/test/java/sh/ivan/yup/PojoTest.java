package sh.ivan.yup;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.NotNull;
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

class PojoTest {
    Jsr380ToYupConverter converter = new Jsr380ToYupConverter();

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
                        "object({ name: string(), age: number().nullable().integer(), address: Address.nullable(), })");
    }

    static class Person {
        @NotNull
        public String name;

        public Integer age;
        public Address address;

        public void setAge(@NotNull Integer age) {
            this.age = age;
        }
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
}
