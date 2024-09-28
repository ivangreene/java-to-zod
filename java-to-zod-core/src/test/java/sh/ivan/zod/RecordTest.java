package sh.ivan.zod;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;

class RecordTest extends JavaObjectTest {

    @Test
    void shouldConvertBasicRecord() {
        testShouldConvertBasicObject_Address(Address.class);
    }

    record Address(@NotNull String street, String streetTwo, @NotNull String city, @NotNull String country) {}

    @Test
    void shouldConvertRecordWithReference() {
        testShouldConvertObjectWithReference_Person(Person.class);
    }

    record Person(@NotNull String name, Integer age, Address address) {}

    @Test
    void shouldCombineAnnotationsFromConstructorAndGetter() {
        testShouldCombineAnnotationsFromDeclarationAndGetter_Book(Book.class);
    }

    record Book(@NotEmpty String name) {
        @Size(min = 1, max = 50)
        public String getName() {
            return name;
        }
    }
}
