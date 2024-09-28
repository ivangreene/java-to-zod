package sh.ivan.pojo;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;

public record BooleanHolder(
        @AssertTrue Boolean mustBeTrue, @AssertFalse boolean mustBeFalse, Boolean boxed, boolean primitive) {}
