const { ApiResponseSchema } = require('./schemas');
const { isValid } = require('./test-utils');

describe('ApiResponse', () => {
    test('enum values should be valid', () => {
        const status = ApiResponseSchema.shape.status;
        expect(isValid(status, 'PASSED')).toBe(true);
        expect(isValid(status, 'FAILED')).toBe(true);
        expect(isValid(status, 'UNKNOWN')).toBe(true);
    });

    test('non-enum values should be invalid', () => {
        const status = ApiResponseSchema.shape.status;
        expect(isValid(status, 'OOPS')).toBe(false);
        expect(isValid(status, 200)).toBe(false);
        expect(isValid(status, true)).toBe(false);
        expect(isValid(status, {})).toBe(false);
    });
});
