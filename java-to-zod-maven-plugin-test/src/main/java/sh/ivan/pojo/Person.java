package sh.ivan.pojo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;

public class Person {
    public UUID id;

    @NotBlank(message = "cannot be blank")
    public String firstName;

    @NotEmpty(message = "cannot be empty")
    public String lastName;

    @NotNull
    public String job;

    @Pattern(regexp = "https?://.*", message = "must be a valid 'URL' (\" - \\)")
    public String homepage;

    @Email
    public String email;

    public Person child;
}
