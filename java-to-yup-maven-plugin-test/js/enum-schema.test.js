const { ApiResponseSchema } = require('./schemas');

describe('ApiResponse', () => {
    test('enum values should be valid', () => {
        const status = ApiResponseSchema.fields.status;
        expect(status.isValidSync('PASSED')).toBe(true);
        expect(status.isValidSync('FAILED')).toBe(true);
        expect(status.isValidSync('UNKNOWN')).toBe(true);
    });

    test('non-enum values should be invalid', () => {
        const status = ApiResponseSchema.fields.status;
        expect(status.isValidSync('OOPS')).toBe(false);
        expect(status.isValidSync(200)).toBe(false);
        expect(status.isValidSync(true)).toBe(false);
        expect(status.isValidSync({})).toBe(false);
    });
});
