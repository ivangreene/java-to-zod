package sh.ivan.zod;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.assertj.core.api.InstanceOfAssertFactories;
import sh.ivan.zod.schema.NumberSchema;
import sh.ivan.zod.schema.ObjectSchema;
import sh.ivan.zod.schema.ReferenceSchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.StringSchema;
import sh.ivan.zod.schema.attribute.IntegerAttribute;
import sh.ivan.zod.schema.attribute.OptionalNullableAttribute;
import sh.ivan.zod.schema.attribute.SizeAttribute;

public abstract class JavaObjectTest extends JavaToZodConverterTest {
    void testShouldConvertBasicObject_Address(Class<?> addressClass) {
        var schema = converter.buildSchema(addressClass);
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

    void testShouldConvertObjectWithReference_Person(Class<?> personClass) {
        var schema = converter.buildSchema(personClass);
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
                        "object({ name: string(), age: number().int().optional().nullable(), address: lazy(() => Address.optional().nullable()), })");
    }

    void testShouldCombineAnnotationsFromDeclarationAndGetter_Book(Class<?> bookClass) {
        var schema = converter.buildSchema(bookClass);
        var objectSchemaAssert = assertThat(schema).asInstanceOf(InstanceOfAssertFactories.type(ObjectSchema.class));
        objectSchemaAssert
                .extracting(ObjectSchema::getFields)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Schema.class))
                .hasSize(1)
                .containsEntry("name", new StringSchema(Set.of(new SizeAttribute(1, 50))));
        objectSchemaAssert
                .extracting(ObjectSchema::asZodSchema)
                .isEqualTo("object({ name: string().min(1).max(50), })");
    }
}
