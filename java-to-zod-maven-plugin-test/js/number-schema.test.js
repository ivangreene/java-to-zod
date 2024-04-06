import { NumberHolderSchema } from './schemas';
import { isValid } from './test-utils';

describe('NumberHolderSchema', () => {
    test('should support maxed Integer', () => {
        const maxedInteger = NumberHolderSchema.shape.maxed;
        expect(isValid(maxedInteger, 300)).toBe(true);
        expect(isValid(maxedInteger, 1)).toBe(true);
        expect(isValid(maxedInteger, 0)).toBe(true);
        expect(isValid(maxedInteger, -500)).toBe(true);
        expect(isValid(maxedInteger, null)).toBe(true);
        expect(isValid(maxedInteger, 12.51)).toBe(false);
        expect(isValid(maxedInteger, 301)).toBe(false);
    });

    test('should support minned primitive int', () => {
        const minnedPrimitiveInt = NumberHolderSchema.shape.minned;
        expect(isValid(minnedPrimitiveInt, 100)).toBe(true);
        expect(isValid(minnedPrimitiveInt, 500)).toBe(true);
        expect(isValid(minnedPrimitiveInt, null)).toBe(false);
        expect(isValid(minnedPrimitiveInt, 200.5)).toBe(false);
        expect(isValid(minnedPrimitiveInt, 0)).toBe(false);
        expect(isValid(minnedPrimitiveInt, 99)).toBe(false);
        expect(isValid(minnedPrimitiveInt, 1)).toBe(false);
        expect(isValid(minnedPrimitiveInt, -5)).toBe(false);
    });

    test('should support negative Long', () => {
        const negativeLong = NumberHolderSchema.shape.negative;
        expect(isValid(negativeLong, -5)).toBe(true);
        expect(isValid(negativeLong, -300)).toBe(true);
        expect(isValid(negativeLong, null)).toBe(true);
        expect(isValid(negativeLong, -5.2)).toBe(false);
        expect(isValid(negativeLong, 0)).toBe(false);
        expect(isValid(negativeLong, 5)).toBe(false);
    });

    test('should support positive BigInteger', () => {
        const positiveBigInteger = NumberHolderSchema.shape.positive;
        expect(isValid(positiveBigInteger, 5)).toBe(true);
        expect(isValid(positiveBigInteger, 300)).toBe(true);
        expect(isValid(positiveBigInteger, null)).toBe(true);
        expect(isValid(positiveBigInteger, 5.2)).toBe(false);
        expect(isValid(positiveBigInteger, 0)).toBe(false);
        expect(isValid(positiveBigInteger, -5)).toBe(false);
    });

    test('should support negative or zero BigDecimal', () => {
        const negativeOrZeroBigDecimal = NumberHolderSchema.shape.negativeOrZero;
        expect(isValid(negativeOrZeroBigDecimal, -5)).toBe(true);
        expect(isValid(negativeOrZeroBigDecimal, -5.3)).toBe(true);
        expect(isValid(negativeOrZeroBigDecimal, -300)).toBe(true);
        expect(isValid(negativeOrZeroBigDecimal, -300.25)).toBe(true);
        expect(isValid(negativeOrZeroBigDecimal, null)).toBe(true);
        expect(isValid(negativeOrZeroBigDecimal, 0)).toBe(true);
        expect(isValid(negativeOrZeroBigDecimal, 5.2)).toBe(false);
        expect(isValid(negativeOrZeroBigDecimal, 5)).toBe(false);
    });

    test('should support positive or zero primitive float', () => {
        const positiveOrZeroPrimitiveFloat = NumberHolderSchema.shape.positiveOrZero;
        expect(isValid(positiveOrZeroPrimitiveFloat, 5)).toBe(true);
        expect(isValid(positiveOrZeroPrimitiveFloat, 5.3)).toBe(true);
        expect(isValid(positiveOrZeroPrimitiveFloat, 300)).toBe(true);
        expect(isValid(positiveOrZeroPrimitiveFloat, 300.25)).toBe(true);
        expect(isValid(positiveOrZeroPrimitiveFloat, 0)).toBe(true);
        expect(isValid(positiveOrZeroPrimitiveFloat, null)).toBe(false);
        expect(isValid(positiveOrZeroPrimitiveFloat, -5)).toBe(false);
        expect(isValid(positiveOrZeroPrimitiveFloat, -5.3)).toBe(false);
        expect(isValid(positiveOrZeroPrimitiveFloat, -300)).toBe(false);
        expect(isValid(positiveOrZeroPrimitiveFloat, -300.25)).toBe(false);
    });

    test('should support list of negative Integers', () => {
        const negativeIntegers = NumberHolderSchema.shape.negativeIntegers;
        expect(isValid(negativeIntegers, [-1])).toBe(true);
        expect(isValid(negativeIntegers, [-1, -2, -3])).toBe(true);
        expect(isValid(negativeIntegers, [])).toBe(true);
        expect(isValid(negativeIntegers, null)).toBe(true);
        expect(isValid(negativeIntegers, [-1.23])).toBe(false);
        expect(isValid(negativeIntegers, [null])).toBe(false);
        expect(isValid(negativeIntegers, [1])).toBe(false);
        expect(isValid(negativeIntegers, [-1, 1, -3])).toBe(false);
    });
});
