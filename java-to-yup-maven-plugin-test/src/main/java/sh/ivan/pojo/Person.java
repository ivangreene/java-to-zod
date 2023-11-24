package sh.ivan.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class Person {
    public UUID id;

    @NotBlank
    public String firstName;

    @NotEmpty
    public String lastName;

    @NotNull
    public String job;
}
