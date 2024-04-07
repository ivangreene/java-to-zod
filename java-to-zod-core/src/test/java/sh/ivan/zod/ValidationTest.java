package sh.ivan.zod;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Pattern;
import org.junit.jupiter.api.Test;

class ValidationTest {

    final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    final Validator validator = factory.getValidator();

    @Test
    void shouldMatchEntirePattern() {
        var person = new Person();

        person.homepage = "foo-https://bar";
        var violations = validator.validate(person);
        assertThat(violations).hasSize(1);

        person.homepage = "https://bar";
        violations = validator.validate(person);
        assertThat(violations).isEmpty();
    }

    public static class Person {
        @Pattern(regexp = "https?://.*")
        public String homepage;
    }
}
