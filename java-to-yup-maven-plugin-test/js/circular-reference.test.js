const { PersonSchema } = require('./schemas');

describe('PersonSchema', () => {
    test('should support circular reference validation', () => {
        const child = {
            firstName: 'The',
            lastName: 'Child',
        };
        const parent = {
            firstName: 'The',
            lastName: 'Parent',
            child,
        };
        expect(PersonSchema.isValidSync(parent)).toBe(true);
    });

    test('should invalidate invalid circular reference', () => {
        const invalidChild = {
            firstName: null,
            lastName: null,
        };
        const parent = {
            firstName: 'The',
            lastName: 'Parent',
            child: invalidChild,
        };
        expect(PersonSchema.isValidSync(parent)).toBe(false);
    });

    test('should support arbitrary depth', () => {
        const parent = {
            firstName: 'The',
            lastName: 'Parent',
            child: {
                firstName: 'The',
                lastName: 'Child',
                child: {
                    firstName: 'The',
                    lastName: 'Grand Child',
                    child: {
                        firstName: null,
                        lastName: null,
                    },
                },
            },
        };
        expect(PersonSchema.isValidSync(parent)).toBe(false);
    });
});
