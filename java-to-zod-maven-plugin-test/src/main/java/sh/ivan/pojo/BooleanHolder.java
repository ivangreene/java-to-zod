package sh.ivan.pojo;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;

public class BooleanHolder {
    @AssertTrue
    public Boolean mustBeTrue;

    @AssertFalse
    public boolean mustBeFalse;

    public Boolean boxed;

    public boolean primitive;
}
