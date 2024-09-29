package sh.ivan.zod.resources;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TestPersonClass {
    @NotBlank(message = "Must not be blank.")
    @Size(min = 1, message = "Please supply a name.")
    private String fName;

    @NotBlank(message = "Must not be blank.")
    @Size(min = 1, message = "Please supply a name.")
    private String lName;

    @Min(value = 0, message = "Must not be negative.")
    private int age;

    public TestPersonClass(String fName, int age) {
        this.fName = fName;
        this.age = age;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}