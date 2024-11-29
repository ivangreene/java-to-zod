package sh.ivan.zod.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.Objects;

public final class TestPersonClass {

    private final Long id;
    private final Date dateOfBirth;
    private final String fName;

    @NotBlank(message = "Must not be blank")
    @Size(min = 1, message = "Please supply a name.")
    private final String lName;

    public TestPersonClass(
            Long id,
            Date dateOfBirth,

            String fName,
            String lName
    ) {
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.fName = fName;
        this.lName = lName;
    }

    /**
     * Non-getter method shouldn't be in schema.
     * */
    public TestPersonClass unWrapDto() {
        return this;
    }

    public String getlName() {
        return lName;
    }

    public Long getId() {
        return id();
    }

    /**
     * Non-getter method shouldn't be in schema.
     * */
    public Long id() {
        return id;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @NotBlank(message = "Must not be blank")
    @Size(min = 1, message = "Please supply a name.")
    public String getfName() {
        return fName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateOfBirth, fName, lName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TestPersonClass) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.dateOfBirth, that.dateOfBirth) &&
                Objects.equals(this.fName, that.fName) &&
                Objects.equals(this.lName, that.lName);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "party_id = " + id + ", " +
                "dateOfBirth = " + dateOfBirth + ", " +
                "fName = " + fName + ", " +
                "lName = " + lName + ")";
    }

}