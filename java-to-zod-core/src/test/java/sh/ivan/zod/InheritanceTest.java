package sh.ivan.zod;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import sh.ivan.zod.schema.ArraySchema;
import sh.ivan.zod.schema.ObjectSchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.StringSchema;
import sh.ivan.zod.schema.attribute.SizeAttribute;

class InheritanceTest extends JavaToZodConverterTest {

    @Test
    void shouldHandleBasicAttributeInheritance() {
        assertThatField("name")
                .isEqualTo(new StringSchema(Set.of(
                        new SizeAttribute(1, Integer.MAX_VALUE),
                        new SizeAttribute(5, Integer.MAX_VALUE),
                        new SizeAttribute(0, 50))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("string().min(1).min(5).max(50)");
    }

    @Test
    void shouldHandleAnnotatedListItemInheritance() {
        assertThatField("favoriteFoods")
                .isEqualTo(new ArraySchema(
                        new StringSchema(Set.of(
                                new SizeAttribute(1, Integer.MAX_VALUE),
                                new SizeAttribute(3, Integer.MAX_VALUE),
                                new SizeAttribute(0, 40))),
                        Set.of(new SizeAttribute(1, Integer.MAX_VALUE), new SizeAttribute(0, 5))))
                .extracting(Schema::asZodSchema)
                .isEqualTo("array(string().min(1).min(3).max(40)).min(1).max(5)");
    }

    static class Animal {
        @NotEmpty
        private String name;

        private List<@NotEmpty String> favoriteFoods;

        @Size(min = 5)
        public String getName() {
            return name;
        }

        @NotEmpty
        public List<@Size(min = 3) String> getFavoriteFoods() {
            return favoriteFoods;
        }
    }

    static class Human extends Animal {
        @Size(max = 50)
        @Override
        public String getName() {
            return super.getName();
        }

        @Size(max = 5)
        @Override
        public List<@Size(max = 40) String> getFavoriteFoods() {
            return super.getFavoriteFoods();
        }
    }

    private ObjectAssert<Schema> assertThatField(String fieldName) {
        var schema = (ObjectSchema) converter.buildSchema(Human.class);
        return assertThat(schema.getFields().get(fieldName));
    }
}
