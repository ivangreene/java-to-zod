const { BooleanHolderSchema } = require('./schemas');

describe('BooleanHolderSchema', () => {
    test('should support AssertTrue on boxed', () => {
        const mustBeTrue = BooleanHolderSchema.fields.mustBeTrue;
        expect(mustBeTrue.isValidSync(true)).toBe(true);
        expect(mustBeTrue.isValidSync(null)).toBe(true);
        expect(mustBeTrue.isValidSync(false)).toBe(false);
    });

    test('should support AssertFalse on primitive', () => {
        const mustBeFalse = BooleanHolderSchema.fields.mustBeFalse;
        expect(mustBeFalse.isValidSync(false)).toBe(true);
        expect(mustBeFalse.isValidSync(null)).toBe(false);
        expect(mustBeFalse.isValidSync(true)).toBe(false);
    });

    test('should support boxed', () => {
        const boxed = BooleanHolderSchema.fields.boxed;
        expect(boxed.isValidSync(false)).toBe(true);
        expect(boxed.isValidSync(true)).toBe(true);
        expect(boxed.isValidSync(null)).toBe(true);
        expect(boxed.isValidSync('foo')).toBe(false);
    });

    test('should support primitive', () => {
        const primitive = BooleanHolderSchema.fields.primitive;
        expect(primitive.isValidSync(false)).toBe(true);
        expect(primitive.isValidSync(true)).toBe(true);
        expect(primitive.isValidSync(null)).toBe(false);
        expect(primitive.isValidSync('foo')).toBe(false);
    });
});
