import { ApiResponseSchema } from './schemas';
import { isValid } from './test-utils';

describe('ApiResponse.received', () => {
    test('date or null/undefined values should be valid', () => {
        const received = ApiResponseSchema.shape.received;
        expect(isValid(received, new Date())).toBe(true);
        expect(isValid(received, null)).toBe(true);
        expect(isValid(received, undefined)).toBe(true);
    });

    test('non-date values should be invalid', () => {
        const received = ApiResponseSchema.shape.received;
        expect(isValid(received, '2022-01-12T00:00:00.000Z')).toBe(false);
        expect(isValid(received, 200)).toBe(false);
        expect(isValid(received, true)).toBe(false);
        expect(isValid(received, {})).toBe(false);
    });
});
