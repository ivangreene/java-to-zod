package sh.ivan.jty;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.Data;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import sh.ivan.jty.schema.NumberSchema;
import sh.ivan.jty.schema.ObjectSchema;
import sh.ivan.jty.schema.ReferenceSchema;
import sh.ivan.jty.schema.Schema;
import sh.ivan.jty.schema.StringSchema;

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
                .hasSize(3)
                .containsEntry("street", new StringSchema(Set.of()))
                .containsEntry("city", new StringSchema(Set.of()))
                .containsEntry("country", new StringSchema(Set.of()));
        objectSchemaAssert.extracting(ObjectSchema::yupType)
                .isEqualTo("object({ city: string(), country: string(), street: string(), })");
    }

    @Test
    void shouldConvertPojoWithReference() {
        var schema = converter.buildSchema(Person.class);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert.extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .hasSize(3)
                .containsEntry("name", new StringSchema(Set.of()))
                .containsEntry("age", new NumberSchema(Set.of()))
                .containsEntry("address", new ReferenceSchema("Address", Set.of()));
        objectSchemaAssert.extracting(ObjectSchema::yupType)
                .isEqualTo("object({ address: Address, age: number(), name: string(), })");
    }

    @Data
    static class Person {
        private String name;
        private Integer age;
        private Address address;
    }

    @Data
    static class Address {
        private String street;
        private String city;
        private String country;
    }
}
