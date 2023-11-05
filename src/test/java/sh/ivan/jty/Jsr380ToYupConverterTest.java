package sh.ivan.jty;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import sh.ivan.jty.schema.NumberSchema;
import sh.ivan.jty.schema.ObjectSchema;
import sh.ivan.jty.schema.ReferenceSchema;
import sh.ivan.jty.schema.Schema;
import sh.ivan.jty.schema.StringSchema;
import sh.ivan.jty.schema.attribute.NullableAttribute;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

class Jsr380ToYupConverterTest {
    Jsr380ToYupConverter converter = new Jsr380ToYupConverter();

    @Test
    void objectShouldHaveNoFields() {
        var schema = converter.buildSchema(Object.class);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert.extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .isEmpty();
        objectSchemaAssert.extracting(ObjectSchema::yupType)
                .isEqualTo("object({ })");
    }

    @Test
    void numbersShouldBeNumbers() {
        assertIsNumber(converter.buildSchema(Float.class));
        assertIsNumber(converter.buildSchema(Double.class));
        assertIsNumber(converter.buildSchema(Long.class));
        assertIsNumber(converter.buildSchema(Integer.class));
        assertIsNumber(converter.buildSchema(BigInteger.class));
        assertIsNumber(converter.buildSchema(BigDecimal.class));
    }

    void assertIsNumber(Schema schema) {
        assertThat(schema).isInstanceOf(NumberSchema.class)
                .extracting(Schema::yupType)
                .isEqualTo("number()");
    }

    @Test
    void shouldConvertBasicPojo() {
        var schema = converter.buildSchema(Address.class);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert.extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .hasSize(4)
                .containsEntry("street", new StringSchema(Set.of()))
                .containsEntry("streetTwo", new StringSchema(Set.of(new NullableAttribute())))
                .containsEntry("city", new StringSchema(Set.of()))
                .containsEntry("country", new StringSchema(Set.of()));
        objectSchemaAssert.extracting(ObjectSchema::asYupSchema)
                .isEqualTo("object({ street: string(), streetTwo: string().nullable(), city: string(), country: string(), })");
    }

    @Test
    void shouldConvertPojoWithReference() {
        var schema = converter.buildSchema(Person.class);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert.extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .hasSize(3)
                .containsEntry("name", new StringSchema(Set.of()))
                .containsEntry("age", new NumberSchema(Set.of(new NullableAttribute())))
                .containsEntry("address", new ReferenceSchema("Address", Set.of(new NullableAttribute())));
        objectSchemaAssert.extracting(ObjectSchema::asYupSchema)
                .isEqualTo("object({ name: string(), age: number().nullable(), address: Address.nullable(), })");
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
