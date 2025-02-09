package sh.ivan.pojo;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;

public record BooleanHolderWithMessages(
        @AssertTrue(message = "Needs to be true!") Boolean mustBeTrue,
        @AssertFalse(message = "Needs to be false!") boolean mustBeFalse,
        Boolean boxed,
        boolean primitive) {}
