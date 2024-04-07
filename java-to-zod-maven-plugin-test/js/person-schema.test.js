import { PersonSchema } from './schemas';
import { isValid } from './test-utils';

describe('PersonSchema', () => {
    test('should support nullable UUID', () => {
        const id = PersonSchema.shape.id;
        expect(isValid(id, '389ba84f-0c10-41f6-9df5-86faf0ccae11')).toBe(true);
        expect(isValid(id, null)).toBe(true);
        expect(isValid(id, '389ba84f-0c10-41f6-9df5-86faf0ccae1')).toBe(false);
        expect(isValid(id, '1000')).toBe(false);
    });

    test('should support NotBlank String', () => {
        const firstName = PersonSchema.shape.firstName;
        expect(isValid(firstName, 'Foo')).toBe(true);
        expect(isValid(firstName, null)).toBe(false);
        expect(isValid(firstName, '')).toBe(false);
        expect(isValid(firstName, ' \t\r\n')).toBe(false);
    });

    test('should support NotEmpty String', () => {
        const lastName = PersonSchema.shape.lastName;
        expect(isValid(lastName, 'Foo')).toBe(true);
        expect(isValid(lastName, ' \t\r\n')).toBe(true);
        expect(isValid(lastName, ' ')).toBe(true);
        expect(isValid(lastName, '\t')).toBe(true);
        expect(isValid(lastName, '\n')).toBe(true);
        expect(isValid(lastName, null)).toBe(false);
        expect(isValid(lastName, '')).toBe(false);
    });

    test('should support NotNull String', () => {
        const job = PersonSchema.shape.job;
        expect(isValid(job, 'Foo')).toBe(true);
        expect(isValid(job, '')).toBe(true);
        expect(isValid(job, ' ')).toBe(true);
        expect(isValid(job, null)).toBe(false);
    });

    test('should support Pattern String', () => {
        const homepage = PersonSchema.shape.homepage;
        expect(isValid(homepage, 'https://example.com')).toBe(true);
        expect(isValid(homepage, 'http://example.com')).toBe(true);
        expect(isValid(homepage, null)).toBe(true);
        expect(isValid(homepage, undefined)).toBe(true);

        expect(isValid(homepage, 'foo-https://example.com')).toBe(false);
        expect(isValid(homepage, 'HTTP://example.com')).toBe(false);
        expect(isValid(homepage, '')).toBe(false);
        expect(isValid(homepage, 'x')).toBe(false);
    });

    test('should validate a valid person', () => {
        expect(isValid(PersonSchema, {
            id: '389ba84f-0c10-41f6-9df5-86faf0ccae11', firstName: 'John', lastName: 'Doe', job: 'Human'
        })).toBe(true);
        expect(isValid(PersonSchema, {
            firstName: 'John', lastName: 'Doe', job: 'Human'
        })).toBe(true);
    });

    test('should invalidate on missing NotEmpty/NotBlank/NotNull fields', () => {
        expect(isValid(PersonSchema, {
            firstName: 'John', lastName: 'Doe'
        })).toBe(false);
        expect(isValid(PersonSchema, {
            lastName: 'Doe', job: 'Human'
        })).toBe(false);
        expect(isValid(PersonSchema, {
            firstName: 'John', job: 'Human'
        })).toBe(false);
    });
});
