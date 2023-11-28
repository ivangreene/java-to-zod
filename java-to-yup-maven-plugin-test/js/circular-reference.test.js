const { PersonSchema } = require('./schemas');

describe('PersonSchema', () => {
    test('should support circular reference validation', () => {
        const child = {
            firstName: 'The',
            lastName: 'Child',
            job: 'Human',
        };
        const parent = {
            firstName: 'The',
            lastName: 'Parent',
            job: 'Human',
            child,
        };
        expect(PersonSchema.isValidSync(parent)).toBe(true);
    });

    test('should invalidate invalid circular reference', () => {
        const invalidChild = {
            firstName: null,
            lastName: null,
            job: null,
        };
        const parent = {
            firstName: 'The',
            lastName: 'Parent',
            job: 'Human',
            child: invalidChild,
        };
        expect(PersonSchema.isValidSync(parent)).toBe(false);
        delete parent.child;
        expect(PersonSchema.isValidSync(parent)).toBe(true);
    });

    test('should support arbitrary depth', () => {
        const parent = {
            firstName: 'The',
            lastName: 'Parent',
            job: 'Human',
            child: {
                firstName: 'The',
                lastName: 'Child',
                job: 'Human',
                child: {
                    firstName: 'The',
                    lastName: 'Grand Child',
                    job: 'Human',
                    child: {
                        firstName: null,
                        lastName: null,
                        job: null,
                    },
                },
            },
        };
        expect(PersonSchema.isValidSync(parent)).toBe(false);
        delete parent.child.child.child;
        expect(PersonSchema.isValidSync(parent)).toBe(true);
    });
});
