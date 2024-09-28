package sh.ivan.zod;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.junit.jupiter.api.Test;

class PojoTest extends JavaObjectTest {

    @Test
    void shouldConvertBasicPojo() {
        testShouldConvertBasicObject_Address(Address.class);
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
        testShouldConvertObjectWithReference_Person(Person.class);
    }

    static class Person {
        @NotNull
        public String name;

        public Integer age;
        public Address address;
    }

    @Test
    void shouldCombineAnnotationsFromFieldAndGetter() {
        testShouldCombineAnnotationsFromDeclarationAndGetter_Book(Book.class);
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
