package sh.ivan.pojo;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class Person {
    public UUID id;

    @NotBlank
    public String firstName;

    @NotBlank
    public String lastName;
}
