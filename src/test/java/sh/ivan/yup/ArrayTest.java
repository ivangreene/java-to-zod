package sh.ivan.yup;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.junit.jupiter.api.Test;
import sh.ivan.yup.schema.ObjectSchema;

class ArrayTest {
    Jsr380ToYupConverter converter = new Jsr380ToYupConverter();

    @Test
    void shouldHandleListProperty() {
        var schema = converter.buildSchema(Author.class);
        assertThat(schema).isInstanceOf(ObjectSchema.class);
        var objectSchema = (ObjectSchema) schema;
        assertThat(objectSchema.getFields()).hasSize(1);
        var booksSchema = objectSchema.getFields().get("books");
        assertThat(booksSchema.asYupSchema()).isEqualTo("array().of(string())");
    }

    static class Author {
        @NotNull
        public List<String> books;
    }
}
