package sh.ivan.zod.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;


public record TestPersonRecord(
        Long id,
        Date dateOfBirth,
        @NotBlank(message = "Must not be blank")
        @Size(min = 1, message = "Please supply a name.")
        String fName,
        String lName
) {

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "party_id = " + id + ", " +
                "dateOfBirth = " + dateOfBirth + ", " +
                "fName = " + fName + ", " +
                "lName = " + lName + ")";
    }


    public TestPersonRecord unWrapDto() {
        return this;
    }

    @NotBlank(message = "Must not be blank")
    @Size(min = 1, message = "Please supply a name.")
    @Override
    public String lName() {
        return lName;
    }

    public Long getId() {
        return id();
    }
}