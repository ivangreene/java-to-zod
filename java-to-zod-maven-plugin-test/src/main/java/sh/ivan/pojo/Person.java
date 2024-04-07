package sh.ivan.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;

public class Person {
    public UUID id;

    @NotBlank
    public String firstName;

    @NotEmpty
    public String lastName;

    @NotNull
    public String job;

    @Pattern(regexp = "https?://.*")
    public String homepage;

    public Person child;
}
