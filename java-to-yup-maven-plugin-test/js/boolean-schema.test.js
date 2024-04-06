const { BooleanHolderSchema } = require('./schemas');
const { isValid } = require('./test-utils');

describe('BooleanHolderSchema', () => {
    test('should support AssertTrue on boxed', () => {
        const mustBeTrue = BooleanHolderSchema.shape.mustBeTrue;
        expect(isValid(mustBeTrue, true)).toBe(true);
        expect(isValid(mustBeTrue, null)).toBe(true);
        expect(isValid(mustBeTrue, false)).toBe(false);
    });

    test('should support AssertFalse on primitive', () => {
        const mustBeFalse = BooleanHolderSchema.shape.mustBeFalse;
        expect(isValid(mustBeFalse, false)).toBe(true);
        expect(isValid(mustBeFalse, null)).toBe(false);
        expect(isValid(mustBeFalse, true)).toBe(false);
    });

    test('should support boxed', () => {
        const boxed = BooleanHolderSchema.shape.boxed;
        expect(isValid(boxed, false)).toBe(true);
        expect(isValid(boxed, true)).toBe(true);
        expect(isValid(boxed, null)).toBe(true);
        expect(isValid(boxed, 'foo')).toBe(false);
    });

    test('should support primitive', () => {
        const primitive = BooleanHolderSchema.shape.primitive;
        expect(isValid(primitive, false)).toBe(true);
        expect(isValid(primitive, true)).toBe(true);
        expect(isValid(primitive, null)).toBe(false);
        expect(isValid(primitive, 'foo')).toBe(false);
    });
});
