package sh.ivan.pojo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class NumberHolder {
    @Max(300L)
    public Integer maxed;

    @Min(100L)
    public short minned;

    @Negative
    public Long negative;

    @Positive
    public BigInteger positive;

    @NegativeOrZero
    public BigDecimal negativeOrZero;

    @PositiveOrZero
    public float positiveOrZero;

    public List<@Negative @NotNull Integer> negativeIntegers;
}
