import { BooleanHolderSchema, BooleanHolderWithMessagesSchema } from './schemas';
import { isValid } from './test-utils';

const testSchema = (name, booleanSchema) => {
    describe(name, () => {
        test('should support AssertTrue on boxed', () => {
            const mustBeTrue = booleanSchema.shape.mustBeTrue;
            expect(isValid(mustBeTrue, true)).toBe(true);
            expect(isValid(mustBeTrue, null)).toBe(true);
            expect(isValid(mustBeTrue, false)).toBe(false);
        });

        test('should support AssertFalse on primitive', () => {
            const mustBeFalse = booleanSchema.shape.mustBeFalse;
            expect(isValid(mustBeFalse, false)).toBe(true);
            expect(isValid(mustBeFalse, null)).toBe(false);
            expect(isValid(mustBeFalse, true)).toBe(false);
        });

        test('should support boxed', () => {
            const boxed = booleanSchema.shape.boxed;
            expect(isValid(boxed, false)).toBe(true);
            expect(isValid(boxed, true)).toBe(true);
            expect(isValid(boxed, null)).toBe(true);
            expect(isValid(boxed, 'foo')).toBe(false);
        });

        test('should support primitive', () => {
            const primitive = booleanSchema.shape.primitive;
            expect(isValid(primitive, false)).toBe(true);
            expect(isValid(primitive, true)).toBe(true);
            expect(isValid(primitive, null)).toBe(false);
            expect(isValid(primitive, 'foo')).toBe(false);
        });
    });
};

testSchema('BooleanHolderSchema', BooleanHolderSchema);
testSchema('BooleanHolderWithMessagesSchema', BooleanHolderWithMessagesSchema);
