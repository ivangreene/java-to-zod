package sh.ivan.jty;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.Data;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import sh.ivan.jty.schema.NumberSchema;
import sh.ivan.jty.schema.ObjectSchema;
import sh.ivan.jty.schema.Schema;
import sh.ivan.jty.schema.StringSchema;

import java.math.BigDecimal;
import java.math.BigInteger;

class Jsr380ToYupConverterTest {
    Jsr380ToYupConverter converter = new Jsr380ToYupConverter();

    @Test
    void objectShouldHaveNoFields() {
        var schema = converter.buildSchema(Object.class);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert.extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .isEmpty();
        objectSchemaAssert.extracting(ObjectSchema::asYupSchema)
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
                .extracting(Schema::asYupSchema)
                .isEqualTo("number()");
    }

    @Test
    void shouldConvertBasicPojo() {
        var schema = converter.buildSchema(Person.class);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert.extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .hasSize(2)
                .containsEntry("name", new StringSchema())
                .containsEntry("age", new NumberSchema());
        objectSchemaAssert.extracting(ObjectSchema::asYupSchema)
                .isEqualTo("object({ age: number(), name: string(), })");
    }

    @Data
    class Person {
        private String name;
        private Integer age;
    }
}
