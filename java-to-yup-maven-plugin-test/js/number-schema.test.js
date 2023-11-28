const { NumberHolderSchema } = require('./schemas');

describe('NumberHolderSchema', () => {
    test('should support maxed Integer', () => {
        const maxedInteger = NumberHolderSchema.fields.maxed;
        expect(maxedInteger.isValidSync(300)).toBe(true);
        expect(maxedInteger.isValidSync(1)).toBe(true);
        expect(maxedInteger.isValidSync(0)).toBe(true);
        expect(maxedInteger.isValidSync(-500)).toBe(true);
        expect(maxedInteger.isValidSync(null)).toBe(true);
        expect(maxedInteger.isValidSync(12.51)).toBe(false);
        expect(maxedInteger.isValidSync(301)).toBe(false);
    });

    test('should support minned primitive int', () => {
        const minnedPrimitiveInt = NumberHolderSchema.fields.minned;
        expect(minnedPrimitiveInt.isValidSync(100)).toBe(true);
        expect(minnedPrimitiveInt.isValidSync(500)).toBe(true);
        expect(minnedPrimitiveInt.isValidSync(null)).toBe(false);
        expect(minnedPrimitiveInt.isValidSync(200.5)).toBe(false);
        expect(minnedPrimitiveInt.isValidSync(0)).toBe(false);
        expect(minnedPrimitiveInt.isValidSync(99)).toBe(false);
        expect(minnedPrimitiveInt.isValidSync(1)).toBe(false);
        expect(minnedPrimitiveInt.isValidSync(-5)).toBe(false);
    });

    test('should support negative Long', () => {
        const negativeLong = NumberHolderSchema.fields.negative;
        expect(negativeLong.isValidSync(-5)).toBe(true);
        expect(negativeLong.isValidSync(-300)).toBe(true);
        expect(negativeLong.isValidSync(null)).toBe(true);
        expect(negativeLong.isValidSync(-5.2)).toBe(false);
        expect(negativeLong.isValidSync(0)).toBe(false);
        expect(negativeLong.isValidSync(5)).toBe(false);
    });

    test('should support positive BigInteger', () => {
        const positiveBigInteger = NumberHolderSchema.fields.positive;
        expect(positiveBigInteger.isValidSync(5)).toBe(true);
        expect(positiveBigInteger.isValidSync(300)).toBe(true);
        expect(positiveBigInteger.isValidSync(null)).toBe(true);
        expect(positiveBigInteger.isValidSync(5.2)).toBe(false);
        expect(positiveBigInteger.isValidSync(0)).toBe(false);
        expect(positiveBigInteger.isValidSync(-5)).toBe(false);
    });

    test('should support negative or zero BigDecimal', () => {
        const negativeOrZeroBigDecimal = NumberHolderSchema.fields.negativeOrZero;
        expect(negativeOrZeroBigDecimal.isValidSync(-5)).toBe(true);
        expect(negativeOrZeroBigDecimal.isValidSync(-5.3)).toBe(true);
        expect(negativeOrZeroBigDecimal.isValidSync(-300)).toBe(true);
        expect(negativeOrZeroBigDecimal.isValidSync(-300.25)).toBe(true);
        expect(negativeOrZeroBigDecimal.isValidSync(null)).toBe(true);
        expect(negativeOrZeroBigDecimal.isValidSync(0)).toBe(true);
        expect(negativeOrZeroBigDecimal.isValidSync(5.2)).toBe(false);
        expect(negativeOrZeroBigDecimal.isValidSync(5)).toBe(false);
    });

    test('should support positive or zero primitive float', () => {
        const positiveOrZeroPrimitiveFloat = NumberHolderSchema.fields.positiveOrZero;
        expect(positiveOrZeroPrimitiveFloat.isValidSync(5)).toBe(true);
        expect(positiveOrZeroPrimitiveFloat.isValidSync(5.3)).toBe(true);
        expect(positiveOrZeroPrimitiveFloat.isValidSync(300)).toBe(true);
        expect(positiveOrZeroPrimitiveFloat.isValidSync(300.25)).toBe(true);
        expect(positiveOrZeroPrimitiveFloat.isValidSync(0)).toBe(true);
        expect(positiveOrZeroPrimitiveFloat.isValidSync(null)).toBe(false);
        expect(positiveOrZeroPrimitiveFloat.isValidSync(-5)).toBe(false);
        expect(positiveOrZeroPrimitiveFloat.isValidSync(-5.3)).toBe(false);
        expect(positiveOrZeroPrimitiveFloat.isValidSync(-300)).toBe(false);
        expect(positiveOrZeroPrimitiveFloat.isValidSync(-300.25)).toBe(false);
    });

    test('should support list of negative Integers', () => {
        const negativeIntegers = NumberHolderSchema.fields.negativeIntegers;
        expect(negativeIntegers.isValidSync([-1])).toBe(true);
        expect(negativeIntegers.isValidSync([-1, -2, -3])).toBe(true);
        expect(negativeIntegers.isValidSync([])).toBe(true);
        expect(negativeIntegers.isValidSync(null)).toBe(true);
        expect(negativeIntegers.isValidSync([-1.23])).toBe(false);
        expect(negativeIntegers.isValidSync([null])).toBe(false);
        expect(negativeIntegers.isValidSync([1])).toBe(false);
        expect(negativeIntegers.isValidSync([-1, 1, -3])).toBe(false);
    });
});
