import { PersonSchema } from './schemas';
import { isValid } from './test-utils';

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
        expect(isValid(PersonSchema, parent)).toBe(true);
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
        expect(isValid(PersonSchema, parent)).toBe(false);
        delete parent.child;
        expect(isValid(PersonSchema, parent)).toBe(true);
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
        expect(isValid(PersonSchema, parent)).toBe(false);
        delete parent.child.child.child;
        expect(isValid(PersonSchema, parent)).toBe(true);
    });
});
