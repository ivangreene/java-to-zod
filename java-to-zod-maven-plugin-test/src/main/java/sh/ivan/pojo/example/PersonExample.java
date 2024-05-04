package sh.ivan.pojo.example;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PersonExample {
    @NotEmpty(message = "must not be empty")
    public String givenName;

    @NotEmpty
    public String surname;

    @Positive(message = "must be positive")
    public int age;

    @Email(message = "must be a valid email")
    public String email;

    @NotNull
    public AccountType accountType;
}

enum AccountType {
    PREMIUM,
    STANDARD,
    BASIC,
}
