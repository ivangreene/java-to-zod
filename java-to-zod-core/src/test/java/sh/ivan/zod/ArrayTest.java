package sh.ivan.zod;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.junit.jupiter.api.Test;
import sh.ivan.zod.schema.ObjectSchema;

class ArrayTest extends JavaToZodConverterTest {

    @Test
    void shouldHandleListProperty() {
        var schema = converter.buildSchema(Author.class);
        assertThat(schema).isInstanceOf(ObjectSchema.class);
        var objectSchema = (ObjectSchema) schema;
        assertThat(objectSchema.getFields()).hasSize(1);
        var booksSchema = objectSchema.getFields().get("books");
        assertThat(booksSchema.asZodSchema()).isEqualTo("array(string().min(1))");
    }

    static class Author {
        @NotNull
        public List<@NotEmpty String> books;
    }

    @Test
    void shouldHandleListPropertyAnnotatedOnlyOnGetter() {
        var schema = converter.buildSchema(GetterAuthor.class);
        assertThat(schema).isInstanceOf(ObjectSchema.class);
        var objectSchema = (ObjectSchema) schema;
        assertThat(objectSchema.getFields()).hasSize(1);
        var booksSchema = objectSchema.getFields().get("books");
        assertThat(booksSchema.asZodSchema()).isEqualTo("array(string().min(1))");
    }

    static class GetterAuthor {
        private List<String> books;

        @NotNull
        public List<@NotEmpty String> getBooks() {
            return books;
        }
    }

    @Test
    void shouldHandleArrayProperty() {
        var schema = converter.buildSchema(NotePad.class);
        assertThat(schema).isInstanceOf(ObjectSchema.class);
        var objectSchema = (ObjectSchema) schema;
        assertThat(objectSchema.getFields()).hasSize(1);
        var booksSchema = objectSchema.getFields().get("notes");
        assertThat(booksSchema.asZodSchema()).isEqualTo("array(string().optional().nullable())");
    }

    static class NotePad {
        @NotNull
        public String[] notes;
    }

    @Test
    void shouldHandleNotEmptyList() {
        var schema = converter.buildSchema(PublishedAuthor.class);
        assertThat(schema).isInstanceOf(ObjectSchema.class);
        var objectSchema = (ObjectSchema) schema;
        assertThat(objectSchema.getFields()).hasSize(1);
        var booksSchema = objectSchema.getFields().get("books");
        assertThat(booksSchema.asZodSchema()).isEqualTo("array(string().optional().nullable()).min(1)");
    }

    static class PublishedAuthor {
        @NotNull
        @NotEmpty
        public List<String> books;
    }

    @Test
    void shouldHandleTypeAttributesFromComponentType() {
        var schema = converter.buildSchema(NumberHolder.class);
        assertThat(schema).isInstanceOf(ObjectSchema.class);
        var objectSchema = (ObjectSchema) schema;
        assertThat(objectSchema.getFields()).hasSize(1);
        var integersSchema = objectSchema.getFields().get("integers");
        assertThat(integersSchema.asZodSchema())
                .isEqualTo("array(number().int().positive().optional().nullable()).min(1)");
    }

    static class NumberHolder {
        @NotNull
        @NotEmpty
        public List<@Positive Integer> integers;
    }
}
